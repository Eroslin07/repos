package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import org.apache.ibatis.annotations.Param;

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
    /**
     * 更新单个消息状态
     *
     */
    void updateNoticeStatus(@Param("vo") NoticeVO noticeVO);

    /**
     * 批量更新消息状态
     *
     */
    void updateAllNoticeStatus(String id);

    void updateAllNoticeStatus(List<String> list);


    /**
     * 批量删除消息/目前只考虑单个
     *
     */
    //void deleteAllNoticeStatus(String id);
    void deleteAllNoticeStatus(List<String> list);


}
