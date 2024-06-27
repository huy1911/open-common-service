package com.lpb.mid.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lpb.mid.utils.JsonHelpers;

import java.util.List;


/**
 * @author vietnq3
 * */
public class RequestContext extends BaseObject {
    private ServiceHeader serviceHeader;
    private Object requestData;
    @JsonIgnore
    private long startTime;

    public static RequestContext init() {
        RequestContext context = new RequestContext();
        context.setServiceHeader(new ServiceHeader());
        context.setStartTime(System.nanoTime());
        return context;
    }

    public static RequestContext init(ServiceHeader serviceHeader) {
        RequestContext context = new RequestContext();
        context.setServiceHeader(serviceHeader);
        context.setStartTime(System.nanoTime());
        return context;
    }

    public <T> T getRequestData(Class<T> clazz) {
        Object data = this.getRequestData();
        return data == null ? null : (T) data;
    }

    public String getServiceMessageId() {
        return this.serviceHeader != null ? this.serviceHeader.getServiceMessageId() : null;
    }

    public String getClientMessageId() {
        return this.serviceHeader != null ? this.serviceHeader.getClientMessageId() : null;
    }

    public String getHttpMethod() {
        return this.serviceHeader != null ? this.serviceHeader.getHttpMethod() : null;
    }

    public String getSourceAppId() {
        return this.serviceHeader != null ? this.serviceHeader.getSourceAppId() : null;
    }

    public String getServicePath() {
        return this.serviceHeader != null ? this.serviceHeader.getServicePath() : null;
    }

    public Object getRequestData() {
        return this.requestData;
    }

    public String getRequestDataString() {
        return this.requestData != null ? JsonHelpers.toJSONString(this.requestData) : "";
    }

    public String getAuthorization() {
        return this.getServiceHeader().getAuthorization();
    }

    public ServiceHeader getServiceHeader() {
        return this.serviceHeader;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setServiceHeader(ServiceHeader serviceHeader) {
        this.serviceHeader = serviceHeader;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }

    @JsonIgnore
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public List<ServiceInfo> getPermissions() {
        return this.getServiceHeader().getPermissions();
    }
    public RequestContext() {
    }

    public RequestContext(ServiceHeader serviceHeader, Object requestData, long startTime) {
        this.serviceHeader = serviceHeader;
        this.requestData = requestData;
        this.startTime = startTime;
    }
}
