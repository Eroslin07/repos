package com.newtouch.uctp.module.bpm.api.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceByKeyReqDTO;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.newtouch.uctp.module.bpm.enums.ApiConstants;

/**
 * 流程实例 Api 接口
 *
 * @author 芋道源码
 */
@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 流程实例")
public interface BpmProcessInstanceApi {

    String PREFIX = ApiConstants.PREFIX + "/app/process-instance";

    /**
     * 创建流程实例（提供给内部）
     *
     * @param userId 用户编号
     * @param reqDTO 创建信息
     * @return 实例的编号
     */
    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建流程实例（提供给内部）")
    String createProcessInstance(@RequestParam("userId") Long userId, @Valid BpmProcessInstanceCreateReqDTO reqDTO);

    /**
     * 利润提现流程可远程调用
     * @param createReqVO
     * @return
     */
    @PostMapping(PREFIX + "/v3/create")
    @Operation(summary = "根据流程定义标识（业务类型）新建流程")
    public CommonResult<String> createProcessInstanceByKey(@Valid @RequestBody BpmProcessInstanceByKeyReqDTO createReqVO);
}
