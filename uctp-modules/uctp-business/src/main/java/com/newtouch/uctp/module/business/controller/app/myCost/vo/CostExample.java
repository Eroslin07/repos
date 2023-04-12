package com.newtouch.uctp.module.business.controller.app.myCost.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
* 我的费用  VO
*/
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostExample {


    @Schema(description = "车辆品牌")
    private String brand;

    @Schema(description = "年度")
    private String year;

    @Schema(description = "月份")
    private String mon;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }
}
