package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashRespVo;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppHomeCarInfoPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppHomeCarInfoRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@InterceptorIgnore(tenantLine = "true")
@Mapper
public interface MerchantCashMapper extends BaseMapperX<MerchantCashDO> {


    Page<MerchantCashRespVo> selectAppHomePage(@Param("pg") Page<MerchantCashRespVo> page, @Param("param") String param,
                                               @Param("merchantCashReq") MerchantCashReqVO merchantCashReq);

    default PageResult<MerchantCashDO> queryPageByAccountNo(MerchantCashReqVO merchantCashReq) {
        LambdaQueryWrapper<MerchantCashDO> queryWrapper = new LambdaQueryWrapper<MerchantCashDO>()
                .eq(MerchantCashDO::getAccountNo, merchantCashReq.getAccountNo())
                .orderByDesc(MerchantCashDO::getCreateTime);
        if (merchantCashReq.getType() == 2) {
            //冻结明细
            queryWrapper.in(MerchantCashDO::getTradeType, AccountConstants.TRADE_TYPE_PREEMPTION, AccountConstants.TRADE_TYPE_RELEASE, AccountConstants.TRADE_TYPE_WITHDRAWING);
        }

        if (merchantCashReq.getType() == 3) {
            //支出明细
            queryWrapper.in(MerchantCashDO::getProfitLossType, AccountConstants.PROFIT_LOSS_TYPE_DISBURSEMENT);
        }

        if (merchantCashReq.getType() == 4) {
            //收入明细
            queryWrapper.in(MerchantCashDO::getProfitLossType, AccountConstants.PROFIT_LOSS_TYPE_INCOME);
        }
        return selectPage(merchantCashReq, queryWrapper);

    }

    MerchantCashDO queryContractNoAmount(@Param("contractNo") String contractNo, @Param("tradeTypes") List<String> tradeTypes);


}
