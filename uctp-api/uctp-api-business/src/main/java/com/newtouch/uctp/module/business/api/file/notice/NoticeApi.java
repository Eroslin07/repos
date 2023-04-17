package com.newtouch.uctp.module.business.api.file.notice;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import com.newtouch.uctp.module.business.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name =  "RPC 服务 - 消息")
public interface NoticeApi {

    String PREFIX = ApiConstants.PREFIX + "/notice";


    @PostMapping(PREFIX + "/saveTaskNotice")
    @Operation(summary = "保存流程消息信息")
    CommonResult<String> saveTaskNotice(@RequestParam("type") String type,@RequestParam("contentType") String contentType,@RequestParam("reason") String reason, @Valid @RequestBody BpmFormResVO bpmFormMainVO);


}
