package com.newtouch.uctp.module.system.api.dict;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.system.api.dict.dto.DictDataRespDTO;
import com.newtouch.uctp.module.system.controller.admin.dict.vo.data.DictDataExportReqVO;
import com.newtouch.uctp.module.system.convert.dict.DictDataConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dict.DictDataDO;
import com.newtouch.uctp.module.system.service.dict.DictDataService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class DictDataApiImpl implements DictDataApi {

    @Resource
    private DictDataService dictDataService;

    @Override
    public CommonResult<Boolean> validateDictDatas(String dictType, Collection<String> values) {
        dictDataService.validateDictDataList(dictType, values);
        return success(true);
    }

    @Override
    public CommonResult<DictDataRespDTO> getDictData(String dictType, String value) {
        DictDataDO dictData = dictDataService.getDictData(dictType, value);
        return success(DictDataConvert.INSTANCE.convert02(dictData));
    }

    @Override
    public CommonResult<DictDataRespDTO> parseDictData(String dictType, String label) {
        DictDataDO dictData = dictDataService.parseDictData(dictType, label);
        return success(DictDataConvert.INSTANCE.convert02(dictData));
    }

    @Override
    public CommonResult<DictDataRespDTO> DictDataList(String dictType) {
        DictDataExportReqVO reqVO = new DictDataExportReqVO();
        List<DictDataDO> dictDataList = dictDataService.getDictDataList(reqVO);

        return null;
    }

}
