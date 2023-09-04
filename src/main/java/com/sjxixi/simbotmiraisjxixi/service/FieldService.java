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
        fieldMapper.insertSelective(field);
        return ResultVo.success("添加成功！", null);
    }

    /**
     * 根据id修改
     *
     * @param field 修改对象
     * @return 返回对象
     */
    public ResultVo updateField(Field field) {
        fieldMapper.update(field);
        return ResultVo.success("修改成功！", field);
    }

    /**
     * 分页 + 模糊 查询
     *
     * @param paging 分页对象
     * @param isPage 是否分页
     * @return 返回对象
     */
    public ResultVo getFieldImportant(PagingVo paging, boolean isPage) {
        if (!isPage) {
            return ResultVo.success("查询成功！", getFieldNotPage());
        }
        PagingVoUtil.getPagingVo(paging);
        Page<Field> page = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.like(paging.getName()).or(FIELD.DESCRIBE.like(paging.getCode())))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));
        return ResultVo.successPage("查询成功！", page.getRecords(), page.getTotalRow());
    }

    /**
     * 查询
     *
     * @return 返回对象
     */
    public List<Field> getFieldNotPage() {
        return QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .list();
    }

    /**
     * 通过 name 查询
     *
     * @param fieldName name
     * @return 返回对象
     */
    public ResultVo getFieldByName(String fieldName) {
        Field field = QueryChain.of(fieldMapper)
                .select(FIELD.ALL_COLUMNS).from(FIELD)
                .where(FIELD.NAME.eq(fieldName))
                .one();
        return ResultVo.success("查询成功！", field);
    }
}