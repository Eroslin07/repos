package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CostExample;
import com.newtouch.uctp.module.business.domain.app.NoticeInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 消息 Service 接口
 *
 * @author lc
 */
public interface NoticeService {

    /**
     * 获得消息信息
     * @return 消息do
     */

    List<NoticeInfoDO> getNotices();



}
