package com.newtouch.uctp.module.infra.convert.file;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.newtouch.uctp.module.infra.controller.admin.file.vo.file.FileRespVO;
import com.newtouch.uctp.module.infra.dal.dataobject.file.FileDO;

@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    FileRespVO convert(FileDO bean);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> page);

    List<FileRespDTO> convertList(List<FileDO> list);

    FileRespDTO convertRespDTO(FileDO bean);
}
