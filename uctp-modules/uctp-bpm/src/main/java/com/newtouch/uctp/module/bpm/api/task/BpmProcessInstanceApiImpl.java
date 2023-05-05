package com.newtouch.uctp.module.bpm.api.task;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceByKeyReqDTO;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.newtouch.uctp.module.bpm.service.task.BpmProcessInstanceService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * Flowable 流程实例 Api 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Service
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
