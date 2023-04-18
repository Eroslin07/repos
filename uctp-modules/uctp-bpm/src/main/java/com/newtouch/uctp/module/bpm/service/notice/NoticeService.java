package com.newtouch.uctp.module.bpm.service.notice;


import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;


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
    String saveTaskNotice(String type, String contentType,String reason, BpmFormMainVO bpmFormResVO);
    /**
     * 保存消息
     *
     */
    String saveNotice(Map<String,String> map);

}
