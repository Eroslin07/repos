package com.newtouch.uctp.module.business.service.impl;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.dal.mysql.CostMapper;
import com.newtouch.uctp.module.business.service.CostService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 车辆主表 Service 实现类
 *
 * @author lc
 */
@Service
@Validated
public class CostServiceImpl implements CostService {

    @Resource
    private CostMapper costMapper;


    @Override
    public List<AppMyCostVO> getMyCosts(CostExample example) {
        return costMapper.getMyCosts(example);
    }

    @Override
    public AppMyCostVO getMyCosts1(String brand, String year, String mon) {
        return costMapper.getMyCosts1(brand,year,mon);
    }
}
