package com.newtouch.uctp.module.business.controller.app.carInfo;

import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoRespVO;
import com.newtouch.uctp.module.business.convert.app.CarInfoConvert;
import com.newtouch.uctp.module.business.domain.app.CarInfoDO;
import com.newtouch.uctp.module.business.service.CarInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;


@Tag(name =  "用户 APP - 车辆主表")
@RestController
@RequestMapping("/uctp/car-info")
@Validated
public class AppCarInfoController {

    @Resource
    private CarInfoService carInfoService;


    @GetMapping("/page")
    @Operation(summary = "获得车辆主表分页")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<PageResult<AppCarInfoRespVO>> getCarInfoPage(@Valid AppCarInfoPageReqVO pageVO) {
        PageResult<CarInfoDO> pageResult = carInfoService.getCarInfoPage(pageVO);
        return success(CarInfoConvert.INSTANCE.convertPage(pageResult));
    }


}
