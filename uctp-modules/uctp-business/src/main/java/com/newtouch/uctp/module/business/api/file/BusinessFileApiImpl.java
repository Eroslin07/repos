package com.newtouch.uctp.module.business.api.file;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.file.dto.FileInsertReqDTO;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.business.service.BusinessFileService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class BusinessFileApiImpl implements BusinessFileApi {
    @Resource
    private BusinessFileService businessFileService;

    @Override
    public CommonResult<String> saveToBusinessFile(FileInsertReqDTO reqDTO ) {
        //保存图片到中间表
        List<String> carUrl = reqDTO.getUrl();
        for(int a=0;a<carUrl.size();a++){//车辆图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(carUrl.get(a)));
            businessFileDO.setMainId(Long.valueOf(reqDTO.getMainId()));//主表id
            businessFileDO.setFileType(reqDTO.getType());//图片类型
            businessFileDO.setTenantId(reqDTO.getTenantId());
            businessFileService.insert(businessFileDO);
        }
        return success("提交成功");
    }
}
