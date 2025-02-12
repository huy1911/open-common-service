package com.lpb.mid.log;


import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class RequestWrapper extends HttpServletRequestWrapper {
    private String uuid;
    private long startTime;
    private final String body;
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public RequestWrapper(String uuid, long startTime, HttpServletRequest request, boolean isWriteData) throws IOException {
        super(request);
        this.uuid = uuid;
        this.startTime = startTime;
        StringBuilder stringBuilder = new StringBuilder();
        if (isWriteData) {
            BufferedReader bufferedReader = null;
            try {
                String contentType = request.getContentType();
                if (contentType != null && contentType.startsWith("multipart/form-data")) {
//                    stringBuilder.append(request.getParameter("request"));
                } else {
                    InputStream inputStream = request.getInputStream();
                    if (inputStream != null) {
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        char[] charBuffer = new char[128];
                        int bytesRead = -1;
                        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                            stringBuilder.append(charBuffer, 0, bytesRead);
                        }
                    } else {
                        stringBuilder.append("");
                    }
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw ex;
                    }
                }
            }
        }
        //Store request pody content in 'body' variable
        body = stringBuilder.toString();
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    //Use this method to read the request body N times
    public String getBody() {
        return this.body;
    }



}