package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class PicResp {

    @Schema(description = "驾驶证")
    private  List<String> drivingPics;
    @Schema(description = "行驶证")
    private  List<String> certificatePics;

    @Schema(description = "首次登记时间")
    private String firstRegistDate;

    @Schema(description = "发动机编号")
    private String engineNum;
}
