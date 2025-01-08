package com.cornerstone4j.core.model.signal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "设备信息")
public class DeviceSignal {

    @Schema(description = "操作系统", example = "iPadOS 16.6.1")
    private String operatingSystem;
    @Schema(description = "系统版本", example = "iPadOS 16.6.1")
    private String systemVersion;
    @Schema(description = "系统版本编码", example = "1")
    private Long systemVersionCode;
    @Schema(description = "连接类型", example = "ReachableViaLocalAreaNetwork wifi")
    private String connectionType;
    @Schema(description = "经纬度")
    private String coordinate;
    @Schema(description = "语言", example = "ChineseSimplified")
    private String language;
    @Schema(description = "国家")
    private String country;
    @Schema(description = "终端", example = "IPhonePlayer")
    private String terminal;
    @Schema(description = "类型", example = "iPad11,1")
    private String model;
}
