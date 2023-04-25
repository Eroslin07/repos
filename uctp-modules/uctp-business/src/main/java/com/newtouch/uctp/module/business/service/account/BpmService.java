package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "bpm-server")
public interface BpmService {

    @PostMapping("app-api/bpm/app/process-instance/v3/create")
    CommonResult<String> create(@RequestHeader("tenant-id") Long tenantId,
                                @RequestHeader("Authorization") String token,
                                @RequestBody Map<String, Object> body);
}
