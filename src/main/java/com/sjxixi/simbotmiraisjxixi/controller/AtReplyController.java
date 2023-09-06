package com.sjxixi.simbotmiraisjxixi.controller;

import com.sjxixi.simbotmiraisjxixi.service.AtReplyService;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "atReply", description = "消息管理")
@CrossOrigin
@RestController
@RequestMapping("atReply")
public class AtReplyController {
    @Resource
    AtReplyService atReplyService;

    @Operation(summary = "获取黑名单", description = "分页查询",
            parameters = {@Parameter(name = "paging", description = "查询对象")})
    @GetMapping
    public ResultVo getResultOne(PagingVo paging) {
        return atReplyService.getReplyList(paging);
    }
}
