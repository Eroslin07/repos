package com.newtouch.uctp.module.business.convert.qysconfig;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigRespVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qysconfig.QysConfigDO;
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


}
