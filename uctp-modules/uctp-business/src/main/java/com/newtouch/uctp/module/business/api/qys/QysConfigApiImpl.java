package com.newtouch.uctp.module.business.api.qys;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.qys.dto.QysConfigDTO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
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

    @Override
    public CommonResult<Boolean> userAuth(Long userId) {
        qysConfigService.userAuth(userId);
        return success(Boolean.TRUE);
    }

    @Override
    public CommonResult<Boolean> companySign(Long contractId) {
        qysConfigService.companySign(contractId);
        return success(Boolean.TRUE);
    }

    @Override
    public CommonResult<QysConfigDTO> getByDeptId(Long deptId) {
        QysConfigDO configDO = qysConfigService.getByDeptId(deptId);
//        return success(QysConfigConvert.INSTANCE.convert02(configDO));
        return success(new QysConfigDTO());
    }
}
