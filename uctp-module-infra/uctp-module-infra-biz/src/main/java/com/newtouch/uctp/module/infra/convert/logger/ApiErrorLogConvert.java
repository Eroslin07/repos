package com.newtouch.uctp.module.infra.convert.logger;

import com.newtouch.uctp.framework.apilog.core.service.ApiErrorLog;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import com.newtouch.uctp.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogExcelVO;
import com.newtouch.uctp.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogRespVO;
import com.newtouch.uctp.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * API 错误日志 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ApiErrorLogConvert {

    ApiErrorLogConvert INSTANCE = Mappers.getMapper(ApiErrorLogConvert.class);

    ApiErrorLogRespVO convert(ApiErrorLogDO bean);

    PageResult<ApiErrorLogRespVO> convertPage(PageResult<ApiErrorLogDO> page);

    List<ApiErrorLogExcelVO> convertList02(List<ApiErrorLogDO> list);

    ApiErrorLogDO convert(ApiErrorLogCreateReqDTO bean);

}
