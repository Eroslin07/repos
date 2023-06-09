package com.newtouch.uctp.module.business.service.account.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantAccountRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosBindReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantAccountMapper;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.service.account.MerchantAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Validated
@Slf4j
public class MerchantAccountServiceImpl extends ServiceImpl<MerchantAccountMapper, MerchantAccountDO> implements MerchantAccountService {

    @Resource
    private MerchantAccountMapper merchantAccountMapper;

    @Override
    public MerchantAccountDO queryByAccountNo(String accountNo) {
        return merchantAccountMapper.queryByAccountNo(accountNo);
    }

    @Override
    @Transactional
    public int updateCash(String accountNo, Long tranAmount, Integer revision, int type) {
        return merchantAccountMapper.updateCash(accountNo, tranAmount, revision, type);
    }

    @Override
    @Transactional
    public MerchantAccountDO changeCash(String accountNo, Long tranAmount, Integer revision, String tradeType) {

        MerchantAccountDO merchantAccountDO = merchantAccountMapper.selectForUpdateByAccountNo(accountNo);
        log.info("保证金信息变更，当前账户信息：{}", JSON.toJSONString(merchantAccountDO));
        if (revision != null && !revision.equals(merchantAccountDO.getRevision())) {
            log.info("请求版本号错误，reversion {}，accountNo：{}", revision, accountNo);
            //版本号错误
            throw new ServiceException(AccountConstants.ERROR_CODE_REVERSION_ERROR, AccountConstants.ERROR_MESSAGE_REVERSION_ERROR);
        }

        if (AccountConstants.TRADE_TYPE_DEDUCTION.equals(tradeType) || AccountConstants.TRADE_TYPE_RELEASE.equals(tradeType) || AccountConstants.TRADE_TYPE_WITHDRAW.equals(tradeType)) {
            if (merchantAccountDO == null || merchantAccountDO.getFreezeCash() < tranAmount) {
                log.info("当前账户冻结金额不足");
                throw new ServiceException(AccountConstants.ERROR_CODE_INSUFFICIENT_FREEZE_CASH, AccountConstants.ERROR_MESSAGE_INSUFFICIENT_FREEZE_CASH);
            }
        } else {
            if (merchantAccountDO == null || merchantAccountDO.getAvailableCash() < tranAmount) {
                log.info("当前账户可用余额不足");
                throw new ServiceException(AccountConstants.ERROR_CODE_INSUFFICIENT_AVAILABLE_BALANCE, AccountConstants.ERROR_MESSAGE_INSUFFICIENT_AVAILABLE_BALANCE);
            }
        }

        merchantAccountDO.setRevision(merchantAccountDO.getRevision() + 1);
        switch (tradeType) {
            case AccountConstants.TRADE_TYPE_WITHDRAW:
                merchantAccountDO.setCash(merchantAccountDO.getCash() - tranAmount);
                merchantAccountDO.setFreezeCash(merchantAccountDO.getFreezeCash() - tranAmount);
                break;
            case AccountConstants.TRADE_TYPE_WITHDRAWING:
            case AccountConstants.TRADE_TYPE_PREEMPTION:
                merchantAccountDO.setAvailableCash(merchantAccountDO.getAvailableCash() - tranAmount);
                merchantAccountDO.setFreezeCash(merchantAccountDO.getFreezeCash() + tranAmount);
                break;
            case AccountConstants.TRADE_TYPE_DEDUCTION:
                merchantAccountDO.setFreezeCash(merchantAccountDO.getFreezeCash() - tranAmount);
                break;
            case AccountConstants.TRADE_TYPE_RELEASE:
                merchantAccountDO.setFreezeCash(merchantAccountDO.getFreezeCash() - tranAmount);
                merchantAccountDO.setAvailableCash(merchantAccountDO.getAvailableCash() + tranAmount);
                break;
        }
        log.info("保证金信息变更，修改账户信息：{}", JSON.toJSONString(merchantAccountDO));
        merchantAccountMapper.updateById(merchantAccountDO);
        return merchantAccountDO;
    }

    @Override
    public int updateByLock(MerchantAccountDO account) {
        LambdaUpdateWrapper<MerchantAccountDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MerchantAccountDO::getId, account.getId());
        updateWrapper.eq(MerchantAccountDO::getRevision, account.getRevision());
        updateWrapper.set(MerchantAccountDO::getRevision, account.getRevision() + 1);

        int rows = this.merchantAccountMapper.update(account, updateWrapper);
        return rows;
    }

    @Override
    public MerchantAccountRespVO get(Long merchantId) {

        MerchantAccountDO accountDO = merchantAccountMapper.selectOne(new LambdaQueryWrapper<MerchantAccountDO>()
                .eq(MerchantAccountDO::getMerchantId, merchantId));

        return MerchantAccountRespVO.build(accountDO);
    }

    @Override
    public String bindPosMrhNo(PosBindReqVO req) {
        String posMrhNo = req.getPosMrhNo();
        Long merchantId = req.getMerchantId();
        MerchantAccountDO account = getOne(
                new LambdaQueryWrapperX<MerchantAccountDO>()
                        .eq(MerchantAccountDO::getMerchantId, merchantId)
                        .eq(MerchantAccountDO::getDeleted, Boolean.FALSE)
        );
        if (Objects.isNull(account)) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "商户信息不存在或未开户虚拟账户!");
        }
        if (StringUtils.isNotBlank(account.getPosMrhNo()) && posMrhNo.equals(account.getPosMrhNo())) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "POS商户编号已绑定!");
        }
        account.setPosMrhNo(posMrhNo);
        return updateById(account) ? "POS商户编号绑定成功" : "数据异常，联系管理员!";
    }

    @Override
    public String getPosMrhNoInfo(Long merchantId) {
        MerchantAccountDO account = getOne(
                new LambdaQueryWrapperX<MerchantAccountDO>()
                        .eq(MerchantAccountDO::getMerchantId, merchantId)
                        .eq(MerchantAccountDO::getDeleted, Boolean.FALSE)
        );

        if (Objects.isNull(account)) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "商户信息不存在或未开户虚拟账户!");
        }

        if (StringUtils.isBlank(account.getPosMrhNo())) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "POS商户编号未绑定!");
        }

        return account.getPosMrhNo();
    }

    @Override
    public String posMrhNoModify(PosBindReqVO req) {
        String posMrhNo = req.getPosMrhNo();
        Long merchantId = req.getMerchantId();
        MerchantAccountDO account = getOne(
                new LambdaQueryWrapperX<MerchantAccountDO>()
                        .eq(MerchantAccountDO::getMerchantId, merchantId)
                        .eq(MerchantAccountDO::getDeleted, Boolean.FALSE)
        );
        if (Objects.isNull(account)) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "商户信息不存在或未开户虚拟账户!");
        }
        account.setPosMrhNo(posMrhNo);
        return updateById(account) ? "POS商户编号修改成功" : "数据异常，联系管理员!";
    }

    /**
     * 商户虚拟账户号生成
     *
     * @param prefix 账户号前缀: 商户手机号
     * @return
     */
    private String generaAccountNo(String prefix) {
        StringBuffer accountNo = new StringBuffer();
        accountNo.append(prefix);
        accountNo.append(RandomUtil.randomNumbers(7));
        return accountNo.toString();
    }
}
