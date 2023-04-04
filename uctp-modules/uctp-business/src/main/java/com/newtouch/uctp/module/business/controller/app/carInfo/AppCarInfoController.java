package com.newtouch.uctp.module.business.controller.app.carInfo;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.convert.app.CarInfoConvert;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.service.CarInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "App管理 - 车辆管理")
@RestController
@RequestMapping("/uctp/car-info")
@Validated
public class AppCarInfoController {
    @Resource
    private CarInfoService carInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建车辆主表")
    public CommonResult<String> createCarInfo(@Valid @RequestBody AppCarInfoCreateReqVO createReqVO) {
        return success(carInfoService.createCarInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新车辆主表")
    public CommonResult<Boolean> updateCarInfo(@Valid @RequestBody AppCarInfoUpdateReqVO updateReqVO) {
        carInfoService.updateCarInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除车辆主表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteCarInfo(@RequestParam("id") String id) {
        carInfoService.deleteCarInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得车辆主表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCarInfoRespVO> getCarInfo(@RequestParam("id") String id) {
        CarInfoDO carInfo = carInfoService.getCarInfo(id);
        return success(CarInfoConvert.INSTANCE.convert(carInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得车辆主表列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024")
    public CommonResult<List<AppCarInfoRespVO>> getCarInfoList(@RequestParam("ids") Collection<String> ids) {
        List<CarInfoDO> list = carInfoService.getCarInfoList(ids);
        return success(CarInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得车辆主表分页")
    public CommonResult<PageResult<AppCarInfoRespVO>> getCarInfoPage(@Valid AppCarInfoPageReqVO pageVO) {
        PageResult<CarInfoDO> pageResult = carInfoService.getCarInfoPage(pageVO);
        return success(CarInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/home/page")
    @Operation(summary = "获得APP首页分页")
    public CommonResult<PageResult<AppHomeCarInfoRespVO>> getHomeCarInfoPage(@Valid AppHomeCarInfoPageReqVO pageVO) {
        PageResult<AppHomeCarInfoRespVO> pageResult = carInfoService.getHomeCarInfoPage(pageVO);
        return success(pageResult);
    }



}