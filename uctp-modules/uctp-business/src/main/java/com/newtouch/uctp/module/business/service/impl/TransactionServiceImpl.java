package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.newtouch.uctp.module.business.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Validated
public class TransactionServiceImpl implements TransactionService {


    private final static String X_SPDB_CLIENT_ID = "0010d65a-1a3e-4a82-8b32-47a7cd2783dc";
    private final static String X_SPDB_CLIENT_SECRET = "MTPhMC00NzA3LTg1MTctZWVzZDAzNmJjOWJ3MC43MDP0NTM0MzczNDUxMjQ4MC45";

    @Override
    public void transactionStatus(String tranNo) {
        // 查询银行交易状态是否成功
    }

//    @Override
//    public void transferAccount(TransferAccountDTO transferAccountDTO) {
//        // 交易流水标识生成
//
//        // 生成银行端请求报文
//
//        // 异步任务处理结果
//    }

    @Override
    public void payStatus() {
        
    }

    @Override
    public void transferAccountToCash() {
        // 获取银行账户入账通知

        // 获取商户账户信息

        // 根据商户姓名、银行账户姓名 确认转账信息

        // 更新保证金明细信息保障金充值成功 记录 保证金额度、是否存在通道手续费明细
    }

    @Override
    public String tranNoByContractNo() {
        return null;
    }

    @Override
    public String bankAccountOpen() {
        return null;
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

    /**
     * 交易流水号生成
     *
     * @return
     */
    private String generaTranNo() {
        StringBuffer tranNo = new StringBuffer();
        // 三位固定值后续根据业务可以做区分
        tranNo.append("101");
        // 时间戳毫秒单位
        tranNo.append(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_MS_FORMATTER));
        // 十位随机字符
        tranNo.append(RandomUtil.randomNumbers(10));
        return tranNo.toString();
    }

    public static void main(String[] args) {
        TransactionServiceImpl transactionService = new TransactionServiceImpl();
        System.out.println("交易流水号: " + transactionService.generaTranNo());
        System.out.println("商户账户号: " + transactionService.generaAccountNo("18516602911"));
    }
}
