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
     * Web 新增
     *
     * @param user 用户实体
     * @return 返回对象
     */
    public ResultVo addUser(Users user) {

        if (addUserSimBot(user) == ResultVo.ERROR) {
            throw new RuntimeException("存在用户");
        }

        return ResultVo.success("新增成功！", null);
    }

    /**
     * sim-bot 新增
     *
     * @param user 用户对象
     * @return 返回对象
     */
    public Integer addUserSimBot(Users user) {
        logger.info("[成员新增] 新增如下成员：" + user);

        // 判断是否存在此用户
        LogicDeleteManager.skipLogicDelete();

        if (QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.CODE.eq(user.getCode()))
                .one() != null) {
            // 存在此用户
            logger.info("[成员新增] 用户：" + user.getName() + " QQ: " + user.getCode() + " 已存在");

            return ResultVo.ERROR;
        }

        logger.info("[成员新增] 用户：" + user.getName() + " QQ: " + user.getCode() + " 不存在，正在执行新增");

        usersMapper.insertSelective(user);

        return ResultVo.SUCCESS;
    }

    /**
     * Web 删除
     *
     * @param id 用户id
     * @return 返回对象
     */
    public ResultVo deleteUserById(Integer id) {

        if (deleteUserByIdSimBot(id) != ResultVo.SUCCESS) {
            throw new RuntimeException("无法删除，请重试");
        }

        return ResultVo.success("删除成功！", null);
    }

    /**
     * sim-bot 删除
     *
     * @param id 用户id
     * @return 返回对象
     */
    public Integer deleteUserByIdSimBot(Integer id) {

        logger.info("[黑名单] 用户 ID: " + id + " 被拉入黑名单");

        usersMapper.deleteById(id);

        return ResultVo.SUCCESS;
    }

    /**
     * Web 根据QQ号删除
     *
     * @param code QQ号
     * @return 返回对象
     */
    public ResultVo deleteUserByCode(String code) {

        if (deleteUserByCodeSimBot(code) != 200) {
            throw new RuntimeException("删除失败");
        }

        return ResultVo.fail("删除成功", null);
    }

    /**
     * sim-bot 根据QQ号删除
     *
     * @param code QQ号
     * @return 返回对象
     */
    public Integer deleteUserByCodeSimBot(String code) {
        logger.info("[黑名单] 用户 QQ号: " + code + " 被拉入黑名单");

        QueryWrapper where = new QueryWrapper().where(USERS.CODE.eq(code));
        int i = usersMapper.deleteByQuery(where);

        if (i == 1) {
            logger.info("[黑名单] 成功！");

            return ResultVo.SUCCESS;
        }

        logger.info("[黑名单] 失败！");

        return ResultVo.ERROR;
    }

    /**
     * Web 通过 ID 将黑名单变为白名单
     *
     * @param id ID
     * @return 返回结果
     */
    public ResultVo deleteFalseUserById(Integer id) {

        if (deleteFalseUserByIdSimBot(id) != 200) {
            throw new RuntimeException("修改失败");
        }

        return ResultVo.success("修改成功！", null);
    }

    /**
     * sim-bot 通过 ID 将黑名单变为白名单
     *
     * @param id ID
     * @return 返回对象
     */
    public Integer deleteFalseUserByIdSimBot(Integer id) {
        logger.info("[黑名单] 用户 ID: " + id + " 从黑名单中释放");

        LogicDeleteManager.skipLogicDelete();

        if (UpdateChain.of(Users.class)
                .set(USERS.STATUS, 0)
                .where(USERS.ID.eq(id))
                .update()) {

            logger.info("[黑名单] 成功");

            return ResultVo.SUCCESS;
        }

        logger.info("[黑名单] 失败");

        return ResultVo.ERROR;
    }

    /**
     * 修改
     *
     * @param user 用户实体
     * @return 返回对象
     */
    public ResultVo updateUser(Users user) {

        if (updateUserSimBot(user) != ResultVo.SUCCESS) {
            throw new RuntimeException("修改失败");
        }

        return ResultVo.success("修改成功！", null);
    }

    /**
     * sim-bot 修改
     *
     * @param user 用户实体
     * @return 返回对象
     */
    public Integer updateUserSimBot(Users user) {

        logger.info("[成员修改] 修改内容为：" + user);

        if (usersMapper.update(user) != 1) {

            logger.info("[成员修改] 失败");

            return ResultVo.ERROR;
        }

        logger.info("[成员修改] 成功");

        return ResultVo.SUCCESS;
    }

    /**
     * Web 模糊查询 + 分页
     *
     * @param paging 用户实体
     * @return 返回对象
     */
    public ResultVo getUser(PagingVo paging) {

        logger.info("[分页查询] 分页内容 " + paging);

        PagingVoUtil.getPagingVo(paging);

        Page<Users> page = QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.CODE.like(paging.getValueOne()).or(USERS.NAME.eq(paging.getValueTow())))
                .and(USERS.STATUS.eq(0))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));

        return ResultVo.successPage("查询成功！", page.getRecords(), page.getTotalRow());
    }

    /**
     * Web 查询黑名单 模糊查询 + 分页
     *
     * @param paging 分页对象
     * @return 返回对象
     */
    public ResultVo getFalseUser(PagingVo paging) {

        logger.info("[分页查询] 分页内容 " + paging);

        PagingVoUtil.getPagingVo(paging);

        LogicDeleteManager.skipLogicDelete();

        Page<Users> page = QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.NAME.like(paging.getValueOne()).or(USERS.CODE.eq(paging.getValueTow())))
                .and(USERS.STATUS.eq(1))
                .page(new Page<>(paging.getPageIndex(), paging.getPageSize()));

        return ResultVo.successPage("查询成功！", page.getRecords(), page.getTotalRow());
    }

    /**
     * sim-bot 通过QQ号查询
     *
     * @param code QQ号
     * @return 返回对象
     */
    public Users getUserByCodeSimBot(String code) {
        logger.info("[QQ查询] QQ号：" + code);

        LogicDeleteManager.skipLogicDelete();

        return QueryChain.of(usersMapper)
                .select(USERS.ALL_COLUMNS).from(USERS)
                .where(USERS.CODE.eq(code))
                .and(USERS.STATUS.eq(0))
                .one();
    }
}