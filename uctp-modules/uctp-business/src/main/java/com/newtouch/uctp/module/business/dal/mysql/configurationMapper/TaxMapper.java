package com.newtouch.uctp.module.business.dal.mysql.configurationMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName TaxMapper
 * @Author: zhang
 * @Date 2023/5/6
 */
@Mapper
public interface TaxMapper {
    /** 创建配置信息 */
    public void createTax(@Param("reqVO") TaxRespVO reqVO);

    /** 查询税率配置信息 */
    Page<TaxRespVO> getTaxPage(Page<TaxRespVO> page, @Param("reqVO") TaxReqVO pageVO);

    /** 导出税率配置信息 */
    List<TaxExcelVO> listTax(@Param("reqVO") TaxExportReqVO reqVO);

    /** 查询税率配置信息详情 */
    TaxRespVO getTax(@Param("id") Long id);

    /** 修改税率配置信息 */
    void updateTax(@Param("reqVO") TaxUpdateReqVO reqVO);
}
