package com.newtouch.uctp.module.business.dal.mysql.contract;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName TempMapper
 * @Author: zhang
 * @Date 2023/4/20.
 */
@Mapper
public interface TempMapper {
    Page<TempRespVO> getTempPage(Page<TempRespVO> page, @Param("reqVO") TempReqVO pageVO);

    List<TempExcelVO> tempList(@Param("reqVO") TempExportReqVO reqVO);

    TempRespVO getTemp(@Param("id") Long id);

    void updateTemp(@Param("reqVO") TempUpdateReqVO reqVO);

    void deleteTemp(@Param("id") Long id);

}
