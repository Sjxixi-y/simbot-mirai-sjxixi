package com.sjxixi.simbotmiraisjxixi.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.sjxixi.simbotmiraisjxixi.entity.AtReply;
import com.sjxixi.simbotmiraisjxixi.mapper.AtReplyMapper;
import com.sjxixi.simbotmiraisjxixi.util.PagingVoUtil;
import com.sjxixi.simbotmiraisjxixi.vo.AtReplyTwoVo;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ReplyVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sjxixi.simbotmiraisjxixi.entity.table.AtReplyTableDef.AT_REPLY;

@Service
public class AtReplyService {
    @Resource
    AtReplyMapper atReplyMapper;

    private static final Logger logger = LoggerFactory.getLogger(AtReplyService.class);

    /**
     * Web 新增
     *
     * @param atReply 数据对象
     * @return 返回对象
     */
    public ResultVo addReply(AtReply atReply) {

        if (addReplySimBot(atReply) != ResultVo.SUCCESS) {
            throw new RuntimeException("新增失败");
        }

        return ResultVo.success("新增成功", null);
    }

    /**
     * sim-bot 新增
     *
     * @param atReply 数据对象
     * @return 返回对象
     */
    public int addReplySimBot(AtReply atReply) {
        logger.info("[新增菜单] " + atReply);

        if (!isSole(atReply.getValue())) {
            logger.info("[新增菜单] 字段重复");

            throw new RuntimeException("标题或内容不能重复");
        }

        if (atReplyMapper.insertSelective(atReply) != 1) {
            logger.info("[新增菜单] 失败");

            return ResultVo.ERROR;
        }

        logger.info("[新增菜单] 成功");

        return ResultVo.SUCCESS;
    }

    /**
     * Web 新增二级标题
     *
     * @param atReplyTwoVo 数据对象
     * @return 返回对象
     */
    public ResultVo addReplyTwo(AtReplyTwoVo atReplyTwoVo) {
        if (addReplySimBot(atReplyTwoVo) != ResultVo.SUCCESS) {
            throw new RuntimeException("失败");
        }
        return ResultVo.success("成功", null);
    }

    /**
     * sim-bot 新增二级标题
     *
     * @param atReplyTwoVo 数据对象
     * @return 返回对象
     */
    @Transactional
    public int addReplySimBot(AtReplyTwoVo atReplyTwoVo) {
        logger.info("[添加二级目录] " + atReplyTwoVo);

        AtReply atReplyTwo = AtReply.builder()
                .value(atReplyTwoVo.getValue())
                .level(2)
                .superior(atReplyTwoVo.getSuperior())
                .build();

        logger.info("[添加二级目录] 创建二级目录对象：" + atReplyTwo);

        if (addReplySimBot(atReplyTwo) != ResultVo.SUCCESS) {

            logger.info("[添加二级目录] 失败");

            return ResultVo.ERROR;
        }

        logger.info("[添加二级目录] 成功");

        String values = atReplyTwoVo.getValues();

        AtReply getAtReplyOne = getReplySimBot(atReplyTwoVo.getValue());

        logger.info("[添加三级目录] 获取二级目录" + getAtReplyOne);

        String[] s = values.split("\n");

        logger.info("[添加三级目录] 切割后，长度：" + s.length);

        for (String str : s) {

            logger.info("[添加三级目录] 输入内容如下：" + str);

            if (addReplySimBot(AtReply.builder()
                    .value(str)
                    .level(3)
                    .superior(getAtReplyOne.getAid()).build()) != ResultVo.SUCCESS) {

                logger.info("[添加三级目录] 失败");

                return ResultVo.ERROR;
            }

            logger.info("[添加三级目录] 成功");
        }

        logger.info("[添加全部目录] 成功");

        return ResultVo.SUCCESS;
    }

    /**
     * Web 删除
     *
     * @param id 数据对象
     * @return 返回对象
     */
    public ResultVo deleteReplyById(Integer id) {

        if (deleteReplyByIdSimBot(id) != ResultVo.SUCCESS) {
            throw new RuntimeException("删除失败");
        }

        return ResultVo.success("删除成功", null);
    }

    /**
     * sim-bot 删除
     *
     * @param id 数据对象
     * @return 返回对象
     */
    public int deleteReplyByIdSimBot(Integer id) {
        logger.info("[根据ID删除菜单标题] id: " + id);

        AtReply atReply = QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.AID.eq(id))
                .one();

        logger.info("[根据ID删除菜单标题] level -> " + atReply.getLevel());

        if (atReply.getLevel() == 1) {

            List<Integer> list = QueryChain.of(atReplyMapper)
                    .select(AT_REPLY.AID).from(AT_REPLY)
                    .where(AT_REPLY.SUPERIOR.eq(id))
                    .listAs(Integer.class);

            QueryWrapper queryWrapper = new QueryWrapper()
                    .where(AT_REPLY.AID.eq(id)
                            .or(AT_REPLY.SUPERIOR.eq(id))
                            .or(AT_REPLY.SUPERIOR.in(list))
                    );

            atReplyMapper.deleteByQuery(queryWrapper);

        } else if (atReply.getLevel() == 2) {

            QueryWrapper queryWrapper = new QueryWrapper()
                    .where(AT_REPLY.AID.eq(id).or(AT_REPLY.SUPERIOR.eq(id)));

            atReplyMapper.deleteByQuery(queryWrapper);

        } else if (atReply.getLevel() == 3) {

            QueryWrapper queryWrapper = new QueryWrapper()
                    .where(AT_REPLY.AID.eq(id));

            if (atReplyMapper.deleteByQuery(queryWrapper) != 1) {

                logger.info("[根据ID删除菜单标题] 失败");

                return ResultVo.ERROR;
            }
        }

