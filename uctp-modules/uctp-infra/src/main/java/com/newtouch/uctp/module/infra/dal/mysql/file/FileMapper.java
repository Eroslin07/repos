package com.newtouch.uctp.module.infra.dal.mysql.file;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.newtouch.uctp.module.infra.dal.dataobject.file.FileDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文件操作 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FileMapper extends BaseMapperX<FileDO> {

    default PageResult<FileDO> selectPage(FilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .betweenIfPresent(FileDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FileDO::getId));
    }

    int updateCarInfoStatus(@Param("salesStatus") Integer salesStatus,
                                @Param("status") Integer status,@Param("statusThree") Integer statusThree,
                            @Param("carId") Long carId);

    int deleteByMainIdAndType(@Param("mainId") Long id, @Param("fileType") String fileType);

    int deleteInfraFile(@Param("id") Long id);
}
