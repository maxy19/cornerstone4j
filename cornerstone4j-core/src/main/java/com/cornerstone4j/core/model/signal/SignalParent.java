package com.cornerstone4j.core.model.signal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static cn.hutool.core.text.CharSequenceUtil.isEmpty;
import static cn.hutool.core.text.StrPool.UNDERLINE;


@Data
@Schema(description = "信号基类")
public class SignalParent<T> implements Serializable {

    @Schema(description = "用户信息")
    private UserSignal userSignal;
    @Schema(description = "设备信息")
    private DeviceSignal deviceSignal;
    @Schema(description = "客户端信息")
    private ClientSignal clientSignal;
    @Schema(description = "时间信息")
    private TimeSignal timeSignal;

    public Locale getLocale() {
        if (clientSignal != null) {
            return convert(clientSignal.getLanguage());
        } else {
            return convert(deviceSignal.getLanguage());
        }
    }

    private Locale convert(String language) {
        if (isEmpty(language)) {
            return Locale.CHINA;
        }
        Locale locale;
        if (language.contains(UNDERLINE)) {
            List<String> langAndCountry = Arrays.stream(language.split(UNDERLINE)).toList();
            locale = new Locale(langAndCountry.get(0), langAndCountry.get(1));
        } else {
            locale = new Locale(language);
        }
        return locale;
    }
}
