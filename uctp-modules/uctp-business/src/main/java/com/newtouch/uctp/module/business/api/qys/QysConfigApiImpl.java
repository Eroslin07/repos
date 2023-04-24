package com.newtouch.uctp.module.business.api.qys;

import java.io.FileNotFoundException;

import javax.annotation.Resource;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

public class QysConfigApiImpl implements QysConfigApi{
    @Resource
    private QysConfigService qysConfigService;


    @Override
    public CommonResult<Boolean> companyAuth(Long userId) {
        try {
            qysConfigService.companyAuth(userId);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return success(Boolean.TRUE);
    }
}
