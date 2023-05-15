package com.newtouch.uctp.module.business.service.bank.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.openinvoice.BpmOpenInvoiceApi;
import com.newtouch.uctp.module.bpm.api.task.BpmProcessInstanceApi;
import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationRespVO;
import com.newtouch.uctp.module.business.enums.bank.*;
import com.newtouch.uctp.module.business.service.DeptService;
import com.newtouch.uctp.module.business.service.account.dto.CollectPaymentFailedFormDTO;
import com.newtouch.uctp.module.business.service.account.dto.ProfitPresentFormDTO;
import com.newtouch.uctp.module.business.service.bank.dto.OutGoldDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.exception.BankException;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionRecordDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import com.newtouch.uctp.module.business.service.bank.BankAPIService;
import com.newtouch.uctp.module.business.service.bank.TransactionRecordService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.BalancesWithdrawalRequest;
import com.newtouch.uctp.module.business.service.bank.request.InnerTransferRequest;
import com.newtouch.uctp.module.business.service.bank.request.NominalAccountRequest;
import com.newtouch.uctp.module.business.service.bank.request.TechAddressesRequest;
import com.newtouch.uctp.module.business.service.bank.response.BalancesWithdrawalResponse;
import com.newtouch.uctp.module.business.service.bank.response.InnerTransferResponse;
import com.newtouch.uctp.module.business.service.bank.response.NominalAccountResponse;
import com.newtouch.uctp.module.business.service.bank.response.TechAddressesResponse;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
@Validated
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private TransactionRecordService transactionRecordService;

    @Resource
    private BankAPIService bankAPIService;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Resource
    private BpmOpenInvoiceApi bpmOpenInvoiceApi;


    @Override
    public DepositsNotificationRespVO noticePaymentResult(DepositsNotificationReqVO request) throws BankException {
        DepositsNotificationRespVO respVO = new DepositsNotificationRespVO();
        LocalDateTime now = LocalDateTime.now();


        respVO.setResultHead(DepositsNotificationRespVO.ResultHead.builder()
                .noticeDate(SPDBBankTrans.TRAN_DATE_FORMAT.get(now))
                .noticeTime(SPDBBankTrans.TRAN_TIME_FORMAT.get(now))
                .noticeSerNo(request.getPostBeanHead().getNoticeSerNo())
                .noticeType(NotificationTranCode.NOTICE_TYPE.getCode())
                .statusCode(NotificationResultCode.RESULT_STATUS_CODE.getCode())
                .rspMessage("").build());
        respVO.setData(DepositsNotificationRespVO.ResultBody.builder()
                .returnCode(NotificationResultCode.RESULT_RETURN_CODE.getCode())
                .returnMessage("")
                .build());
        return respVO;
    }

