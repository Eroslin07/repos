package com.newtouch.uctp.module.business.convert.qys;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.newtouch.uctp.module.business.api.qys.dto.QysConfigDTO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigRespVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 契约锁 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface QysConfigConvert {

    QysConfigConvert INSTANCE = Mappers.getMapper(QysConfigConvert.class);

    QysConfigDO convert(QysConfigCreateReqVO bean);

    QysConfigDO convert(QysConfigUpdateReqVO bean);

    QysConfigRespVO convert(QysConfigDO bean);

    List<QysConfigRespVO> convertList(List<QysConfigDO> list);

    PageResult<QysConfigRespVO> convertPage(PageResult<QysConfigDO> page);


    List<QiyuesuoChannelProperties> convert01(List<QysConfigDO> configDOS);

    QysConfigDTO convert02(QysConfigDO configDO);
}
