package com.lpb.mid.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ESLogging {
    private String appId;
    private String referenceId;
    private String sourceEnv;
    private String requestMsg; //(mã hóa SHA256)
    private String responseMsg; //(mã hóa SHA256)
    private String coreRefNo;
    private String error;
    private Long dueTime;
    private String type; //(LOV: API,KAFKA)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'")
    private Date startedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'")
    private Date endDate;

    private String sourceSystem;
    private String destination;

}
