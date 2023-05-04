package com.newtouch.uctp.module.business.api.file;

import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.file.dto.FileInsertReqDTO;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.business.service.BusinessFileService;

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
        System.out.println("========进入保存图片方法=========");
        //保存图片到中间表
        List<String> url = reqDTO.getUrl();
        for(int a=0;a<url.size();a++){
            BusinessFileDO businessFileDO = new BusinessFileDO();
            System.out.println("========进入循环=========");
            businessFileDO.setId(Long.valueOf(url.get(a)));
            businessFileDO.setMainId(Long.valueOf(reqDTO.getMainId()));//主表id
            businessFileDO.setFileType(reqDTO.getType());//图片类型
            businessFileDO.setTenantId(reqDTO.getTenantId());

            businessFileService.insert(businessFileDO);
            System.out.println("===============插入图片中间表================Id:" + businessFileDO.getId());
            System.out.println(JSONObject.toJSON(businessFileDO));
        }
        return success("提交成功");
    }
}
