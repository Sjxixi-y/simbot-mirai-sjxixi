package com.sjxixi.simbotmiraisjxixi.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.sjxixi.simbotmiraisjxixi.entity.AtReply;
import com.sjxixi.simbotmiraisjxixi.entity.Users;
import com.sjxixi.simbotmiraisjxixi.mapper.AtReplyMapper;
import com.sjxixi.simbotmiraisjxixi.util.PagingVoUtil;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sjxixi.simbotmiraisjxixi.entity.table.AtReplyTableDef.AT_REPLY;
import static com.sjxixi.simbotmiraisjxixi.entity.table.UsersTableDef.USERS;

@Service
public class AtReplyService {
    @Resource
    AtReplyMapper atReplyMapper;

    private static final Logger logger = LoggerFactory.getLogger(AtReplyService.class);
    // 增
    // 删
    // 改
    // 查

    /**
     * web 模糊查询 + 分页查询
     *
     * @return 对象集合
     */
    public ResultVo getReplyList(PagingVo paging) {
        logger.info("[分页查询] " + paging);

        PagingVoUtil.getPagingVo(paging);

        Page<AtReply> page = QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.LEVEL.eq(paging.getValueOne()).and(AT_REPLY.VALUE.like(paging.getValueTow())))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));

        return ResultVo.successPage("查询成功", page.getRecords(), page.getTotalRow());
    }

    /**
     * sim-bot 查询
     *
     * @param level    问题等级
     * @param superior 上级问题
     * @return 对象集合
     */
    public List<AtReply> getReplyListSimBot(Integer level, Integer superior) {
        logger.info("[查询] 级别：" + level + " 上级：" + superior);

        return QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.LEVEL.eq(level).and(AT_REPLY.SUPERIOR.eq(superior)))
                .list();
    }
}