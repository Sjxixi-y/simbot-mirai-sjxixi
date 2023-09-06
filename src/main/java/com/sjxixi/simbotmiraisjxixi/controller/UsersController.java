package com.sjxixi.simbotmiraisjxixi.controller;


import com.sjxixi.simbotmiraisjxixi.entity.Users;
import com.sjxixi.simbotmiraisjxixi.service.UsersService;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "用户管理")
@CrossOrigin
@RestController
@RequestMapping("users")
public class UsersController {

    @Resource
    UsersService usersService;


    /**
     * 新增用户
     *
     * @param users 用户实体
     * @return 返回对象
     */
    @Operation(summary = "新增用户", description = "新增用户",
            parameters = {@Parameter(name = "user", description = "用户实体")})
    //
    @PostMapping
    public ResultVo addUser(@RequestBody Users users) {
        return usersService.addUser(users);
    }

    /**
     * 根据 ID 删除用户
     *
     * @param id 用户ID
     * @return 返回对象
     */
    @Operation(summary = "删除用户", description = "根据ID删除用户",
            parameters = {@Parameter(name = "id", description = "用户id")})
    //
    @DeleteMapping("{id}")
    public ResultVo deleteUser(@PathVariable Integer id) {
        return usersService.deleteUserById(id);
    }

    /**
     * 根据 ID 取消删除
     *
     * @param id 用户ID
     * @return 返回对象
     */
    @Operation(summary = "取消删除用户", description = "根据ID取消删除用户",
            parameters = {@Parameter(name = "id", description = "黑名单用户id")})
    //
    @DeleteMapping("/false/{id}")
    public ResultVo deleteFalseUser(@PathVariable Integer id) {
        return usersService.deleteFalseUserById(id);
    }

    /**
     * 更新用户
     *
     * @param users 用户实体
     * @return 返回对象
     */
    @Operation(summary = "更新用户", description = "更新用户",
            parameters = {@Parameter(name = "user", description = "用户实体")})
    //
    @PutMapping
    public ResultVo updateUser(@RequestBody Users users) {
        return usersService.updateUser(users);
    }

    /**
     * 分页查询 + 模糊查询 + 白名单
     *
     * @param paging 分页对象
     * @return 返回对象
     */
    // 文档配置
    @Operation(summary = "获取用户", description = "分页查询",
            parameters = {@Parameter(name = "paging", description = "查询对象")})
    //
    @GetMapping
    public ResultVo getUser(PagingVo paging) {
        return usersService.getUser(paging);
    }

    /**
     * 分页查询 + 模糊查询 + 黑名单
     *
     * @param paging 分页对象
     * @return 返回对象
     */
    //
    @Operation(summary = "获取黑名单", description = "分页查询",
            parameters = {@Parameter(name = "paging", description = "查询对象")})
    //
    @GetMapping("/false")
    public ResultVo getFalse(PagingVo paging) {
        return usersService.getFalseUser(paging);
    }
}