        logger.info("[根据ID删除菜单标题] 成功");

        return ResultVo.SUCCESS;
    }

    /**
     * Web 修改
     *
     * @param atReply 数据对象
     * @return 返回对象
     */
    public ResultVo updateReply(AtReply atReply) {

        if (updateReplySimBot(atReply) != ResultVo.SUCCESS) {
            throw new RuntimeException("修改失败");
        }

        return ResultVo.success("修改成功", null);
    }

    /**
     * sim-bot 修改
     *
     * @param atReply 数据对象
     * @return 返回对象
     */
    public int updateReplySimBot(AtReply atReply) {

        logger.info("[修改菜单标题] " + atReply);

        if (!UpdateChain.of(AtReply.class)
                .set(AT_REPLY.VALUE, atReply.getValue())
                .where(AT_REPLY.AID.eq(atReply.getAid()))
                .update()) {
            logger.info("[修改菜单标题] 失败");

            return ResultVo.ERROR;
        }

        logger.info("[修改菜单标题] 成功");

        return ResultVo.SUCCESS;
    }

    public ResultVo updateReply(AtReplyTwoVo atReplyTwoVo) {
        if (updateReplySimBot(atReplyTwoVo) != ResultVo.SUCCESS) {
            throw new RuntimeException("修改失败");
        }
        return ResultVo.success("修改成功", null);
    }

    public Integer updateReplySimBot(AtReplyTwoVo atReplyTwoVo) {
        logger.info("[修改二级目录] " + atReplyTwoVo);

        boolean is = UpdateChain.of(AtReply.class)
                .set(AT_REPLY.VALUE, atReplyTwoVo.getValue())
                .where(AT_REPLY.AID.eq(atReplyTwoVo.getAid()))
                .update();

        if (!is) {
            logger.info("[修改二级目录] 失败");

            return ResultVo.ERROR;
        }

        logger.info("[修改二级目录] 成功");

        String values = atReplyTwoVo.getValues();

        AtReply getAtReplyOne = getReplySimBot(atReplyTwoVo.getValue());

        logger.info("[修改三级目录] 获取二级目录" + getAtReplyOne);

        String[] s = values.split("\n");

        logger.info("[添加三级目录] 切割后，长度：" + s.length);


        // 删除原来的三级目录
        List<AtReply> listSimBot = getReplyListSimBot(getAtReplyOne.getValue());

        listSimBot.forEach(AtReply -> {
            deleteReplyByIdSimBot(AtReply.getAid());
        });

        for (String str : s) {

            logger.info("[添加三级目录] 输入内容如下：" + str);

            if (addReplySimBot(AtReply.builder()
                    .value(str)
                    .level(3)
                    .superior(getAtReplyOne.getAid()).build()) != ResultVo.SUCCESS) {

                logger.info("[添加三级目录] 失败");

                return ResultVo.ERROR;
            }

            logger.info("[添加三级目录] 成功");
        }

        logger.info("[添加全部目录] 成功");

        return ResultVo.SUCCESS;
    }

    /**
     * web 模糊查询 + 分页查询
     *
     * @return 对象集合
     */
    public ResultVo getReplyList(PagingVo paging) {
        logger.info("[分页查询] " + paging);

        PagingVoUtil.getPagingVo(paging);

        // 查询所以一级标题
        Page<AtReply> page = QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.LEVEL.eq(paging.getValueOne()).and(AT_REPLY.VALUE.like(paging.getValueTow())))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));

        // 一级标题
        List<AtReply> atReplyList = page.getRecords();

        // 返回集合
        List<ReplyVo> replyVoList = new ArrayList<>();

        atReplyList.forEach(atReply -> {
            // 查询二级标题
            List<AtReply> list = QueryChain.of(atReplyMapper)
                    .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                    .where(AT_REPLY.SUPERIOR.eq(atReply.getAid()))
                    .list();
            // 封装返回集合
            ReplyVo replyVo = new ReplyVo(atReply, list);

            replyVoList.add(replyVo);
        });

        return ResultVo.successPage("查询成功", replyVoList, page.getTotalRow());
    }

    /**
     * sim-bot 查询
     *
     * @param level 问题等级
     * @return 对象集合
     */
    public List<AtReply> getReplyListSimBot(Integer level) {
        logger.info("[查询] 级别：" + level);

        return QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.LEVEL.eq(level))
                .list();
    }

    public ResultVo getReplyList(String value) {
        return ResultVo.success("成功", getReplyListSimBot(value));
    }

    /**
     * sim-bot 查询
     *
     * @param value 问题内容
     * @return 返回对象
     */
    public List<AtReply> getReplyListSimBot(String value) {
        logger.info("[查询] 问题：" + value);

        return QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.SUPERIOR.in(
                        QueryChain.of(atReplyMapper)
                                .select(AT_REPLY.AID).from(AT_REPLY)
                                .where(AT_REPLY.VALUE.eq(value))
                                .listAs(Integer.class)
                ))
                .list();
    }

    /**
     * sim-bot 查询
     *
     * @param value 问题
     * @return 对象集合
     */
    public AtReply getReplySimBot(String value) {

        logger.info("[查询] 通过 value 查询：" + value);

        return QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.VALUE.eq(value))
                .one();
    }

    public boolean isSole(String value) {
        List<AtReply> list = QueryChain.of(atReplyMapper)
                .select(AT_REPLY.ALL_COLUMNS).from(AT_REPLY)
                .where(AT_REPLY.VALUE.eq(value))
                .list();
        if (list == null) {
            return true;
        }

        return list.size() < 1;
    }
}