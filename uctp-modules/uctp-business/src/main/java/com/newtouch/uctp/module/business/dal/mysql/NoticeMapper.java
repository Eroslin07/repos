package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 我的费用 Mapper
 */
@Mapper
public interface NoticeMapper {


    List<NoticeInfoDO> getNotices();

    void updateNoticeStatus(@Param("vo") NoticeVO noticeVO);


}