//    @Override
//    public String orderPayment(String contractNo) {
//
////        String mrchNo = "";
////        String pymtMd = "";
////        String strNo = "";
////        String stdntId = "";
////        String ordrAmt = "";
////        String mrchOrdrNo = "";
////
////        OrderPayRequest param = OrderPayRequest.buildPayReq(mrchNo, pymtMd, strNo, stdntId, ordrAmt, mrchOrdrNo);
////
////        String url = BankConstants.API_URL + BankConstants.ORDERS_PAY_API;
////        OrderPayResponse response = SPDBSMSignature.request(url, SPDBSMSignature.REQUEST_METHOD_POST, param, Boolean.FALSE, OrderPayResponse.class);
////        String statusCode = response.getStatusCode();
////
////        TransactionRecordDO transactionRecordDO = new TransactionRecordDO();
////        transactionRecordDO.setTranNo(response.getTransNo());
////        transactionRecordDO.setTranType("");
////        transactionRecordDO.setContractNo("");
////        transactionRecordDO.setPayerName("");
////        transactionRecordDO.setPayerBankName("");
////        transactionRecordDO.setPayerBankAccount("");
////        transactionRecordDO.setPayeeName("");
////        transactionRecordDO.setPayeeBankName("");
////        transactionRecordDO.setPayeeBankAccount("");
////        transactionRecordDO.setApproveAccount("");
////        transactionRecordDO.setTranAmount(0L);
////        transactionRecordDO.setTranState(AccountEnum.bankResultCodeMap.get(response.getStatusCode()));
////        transactionRecordDO.setBankResultCode(response.getStatusCode());
////        transactionRecordDO.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(response.getStatusCode()));
////        transactionRecordService.save(transactionRecordDO);
////
////        transactionLogService.save(TransactionLogDO.builder()
////                .tranId(mrchOrdrNo)
////                .tranBeginTime(LocalDateTime.now())
////                .tranEndTime(LocalDateTime.now())
////                .tranRequest(JSON.toJSONString(param))
////                .tranResponse(JSON.toJSONString(response))
////                .tranStatus(response.getStatusCode()).build());
//
//
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public TransactionRecordDO orderPayStatus(String tranNo) {
//        //交易处理中订单查询浦发接口，其余状态查询系统数据
//        TransactionRecordDO transactionRecord = transactionRecordService.getOne(new LambdaQueryWrapperX<TransactionRecordDO>().eq(TransactionRecordDO::getTranNo, tranNo));
////
////        //只查询系统状态为 交易处理中 的数据
////        if (transactionRecord != null && AccountEnum.TRAN_STATE_DURING.getKey().equals(transactionRecord.getTranState())) {
////
////            String mrchOrdrNo = "";
////            OrdersPayStatusRequest param = OrdersPayStatusRequest.builder()
////                    //TODO:商户编号-必填
////                    .mrchNo("")
////                    //TODO:商户订单号-必填
////                    //TODO:商户订单号不能使用交易合同号，一个交易合同号有可能会产生多笔流水
////                    .mrchOrdrNo("")
////                    //TODO:订单支付日期-必填
////                    .pymtDt("").build();
////
////            String url = BankConstants.API_URL + BankConstants.ORDERS_PAY_STATUS;
////
////            OrderPayResponse response = new OrderPayResponse();
////            try {
////                response = SPDBSMSignature.request(url, SPDBSMSignature.REQUEST_METHOD_POST, param, Boolean.FALSE, OrderPayResponse.class);
////            } catch (Exception e) {
////                log.error("当前接口调用失败 url:{}, err msg:{}", url, e.getMessage());
////                response.setStatusCode(AccountEnum.BANK_RESULT_CODE_FAIL.getKey());
////                response.setStatusMsg(e.getCause().toString());
////            } finally {
////                //接口调用日志
////                transactionLogService.save(TransactionLogDO.builder()
////                        .tranId(mrchOrdrNo)
////                        .tranBeginTime(LocalDateTime.now())
////                        .tranEndTime(LocalDateTime.now())
////                        .tranRequest(JSON.toJSONString(param))
////                        .tranResponse(JSON.toJSONString(response))
////                        .tranStatus(StringUtils.isEmpty(response.getStatusCode()) ? AccountEnum.TRAN_STATE_FAIL.getKey() : response.getStatusCode())
////                        .build());
////            }
////
////            //银行返回支付状态后落表
////            String bankResultCode = response.getStatusCode();
////            if (StringUtils.isNotEmpty(bankResultCode) && !bankResultCode.equals(transactionRecord.getBankResultCode())) {
////
////                transactionRecord.setTranState(AccountEnum.bankResultCodeMap.get(bankResultCode));
////                transactionRecord.setBankResultCode(bankResultCode);
////                transactionRecord.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(bankResultCode));
////                transactionRecordService.updateById(transactionRecord);
////            }
////        }
//
//        return transactionRecord;
//    }


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
    public Boolean outGold(OutGoldDTO outGoldDTO, MerchantBankDO bank) {
        try {
            Assert.isTrue(!Objects.isNull(bank), "商户银行信息不能为空!");
            Assert.isTrue(NumberUtil.isInteger(outGoldDTO.getTranAmt()), "交易金额格式错误!必须是整数以分为单位");
            BalancesWithdrawalRequest param = new BalancesWithdrawalRequest();
            param.setAreaCode(BankConstants.AREA_CODE);
            param.setSettleAcctNo(BankConstants.ACCT_NO);
            param.setCapitalAcctNo(bank.getChildAcctNo());
            //商户交易流水号-必填
            param.setChannelSeqNo(SPDBBankTrans.TRAN_SEQ_NO.get(LocalDateTime.now()));
            //出金类型-必填 06-按子账户出金
            param.setGldYldTypeCd(BankOutGoldType.CHILD_ACCT_OUT_GOLD.getCode());
            param.setAuthrCd(bank.getAuthCode());
            //收款方开户行名称-必填
            param.setOpnBnkNm(BankConstants.OTHER_BANK_NAME);
            //TODO:收款方开户行行号-必填 支付收车款场景问题
            param.setOpenBrNo(BankConstants.OTHER_BANK_NO);
            //TODO:收款方户名-必填
            param.setPyAcctNm(outGoldDTO.getPayeeAcctName());
            //收款方账号-必填
            param.setPyAcctNo(outGoldDTO.getPayeeAcctNo());
            //交易金额-必填
            param.setTranAmt(MonetaryUnit.CENT.get(outGoldDTO.getTranAmt()));

            BalancesWithdrawalResponse response = new BalancesWithdrawalResponse();
            String requestMessage = JSONObject.toJSONString(param);
            String responseMessage = null;

            responseMessage = bankAPIService.post(BankConstants.BALANCES_WITHDRAWALS_API, requestMessage);
            if (responseMessage != null) {
                response = JSONObject.parseObject(responseMessage, BalancesWithdrawalResponse.class);
                if (!ResponseStatusCode.TRAN_SUCCESS.getCode().equals(response.getStatusCode())) {
                    if (OutGoldDTO.PayTo.OUT.getCode().equals(outGoldDTO.getTo())) {
                        // 收车款出金业务失败 调用支付流程
                        collectPaymentFailed(outGoldDTO);
                    }
                    throw new BankException(requestMessage, response.getStatusMsg());
                }
                //支付成功调起开票流程
                collectPaymentSuccess(outGoldDTO.getContractNo());
            }
            TransactionRecordDO transactionRecordDO = new TransactionRecordDO();
            transactionRecordDO.setTranNo(response.getTransNo());
            transactionRecordDO.setTranType(outGoldDTO.getTranType());
            transactionRecordDO.setContractNo(outGoldDTO.getContractNo().toString());
            transactionRecordDO.setPayerName(bank.getChildAcctNo());
            transactionRecordDO.setPayerBankName(BankConstants.OPEN_BRANCH_NAME);
            transactionRecordDO.setPayerBankAccount(bank.getChildAcctNo());
            transactionRecordDO.setPayeeName(outGoldDTO.getPayeeAcctName());
            transactionRecordDO.setPayeeBankName(outGoldDTO.getOpenBankName());
            transactionRecordDO.setPayeeBankAccount(outGoldDTO.getAccountNo());
            transactionRecordDO.setApproveAccount(TranApprove.UNRECONCILED.getCode());
            transactionRecordDO.setTranAmount(NumberUtil.binaryToLong(outGoldDTO.getTranAmt()));
            transactionRecordDO.setBankResultCode(response.getStatusCode());
            transactionRecordDO.setTranState(AccountEnum.bankResultCodeMap.get(response.getStatusCode()));
            transactionRecordDO.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(response.getStatusCode()));
            transactionRecordService.save(transactionRecordDO);

        } catch (BankException e) {
            log.error("调用银行接口异常", e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("调用流程异常: ", e);
        } catch (ServiceException e) {
            log.error("创建流程实例异常: ", e);
        } catch (Exception e) {
            log.error("服务异常异常: ", e);
            throw e;
        }
        return Boolean.TRUE;
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
        String tranNo = "generateTranNo();";

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
//        // 调用银行接口
//        try {
//            responseMessage = SPDBSMSignature.call(HttpMethod.POST.name(), BankConstants.UNKNOWN_CLEARINGS_API, requestMessage);
//            log.info("合同：{}的银行响应报文是：{}", contractNo, responseMessage);
//            if (responseMessage == null) {
//                log.error("银行响应报文为空，交易失败");
//                response = new InnerTransferResponse();
//                response.setStatusCode(ResponseStatusCode.UNKNOWN_ERROR.getCode());
//                response.setStatusMsg(ResponseStatusCode.UNKNOWN_ERROR.getValue());
//            } else {
//                response = JSONObject.parseObject(responseMessage, InnerTransferResponse.class);
//            }
//
//            // 交易记录
//            TransactionRecordDO transactionRecordDO = new TransactionRecordDO();
//            transactionRecordDO.setTranNo(tranNo);
//            transactionRecordDO.setTranType(tranType);
//            transactionRecordDO.setContractNo(contractNo);
//            transactionRecordDO.setPayerName(outSubAccountName);
//            transactionRecordDO.setPayerBankName("浦发银行");
//            transactionRecordDO.setPayerBankAccount(outSubAccountNo);
//            transactionRecordDO.setPayeeName("");
//            transactionRecordDO.setPayeeBankName("浦发银行");
//            transactionRecordDO.setPayeeBankAccount(inSubAccountNo);
//            transactionRecordDO.setApproveAccount("");
//            transactionRecordDO.setTranAmount(tranAmount);
//            transactionRecordDO.setBankResultCode(response.getStatusCode());
//            transactionRecordDO.setTranState(AccountEnum.bankResultCodeMap.get(response.getStatusCode()));
//            transactionRecordDO.setBankResultReason(StringUtils.isNotEmpty(response.getStatusMsg()) ? response.getStatusMsg() : AccountEnum.getName(response.getStatusCode()));
//            transactionRecordService.save(transactionRecordDO);
//
//            return response;
//        } catch (Exception e) {
//            log.error("调用银行接口失败", e);
//            throw new RuntimeException("调用银行接口失败", e);
//        } finally {
//            // 记录交易日志
//            transactionLogService.save(TransactionLogDO.builder()
//                    .tranId(tranNo)
//                    .tranBeginTime(now)
//                    .tranEndTime(now)
//                    .tranRequest(requestMessage)
//                    .tranResponse(responseMessage)
//                    .tranStatus(response.getStatusCode())
//                    .build());
//        }
        return null;
    }

    @Override
    public String unKnowClearing(String contractNo) {
//        LocalDateTime now = LocalDateTime.now();
//
//        UnKnowClearingRequest request = new UnKnowClearingRequest();
//        request.setTranDate(now.format(DateTimeFormatter.ofPattern(BankConstants.tranDateFormat)));
//        request.setTranTime(now.format(DateTimeFormatter.ofPattern(BankConstants.tranTimeFormat)));
//        request.setAreaCode(BankConstants.AREA_CODE);
//        request.setSettleAcctNo(BankConstants.ACCT_NO);
//        request.setChannelSeqNo(this.generateTranNo());
//
//        request.setYlkTranSeqNo(null); // 还不确定该值从哪来
//        request.setOrgTranSeqNo(null);
//
//        boolean transExists = true; // TODO 需要判断该笔交易是否真实存在
//        if (transExists) { // 存在该笔交易，则按子账号入金
//            log.info("存在交易：{}，按子账号入金", contractNo);
//            request.setClrgTp(ClearingType.DEPOSITS_BY_SUB_ACCOUNT.getCode());
//            request.setClrgRsltDsc(ClearingType.DEPOSITS_BY_SUB_ACCOUNT.getValue());
//        } else { // 不存在该笔交易，则原路退回
//            log.warn("不存在交易：{}，原路退回", contractNo);
//            request.setClrgTp(ClearingType.ORIGINAL_WAY_BACK.getCode());
//            request.setClrgRsltDsc(ClearingType.ORIGINAL_WAY_BACK.getValue());
//        }
//        request.setRsrvFld(null);
//        request.setRsrvFld1(null);
//        request.setRsrvFld2(null);
//
//        String requestMessage = JSONObject.toJSONString(request);
//        log.info("交易：{}的银行请求报文是：{}", contractNo, requestMessage);
//
//        String responseMessage = null;
//        // 调用银行接口
//        try {
//            responseMessage = SPDBSMSignature.call(HttpMethod.POST.name(), BankConstants.UNKNOWN_CLEARINGS_API, requestMessage);
//            log.info("交易：{}的银行响应报文是：{}", contractNo, responseMessage);
//            if (responseMessage == null) {
//                throw new RuntimeException("银行响应报文为空，交易失败");
//            }
//
//            UnKnowClearingResponse response = JSONObject.parseObject(responseMessage, UnKnowClearingResponse.class);
//
//            if (ResponseStatusCode.TRAN_SUCCESS.getCode().equals(response.getStatusCode())) {
//                // 交易成功
//
//            }
//
//        } catch (Exception e) {
//            log.error("调用银行接口失败", e);
//
//        } finally {
//            // TODO 记录交易日志
//        }

        return null;
    }

    @Override
    public TechAddressesResponse techAddressesGenerate(TechAddressesRequest techAddressesRequest) {
        techAddressesRequest.setMrchId(BankConstants.MERCHANT_ID);
        String requestMessage = JSONObject.toJSONString(techAddressesRequest);
        String responseMessage = null;
        try {
            responseMessage = bankAPIService.post(BankConstants.TECH_ADDRESS_API, requestMessage);
            TechAddressesResponse response = null;
            if (responseMessage != null) {
                response = JSONObject.parseObject(responseMessage, TechAddressesResponse.class);
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
    public void collectPaymentFailed(OutGoldDTO outGoldDTO) {
        // 发起流程
        //String businessKey = this.createPaymentFailedProcess(accountNo, mp.getId());
//        Map<String, Object> variables = new HashMap<>();
//        Map<String, Object> formDataJson = new HashMap<>();
//        Map<String, Object> formMain = new HashMap<>();
//
//        CollectPaymentFailedFormDTO profitPresentFormDTO = this.merchantProfitMapper.selectProfitById(profitId);
//        // 处理数据
//        if (profitPresentFormDTO != null) {
//            profitPresentFormDTO.setTelNo("122222");
//            profitPresentFormDTO.setMerchantName("XX商户");
//            profitPresentFormDTO.setBankOfDeposit("XX开户行");
//            List<ProfitPresentFormDetailDTO> details = profitPresentFormDTO.getProfitDetails();
//            if (details != null && !details.isEmpty()) {
//                details.parallelStream().forEach(e -> {
//                    e.setMerchantName(profitPresentFormDTO.getMerchantName());
//                    e.setTelNo(profitPresentFormDTO.getTelNo());
//                    e.setBalanceAmount(profitPresentFormDTO.getBalanceAmount());
//                });
//            }
//        }
//
//        formMain.put("merchantId", SecurityFrameworkUtils.getLoginUser().getTenantId());
//        formMain.put("formDataJson", profitPresentFormDTO);
//
//        formDataJson.put("formMain", formMain);
//
//        variables.put("marketName", "");
//        variables.put("merchantName", "");
//        variables.put("startUserId", SecurityFrameworkUtils.getLoginUser().getId());
//        variables.put("formDataJson", formDataJson);
//
//        log.info("开始调用发起流程接口，利润提现ID: {}", profitId);
//        BpmProcessInstanceByKeyReqDTO req = new BpmProcessInstanceByKeyReqDTO();
//        req.setProcDefKey(BpmDefTypeEnum.LRTX.name());
//        req.setVariables(variables);
//        CommonResult<String> r = bpmProcessInstanceApi.createProcessInstanceByKey(req);
//        log.info("利润提现ID{}， 创建利润提现流程结果：{}", profitId, r);
//        if (r.isError()) {
//            log.error("账户：{}，利润提现流程创建失败", accountNo);
//            throw exception(ACC_PRESENT_ERROR);
//        }
//        return r.getData();
    }

    @Override
    public void collectPaymentSuccess(Long contractNo) {
        CommonResult<String> result = null;
        try {
            result = bpmOpenInvoiceApi.createOpenInvoiceBpm(contractNo, "SCKP");

            //return result;
        } catch (Exception e) {
            log.error("收车款支付成功->开发票异常: ", e.getMessage());
        }
    }


    private String printExceptionMessage(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static void main(String[] args) {
    }
}
