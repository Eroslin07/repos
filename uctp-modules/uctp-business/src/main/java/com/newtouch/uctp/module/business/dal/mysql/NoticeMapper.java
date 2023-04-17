package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 我的费用 Mapper
 */
@Mapper
public interface NoticeMapper extends BaseMapperX<NoticeInfoDO> {


    List<NoticeInfoDO> getNotices(String businessID);

    void updateNoticeStatus(@Param("vo") NoticeVO noticeVO);

    void updateAllNoticeStatus(String id);

    void deleteAllNoticeStatus(String id);

    int getUnreadNoticeCount(String businessID);



}
