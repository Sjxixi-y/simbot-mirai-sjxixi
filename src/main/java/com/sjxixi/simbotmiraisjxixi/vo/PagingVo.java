package com.sjxixi.simbotmiraisjxixi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(name = "Paging", title = "分页对象")
public class PagingVo {
    String name;
    String code;
    Integer pageIndex;
    Integer pageSize;
}