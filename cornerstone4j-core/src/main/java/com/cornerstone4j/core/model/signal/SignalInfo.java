package com.cornerstone4j.core.model.signal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "信号信息")
public class SignalInfo<T> extends SignalParent implements Serializable {

    @Schema(description = "事件信息")
    private EventSignal<T> eventSignal;
     
}
