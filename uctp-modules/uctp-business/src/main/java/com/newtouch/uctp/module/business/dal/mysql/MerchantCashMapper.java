package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@InterceptorIgnore(tenantLine = "true")
@Mapper
public interface MerchantCashMapper extends BaseMapperX<MerchantCashDO> {

    default PageResult<MerchantCashDO> queryPageByAccountNo(MerchantCashReqVO merchantCashReq) {
        return selectPage(merchantCashReq, new LambdaQueryWrapper<MerchantCashDO>()
                .eq(MerchantCashDO::getAccountNo, merchantCashReq.getAccountNo())
                .orderByDesc(MerchantCashDO::getCreateTime));

    }

    MerchantCashDO queryContractNoAmount(@Param("contractNo") String contractNo, @Param("tradeTypes") List<String> tradeTypes);


}
