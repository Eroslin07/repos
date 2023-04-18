package com.newtouch.uctp.module.bpm.eunms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 站内信息相关枚举
 *
 * @author hr
 */
@Getter
@AllArgsConstructor
public enum StationInfoEnum {

    //收车
    RECEIVE_FAIR_FAIL(11, "公允价值退回"),//公允价值退回
    RECEIVE_FAIR_SUCCESS(12, "公允价值通过"),//公允价值通过
    RECEIVE_W_CONTRACT_SUCCESS(13, "收车委托完成"),//收车委托完成
    RECEIVE_CONTRACT_SUCCESS(14, "收车交易成功"),//收车交易成功
    RECEIVE_CONTRACT_FAIL(15, "收车交易异常-收款款支付失败"),//收车交易异常-收款款支付失败。市场方尚未介入。

    //卖车
    SELL_FAIR_FAIL1(21, "公允价值退回"),//公允价值退回
    SELL_FAIR_SUCCESS(22, "公允价值通过"),//公允价值通过
    SELL_W_CONTRACT_SUCCESS(23, "收车委托完成"),//收车委托完成
    SELL_CONTRACT_SUCCESS(24, "收车交易成功"),//收车交易成功
    SELL_CONTRACT_FAIL(25, "收车交易异常-未及时支付提醒"),//收车交易异常-未及时支付提醒

    //我的账号-保证金：

    BOND_RECHARGE(31,"保证金充值"),//保证金充值
    BOND_DEDUCT(32,"保证金实扣"),//保证金实扣
    BOND_BACK_FILL(33,"保证金回填"),//保证金回填
    BOND_SUCCESS(34,"保证金提现"),//保证金提现
    BOND_WITHHOLD(35,"保证金预扣"),//保证金预扣
    BOND_RELEASE(36,"保证金预扣释放"),//保证金预扣释放

    //我的账号-利润

    PROFIT_SELL(41,"卖车利润"), //卖车利润
    PROFIT_WITHDRAWING(42,"利润提现中"), //利润提现中
    PROFIT_WITHDRAWAL(43,"利润提现"), //利润提现
    WAITING_BACK_FILL(44,"待回填保证金"), //待回填保证金
    TAX_DEDUCTION(45,"税费扣减"), //税费扣减
    FEE_DEDUCTION(46,"服务费扣减"), //服务费扣减
    BACK_FILL(47,"保证金回填") //保证金回填
;
/*
    //收车
    RECEIVE_FAIR_FAIL1(11, "您的收车价格XXX元偏离了市场公允价值，经由市场方审核不通过，原因：XXX。请及时关注。"),//公允价值退回
    //RECEIVE_FAIR_FAIL2(12, "您的收车价格XXX元价格偏离了市场公允价值，经由市场方审核通过，请及时处理合同签章。"),
    RECEIVE_FAIR_SUCCESS(12, "您的收车价格XXX元价格偏离了市场公允价值，经由市场方审核通过，请及时处理合同签章。"),//公允价值通过
    RECEIVE_W_CONTRACT_SUCCESS(13, "您的合同编号XXX，收车价格XXX元的收车委托合同市场方已签章完成。"),//收车委托完成
    RECEIVE_CONTRACT_SUCCESS(14, "您的合同编号XXX，收车价格XXX元的收车款已成功支付给卖家，请关注。"),//收车交易成功
    RECEIVE_CONTRACT_FAIL(15, "您的合同编号XXX，收车价格XXX元的收车合同，因XXX错误导致支付失败。请及时介入处理。"),//收车交易异常-收款款支付失败。市场方尚未介入。

    //卖车
    SELL_FAIR_FAIL1(21, "您的卖车价格XXX元偏离了市场公允价值，经由市场方审核不通过，原因：XXX。请及时关注。"),//公允价值退回
    SELL_FAIR_SUCCESS(22, "您的合同编号XXX，卖车价格XXX元价格偏离了市场公允价值，经由市场方审核通过，请及时处理合同签章。"),//公允价值通过
    SELL_W_CONTRACT_SUCCESS(23, "您的合同编号XXX，卖车价格XXX元的卖车委托合同市场方已签章完成。"),//收车委托完成
    SELL_CONTRACT_SUCCESS(24, "您的合同编号XXX，卖车价格XXX元的卖车款已收到，请关注。"),//收车交易成功
    SELL_CONTRACT_FAIL(25, "您的合同编号XXX，卖车价格XXX元的卖车款已过10分钟尚未支付，请及时提醒买家付款。"),//收车交易异常-未及时支付提醒

    //我的账号-保证金：

    BOND_RECHARGE(31,"您的保证金充值已成功，请查看。"),//保证金充值
    BOND_DEDUCT(32,"您有一笔保证金实扣用于收车业务，请查看。"),//保证金实扣
    BOND_BACK_FILL(33,"您有一笔保证金入账，来源于保证金回填，请查看。"),//保证金回填
    BOND_SUCCESS(34,"您的保证金提现已成功，请查看。"),//保证金提现
    BOND_WITHHOLD(35,"您有一笔保证金预扣用于收车业务，请查看。"),//保证金预扣
    BOND_RELEASE(36,"您有一笔保证金预扣释放源于收车业务终止，请查看。"),//保证金预扣释放

    //我的账号-利润

    PROFIT_SELL(41," 您有一笔卖车利润划入，请查看。"), //卖车利润
    PROFIT_WITHDRAWING(42," 您的利润提现已申请，目前正在审批中，请查看。"), //利润提现中
    profit_Withdrawal(43," 您的利润提现已成功，请查看。"), //利润提现
    WAITING_BACK_FILL(44," 您的卖车金额不足需预扣减，请查看。"), //待回填保证金
    TAX_DEDUCTION(45," 您有一笔税费扣减，请查看。"), //税费扣减
    FEE_DEDUCTION(46," 您有一笔税费扣减，请查看。"), //服务费扣减
    BACK_FILL(47," 您有一笔利润扣减用于保证金回填，请查看。") //保证金回填
;
*/

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;

}
