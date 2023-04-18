package com.newtouch.uctp.module.bpm.service.notice;


import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 消息 Service 接口
 *
 * @author hr
 */
public interface NoticeService {


    /**
     * 保存消息
     *
     */
    String saveTaskNotice(String type, String contentType,String reason, BpmFormResVO bpmFormResVO);
    /**
     * 保存消息
     *
     */
    String saveNotice(Map<String,String> map);

}
