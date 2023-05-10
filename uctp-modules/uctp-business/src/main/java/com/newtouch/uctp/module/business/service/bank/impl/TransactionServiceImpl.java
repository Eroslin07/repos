package com.newtouch.uctp.module.business.service.bank.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.exception.BankException;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionLogDO;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionRecordDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import com.newtouch.uctp.module.business.enums.TranType;
import com.newtouch.uctp.module.business.enums.bank.BankConstants;
import com.newtouch.uctp.module.business.enums.bank.ClearingType;
import com.newtouch.uctp.module.business.enums.bank.ResponseStatusCode;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import com.newtouch.uctp.module.business.service.bank.BankAPIService;
import com.newtouch.uctp.module.business.service.bank.TransactionLogService;
import com.newtouch.uctp.module.business.service.bank.TransactionRecordService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.*;
import com.newtouch.uctp.module.business.service.bank.response.*;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import com.newtouch.uctp.module.business.util.bank.SPDBSMSignature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.codehaus.groovy.runtime.IOGroovyMethods.newPrintWriter;

@Service
@Validated
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private TransactionRecordService transactionRecordService;

    @Resource
    private TransactionLogService transactionLogService;

    @Resource
    private MerchantBankService merchantBankService;

    @Resource
    private MerchantAccountService merchantAccountService;

    @Resource
    private BankAPIService bankAPIService;


    @Override
    public void noticePaymentResult() {

    }

    @Override
    public String orderPayment(String contractNo) {

        String mrchNo = "";
        String pymtMd = "";
        String strNo = "";
        String stdntId = "";
        String ordrAmt = "";
        String mrchOrdrNo = "";

        OrderPayRequest param = OrderPayRequest.buildPayReq(mrchNo, pymtMd, strNo, stdntId, ordrAmt, mrchOrdrNo);

        String url = BankConstants.API_URL + BankConstants.ORDERS_PAY_API;
        OrderPayResponse response = SPDBSMSignature.request(url, SPDBSMSignature.REQUEST_METHOD_POST, param, Boolean.FALSE, OrderPayResponse.class);
        String statusCode = response.getStatusCode();

        TransactionRecordDO transactionRecordDO = new TransactionRecordDO();
        transactionRecordDO.setTranNo(response.getTransNo());
        transactionRecordDO.setTranType("");
        transactionRecordDO.setContractNo("");
        transactionRecordDO.setPayerName("");
        transactionRecordDO.setPayerBankName("");
        transactionRecordDO.setPayerBankAccount("");
        transactionRecordDO.setPayeeName("");
        transactionRecordDO.setPayeeBankName("");
        transactionRecordDO.setPayeeBankAccount("");
        transactionRecordDO.setApproveAccount("");
        transactionRecordDO.setTranAmount(0L);
        transactionRecordDO.setTranState(AccountEnum.bankResultCodeMap.get(response.getStatusCode()));
        transactionRecordDO.setBankResultCode(response.getStatusCode());
        transactionRecordDO.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(response.getStatusCode()));
        transactionRecordService.save(transactionRecordDO);

        transactionLogService.save(TransactionLogDO.builder()
                .tranId(mrchOrdrNo)
                .tranBeginTime(LocalDateTime.now())
                .tranEndTime(LocalDateTime.now())
                .tranRequest(JSON.toJSONString(param))
                .tranResponse(JSON.toJSONString(response))
                .tranStatus(response.getStatusCode()).build());


        return null;
    }

    @Override
    @Transactional
    public TransactionRecordDO orderPayStatus(String tranNo) {
        //交易处理中订单查询浦发接口，其余状态查询系统数据
        TransactionRecordDO transactionRecord = transactionRecordService.getOne(new LambdaQueryWrapperX<TransactionRecordDO>().eq(TransactionRecordDO::getTranNo, tranNo));

        //只查询系统状态为 交易处理中 的数据
        if (transactionRecord != null && AccountEnum.TRAN_STATE_DURING.getKey().equals(transactionRecord.getTranState())) {

            String mrchOrdrNo = "";
            OrdersPayStatusRequest param = OrdersPayStatusRequest.builder()
                    //TODO:商户编号-必填
                    .mrchNo("")
                    //TODO:商户订单号-必填
                    //TODO:商户订单号不能使用交易合同号，一个交易合同号有可能会产生多笔流水
                    .mrchOrdrNo("")
                    //TODO:订单支付日期-必填
                    .pymtDt("").build();

            String url = BankConstants.API_URL + BankConstants.ORDERS_PAY_STATUS;

            OrderPayResponse response = new OrderPayResponse();
            try {
                response = SPDBSMSignature.request(url, SPDBSMSignature.REQUEST_METHOD_POST, param, Boolean.FALSE, OrderPayResponse.class);
            } catch (Exception e) {
                log.error("当前接口调用失败 url:{}, err msg:{}", url, e.getMessage());
                response.setStatusCode(AccountEnum.BANK_RESULT_CODE_FAIL.getKey());
                response.setStatusMsg(e.getCause().toString());
            } finally {
                //接口调用日志
                transactionLogService.save(TransactionLogDO.builder()
                        .tranId(mrchOrdrNo)
                        .tranBeginTime(LocalDateTime.now())
                        .tranEndTime(LocalDateTime.now())
                        .tranRequest(JSON.toJSONString(param))
                        .tranResponse(JSON.toJSONString(response))
                        .tranStatus(StringUtils.isEmpty(response.getStatusCode()) ? AccountEnum.TRAN_STATE_FAIL.getKey() : response.getStatusCode())
                        .build());
            }

            //银行返回支付状态后落表
            String bankResultCode = response.getStatusCode();
            if (StringUtils.isNotEmpty(bankResultCode) && !bankResultCode.equals(transactionRecord.getBankResultCode())) {

                transactionRecord.setTranState(AccountEnum.bankResultCodeMap.get(bankResultCode));
                transactionRecord.setBankResultCode(bankResultCode);
                transactionRecord.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(bankResultCode));
                transactionRecordService.updateById(transactionRecord);
            }
        }

        return transactionRecord;
    }


    @Override
    public NominalAccountResponse nominalAccountGenerate(NominalAccountRequest nominalAccountRequest) {
        String requestMessage = JSONObject.toJSONString(nominalAccountRequest);
        String responseMessage = null;
        // 调用银行接口
        try {
            responseMessage = bankAPIService.post(BankConstants.NOMINAL_ACCOUNT_API, requestMessage);
            NominalAccountResponse response = null;
            if (responseMessage != null) {
                response = JSONObject.parseObject(responseMessage, NominalAccountResponse.class);
                if (!ResponseStatusCode.TRAN_SUCCESS.getCode().equals(response.getStatusCode())) {
                    throw new BankException(requestMessage, response.getStatusMsg());
                }
            }
            return response;
        } catch (Exception e) {
            log.error("调用银行接口异常", e);
            throw e;
        }
    }


    @Override
    public TransactionRecordDO outGold(String bankNo, Long amount, String tranType, String contractNo) {

        //子账户出金到银行卡，提现操作时给到银行卡号及金额
        MerchantBankDO merchantBankDO = merchantBankService.getOne(
                new LambdaQueryWrapperX<MerchantBankDO>()
                        .eq(MerchantBankDO::getBankNo, bankNo)
                        .eq(MerchantBankDO::getDeleted, Boolean.FALSE));

        String accountNo = merchantBankDO.getAccountNo();
        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);
        String tranNo = generateTranNo();

        BalancesWithdrawalRequest param = new BalancesWithdrawalRequest();
        //TODO:交易地区代码-必填 约定的代发平台专用代码
        param.setAreaCode("");
        //TODO:监管账号-必填 代发平台子公司存管账户账号
        param.setSettleAcctNo("");
        //TODO:子账号-必填 发薪企业台账子账户出金类型为06时必输
        String capitalAcctNo = "";
        param.setCapitalAcctNo(capitalAcctNo);
        //商户交易流水号-必填
        param.setChannelSeqNo(tranNo);
        //出金类型-必填 06-按子账户出金
        param.setGldYldTypeCd("06");
        //TODO:授权码-必填
        param.setAuthrCd("");
        //收款方开户行名称-必填
        param.setOpnBnkNm(merchantBankDO.getBankName());
        //TODO:收款方开户行行号-必填
        param.setOpenBrNo("");
        //TODO:收款方户名-必填
        param.setPyAcctNm("");
        //收款方账号-必填
        param.setPyAcctNo(merchantBankDO.getBankNo());
        //交易金额-必填
        param.setTranAmt(BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_UP).toString());

        String url = BankConstants.API_URL + BankConstants.BALANCES_WITHDRAWALS_API;

        BalancesWithdrawalResponse response = new BalancesWithdrawalResponse();
        try {
            response = SPDBSMSignature.request(url, SPDBSMSignature.REQUEST_METHOD_POST, param, Boolean.FALSE, BalancesWithdrawalResponse.class);

            if (null == response) {
                response = new BalancesWithdrawalResponse();
                response.setStatusCode(AccountEnum.BANK_RESULT_CODE_UNKNOWN.getKey());
            }
        } catch (Exception e) {
            log.error("当前接口调用失败 url:{}, err msg:{}", url, e.getMessage());
            response.setStatusCode(AccountEnum.BANK_RESULT_CODE_FAIL.getKey());
            response.setStatusMsg(e.getCause().toString());

        } finally {
            //接口调用日志
            transactionLogService.save(TransactionLogDO.builder()
                    .tranId(tranNo)
                    .tranBeginTime(LocalDateTime.now())
                    .tranEndTime(LocalDateTime.now())
                    .tranRequest(JSON.toJSONString(param))
                    .tranResponse(JSON.toJSONString(response))
                    .tranStatus(StringUtils.isEmpty(response.getStatusCode()) ? AccountEnum.TRAN_STATE_FAIL.getKey() : response.getStatusCode())
                    .build());
        }

        TransactionRecordDO transactionRecordDO = new TransactionRecordDO();
        transactionRecordDO.setTranNo(tranNo);
        transactionRecordDO.setTranType(tranType);
        transactionRecordDO.setContractNo(contractNo);
        transactionRecordDO.setPayerName("子账户");
        transactionRecordDO.setPayerBankName("浦发银行");
        transactionRecordDO.setPayerBankAccount(capitalAcctNo);
        transactionRecordDO.setPayeeName("");
        transactionRecordDO.setPayeeBankName("浦发银行");
        transactionRecordDO.setPayeeBankAccount(bankNo);
        transactionRecordDO.setApproveAccount("");
        transactionRecordDO.setTranAmount(amount);
        transactionRecordDO.setBankResultCode(response.getStatusCode());
        transactionRecordDO.setTranState(AccountEnum.bankResultCodeMap.get(response.getStatusCode()));
        transactionRecordDO.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(response.getStatusCode()));
        transactionRecordService.save(transactionRecordDO);
        return transactionRecordDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 新建事务，避免调用方的事务回滚影响交易日志记录
    public InnerTransferResponse innerTransfer(String accountNo,
                                               String contractNo,
                                               String tranType,
                                               Long tranAmount,
                                               String remark) {
        log.info("调用子账户互转接口。账户：{}，合同：{}，交易类型：{}，金额：{}，备注：{}", accountNo, contractNo, tranType, tranAmount, remark);
        if (StringUtils.isBlank(accountNo)) {
            throw new IllegalArgumentException("账户号不能为空");
        }
        if (StringUtils.isBlank(contractNo)) {
            throw new IllegalArgumentException("合同号不能为空");
        }
        if (StringUtils.isBlank(tranType)) {
            throw new IllegalArgumentException("交易类型不能为空");
        }
        if (tranAmount == null) {
            throw new IllegalArgumentException("交易金额不能为空");
        }
        if (tranAmount.compareTo(0L) <= 0) {
            throw new IllegalArgumentException("交易金额必需大于0");
        }

        LocalDateTime now = LocalDateTime.now();
        String tranNo = generateTranNo();

        String outSubAccountNo = null; // TODO: 根据交易类型查询对应子账户号
        String outSubAccountName = null; // TODO: 根据交易类型查询对应子账户号
        String inSubAccountNo = null; // TODO: 根据交易类型查询对应子账户号
        String inSubAccountName = null; // TODO: 根据交易类型查询对应子账户号
        String mainAccountNo = null; // TODO: 主账户号

        if (AccountEnum.TRAN_PROFIT_CASH_BACK.getKey().equals(tranType)) {
            // 待清分子账户->商户保证金子账户
            // TODO
        } else if (AccountEnum.TRAN_PROFIT_SERVICE_COST.getKey().equals(tranType)) {
            // 待清分子账户->服务费子账户
            // TODO
        } else if (AccountEnum.TRAN_PROFIT_TAX_COST.getKey().equals(tranType)) {
            // 待清分子账户->税费子账户
            // TODO
        } else if (AccountEnum.TRAN_PROFIT_SALES_PROFIT.getKey().equals(tranType)) {
            // 待清分子账户->商户利润子账户
            // TODO
        } else if (AccountEnum.TRAN_PROFIT_CASH_DEDUCTION.getKey().equals(tranType)) {
            // 商户利润子账户->商户保证金子账户
            // TODO
        } else if (AccountEnum.TRAN_PROFIT_CASH_BACK_FROM_ORIGINAL_PROFIT.getKey().equals(tranType)) {
            // 商户利润子账户->商户保证金子账户
            // TODO
        } else {
            throw new IllegalArgumentException("不支持的交易类型");
        }

        InnerTransferRequest request = new InnerTransferRequest();
        request.setAreaCode(BankConstants.AREA_CODE);
        request.setTranSeqNo(tranNo);
        request.setSettleAcctNo(mainAccountNo);
        request.setPayeeAcctNo(outSubAccountNo);
        request.setPayeeName(outSubAccountName);
        request.setPayeeAcctNo(inSubAccountNo);
        request.setPayeeName(inSubAccountName);
        // 传入的是分，需要转为元
        String tranAmt = BigDecimal.valueOf(tranAmount).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_UP).toString();
        request.setTranAmt(tranAmt);
        request.setRemark(remark);

        String requestMessage = JSONObject.toJSONString(request);
        log.info("合同：{}的银行请求报文是：{}", contractNo, requestMessage);

        String responseMessage = null;
        InnerTransferResponse response = null;
        // 调用银行接口
        try {
            responseMessage = SPDBSMSignature.call(HttpMethod.POST.name(), BankConstants.UNKNOWN_CLEARINGS_API, requestMessage);
            log.info("合同：{}的银行响应报文是：{}", contractNo, responseMessage);
            if (responseMessage == null) {
                log.error("银行响应报文为空，交易失败");
                response = new InnerTransferResponse();
                response.setStatusCode(ResponseStatusCode.UNKNOWN_ERROR.getCode());
                response.setStatusMsg(ResponseStatusCode.UNKNOWN_ERROR.getValue());
            } else {
                response = JSONObject.parseObject(responseMessage, InnerTransferResponse.class);
            }

            // 交易记录
            TransactionRecordDO transactionRecordDO = new TransactionRecordDO();
            transactionRecordDO.setTranNo(tranNo);
            transactionRecordDO.setTranType(tranType);
            transactionRecordDO.setContractNo(contractNo);
            transactionRecordDO.setPayerName(outSubAccountName);
            transactionRecordDO.setPayerBankName("浦发银行");
            transactionRecordDO.setPayerBankAccount(outSubAccountNo);
            transactionRecordDO.setPayeeName("");
            transactionRecordDO.setPayeeBankName("浦发银行");
            transactionRecordDO.setPayeeBankAccount(inSubAccountNo);
            transactionRecordDO.setApproveAccount("");
            transactionRecordDO.setTranAmount(tranAmount);
            transactionRecordDO.setBankResultCode(response.getStatusCode());
            transactionRecordDO.setTranState(AccountEnum.bankResultCodeMap.get(response.getStatusCode()));
            transactionRecordDO.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(response.getStatusCode()));
            transactionRecordService.save(transactionRecordDO);

            return response;
        } catch (Exception e) {
            log.error("调用银行接口失败", e);
            throw new RuntimeException("调用银行接口失败", e);
        } finally {
            // 记录交易日志
            transactionLogService.save(TransactionLogDO.builder()
                    .tranId(tranNo)
                    .tranBeginTime(now)
                    .tranEndTime(now)
                    .tranRequest(requestMessage)
                    .tranResponse(responseMessage)
                    .tranStatus(response.getStatusCode())
                    .build());
        }
    }

    @Override
    public String unKnowClearing(String contractNo) {
        LocalDateTime now = LocalDateTime.now();

        UnKnowClearingRequest request = new UnKnowClearingRequest();
        request.setTranDate(now.format(DateTimeFormatter.ofPattern(BankConstants.tranDateFormat)));
        request.setTranTime(now.format(DateTimeFormatter.ofPattern(BankConstants.tranTimeFormat)));
        request.setAreaCode(BankConstants.AREA_CODE);
        request.setSettleAcctNo(BankConstants.ACCT_NO);
        request.setChannelSeqNo(this.generateTranNo());

        request.setYlkTranSeqNo(null); // 还不确定该值从哪来
        request.setOrgTranSeqNo(null);

        boolean transExists = true; // TODO 需要判断该笔交易是否真实存在
        if (transExists) { // 存在该笔交易，则按子账号入金
            log.info("存在交易：{}，按子账号入金", contractNo);
            request.setClrgTp(ClearingType.DEPOSITS_BY_SUB_ACCOUNT.getCode());
            request.setClrgRsltDsc(ClearingType.DEPOSITS_BY_SUB_ACCOUNT.getValue());
        } else { // 不存在该笔交易，则原路退回
            log.warn("不存在交易：{}，原路退回", contractNo);
            request.setClrgTp(ClearingType.ORIGINAL_WAY_BACK.getCode());
            request.setClrgRsltDsc(ClearingType.ORIGINAL_WAY_BACK.getValue());
        }
        request.setRsrvFld(null);
        request.setRsrvFld1(null);
        request.setRsrvFld2(null);

        String requestMessage = JSONObject.toJSONString(request);
        log.info("交易：{}的银行请求报文是：{}", contractNo, requestMessage);

        String responseMessage = null;
        // 调用银行接口
        try {
            responseMessage = SPDBSMSignature.call(HttpMethod.POST.name(), BankConstants.UNKNOWN_CLEARINGS_API, requestMessage);
            log.info("交易：{}的银行响应报文是：{}", contractNo, responseMessage);
            if (responseMessage == null) {
                throw new RuntimeException("银行响应报文为空，交易失败");
            }

            UnKnowClearingResponse response = JSONObject.parseObject(responseMessage, UnKnowClearingResponse.class);

            if (ResponseStatusCode.TRAN_SUCCESS.getCode().equals(response.getStatusCode())) {
                // 交易成功

            }

        } catch (Exception e) {
            log.error("调用银行接口失败", e);

        } finally {
            // TODO 记录交易日志
        }

        return null;
    }

    @Override
    public TechAddressesResponse techAddressesGenerate(TechAddressesRequest techAddressesRequest) {
        techAddressesRequest.setMrchId(BankConstants.MERCHANT_ID);
        String requestMessage = JSONObject.toJSONString(techAddressesRequest);
        // log.info("交易：{}的银行请求报文是：{}", contractNo, requestMessage);

        String responseMessage = null;
        // 调用银行接口
        try {
            responseMessage = SPDBSMSignature.call(HttpMethod.POST.name(), BankConstants.UNKNOWN_CLEARINGS_API, requestMessage);
            // log.info("交易：{}的银行响应报文是：{}", contractNo, responseMessage);
            if (responseMessage == null) {
                throw new RuntimeException("银行响应报文为空，交易失败");
            }

            UnKnowClearingResponse response = JSONObject.parseObject(responseMessage, UnKnowClearingResponse.class);

            if (ResponseStatusCode.TRAN_SUCCESS.getCode().equals(response.getStatusCode())) {
                // 交易成功

            }

        } catch (Exception e) {
            log.error("调用银行接口失败", e);

        } finally {
            // TODO 记录交易日志
        }

        return null;
    }

    /**
     * 交易流水号生成
     *
     * @return
     */
    private String generateTranNo() {
        StringBuffer tranNo = new StringBuffer();
        // 三位固定值后续根据业务可以做区分
        tranNo.append("101");
        // 时间戳毫秒单位
        tranNo.append(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_MS_FORMATTER));
        // 十位随机字符
        tranNo.append(RandomUtil.randomNumbers(10));
        return tranNo.toString();
    }

    private String printExceptionMessage(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = newPrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern(BankConstants.tranDateFormat)));
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(BankConstants.tranTimeFormat)));
    }
}
