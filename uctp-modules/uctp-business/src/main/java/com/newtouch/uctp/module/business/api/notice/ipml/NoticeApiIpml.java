package com.newtouch.uctp.module.business.api.notice.ipml;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.file.notice.NoticeApi;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import com.newtouch.uctp.module.business.service.NoticeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class NoticeApiIpml implements NoticeApi {

    @Resource
    private NoticeService noticeService;

    @Override
    public CommonResult<String> saveTaskNotice(String type, String contentType, String reason, BpmFormResVO bpmFormMainVO) {


        String notice = noticeService.saveTaskNotice(type, contentType, reason, bpmFormMainVO);
        return success(notice);
    }
}
