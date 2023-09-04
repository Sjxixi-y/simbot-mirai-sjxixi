package com.sjxixi.simbotmiraisjxixi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 统一返回类
 *
 * @author sjxixi
 * @since 2023-08-29 14:29:24
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
//
@Schema(name = "ResultVo", title = "统一返回类")
//
public class ResultVo {
    public static final int SUCCESS = 200;
    public static final int ERROR = 400;

    Integer code;
    String msg;
    Object data;
    Long count;

    public static ResultVo success(String msg, Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(SUCCESS);
        resultVo.setMsg(msg);
        resultVo.setData(data);
        return resultVo;
    }

    public static ResultVo fail(String msg, Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(ERROR);
        resultVo.setMsg(msg);
        resultVo.setData(data);
        return resultVo;
    }

    public static ResultVo successPage(String msg, Object data, Long count) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(SUCCESS);
        resultVo.setMsg(msg);
        resultVo.setData(data);
        resultVo.setCount(count);
        return resultVo;
    }
}

