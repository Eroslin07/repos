package com.newtouch.uctp.module.business.api.file;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.file.dto.FileInsertReqDTO;
import com.newtouch.uctp.module.business.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name =  "RPC 服务 - 文件")
public interface BusinessFileApi {

    String PREFIX = ApiConstants.PREFIX + "/file";


    @PostMapping(PREFIX + "/saveToBusinessFile")
    @Operation(summary = "保存图片到中间表")
    CommonResult<String> saveToBusinessFile(@Valid @RequestBody FileInsertReqDTO createReqDTO);

}
