package com.newtouch.uctp.module.business.controller.app.carinfodetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.CarInfoDetailsService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "App管理 - 车辆明细")
@RestController
@RequestMapping("/uctp/car-info-details")
@Validated
public class CarInfoDetailsController {

    @Resource
    private CarInfoDetailsService carInfoDetailsService;

    @PutMapping("/updateTransManage")
    @Operation(summary = "更新车辆开票的转入地")
    public CommonResult<Boolean> updateTransManage(@RequestParam(name = "carId") Long carId,
                                                   @RequestParam(name = "transManageName", required = false) String transManageName,
                                                   @RequestParam(name = "sellTransManageName", required = false) String sellTransManageName) {
        carInfoDetailsService.updateTransManage(carId, transManageName, sellTransManageName);
        return success(true);
    }
}
