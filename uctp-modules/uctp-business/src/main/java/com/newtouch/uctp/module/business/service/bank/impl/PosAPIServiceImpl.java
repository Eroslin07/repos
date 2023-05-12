package com.newtouch.uctp.module.business.service.bank.impl;

import cn.hutool.core.lang.UUID;
import com.newland.pospp.iot.lib.bean.MessageStatusResponseBean;
import com.newland.pospp.iot.lib.bean.PushMessageRequest;
import com.newland.pospp.iot.lib.constant.MessageStatus;
import com.newland.pospp.iot.lib.exceptions.BusinessException;
import com.newland.pospp.iot.lib.exceptions.NetworkException;
import com.newland.pospp.iot.lib.push.IotPushClient;
import com.newtouch.uctp.module.business.enums.bank.SPDBBankTrans;
import com.newtouch.uctp.module.business.service.bank.PosAPIService;
import com.newtouch.uctp.module.business.service.bank.request.PosPaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PosAPIServiceImpl implements PosAPIService {

    private String pushHost = "payment://com.newland.pospp/payment?";


    @Override
    public String posPaymentPush(PosPaymentRequest request) {

        String outOrderNo = SPDBBankTrans.POS_ORDER_NO.get();
        String amount = request.getAmount();
        String devSn = request.getDevSn();
        String messageId = UUID.randomUUID().toString(true);
        String message = pushHost + "channelId=acquire&amount=" + amount + "&outOrderNo=" + outOrderNo;

        PushMessageRequest pushMessageRequest = new PushMessageRequest(message, messageId, 0, devSn);
        //发起推送&轮询,传入超时时间,建议为200s
        MessageStatusResponseBean messageStatusResponseBean = null;
        try {
            messageStatusResponseBean = IotPushClient.INSTANCE.pushAndInquiryResult(pushMessageRequest, 200 * 1000L);
        } catch (NetworkException e) {
            e.printStackTrace();
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        //状态码处理
        switch (messageStatusResponseBean.getStatus()) {
            //POS已成功处理消息并返回结果
            case MessageStatus.MESSAGE_PROCESS_COMPLETED:
                //获取POS机处理的结果,需要根据相关文档解析结果字段
                String resultMessage = messageStatusResponseBean.getResultMessage();
                break;
            //设备未绑定
            case MessageStatus.DEVICE_NOT_BIND:
                break;
            //设备离线
            case MessageStatus.DEVICE_OFFLINE:
                break;
            //设备忙,拒绝交易
            case MessageStatus.DEVICE_BUSY:
                break;
            //客户端等待应用返回结果超时,需发起新的一笔查询请求确认交易结果
            case MessageStatus.RECEIVE_TRANS_RESULT_TIMEOUT:
                break;
            //客户端已受理推送请求,正在处理中
            case MessageStatus.MESSAGE_RECEIVED:
                break;
        }

        return null;
    }
}
