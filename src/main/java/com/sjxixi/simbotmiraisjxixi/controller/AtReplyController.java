package com.sjxixi.simbotmiraisjxixi.controller;

import com.sjxixi.simbotmiraisjxixi.entity.AtReply;
import com.sjxixi.simbotmiraisjxixi.service.AtReplyService;
import com.sjxixi.simbotmiraisjxixi.vo.AtReplyTwoVo;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("atReply")
public class AtReplyController {
    @Resource
    AtReplyService atReplyService;

    @PostMapping
    public ResultVo addResult(@RequestBody AtReply atReply) {
        return atReplyService.addReply(atReply);
    }

    @PostMapping("/two")
    public ResultVo addResultTwo(@RequestBody AtReplyTwoVo atReplyTwoVo) {
        return atReplyService.addReplyTwo(atReplyTwoVo);
    }

    @DeleteMapping("{id}")
    public ResultVo deleteResultById(@PathVariable Integer id) {
        return atReplyService.deleteReplyById(id);
    }

    @PutMapping
    public ResultVo updateResult(@RequestBody AtReply atReply) {
        return atReplyService.updateReply(atReply);
    }

    @PutMapping("two")
    public ResultVo updateResult(@RequestBody AtReplyTwoVo atReplyTwoVo) {
        return atReplyService.updateReply(atReplyTwoVo);
    }

    @GetMapping
    public ResultVo getResult(PagingVo paging) {
        return atReplyService.getReplyList(paging);
    }

    @GetMapping("{value}")
    public ResultVo getResult(@PathVariable String value) {
        return atReplyService.getReplyList(value);
    }
}