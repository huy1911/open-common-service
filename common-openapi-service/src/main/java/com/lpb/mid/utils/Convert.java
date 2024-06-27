package com.lpb.mid.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lpb.mid.dto.SendKafkaDto;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Convert {
    private static final Logger logger = LoggerFactory.getLogger(Convert.class);

    public static String convertObject(Object object) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getReq(List<String> strings) {
        return strings.stream()
                .filter(str -> str != null && !str.isEmpty())
                .collect(Collectors.joining(Constants.VERTICAL_TILES));
    }

    public static String getUsername(HttpServletRequest request) {
        String userName = null;
        try {
            String authorization = request.getParameter(Constants.Authorization) != null ? request.getParameter(Constants.Authorization) : request.getHeader(Constants.Authorization);
            if (authorization != null) {
                try {
                    logger.info("getUsername :get token by user");
                    authorization = URLDecoder.decode(authorization, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    logger.error("getUsername :get token by user error");
                    throw new RuntimeException(e);
                }
            }
            if (StringUtils.hasText(authorization) && authorization.startsWith(Constants.Bearer)) {
                authorization = authorization.substring(7);
            }
            if (authorization != null && validateJwtToken(authorization)) {
                userName = Jwts.parser().setSigningKey("JwtSecretKey").parseClaimsJws(authorization).getBody().getSubject();
            }
            return userName;
        } catch (Exception e) {
            logger.info("getUserName :get userName error ");
            return userName;
        }
    }

    public static SendKafkaDto getSendKafkaDto(HttpServletRequest request) {
        SendKafkaDto sendKafkaDto = null;
        try {
            String authorization = request.getParameter(Constants.Authorization) != null ? request.getParameter(Constants.Authorization) : request.getHeader(Constants.Authorization);
            if (authorization != null) {
                try {
                    logger.info("getUsername :get token by user");
                    authorization = URLDecoder.decode(authorization, StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    logger.error("getUsername :get token by user error");
                    throw new RuntimeException(e);
                }
            }
            if (StringUtils.hasText(authorization) && authorization.startsWith(Constants.Bearer)) {
                authorization = authorization.substring(7);
            }
            if (authorization != null && validateJwtToken(authorization)) {
                String userName = Jwts.parser().setSigningKey("JwtSecretKey").parseClaimsJws(authorization).getBody().getSubject();
                String customerNo = String.valueOf(Jwts.parser().setSigningKey("JwtSecretKey").parseClaimsJws(authorization).getBody().get("customerNo"));
                sendKafkaDto = SendKafkaDto.builder()
                        .username(userName)
                        .customerNo(customerNo).build();
            }
            return sendKafkaDto;
        } catch (Exception e) {
            logger.info("getUserName :get userName error ");
            return sendKafkaDto;
        }
    }

    public static boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey("JwtSecretKey").parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


    public static String commonGetMac(HttpServletRequest request,Object object, String secretKey){
        List<String> strings = new ArrayList<>();
        strings.add(request.getHeader(Constants.X_SOURCE_ENV));
        strings.add(request.getHeader(Constants.X_REFERENCE_ID));
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);

                if (value != null) {
                    strings.add(value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return HmacUtil.genHmac(Convert.getReq(strings), secretKey);
    }

}
