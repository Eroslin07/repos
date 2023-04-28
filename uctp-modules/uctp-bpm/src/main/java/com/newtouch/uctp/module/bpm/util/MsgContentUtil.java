package com.newtouch.uctp.module.bpm.util;



import com.newtouch.uctp.module.bpm.eunms.StationInfoEnum;

import java.util.HashMap;
import java.util.Map;

public class MsgContentUtil {



    public static Map<String,String > getContent( Map<String,String> map){
        String result="";
        String title="";
        //buyType=1 为买 ，buyType=2 为卖
        String buyType="买";
        String amount=map.get("vehicleReceiptAmount");
        if (map.get("buyType")!=null){
            if (map.get("buyType").equals("2")){
                buyType="卖";
                amount=map.get("sellAmount");
            }

        }
        Map<String,String> contentMap=new HashMap<>();
        //type  为1表示短信推送模版，0表示为站内消息模版
        if ("1".equals(map.get("type"))) {
            switch (map.get("contentType")) {
                case "11"://1
                    result="【翼龙科技】您在翼龙科技平台的账号注册流程因"+map.get("reason")+"原因审批未通过，请及时与市场方进行沟通。";//流程调存一条站内，一条短信
                    break;
                case "12":
                    result="【翼龙科技】恭喜您已经成为翼龙科技平台的一员，请登录翼龙科技小程序进行使用";
                    break;
                case "21"://5
                    result="【翼龙科技】您的"+buyType+"车价格"+amount+"元偏离了市场公允价值，经由市场方审核不通过，请联系市场方沟通处理。";//流程调存一条站内，一条短信
                    break;
                case "22":
                    result="【翼龙科技】"+buyType+"车合同卖家签章【契约锁触发】";
                    break;
                case "23"://4
                    result="【翼龙科技】您的合同编号"+map.get("contractId")+"，"+buyType+"车款"+amount+"元已成功支付给卖家，请关注。";
                    break;

                case "24"://3
                    result="【翼龙科技】您的合同编号"+map.get("contractId")+"，金额"+amount+"元，因"+map.get("reason")+"错误导致支付失败。请及时通知市场方介入处理。";
                    break;

                case "25":
                    result="【翼龙科技】因"+map.get("reason")+"错误导致您的"+buyType+"车款收款失败。二手车交易平台给您发送了一份文件《XXX》，请您访问链接完成签署（链接地址）";
                    break;
                case "26"://2
                    result="【翼龙科技】您的收车合同编号"+map.get("contractId")+"，金额"+amount+"元，"+buyType+"家签署已超过10分钟，仍未签字，请及时与卖/买家沟通处理。";
                    break;

              /*  case "31"://5
                    result="【翼龙科技】您的卖车价格"+amount+"元偏离了市场公允价值，经由市场方审核不通过，请联系市场方沟通处理。";
                    break;
                case "32":
                    result="【翼龙科技】收车合同买家签章【契约锁触发】";
                    break;
                case "33"://4
                    result="【翼龙科技】您的合同编号"+map.get("contractId")+"，卖车款"+amount+"元已成功支付给卖家，请关注。";
                    break;
                case "34":
                    result="【翼龙科技】您的合同编号"+map.get("contractId")+"，金额"+amount+"元，因"+map.get("reason")+"错误导致支付失败。请及时通知市场方介入处理。";
                    break;
                case "35":
                    result="【翼龙科技】因"+map.get("reason")+"错误导致您的卖车款收款失败。二手车交易平台给您发送了一份文件《XXX》，请您访问链接完成签署（链接地址）";
                    break;
                case "36"://2
                    result="【翼龙科技】您的卖车合同编号"+map.get("contractId")+"，金额"+amount+"元，卖家/买家签署已超过10分钟，仍未签字，请及时与卖/买家沟通处理。";
                    break;*/
//-------
                case "40":
                    result = String.format("【翼龙科技】恭喜您已成为车友通一员，交易中电子签需您进行企业认证，请在15分钟内访问%s 完成认证",map.get("url"));
                    break;
                case "41":
                    result = String.format("【翼龙科技】恭喜您已完成企业认证，交易中电子签需您进行个人认证，请在15分钟内访问%s 完成认证", map.get("url"));
                    break;
                case "42":
                    result = String.format("【翼龙科技】恭喜您已成为车友通商户员工，交易中电子签需您进行个人认证，请在15分钟内访问%s 完成认证。", map.get("url"));
                    break;
                case "43":
                    result=map.get("url");
                    break;
                default:
                    result="无匹配模版";

            }
        }
        else if ("0".equals(map.get("type"))) {
            switch (map.get("contentType")) {
                //收车
                case "11":
                    result = "您的收车价格"+amount+"元偏离了市场公允价值，经由市场方审核不通过，原因："+map.get("reason")+"。请及时关注。";
                    //title="公允价值退回";
                    title= StationInfoEnum.RECEIVE_FAIR_FAIL.getDesc();
                    break;
                case "12":
                    result = "您的收车价格"+amount+"元价格偏离了市场公允价值，经由市场方审核通过，请及时处理合同签章。";
                    //title="公允价值通过";
                    title= StationInfoEnum.RECEIVE_FAIR_SUCCESS.getDesc();
                    break;
                case "13":
                    result = "您的合同编号"+map.get("contractId")+"，收车价格"+amount+"元的收车委托合同市场方已签章完成。";
                    //title="收车委托完成";
                    title= StationInfoEnum.RECEIVE_W_CONTRACT_SUCCESS.getDesc();
                    break;
                case "14":
                    result = "您的合同编号"+map.get("contractId")+"，收车价格"+amount+"元的收车款已成功支付给卖家，请关注。";
                    //title="收车交易成功";
                    title= StationInfoEnum.RECEIVE_CONTRACT_SUCCESS.getDesc();
                    break;
                case "15":
                    result = "您的合同编号"+map.get("contractId")+"，收车价格"+amount+"元的收车合同，因："+map.get("reason")+"。错误导致支付失败。请及时介入处理。";
                    //title="收车交易异常-收款款支付失败";
                    title= StationInfoEnum.RECEIVE_CONTRACT_FAIL.getDesc();
                    break;
                //卖车
                case "21":
                    result = "您的卖车价格"+amount+"元偏离了市场公允价值，经由市场方审核不通过，原因："+map.get("reason")+"。请及时关注。";
                    //title="公允价值退回";
                    title= StationInfoEnum.SELL_FAIR_FAIL1.getDesc();
                    break;
                case "22":
                    result = "您的合同编号"+map.get("contractId")+"，卖车价格"+amount+"元价格偏离了市场公允价值，经由市场方审核通过，请及时处理合同签章。";
                    //title="公允价值通过";
                    title= StationInfoEnum.SELL_FAIR_SUCCESS.getDesc();
                    break;
                case "23":
                    result = "您的合同编号"+map.get("contractId")+"，卖车价格"+amount+"元的卖车委托合同市场方已签章完成。";
                    //title="收车委托完成";
                    title= StationInfoEnum.SELL_W_CONTRACT_SUCCESS.getDesc();
                    break;
                case "24":
                    result = "您的合同编号"+map.get("contractId")+"，卖车价格"+amount+"元的卖车款已收到，请关注。";
                    //title="收车交易成功";
                    title= StationInfoEnum.SELL_CONTRACT_SUCCESS.getDesc();
                    break;
                case "25":
                    result = "您的合同编号"+map.get("contractId")+"，卖车价格"+amount+"元的卖车款已过10分钟尚未支付，请及时提醒买家付款。";
                    //title="收车交易异常-未及时支付提醒";
                    title= StationInfoEnum.SELL_CONTRACT_FAIL.getDesc();
                    break;
                //我的账号-保证金：
                case "31":
                    result = "您的保证金充值已成功，请查看。";
                    //title="保证金充值";
                    title= StationInfoEnum.BOND_RECHARGE.getDesc();
                    break;
                case "32":
                    result = "您有一笔保证金实扣用于收车业务，请查看。";
                    //title="保证金实扣";
                    title= StationInfoEnum.BOND_DEDUCT.getDesc();
                    break;
                case "33":
                    result = "您有一笔保证金入账，来源于保证金回填，请查看。";
                    //title="保证金回填";
                    title= StationInfoEnum.BOND_BACK_FILL.getDesc();
                    break;
                case "34":
                    result = "您的保证金提现已成功，请查看。";
                    //title="保证金提现";
                    title= StationInfoEnum.BOND_SUCCESS.getDesc();
                    break;
                case "35":
                    result = "您有一笔保证金预扣用于收车业务，请查看。";
                    //title="保证金预扣";
                    title= StationInfoEnum.BOND_WITHHOLD.getDesc();
                    break;
                case "36":
                    result = "您有一笔保证金预扣释放源于收车业务终止，请查看。";
                    //title="保证金预扣释放";
                    title= StationInfoEnum.BOND_RELEASE.getDesc();
                    break;
                //我的账号-利润
                case "41":
                    result = "您有一笔卖车利润划入，请查看。";
                    //title="卖车利润";
                    title= StationInfoEnum.PROFIT_SELL.getDesc();
                    break;
                case "42":
                    result = "您的利润提现已申请，目前正在审批中，，请查看。";
                    //title="利润提现中";
                    title= StationInfoEnum.PROFIT_WITHDRAWING.getDesc();
                    break;
                case "43":
                    result = "您的利润提现已成功，请查看。";
                    //title="利润提现";
                    title= StationInfoEnum.PROFIT_WITHDRAWAL.getDesc();
                    break;
                case "44":
                    result = "您的卖车金额不足需预扣减，请查看。";
                    //title="待回填保证金";
                    title= StationInfoEnum.WAITING_BACK_FILL.getDesc();
                    break;
                case "45":
                    result = "您有一笔税费扣减，请查看。";
                    //title="税费扣减";
                    title= StationInfoEnum.TAX_DEDUCTION.getDesc();
                    break;
                case "46":
                    result = "您有一笔服务费扣减，请查看。";
                    //title="服务费扣减";
                    title= StationInfoEnum.TAX_DEDUCTION.getDesc();
                    break;
                case "47":
                    result = "您有一笔利润扣减用于保证金回填，请查看。";
                    //title="保证金回填";
                    title= StationInfoEnum.BACK_FILL.getDesc();
                    break;
                default:
                    result = "无匹配模版";
            }
        }
        contentMap.put("content",result);
        contentMap.put("title",title);
        return contentMap;
       /* String result="";
        String title="";
        Map<String,String> contentMap=new HashMap<>();
        //type  为1表示短信推送模版，0表示为站内消息模版
        if ("1".equals(map.get("type"))) {
            switch (map.get("contentType")) {
                case "11":
                    result="【二手车交易平台】您在二手车交易平台的账号注册流程因【"+map.get("reason")+"】原因审批未通过，请及时与市场方进行沟通。";
                    break;
                case "12":
                    result="【二手车交易平台】恭喜您已经成为二手车交易平台的一员，请登录二手车交易小程序进行使用";
                    break;
                case "21":
                    result="【二手车交易平台】您的收车价格"+map.get("vehicleReceiptAmount")+"元偏离了市场公允价值，经由市场方审核不通过，请联系市场方沟通处理。";
                    break;
                case "22":
                    result="【二手车交易平台】收车合同卖家签章【契约锁触发】";
                    break;
                case "23":
                    result="【二手车交易平台】您的合同编号"+map.get("contractId")+"，收车款"+map.get("vehicleReceiptAmount")+"元已成功支付给卖家，请关注。";
                    break;
                case "24":
                    result="【二手车交易平台】您的合同编号"+map.get("contractId")+"，金额"+map.get("vehicleReceiptAmount")+"元，因【"+map.get("reason")+"】错误导致支付失败。请及时通知市场方介入处理。";
                    break;
                case "25":
                    result="【二手车交易平台】因【"+map.get("reason")+"】错误导致您的买车款收款失败。二手车交易平台给您发送了一份文件《XXX》，请您访问链接完成签署（链接地址）";
                    break;
                case "26":
                    result="【二手车交易平台】您的收车合同编号"+map.get("contractId")+"，金额"+map.get("vehicleReceiptAmount")+"元，卖家/买家签署已超过10分钟，仍未签字，请及时与卖/买家沟通处理。";
                    break;

                case "31":
                    result="【二手车交易平台】您的卖车价格"+map.get("sellAmount")+"元偏离了市场公允价值，经由市场方审核不通过，请联系市场方沟通处理。";
                    break;
                case "32":
                    result="【二手车交易平台】收车合同买家签章【契约锁触发】";
                    break;
                case "33":
                    result="【二手车交易平台】您的合同编号"+map.get("contractId")+"，卖车款"+map.get("sellAmount")+"元已成功支付给卖家，请关注。";
                    break;
                case "34":
                    result="【二手车交易平台】您的合同编号"+map.get("contractId")+"，金额"+map.get("sellAmount")+"元，因【"+map.get("reason")+"】错误导致支付失败。请及时通知市场方介入处理。";
                    break;
                case "35":
                    result="【二手车交易平台】因【"+map.get("reason")+"】错误导致您的卖车款收款失败。二手车交易平台给您发送了一份文件《XXX》，请您访问链接完成签署（链接地址）";
                    break;
                case "36":
                    result="【二手车交易平台】您的卖车合同编号"+map.get("contractId")+"，金额"+map.get("sellAmount")+"元，卖家/买家签署已超过10分钟，仍未签字，请及时与卖/买家沟通处理。";
                    break;

                default:
                    result="无匹配模版";

            }
        }else if ("0".equals(map.get("type"))) {
            switch (map.get("contentType")) {
                //收车
                case "11":
                    result = "您的收车价格"+map.get("vehicleReceiptAmount")+"元偏离了市场公允价值，经由市场方审核不通过，因【"+map.get("reason")+"】。请及时关注。";
                    //title="公允价值退回";
                    title= StationInfoEnum.RECEIVE_FAIR_FAIL.getDesc();
                    break;
                case "12":
                    result = "您的收车价格"+map.get("vehicleReceiptAmount")+"元价格偏离了市场公允价值，经由市场方审核通过，请及时处理合同签章。";
                    //title="公允价值通过";
                    title= StationInfoEnum.RECEIVE_FAIR_SUCCESS.getDesc();
                    break;
                case "13":
                    result = "您的合同编号"+map.get("contractId")+"，收车价格"+map.get("vehicleReceiptAmount")+"元的收车委托合同市场方已签章完成。";
                    //title="收车委托完成";
                    title= StationInfoEnum.RECEIVE_W_CONTRACT_SUCCESS.getDesc();
                    break;
                case "14":
                    result = "您的合同编号"+map.get("contractId")+"，收车价格"+map.get("vehicleReceiptAmount")+"元的收车款已成功支付给卖家，请关注。";
                    //title="收车交易成功";
                    title= StationInfoEnum.RECEIVE_CONTRACT_SUCCESS.getDesc();
                    break;
                case "15":
                    result = "您的合同编号"+map.get("contractId")+"，收车价格"+map.get("vehicleReceiptAmount")+"元的收车合同，因【"+map.get("reason")+"】。错误导致支付失败。请及时介入处理。";
                    //title="收车交易异常-收款款支付失败";
                    title= StationInfoEnum.RECEIVE_CONTRACT_FAIL.getDesc();
                    break;
                //卖车
                case "21":
                    result = "您的卖车价格"+map.get("sellAmount")+"元偏离了市场公允价值，经由市场方审核不通过，因【"+map.get("reason")+"】。请及时关注。";
                    //title="公允价值退回";
                    title= StationInfoEnum.SELL_FAIR_FAIL1.getDesc();
                    break;
                case "22":
                    result = "您的卖车价格"+map.get("sellAmount")+"元价格偏离了市场公允价值，经由市场方审核通过，请及时处理合同签章。";
                    //title="公允价值通过";
                    title= StationInfoEnum.SELL_FAIR_SUCCESS.getDesc();
                    break;
                case "23":
                    result = "您的合同编号"+map.get("contractId")+"，卖车价格"+map.get("sellAmount")+"元的卖车委托合同市场方已签章完成。";
                    //title="收车委托完成";
                    title= StationInfoEnum.SELL_W_CONTRACT_SUCCESS.getDesc();
                    break;
                case "24":
                    result = "您的合同编号"+map.get("contractId")+"，卖车价格"+map.get("sellAmount")+"元的卖车款已收到，请关注。";
                    //title="收车交易成功";
                    title= StationInfoEnum.SELL_CONTRACT_SUCCESS.getDesc();
                    break;
                case "25":
                    result = "您的合同编号"+map.get("contractId")+"，卖车价格"+map.get("sellAmount")+"元的卖车款已过10分钟尚未支付，请及时提醒买家付款。";
                    //title="收车交易异常-未及时支付提醒";
                    title= StationInfoEnum.SELL_CONTRACT_FAIL.getDesc();
                    break;
                 //我的账号-保证金：
                case "31":
                    result = "您的保证金充值已成功，请查看。";
                    //title="保证金充值";
                    title= StationInfoEnum.BOND_RECHARGE.getDesc();
                    break;
                case "32":
                    result = "您有一笔保证金实扣用于收车业务，请查看。";
                    //title="保证金实扣";
                    title= StationInfoEnum.BOND_DEDUCT.getDesc();
                    break;
                case "33":
                    result = "您有一笔保证金入账，来源于保证金回填，请查看。";
                    //title="保证金回填";
                    title= StationInfoEnum.BOND_BACK_FILL.getDesc();
                    break;
                case "34":
                    result = "您的保证金提现已成功，请查看。";
                    //title="保证金提现";
                    title= StationInfoEnum.BOND_SUCCESS.getDesc();
                    break;
                case "35":
                    result = "您有一笔保证金预扣用于收车业务，请查看。";
                    //title="保证金预扣";
                    title= StationInfoEnum.BOND_WITHHOLD.getDesc();
                    break;
                case "36":
                    result = "您有一笔保证金预扣释放源于收车业务终止，请查看。";
                    //title="保证金预扣释放";
                    title= StationInfoEnum.BOND_RELEASE.getDesc();
                    break;
                //我的账号-利润
                case "41":
                    result = "您有一笔卖车利润划入，请查看。";
                    //title="卖车利润";
                    title= StationInfoEnum.PROFIT_SELL.getDesc();
                    break;
                case "42":
                    result = "您的利润提现已申请，目前正在审批中，，请查看。";
                    //title="利润提现中";
                    title= StationInfoEnum.PROFIT_WITHDRAWING.getDesc();
                    break;
                case "43":
                    result = "您的利润提现已成功，请查看。";
                    //title="利润提现";
                    title= StationInfoEnum.PROFIT_WITHDRAWAL.getDesc();
                    break;
                case "44":
                    result = "您的卖车金额不足需预扣减，请查看。";
                    //title="待回填保证金";
                    title= StationInfoEnum.WAITING_BACK_FILL.getDesc();
                    break;
                case "45":
                    result = "您有一笔税费扣减，请查看。";
                    //title="税费扣减";
                    title= StationInfoEnum.TAX_DEDUCTION.getDesc();
                    break;
                case "46":
                    result = "您有一笔服务费扣减，请查看。";
                    //title="服务费扣减";
                    title= StationInfoEnum.TAX_DEDUCTION.getDesc();
                    break;
                case "47":
                    result = "您有一笔利润扣减用于保证金回填，请查看。";
                    //title="保证金回填";
                    title= StationInfoEnum.BACK_FILL.getDesc();
                    break;
                default:
                    result = "无匹配模版";
            }
        }
        contentMap.put("content",result);
        contentMap.put("title",title);
        return contentMap;*/
    }
    
}
