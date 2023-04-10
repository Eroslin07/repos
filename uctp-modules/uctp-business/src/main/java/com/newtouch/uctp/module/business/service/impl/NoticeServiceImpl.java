package com.newtouch.uctp.module.business.service.impl;

import com.newtouch.uctp.module.business.domain.app.NoticeInfoDO;
import com.newtouch.uctp.module.business.dal.mysql.NoticeMapper;
import com.newtouch.uctp.module.business.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 车辆主表 Service 实现类
 *
 * @author lc
 */
@Service
@Validated
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;


    @Override
    public List<NoticeInfoDO> getNotices() {
        return noticeMapper.getNotices();
    }
}
