package com.newtouch.uctp.module.bpm.api.task;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceByKeyReqDTO;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.newtouch.uctp.module.bpm.service.task.BpmProcessInstanceService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * Flowable 流程实例 Api 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class BpmProcessInstanceApiImpl implements BpmProcessInstanceApi {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @Override
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO reqDTO) {
        return processInstanceService.createProcessInstance(userId, reqDTO);
    }

    @Override
    public CommonResult<String> createProcessInstanceByKey(BpmProcessInstanceByKeyReqDTO createReqVO) {
        return success(processInstanceService.createProcessInstanceByKey(null, createReqVO.getProcDefKey(), createReqVO.getVariables()));
    }
}
