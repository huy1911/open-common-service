package com.lpb.mid.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpb.mid.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class BaseFilterChain extends OncePerRequestFilter implements Filter {
    protected static final Logger logger = LoggerFactory.getLogger(BaseFilterChain.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        ThreadContext.put(Constants.UUID, uuid);
        ThreadContext.put(Constants.START_TIME, String.valueOf(start));
        ThreadContext.put(Constants.LOG_TYPE, "httprequest");
        //get request
        final RequestWrapper requestWrapper = new RequestWrapper(uuid, start, request, true);
        final ResponseWrapper responseWrapper = new ResponseWrapper(uuid, response);
        ServiceHeader serviceHeader = createServiceHeader(requestWrapper, true);
        requestWrapper.setAttribute(Constants.SERVICE_HEADER, serviceHeader);
        ThreadContext.put(Constants.IP_CLIENT, serviceHeader.getSourceAppId());
        ThreadContext.put(Constants.USERNAME, serviceHeader.getAuthenticationUser());
        ThreadContext.put(Constants.CLIENT_MSG_ID, serviceHeader.getClientMessageId());
        filterChain.doFilter(requestWrapper, responseWrapper);
        //write response info
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        if (isLogResponse(request.getRequestURI())) {
            ThreadContext.put(Constants.RESPONSE_CODE, String.valueOf(responseWrapper.getStatus()));
            ThreadContext.put(Constants.DURATION, String.valueOf(timeElapsed));
            ThreadContext.put(Constants.LOG_TYPE, "httpresponse");
            serviceHeader.setStatus(getHttpStatus(responseWrapper));
            serviceHeader.setResponseBody(getResponseBody(responseWrapper));

            ESLogging esLogging = createESLogging(serviceHeader, requestWrapper);
            esLogging.setStartedDate(Date.from(Instant.ofEpochMilli(start).plus(7, ChronoUnit.HOURS)));
            esLogging.setEndDate(Date.from(Instant.ofEpochMilli(finish).plus(7, ChronoUnit.HOURS)));
            esLogging.setDueTime(timeElapsed);
            ObjectMapper objectMapper = new ObjectMapper();
            String indented = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(serviceHeader);
            String es = objectMapper.writeValueAsString(esLogging);
            logger.info(String.format("Response Info Open API: id=%s, body=%s, esLog = %s", uuid, indented, es));
        }
        ThreadContext.clearAll();
    }

    public ESLogging createESLogging(ServiceHeader serviceHeader, RequestWrapper request) {
        ESLogging esLogging = new ESLogging();
        String xSourceEnv = request.getHeader(Constants.X_SOURCE_ENV);
        esLogging.setAppId(serviceHeader.getDestAppIp());
        esLogging.setReferenceId(serviceHeader.getClientMessageId());
        esLogging.setSourceEnv(xSourceEnv);
        esLogging.setRequestMsg(serviceHeader.getRequestBody());
        esLogging.setResponseMsg(serviceHeader.getResponseBody());
        try {
            JSONObject json = new JSONObject(serviceHeader.getResponseBody());
            String desc = json.getString("description");
            esLogging.setDestination(desc);
        } catch (Exception ignored) {
        }
        return esLogging;
    }

    public ServiceHeader createServiceHeader(RequestWrapper request, boolean writeLogSecurity) {
        ServiceHeader serviceHeader = null;
        try {
            serviceHeader = new ServiceHeader();
            serviceHeader.setHeader(new HashMap<>());
            serviceHeader.setMessageTimeStamp(new Date());
            serviceHeader.setServicePath(request.getRequestURI());
            String xForwardedFor = request.getHeader("x-forwarded-for");
            String sourceEnv = request.getHeader(Constants.X_SOURCE_ENV);
            String xReferenceId = request.getHeader(Constants.X_REFERENCE_ID);
            serviceHeader.setClientMessageId(xReferenceId);
            serviceHeader.setSourceEnv(sourceEnv);
            serviceHeader.setSourceAppIp(xForwardedFor == null ? request.getRemoteHost() : xForwardedFor);
            serviceHeader.setDestAppIp(request.getLocalAddr());
            serviceHeader.setDestAppPort(request.getLocalPort());
            serviceHeader.setHttpMethod(request.getMethod());
            serviceHeader.setHttpPath(getFullURL(request));
            String requestBody = getRequestBody(request);
            serviceHeader.setRequestBody(requestBody);
            String authorization = request.getParameter("Authorization") != null ? request.getParameter("Authorization") : request.getHeader("Authorization");
            serviceHeader.setAuthorization(authorization);
            if (writeLogSecurity) {
                extractSecurityInfo(serviceHeader);
            }
            if (!StringUtils.isEmpty(serviceHeader.getSourceAppId()) || !StringUtils.isEmpty(serviceHeader.getClientMessageId())) {
                serviceHeader.setServiceMessageId(serviceHeader.getSourceAppId() + "-" + serviceHeader.getClientMessageId());
            }
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (headerName.equalsIgnoreCase("Authorization")) {
                    serviceHeader.getHeader().put(headerName, "<<Not recorded to log>>");
                } else {
                    serviceHeader.getHeader().put(headerName, request.getHeader(headerName));
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            ThreadContext.clearAll();
        }

        return serviceHeader;
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        return queryString == null ? requestURL.toString() : requestURL.append('?').append(queryString).toString();
    }

    private boolean isLogResponse(String requestUri) {
        return isWriteDataRequestAndResponse(requestUri);
    }

    public String getRequestBody(RequestWrapper requestWrapper) {
        return requestWrapper.getBody();
    }

    public String getResponseBody(ResponseWrapper responseWrapper) throws UnsupportedEncodingException {
        return new String(responseWrapper.toByteArray(), responseWrapper.getCharacterEncoding());
    }

    private int getHttpStatus(ResponseWrapper responseWrapper) {
        return responseWrapper.getStatus();
    }

    private boolean isWriteDataRequestAndResponse(String url) {
        boolean isWritten = true;
        if (url.contains("swagger") || url.contains("api-docs")) {
            isWritten = false;
        }
        return isWritten;
    }

    private static void extractSecurityInfo(ServiceHeader serviceHeader) {
        String authorization = serviceHeader.getAuthorization();
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            String[] chucks = authorization.split("\\.");
            if (!isEmpty(chucks)) {
                Map<String, String> claims = getJWTClaims(chucks[1]);
                if (!isEmpty(claims)) {
                    serviceHeader.setAuthenticationUser(claims.get("userName"));
                    serviceHeader.setDestAppIp(claims.get("appId"));
                }
            }
        }
    }

    public static boolean isEmpty(String[] values) {
        return values == null || values.length == 0;
    }

    public static boolean isEmpty(Map<?, ?> obj) {
        return obj == null || obj.isEmpty();
    }

    private static Map<String, String> getJWTClaims(String chuck) {
        LinkedHashMap claims = null;
        try {
            byte[] decode = Base64.getDecoder().decode(chuck);
            ObjectMapper objectMapper = new ObjectMapper();
            claims = objectMapper.readValue(new String(decode), LinkedHashMap.class);
        } catch (Exception var3) {
        }
        return claims;
    }


}
