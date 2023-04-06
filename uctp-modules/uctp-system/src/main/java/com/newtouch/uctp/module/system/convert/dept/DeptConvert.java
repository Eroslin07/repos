package com.newtouch.uctp.module.system.convert.dept;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptRespVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;

@Mapper
public interface DeptConvert {

    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    List<DeptRespVO> convertList(List<DeptDO> list);

    List<DeptSimpleRespVO> convertList02(List<DeptDO> list);

    DeptRespVO convert(DeptDO bean);

    DeptDO convert(DeptCreateReqVO bean);

    DeptDO convert(DeptUpdateReqVO bean);

    List<DeptRespDTO> convertList03(List<DeptDO> list);

    DeptRespDTO convert03(DeptDO bean);

    DeptUpdateReqVO convert04(DeptDO bean);

}
