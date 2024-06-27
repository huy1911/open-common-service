package com.lpb.mid.log;

import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
//    private PrintWriter writer = new PrintWriter(bos);
    private String uuid;
    public ResponseWrapper(String uuid, HttpServletResponse response) {
        super(response);
        response.setBufferSize(128*1024);
        response.setCharacterEncoding("UTF-8");
        this.uuid = uuid;
    }
    @Override
    public ServletResponse getResponse() {
        return this;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
            private TeeOutputStream tee = new TeeOutputStream(ResponseWrapper.super.getOutputStream(), bos);
            @Override
            public void write(int b) throws IOException {
                tee.write(b);
            }
        };

    }
    public byte[] toByteArray(){
        return bos.toByteArray();
    }
}