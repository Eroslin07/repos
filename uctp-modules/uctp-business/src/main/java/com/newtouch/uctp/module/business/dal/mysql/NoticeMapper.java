package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CostExample;
import com.newtouch.uctp.module.business.domain.app.NoticeInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 我的费用 Mapper
 */
@Mapper
public interface NoticeMapper {


    List<NoticeInfoDO> getNotices();



}
