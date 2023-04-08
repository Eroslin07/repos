package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppSellCarInfoPageReqVO extends PageParam {
    @Schema(description = "车架号/车辆品牌")
    private String searchValue;
    @Schema(description = "当前登录人Id，目前不知道能不能取到值")
    private Long businessId;
}
