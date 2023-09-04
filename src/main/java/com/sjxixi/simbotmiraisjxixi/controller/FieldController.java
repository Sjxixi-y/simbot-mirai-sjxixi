package com.sjxixi.simbotmiraisjxixi.controller;


import com.sjxixi.simbotmiraisjxixi.entity.Field;
import com.sjxixi.simbotmiraisjxixi.service.FieldService;
import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;
import com.sjxixi.simbotmiraisjxixi.vo.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Field", description = "信息管理")
@CrossOrigin
@RestController
@RequestMapping("field")
public class FieldController {
    @Resource
    FieldService fieldService;

    /**
     * 新增字段
     *
     * @param field 用户ID
     * @return 返回对象
     */
    // 文档配置
    @Operation(summary = "新增信息", description = "新增信息",
            parameters = {@Parameter(name = "field", description = "信息实体")})
    @ApiResponse(responseCode = "200", description = "新增成功")
    //
    @PostMapping
    public ResultVo addField(@RequestBody Field field) {
        return fieldService.addField(field);
    }

    /**
     * 修改字段
     *
     * @param field 用户ID
     * @return 返回对象
     */
    @Operation(summary = "更新信息", description = "更新信息",
            parameters = {@Parameter(name = "field", description = "信息实体")})
    @ApiResponse(responseCode = "200", description = "更新成功")
    //
    @PutMapping
    public ResultVo update(@RequestBody Field field) {
        System.out.println("FieldController: " + field);
        return fieldService.updateField(field);
    }

    /**
     * 分页 + 模糊 查询
     *
     * @param paging 分页对象
     * @return 返回对象
     */
    @Operation(summary = "获取重要信息", description = "分页查询",
            parameters = {@Parameter(name = "paging", description = "查询对象")})
    @ApiResponse(responseCode = "200", description = "查询成功")
    //
    @GetMapping
    public ResultVo getFieldImportant(PagingVo paging) {
        return fieldService.getFieldImportant(paging, true);
    }
}
