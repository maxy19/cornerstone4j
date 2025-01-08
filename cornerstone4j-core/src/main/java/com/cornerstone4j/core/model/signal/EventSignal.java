package com.cornerstone4j.core.model.signal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "事件信息")
public class EventSignal<T> {

    @Schema(description = "请求参数")
    private T info;
    @Schema(description = "接口版本", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer apiVersion;

}
