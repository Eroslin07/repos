package com.newtouch.uctp.module.business.dal.mysql.configurationMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceRespVO;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceUpdateReqVO;
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
public interface MakeInvoiceMapper {
    /** 新建配置信息 */
    void createMakeInvoice(@Param("reqVO") MakeInvoiceRespVO reqVO);

    /** 编辑配置信息 */
    void updateMakeInvoice(@Param("reqVO") MakeInvoiceUpdateReqVO reqVO);

    /** 获取配置信息 */
    MakeInvoiceRespVO getMakeInvoice(@Param("tenantId") Long tenantId);
}
