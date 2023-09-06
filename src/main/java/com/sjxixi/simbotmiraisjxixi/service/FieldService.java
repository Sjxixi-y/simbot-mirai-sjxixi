package com.sjxixi.simbotmiraisjxixi.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.sjxixi.simbotmiraisjxixi.entity.Field;
import com.sjxixi.simbotmiraisjxixi.listener.GroupListener;
import com.sjxixi.simbotmiraisjxixi.mapper.FieldMapper;
import com.sjxixi.simbotmiraisjxixi.util.PagingVoUtil;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sjxixi.simbotmiraisjxixi.entity.table.FieldTableDef.FIELD;


@Service
public class FieldService {
    @Resource
    FieldMapper fieldMapper;

    private static final Logger logger = LoggerFactory.getLogger(FieldService.class);

    /**
     * web 新增
     *
     * @param field 字段对象
     * @return 返回对象
     */
    public ResultVo addField(Field field) {

        if (addFieldSimBot(field) != ResultVo.SUCCESS) {
            throw new RuntimeException("已有对应库");
        }

        return ResultVo.success("添加成功！", null);
    }

    /**
     * sim-bot 新增
     *
     * @param field 字段对象
     * @return 返回对象
     */
    public Integer addFieldSimBot(Field field) {

        logger.info("[新增消息库] 消息对象" + field);

        Field one = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.eq(field.getName()))
                .one();

        if (one != null) {
            logger.info("[新增消息库] 失败");

            return ResultVo.ERROR;
        }

        fieldMapper.insertSelective(field);

        logger.info("[新增消息库] 成功");

        return ResultVo.SUCCESS;
    }

    /**
     * Web 根据id修改
     *
     * @param field 修改对象
     * @return 返回对象
     */
    public ResultVo updateField(Field field) {

        logger.info("[id修改] " + field);

        fieldMapper.update(field);

        return ResultVo.success("修改成功！", field);
    }

    /**
     * web 分页 + 模糊 查询
     *
     * @param paging 分页对象
     * @return 返回对象
     */
    public ResultVo getFieldImportant(PagingVo paging) {

        logger.info("[分页查询] " + paging);

        PagingVoUtil.getPagingVo(paging);

        Page<Field> page = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.like(paging.getValueOne()).or(FIELD.DESCRIBE.like(paging.getValueTow())))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));

        return ResultVo.successPage("查询成功！", page.getRecords(), page.getTotalRow());
    }

    /**
     * sim-bot 查询
     *
     * @return 返回对象
     */
    public List<Field> getFieldSimBot() {

        logger.info("[sim-bot查询]");

        return QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .list();
    }

    /**
     * sim-bot 通过 name 查询
     *
     * @param fieldName name
     * @return 返回对象
     */
    public ResultVo getFieldByName(String fieldName) {

        logger.info("[name查询] name：" + fieldName);

        Field field = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.eq(fieldName))
                .one();

        return ResultVo.success("查询成功！", field);
    }
}