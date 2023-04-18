package com.newtouch.uctp.module.business.convert.qys;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackRespVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysCallbackDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 契约锁回调日志 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface QysCallbackConvert {

    QysCallbackConvert INSTANCE = Mappers.getMapper(QysCallbackConvert.class);

    QysCallbackDO convert(QysCallbackCreateReqVO bean);

    QysCallbackDO convert(QysCallbackUpdateReqVO bean);

    QysCallbackRespVO convert(QysCallbackDO bean);

    List<QysCallbackRespVO> convertList(List<QysCallbackDO> list);

    PageResult<QysCallbackRespVO> convertPage(PageResult<QysCallbackDO> page);


}
