package com.newtouch.uctp.module.infra.service.logger;

import com.newtouch.uctp.framework.apilog.core.service.ApiAccessLog;
import com.newtouch.uctp.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import com.newtouch.uctp.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogExportReqVO;
import com.newtouch.uctp.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import com.newtouch.uctp.module.infra.convert.logger.ApiAccessLogConvert;
import com.newtouch.uctp.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import com.newtouch.uctp.module.infra.dal.mysql.logger.ApiAccessLogMapper;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * API 访问日志 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ApiAccessLogServiceImpl implements ApiAccessLogService {

    @Resource
    private ApiAccessLogMapper apiAccessLogMapper;

    @Override
    public void createApiAccessLog(ApiAccessLogCreateReqDTO createDTO) {
        ApiAccessLogDO apiAccessLog = ApiAccessLogConvert.INSTANCE.convert(createDTO);
        apiAccessLogMapper.insert(apiAccessLog);
    }

    @Override
    public PageResult<ApiAccessLogDO> getApiAccessLogPage(ApiAccessLogPageReqVO pageReqVO) {
        return apiAccessLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ApiAccessLogDO> getApiAccessLogList(ApiAccessLogExportReqVO exportReqVO) {
        return apiAccessLogMapper.selectList(exportReqVO);
    }

}
