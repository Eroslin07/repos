package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarCostVO;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.CostExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 我的费用部分信息 Service 接口
 *
 * @author lc
 */
public interface CostService {

    /**
     * 获得我的费用部分信息
     *
     * @param example 费用查询实体
     * @return 我的费用VO
     */

    List<AppMyCostVO> getMyCosts(@Param("example") CostExample example);

    AppMyCostVO getMyCosts1(@PathVariable String brand,@PathVariable String year, @PathVariable String mon);


    /**
     * 获得车辆费用信息
     *
     * @param id
     * @return 车辆费用信息
     */
    AppCarCostVO getCarCosts(String id);



}
