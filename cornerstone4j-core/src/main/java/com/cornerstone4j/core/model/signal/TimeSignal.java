package com.cornerstone4j.core.model.signal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "时间信息")
public class TimeSignal {
    @Schema(description = "请求时间", example = "1591090999720")
    private long clientTs;
    @Schema(description = "请求时区", example = "GMT+08:00")
    private String timeZone;

}
