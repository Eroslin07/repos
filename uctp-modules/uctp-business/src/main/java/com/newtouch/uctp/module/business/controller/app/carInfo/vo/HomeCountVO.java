package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
public class HomeCountVO implements Serializable {
    private Integer status;

    private String label;

    private Long num;

    private List<HomeCountVO> child = Lists.newArrayList();

}
