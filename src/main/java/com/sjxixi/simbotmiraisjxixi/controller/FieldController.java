package com.sjxixi.simbotmiraisjxixi.controller;


import com.sjxixi.simbotmiraisjxixi.entity.Field;
import com.sjxixi.simbotmiraisjxixi.service.FieldService;
import com.sjxixi.simbotmiraisjxixi.util.NumberUtil;
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

    // 文档配置
    @Operation(summary = "新增信息", description = "新增信息",
            parameters = {@Parameter(name = "field", description = "信息实体")})
    @ApiResponse(responseCode = "200", description = "新增成功")
    //
    @PostMapping
    public ResultVo addField(Field field) {
        return fieldService.addField(field);
    }

    // 文档配置
    @Operation(summary = "删除信息", description = "根据ID删除信息",
            parameters = {@Parameter(name = "id", description = "信息id")})
    @ApiResponse(responseCode = "200", description = "删除成功")
    //
    @DeleteMapping("{id}")
    public ResultVo deleteFieldById(@PathVariable Integer id) {
        return fieldService.deleteFieldById(id);
    }

    // 文档配置
    @Operation(summary = "更新信息", description = "更新信息",
            parameters = {@Parameter(name = "field", description = "信息实体")})
    @ApiResponse(responseCode = "200", description = "更新成功")
    //
    @PutMapping
    public ResultVo update(Field field) {
        return fieldService.updateField(field);
    }

    // 文档配置
    @Operation(summary = "获取重要信息", description = "分页查询",
            parameters = {@Parameter(name = "paging", description = "查询对象")})
    @ApiResponse(responseCode = "200", description = "查询成功")
    //
    @GetMapping("/0")
    public ResultVo getFieldImportant(PagingVo paging) {
        return fieldService.getFieldImportant(paging, NumberUtil.IMPORTANCE, true);
    }

    //
    @Operation(summary = "获取非信息", description = "分页查询",
            parameters = {@Parameter(name = "paging", description = "查询对象")})
    @ApiResponse(responseCode = "200", description = "查询成功")
    //
    @GetMapping
    public ResultVo getFieldNotImportant(PagingVo paging) {
        return fieldService.getFieldImportant(paging, NumberUtil.NOT_IMPORTANCE, true);
    }
}
