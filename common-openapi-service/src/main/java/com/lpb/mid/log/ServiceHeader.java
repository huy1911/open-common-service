package com.lpb.mid.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vietnq3
 * */
@Data
@NoArgsConstructor
public class ServiceHeader {
    private String servicePath;
    private String httpMethod;
    private String clientMessageId;
    private String sourceEnv;
    private String serviceMessageId;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date messageTimeStamp;
    private String sourceAppId;
    private String sourceAppIp;
    private String destAppIp;
    private int destAppPort;
    private String httpPath;
    private String authenticationUser;
    private String requestBody;
    private String responseBody;
    @JsonIgnore
    private String authorization;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private Object userInfo;
    @JsonIgnore
    private List<ServiceInfo> permissions;
    @JsonIgnore
    private Map<String, String> header = new HashMap<>();
}
