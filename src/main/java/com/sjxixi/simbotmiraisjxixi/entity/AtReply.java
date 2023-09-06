package com.sjxixi.simbotmiraisjxixi.entity;

import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * (AtReply)实体类
 *
 * @author sjxixi
 * @since 2023-09-05 14:39:29
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("at_reply")
public class AtReply {
    /**
     * 主键
     */
    private Integer aid;
    /**
     * 值
     */
    private String value;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 上级ID
     */
    private Integer superior;
}

