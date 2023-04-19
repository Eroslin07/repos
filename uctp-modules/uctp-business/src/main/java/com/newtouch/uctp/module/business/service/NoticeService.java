package com.newtouch.uctp.module.business.service;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;

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

    List<NoticeInfoDO> getNotices(String businessID);
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
    /**
     * 保存消息
     *
     */
   // String saveTaskNotice1(String type, String contentType,String reason, BpmFormMainVO bpmFormMainVO);

    String saveTaskNotice(String type, String contentType,String reason, BpmFormResVO bpmFormResVO);
    /**
     * 保存消息
     *
     */
    String saveNotice(Map<String,String> map);
    /**
     * 获取未读消息条数
     *
     */
    int getUnreadNoticeCount(String businessID);

}
