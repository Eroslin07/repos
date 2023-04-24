package com.newtouch.uctp.module.business.api.qys;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;

import javax.annotation.Resource;

import java.io.FileNotFoundException;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

public class QysConfigApiImpl implements QysConfigApi{
    @Resource
    private QysConfigService qysConfigService;


    @Override
    public CommonResult<Boolean> companyAuth(Long userId) throws FileNotFoundException {
        qysConfigService.companyAuth(userId);
        return success(Boolean.TRUE);
    }
}
