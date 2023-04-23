package com.newtouch.uctp.module.business.service.account.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.tenant.core.aop.TenantIgnore;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantCashBackDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantCashBackMapper;
import com.newtouch.uctp.module.business.service.account.MerchantCashBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;
import static cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN;

@Service
@Validated
@Slf4j
public class MerchantCashBackServiceImpl implements MerchantCashBackService {

    @Resource
    private MerchantCashBackMapper merchantCashBackMapper;

    @Override
    @TenantIgnore
    public PageResult<CashBackRespVO> list(String accountNo, CashBackReqVO query) {
        log.info("查询账户{}待回填保证金", accountNo);
        LambdaQueryWrapper<MerchantCashBackDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantCashBackDO::getAccountNo, accountNo)
                .orderByDesc(MerchantCashBackDO::getOccurredTime);

        PageResult<MerchantCashBackDO> pr = merchantCashBackMapper.selectPage(query, queryWrapper);

        List<MerchantCashBackDO> cashBackList = pr.getList();
        if (cashBackList != null && !cashBackList.isEmpty()) {
            List<CashBackRespVO> respVOList = new ArrayList<>();
            for (MerchantCashBackDO c : cashBackList) {
                CashBackRespVO pvo = CashBackRespVO.builder()
                        .id(c.getId().toString())
                        .accountNo(c.getAccountNo())
                        .type(c.getType())
                        .typeText(c.getTypeText())
                        .tradeTo(c.getTradeTo())
                        .tradeToText(c.getTradeToText())
                        .contractNo(c.getContractNo())
                        .amount(c.getAmount())
                        .occurredTime(DateUtil.format(c.getOccurredTime(), NORM_DATE_PATTERN))
                        .build();

                respVOList.add(pvo);
            }

            PageResult<CashBackRespVO> result = new PageResult<>(respVOList, pr.getTotal());
            return result;
        }

        return PageResult.empty();
    }

    @Override
    @TenantIgnore
    public CashBackRespVO detail(String accountNo, Long id) {
        log.info("查询账户{}待回填保证金{}明细", accountNo, id);
        LambdaQueryWrapper<MerchantCashBackDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantCashBackDO::getAccountNo, accountNo);
        queryWrapper.eq(MerchantCashBackDO::getId, id);

        MerchantCashBackDO c = merchantCashBackMapper.selectOne(queryWrapper);
        if (c == null) {
            return null;
        }
        CashBackRespVO pvo = CashBackRespVO.builder()
                .id(c.getId().toString())
                .accountNo(c.getAccountNo())
                .type(c.getType())
                .typeText(c.getTypeText())
                .tradeTo(c.getTradeTo())
                .tradeToText(c.getTradeToText())
                .contractNo(c.getContractNo())
                .amount(c.getAmount())
                .occurredTime(DateUtil.format(c.getOccurredTime(), NORM_DATETIME_PATTERN))
                .build();

        return pvo;
    }
}
