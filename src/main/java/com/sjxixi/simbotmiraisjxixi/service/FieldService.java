package com.sjxixi.simbotmiraisjxixi.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.sjxixi.simbotmiraisjxixi.entity.Field;
import com.sjxixi.simbotmiraisjxixi.mapper.FieldMapper;
import com.sjxixi.simbotmiraisjxixi.util.PagingVoUtil;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sjxixi.simbotmiraisjxixi.entity.table.FieldTableDef.FIELD;


@Service
public class FieldService {
    @Resource
    FieldMapper fieldMapper;

    /**
     * 新增
     *
     * @param field 字段对象
     * @return 返回对象
     */
    public ResultVo addField(Field field) {
        Field one = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.eq(field.getName()))
                .one();
        if (one != null) {
            return ResultVo.fail("删除失败", null);
        }
        fieldMapper.insert(field);
        return ResultVo.success("添加成功！", null);
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return 返回对象
     */
    public ResultVo deleteFieldById(Integer id) {
        Field field = fieldMapper.selectOneById(id);
        if (field.getIsImportant()) {
            return ResultVo.fail("此字段不能被删除", null);
        }
        fieldMapper.deleteById(id);
        return ResultVo.success("删除成功！", null);
    }

    /**
     * 根据Name删除
     *
     * @param name name
     * @return 返回对象
     */
    public ResultVo deleteFieldByName(String name) {
        Field field = QueryChain.of(fieldMapper)
                .select(FIELD.IS_IMPORTANT).from(FIELD)
                .where(FIELD.NAME.eq(name))
                .one();
        if (field.getIsImportant()) {
            return ResultVo.fail("此字段不能被删除", null);
        }

        QueryWrapper query = new QueryWrapper().where(FIELD.NAME.eq(name));
        int i = fieldMapper.deleteByQuery(query);

        if (i == 0) {
            return ResultVo.fail("删除失败", null);
        }
        return ResultVo.success("删除成功！", null);
    }

    /**
     * 根据id修改
     *
     * @param field 修改对象
     * @return 返回对象
     */
    public ResultVo updateField(Field field) {
        return ResultVo.success("修改成功！", null);
    }

    /**
     * 获取全部 重要
     *
     * @param paging 分页对象
     * @return 返回对象
     */
    public ResultVo getFieldImportant(PagingVo paging, Integer isImportance, boolean isPage) {
        if (!isPage) {
            return ResultVo.success("查询成功！", getFieldImportantNotPage());
        }
        PagingVoUtil.getPagingVo(paging);
        Page<Field> page = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.like(paging.getName()).or(FIELD.DESCRIBE.like(paging.getCode())))
                .and(FIELD.IS_IMPORTANT.eq(isImportance))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));
        return ResultVo.successPage("查询成功！", page.getRecords(), page.getTotalRow());
    }

    public List<Field> getFieldImportantNotPage() {
        return QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .and(FIELD.IS_IMPORTANT.eq(1))
                .list();
    }

    /**
     * 通过 name 查询 重要
     *
     * @param fieldName name
     * @return 返回对象
     */
    public ResultVo getFieldByName(String fieldName) {
        Field field = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.eq(fieldName))
                .and(FIELD.IS_IMPORTANT.eq(0))
                .one();
        return ResultVo.success("查询成功！", field);
    }
}
