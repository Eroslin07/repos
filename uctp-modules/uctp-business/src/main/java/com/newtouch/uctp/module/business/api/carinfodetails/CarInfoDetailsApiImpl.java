package com.newtouch.uctp.module.business.api.carinfodetails;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.CarInfoDetailsService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/5/9 17:02
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class CarInfoDetailsApiImpl implements CarInfoDetailsApi {
    @Resource
    private CarInfoDetailsService carInfoDetailsService;
    @Override
    public CommonResult<Boolean> updateTransManage(Long carId, String transManageName, String sellTransManageName) {
        carInfoDetailsService.updateTransManage(carId, transManageName, sellTransManageName);
        return success(true);
    }
}
