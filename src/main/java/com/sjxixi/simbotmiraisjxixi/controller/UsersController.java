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


    // 文档配置
    @Operation(summary = "新增用户", description = "新增用户",
            parameters = {@Parameter(name = "user", description = "用户实体")})
    @ApiResponse(responseCode = "200", description = "新增成功")
    //
    @PostMapping
    public ResultVo addUser(@RequestBody Users users) {
        return usersService.addUser(users);
    }

    // 文档配置
    @Operation(summary = "删除用户", description = "根据ID删除用户",
            parameters = {@Parameter(name = "id", description = "用户id")})
    @ApiResponse(responseCode = "200", description = "删除成功")
    //
    @DeleteMapping("{id}")
    public ResultVo deleteUser(@PathVariable Integer id) {
        return usersService.deleteUserById(id);
    }

    @DeleteMapping("/false/{id}")
    public ResultVo deleteFalseUser(@PathVariable Integer id) {
        return usersService.deleteFalseUserById(id);
    }

    // 文档配置
    @Operation(summary = "更新用户", description = "更新用户",
            parameters = {@Parameter(name = "user", description = "用户实体")})
    @ApiResponse(responseCode = "200", description = "更新成功")
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
    @ApiResponse(responseCode = "200", description = "查询成功")
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
    @ApiResponse(responseCode = "200", description = "查询成功")
    //
    @GetMapping("/false")
    public ResultVo getFalse(PagingVo paging) {
        return usersService.getFalseUser(paging);
    }
}