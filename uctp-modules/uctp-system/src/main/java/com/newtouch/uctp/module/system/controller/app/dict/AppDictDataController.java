package com.newtouch.uctp.module.system.controller.app.dict;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.system.service.dict.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
@Tag(name =  "APP - 字典数据")
@RestController
@RequestMapping("/system/dict-data")
@Validated
public class AppDictDataController {
    @Resource
    private DictDataService dictDataService;
    @GetMapping("/list-map")
    @Operation(summary = "获得车辆状态字典数据列表", description = "一般用于App缓存字典数据在本地")
    public CommonResult<Map<String,String>> getDictDataListMap(@RequestParam String[] dictTypes) {
        return success(dictDataService.getDictDataListMap(dictTypes));
    }
}
