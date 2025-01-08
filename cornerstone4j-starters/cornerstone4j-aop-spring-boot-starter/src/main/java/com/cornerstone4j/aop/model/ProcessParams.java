package com.cornerstone4j.aop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author maxy
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessParams {
    private String reqParamJson;
    private String methodName;
    private String respResultJson;
    private Long duration;
    private String uri;
}
