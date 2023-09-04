package com.sjxixi.simbotmiraisjxixi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.sjxixi.simbotmiraisjxixi.util.NumberUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * (Users)实体类
 *
 * @author sjxixi
 * @since 2023-08-29 14:22:01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table("user")
//
@Schema(name = "User", title = "用户对象")
//
public class Users {
    /**
     * ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;
    /**
     * QQ号
     */
    @Column
    private String code;
    /**
     * 昵称
     */
    @Column
    private String name;
    /**
     * 识别码
     */
    @Column
    private String key;
    /**
     * 权限
     */
    @Column
    private Integer permission = NumberUtil.MEMBER;
    /**
     * 用户状态
     * isLogicDelete = true 表示开启逻辑删除
     */
    @Column(isLogicDelete = true)
    private Boolean status;
    /**
     * 入群时间
     */
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regTime;
    /**
     * 最后发言时间
     */
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
}