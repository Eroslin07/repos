package com.newtouch.uctp.framework.operatelog.core.service;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;

import com.newtouch.uctp.module.system.api.logger.OperateLogApi;
import com.newtouch.uctp.module.system.api.logger.dto.OperateLogCreateReqDTO;

/**
 * 操作日志 Framework Service 实现类
 *
 * 基于 {@link OperateLogApi} 远程服务，记录操作日志
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
public class OperateLogFrameworkServiceImpl implements OperateLogFrameworkService {

    private final OperateLogApi operateLogApi;

    @Override
    @Async
    public void createOperateLog(OperateLog operateLog) {
        OperateLogCreateReqDTO reqDTO = BeanUtil.copyProperties(operateLog, OperateLogCreateReqDTO.class);
        //operateLogApi.createOperateLog(reqDTO).checkError();
        operateLogApi.createOperateLog(reqDTO);
    }

}
