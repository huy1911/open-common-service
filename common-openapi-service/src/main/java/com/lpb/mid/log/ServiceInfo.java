package com.lpb.mid.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceInfo {
    private String serviceId;
    private String productCode;
    private String urlApi;
    private String connectorURL;
    private String connectPort;
    private String methodAction;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String udf6;
    private String udf7;
    private String udf8;
}
