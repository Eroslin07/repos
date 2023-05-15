package com.newtouch.uctp.module.business.dal.mysql.configurationMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CostToMapper
 * @Author: zhang
 * @Date 2023/5/14
 */
@Mapper
public interface CostToMapper {
    /** 创建配置信息 */
    public void createCost(@Param("reqVO") CostRespVO reqVO);

    /** 查询税率配置信息 */
    Page<CostRespVO> getCostPage(Page<CostRespVO> page, @Param("reqVO") CostReqVO pageVO);

    List<CostRespVO> getCostAcquire(Integer type);

    /** 导出税率配置信息 */
    List<CostExcelVO> listCost(@Param("reqVO") CostExportReqVO reqVO);

    /** 查询税率配置信息详情 */
    CostRespVO getCost(@Param("id") Long id);

    /** 修改税率配置信息 */
    void updateCost(@Param("reqVO") CostUpdateReqVO reqVO);
}
