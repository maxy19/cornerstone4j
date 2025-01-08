package com.cornerstone4j.core.model.signal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "用户信息")
public class UserSignal {

    @Schema(description = "用户ID")
    private String loginId;
    @Schema(description = "租户ID")
    private String tenantId;

}
