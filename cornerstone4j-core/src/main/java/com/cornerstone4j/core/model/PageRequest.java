package com.cornerstone4j.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@Schema(description = "分页请求参数")
public class PageRequest implements Serializable {

    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer pageNo = 1;

    @Schema(description = "每页条数，最大值为 100", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer pageSize = 10;

    @Schema(description = "排序规则")
    private List<Sort> sorts = new ArrayList<>();

    @Getter
    @Setter
    @Schema(description = "排序元素")
    public static class Sort implements Serializable {

        /**
         * 排序字段
         */
        @Schema(description = "排序字段")
        private String field;

        /**
         * 是否正序排序
         */
        @Schema(description = "是否正序排序")
        private boolean asc;

    }

    @Schema(hidden = true)
    public final int getOffset() {
        return (pageNo - 1) * pageSize;
    }

}
