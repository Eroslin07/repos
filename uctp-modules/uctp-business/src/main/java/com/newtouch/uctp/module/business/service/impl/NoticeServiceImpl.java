package com.newtouch.uctp.module.business.service.impl;

import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import com.newtouch.uctp.module.business.dal.mysql.NoticeMapper;
import com.newtouch.uctp.module.business.service.NoticeService;
import com.newtouch.uctp.module.business.util.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Override
    public void updateNoticeStatus(NoticeVO noticeVO) {
        noticeMapper.updateNoticeStatus(noticeVO);
    }

    @Override
    public void updateAllNoticeStatus(String id) {
        noticeMapper.updateAllNoticeStatus(id);

    }

    @Override
    public void updateAllNoticeStatus(List<String> list) {

        if (list.size()>2) {
            Map<String, List<String>> map = ListUtil.groupList(list);

            for (int i = 0; i < map.size(); i++) {
                String ids = "";
                for (int a = 0; a < map.get("keyName" + i).size(); a++) {
                    ids += "'" + map.get("keyName" + i).get(a) + "',";
                }
                ids += "'#'";
                noticeMapper.updateAllNoticeStatus(ids);
            }
        }else {
            String ids = "";
            for (int i = 0; i < list.size(); i++) {

                    ids += "'" + list.get(i) + "',";
            }
            ids += "'#'";
            noticeMapper.updateAllNoticeStatus(ids);
        }
    }

    @Override
    public void deleteAllNoticeStatus(List<String> list) {
        String ids = "";
        for (int i = 0; i < list.size(); i++) {

            ids += "'" + list.get(i) + "',";
        }
        ids += "'#'";
        noticeMapper.deleteAllNoticeStatus(ids);
    }
}
