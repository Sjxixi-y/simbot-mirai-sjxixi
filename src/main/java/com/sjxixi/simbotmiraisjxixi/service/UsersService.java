package com.sjxixi.simbotmiraisjxixi.service;

import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.sjxixi.simbotmiraisjxixi.entity.Users;
import com.sjxixi.simbotmiraisjxixi.mapper.UsersMapper;
import com.sjxixi.simbotmiraisjxixi.util.PagingVoUtil;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.sjxixi.simbotmiraisjxixi.entity.table.UsersTableDef.USERS;

@Service
public class UsersService {
    @Resource
    UsersMapper usersMapper;
    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    /**
     * 新增
     *
     * @param user 用户实体
     * @return 返回对象
     */
    public ResultVo addUser(Users user) {
        logger.info("[成员新增] 新增如下成员：" + user);
        // 判断是否存在此用户
        LogicDeleteManager.skipLogicDelete();
        if (QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.CODE.eq(user.getCode()))
                .one() != null) {
            // 存在此用户
            logger.info("[成员新增] 用户：" + user.getName() + " QQ: " + user.getCode() + " 已存在");
            return ResultVo.fail("此用户已存在", null);
        }
        logger.info("[成员新增] 用户：" + user.getName() + " QQ: " + user.getCode() + " 不存在，正在执行新增");
        usersMapper.insertSelective(user);
        return ResultVo.success("新增成功！", null);
    }

    /**
     * 删除
     *
     * @param id 用户id
     * @return 返回对象
     */
    public ResultVo deleteUserById(Integer id) {
        usersMapper.deleteById(id);
        return ResultVo.success("删除成功！", null);
    }

    /**
     * 根据QQ号删除
     *
     * @param code QQ号
     * @return 返回对象
     */
    public ResultVo deleteUserByCode(String code) {
        QueryWrapper where = new QueryWrapper().where(USERS.CODE.eq(code));
        int i = usersMapper.deleteByQuery(where);
        if (i == 1) {
            return ResultVo.success("删除成功！", null);
        }
        return ResultVo.fail("删除失败", null);
    }

    /**
     * 通过 ID 将黑名单变为白名单
     *
     * @param id ID
     * @return 返回结果
     */
    public ResultVo deleteFalseUserById(Integer id) {
        LogicDeleteManager.skipLogicDelete();
        UpdateChain.of(Users.class)
                .set(USERS.STATUS, 0)
                .where(USERS.ID.eq(id))
                .update();
        return ResultVo.success("成功！", null);
    }

    /**
     * 修改
     *
     * @param user 用户实体
     * @return 返回对象
     */
    public ResultVo updateUser(Users user) {
        usersMapper.update(user);
        return ResultVo.success("修改成功！", null);
    }

    /**
     * 模糊查询 + 分页
     *
     * @param paging 用户实体
     * @return 返回对象
     */
    public ResultVo getUser(PagingVo paging) {
        PagingVoUtil.getPagingVo(paging);
        Page<Users> page = QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.CODE.eq(paging.getCode()).or(USERS.NAME.like(paging.getName())))
                .and(USERS.STATUS.eq(0))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));
        return ResultVo.successPage("查询成功！", page.getRecords(), page.getTotalRow());
    }

    /**
     * 查询黑名单
     *
     * @param paging 分页对象
     * @return 返回对象
     */
    public ResultVo getFalseUser(PagingVo paging) {
        PagingVoUtil.getPagingVo(paging);
        LogicDeleteManager.skipLogicDelete();
        Page<Users> page = QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.CODE.eq(paging.getCode()).or(USERS.NAME.like(paging.getName())))
                .and(USERS.STATUS.eq(1))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));
        return ResultVo.successPage("查询成功！", page.getRecords(), page.getTotalRow());
    }

    /**
     * 通过QQ号查询
     *
     * @param code QQ号
     * @return 返回对象
     */
    public ResultVo getUserByCode(String code) {
        LogicDeleteManager.skipLogicDelete();
        Users one = QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.CODE.eq(code))
                .and(USERS.STATUS.eq(0))
                .one();
        return ResultVo.success("查询成功", one);
    }
}