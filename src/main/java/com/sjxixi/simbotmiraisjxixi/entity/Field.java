package com.sjxixi.simbotmiraisjxixi.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * (Field)实体类
 *
 * @author sjxixi
 * @since 2023-08-31 10:16:06
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table("field")
//
@Schema(name = "Field", title = "消息匹配对象")
public class Field {
    /**
     * ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer fid;
    /**
     * 问题名称
     */
    @Column
    private String name;
    /**
     * 问题答案
     */
    @Column
    private String value;
    /**
     * 问题
     */
    @Column
    private String describe;
}

