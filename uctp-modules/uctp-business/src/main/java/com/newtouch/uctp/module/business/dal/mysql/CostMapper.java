package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.module.business.controller.app.myCost.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.CostExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 我的费用 Mapper
 */
@Mapper
public interface CostMapper  {


    List<AppMyCostVO> getMyCosts(@Param("example") CostExample example);

    AppMyCostVO getMyCosts1(@PathVariable String brand,@PathVariable String year, @PathVariable String mon);




}
