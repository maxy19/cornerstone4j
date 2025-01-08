package com.cornerstone4j.core.model.signal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客户端信息")
public class ClientSignal {

    @Schema(description = "应用名称", example = "Director")
    private String appName;
    @Schema(description = "版本名称", example = "6.0.6.135")
    private String versionName;
    @Schema(description = "版本编码", example = "6000006")
    private Long versionCode;
    @Schema(description = "平台", example = "IPhonePlayer")
    private String platform;
    @Schema(description = "语言", example = "zh_CN")
    private String language;

}
