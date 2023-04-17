package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
public class HomeCountVO implements Serializable {
    @Schema(description = "车辆信息")
    private Integer status;
    @Schema(description = "车辆状态说明")
    private String label;
    @Schema(description = "车辆数量")
    private Long num;
    @Schema(description = "下级状态")
    private List<HomeCountVO> child = Lists.newArrayList();

}
