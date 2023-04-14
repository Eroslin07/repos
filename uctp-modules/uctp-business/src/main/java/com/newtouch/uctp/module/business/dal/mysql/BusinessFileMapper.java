package com.newtouch.uctp.module.business.dal.mysql;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 图片中间表 Mapper
 *
 * @author qjj
 */
@Mapper
public interface BusinessFileMapper extends BaseMapperX<BusinessFileDO> {

    default List<BusinessFileDO> getFileByMainId(Long mainId, String fileType){
        return selectList(new LambdaQueryWrapperX<BusinessFileDO>()
                .eqIfPresent(BusinessFileDO::getMainId, mainId)
                .eqIfPresent(BusinessFileDO::getFileType, fileType));
    }

//    @Delete("delete from uctp_business_file where MAIN_ID = #{mainId} and FILE_TYPE = #{fileType}}")
    int deleteByMainIdAndType(@Param("mainId") Long mainId, @Param("fileType") String fileType);
}
