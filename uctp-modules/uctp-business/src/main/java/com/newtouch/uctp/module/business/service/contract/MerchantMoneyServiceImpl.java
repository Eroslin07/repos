package com.newtouch.uctp.module.business.service.contract;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.TransactionRecordReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.dal.mysql.ContractMapper;
import com.newtouch.uctp.module.business.dal.mysql.MerchantAccountMapper;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.business.service.account.AccountCashService;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author helong
 * @date 2023/5/6 11:19
 */
@Slf4j
@Transactional(readOnly = false)
@Service
public class MerchantMoneyServiceImpl implements MerchantMoneyService {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private MerchantAccountMapper merchantAccountMapper;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private AccountCashService accountCashService;
    @Resource
    private AccountProfitService accountProfitService;

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Boolean reserveCash(Long contractId) {
        /*if (true) {
            return true;
        }*/

        // 1.拼装保证金预占的数据
        TransactionRecordReqVO transactionRecordReqVO = this.getTransactionRecordReqByContractId(contractId);
        // 2.进行预占
        Boolean hasReserve = accountCashService.reserve(transactionRecordReqVO);

        return hasReserve;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Boolean releaseCash(Long contractId) {
        if (true) {
            return true;
        }
        // 1.拼装保证金释放的数据
        TransactionRecordReqVO transactionRecordReqVO = this.getTransactionRecordReqByContractId(contractId);
        // 2.进行释放
        Boolean  hasRelease = accountCashService.release(transactionRecordReqVO);

        return hasRelease;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Boolean deductionCash(Long contractId) {
        /*if (true) {
            return true;
        }*/
        // 1.拼装保证金实占的数据
        TransactionRecordReqVO transactionRecordReqVO = this.getTransactionRecordReqByContractId(contractId);
        // 2.进行实占
        Boolean hasDeduction = accountCashService.deduction(transactionRecordReqVO);

        return hasDeduction;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Boolean recorded(Long carId) {
        // 1.根据合同的车辆ID查询车辆的基本信息
        CarInfoDO carInfoDO = carInfoService.getCarInfo(carId);
        if (ObjectUtil.isNull(carInfoDO) || ObjectUtil.isNull(carInfoDO.getBusinessId())
                || ObjectUtil.isNull(carInfoDO.getVehicleReceiptAmount()) || ObjectUtil.isNull(carInfoDO.getSellAmount())) {
            throw new RuntimeException("获取车辆收卖车基本信息失败。");
        }

        // 2.校验卖车合同是否存在（合同类型：1-收车委托合同   2-收车合同  3-卖车委托合同  4-卖车合同）
        ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                .eq(ContractDO::getCarId, carId).eq(ContractDO::getContractType, 4).eq(ContractDO::getInvalided, 0));
        if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getContractId())) {
            throw new RuntimeException("获取车辆卖车合同基本信息失败。");
        }

        // 3.根据商户ID获取商户的虚拟账户信息
        Long merchantId = carInfoDO.getBusinessId();
        MerchantAccountDO merchantAccountDO = merchantAccountMapper.selectOne(new LambdaQueryWrapper<MerchantAccountDO>().eq(MerchantAccountDO::getMerchantId, merchantId));
        if (ObjectUtil.isNull(merchantAccountDO) || !StringUtils.hasText(merchantAccountDO.getAccountNo())) {
            throw new IllegalArgumentException("根据收卖车商户的商户ID获取商户的虚拟账户基本信息失败。");
        }

        //收车金额（单位：元）
        BigDecimal vehicleReceiptAmount = carInfoDO.getVehicleReceiptAmount();
        //收车金额（单位：分）
        Long tranAmount = vehicleReceiptAmount.multiply(new BigDecimal(100)).longValue();
        //卖车金额（单位：元）
        BigDecimal sellAmount = carInfoDO.getSellAmount();
        Long carSalesAmount = sellAmount.multiply(new BigDecimal(100)).longValue();

        // 利润明细表记录
        List<MerchantProfitDO> profitList = accountProfitService.recorded(merchantAccountDO.getAccountNo(), String.valueOf(contractDO.getContractId()),
                tranAmount, carSalesAmount);
        return !CollectionUtils.isEmpty(profitList);
    }

    /**
     * 根据（契约锁）收车合同ID获取保证金操作的组装数据
     * @param contractId
     * @return
     */
    private TransactionRecordReqVO getTransactionRecordReqByContractId(Long contractId) {
        // 1.校验收车合同是否存在（合同类型：1-收车委托合同   2-收车合同  3-卖车委托合同  4-卖车合同）
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getContractId, contractId, ContractDO::getContractType, 2);
        if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getCarId())) {
            throw new RuntimeException("根据合同ID获取[收车收购合同]基本信息失败。");
        }
        // 2.根据合同的车辆ID查询车辆的（收车）基本信息
        CarInfoDO carInfoDO = carInfoService.getCarInfo(contractDO.getCarId());
        if (ObjectUtil.isNull(carInfoDO) || ObjectUtil.isNull(carInfoDO.getBusinessId()) || ObjectUtil.isNull(carInfoDO.getVehicleReceiptAmount())) {
            throw new RuntimeException("根据[收车收购合同]获取车辆收车基本信息失败。");
        }
        //收车金额（单位：元）
        BigDecimal vehicleReceiptAmount = carInfoDO.getVehicleReceiptAmount();
        //收车金额（单位：分）
        Long tranAmount = vehicleReceiptAmount.multiply(new BigDecimal(100)).longValue();

        // 3.根据商户ID获取商户的虚拟账户信息
        Long merchantId = carInfoDO.getBusinessId();
        MerchantAccountDO merchantAccountDO = merchantAccountMapper.selectOne(new LambdaQueryWrapper<MerchantAccountDO>().eq(MerchantAccountDO::getMerchantId, merchantId));
        if (ObjectUtil.isNull(merchantAccountDO) || !StringUtils.hasText(merchantAccountDO.getAccountNo())) {
            throw new IllegalArgumentException("根据收车商户的商户ID获取商户的虚拟账户基本信息失败。");
        }
        // 4.拼装保证金预占的数据进行预占
        TransactionRecordReqVO transactionRecordReqVO = new TransactionRecordReqVO();
        transactionRecordReqVO.setAccountNo(merchantAccountDO.getAccountNo());
        transactionRecordReqVO.setTranAmount(tranAmount);
        transactionRecordReqVO.setRevision(merchantAccountDO.getRevision());
        transactionRecordReqVO.setContractNo(String.valueOf(contractDO.getContractId()));

        return transactionRecordReqVO;
    }
}
