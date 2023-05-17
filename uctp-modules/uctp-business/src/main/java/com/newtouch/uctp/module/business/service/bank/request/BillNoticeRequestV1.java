package com.newtouch.uctp.module.business.service.bank.request;

import com.bocom.api.AbstractBocomRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.bocom.api.BizContent;
import com.newtouch.uctp.module.business.service.bank.response.BillNoticeResponseV1;


public class BillNoticeRequestV1 extends AbstractBocomRequest<BillNoticeResponseV1> {

  @Override
  public Class<BillNoticeResponseV1> getResponseClass() {
    return BillNoticeResponseV1.class;
  }

  @Override
  public boolean isNeedEncrypt() {
    return false;
  }

  @Override
  public String getMethod() {
    return "POST";
  }

  @Override
  public Class<? extends BizContent> getBizContentClass() {
    return BillNoticeRequestV1Biz.class;
  }

  public static class BillNoticeRequestV1Biz implements BizContent {

     /** appid@http://notify.com*/
     @JsonProperty("communication_url")
     private String communicationUrl;

     /** 主键*/
     @JsonProperty("id")
     private String id;

     /** 流水号*/
     @JsonProperty("sqn")
     private String sqn;

     /** 发起渠道【移动版-59 PC版-89 手机银行-51 智慧校园app-58 第三方发起-90 微信公众号-53】*/
     @JsonProperty("req_chn")
     private String reqChn;

     /** 请求系统码*/
     @JsonProperty("req_sys_cde")
     private String reqSysCde;

     /** 0701*/
     @JsonProperty("pay_chn")
     private String payChn;

     /** 订单号*/
     @JsonProperty("ode_no")
     private String odeNo;

     /** 交易金额*/
     @JsonProperty("amt")
     private String amt;

     /** 状态值*/
     @JsonProperty("state")
     private String state;

     /** 交易日期*/
     @JsonProperty("date")
     private String date;

     /** 交易时间*/
     @JsonProperty("time")
     private String time;

     /** 支付参数*/
     @JsonProperty("app_nme")
     private String appNme;

     /** 授权ID*/
     @JsonProperty("ath_id")
     private String athId;

     /** 机构ID*/
     @JsonProperty("org_id")
     private String orgId;

     /** 机构名称*/
     @JsonProperty("org_nme")
     private String orgNme;

     /** 项目编号*/
     @JsonProperty("pay_itm_id")
     private String payItmId;

     /** 项目名称*/
     @JsonProperty("pay_itm_nme")
     private String payItmNme;

     /** 欠费账单编号*/
     @JsonProperty("bill_id")
     private String billId;

     /** 合并缴数据*/
     @JsonProperty("bills")
     private String bills;

     /** 返回码*/
     @JsonProperty("rsp_code")
     private String rspCode;

     /** 返回信息*/
     @JsonProperty("rsp_msg")
     private String rspMsg;

     /** 更新时间*/
     @JsonProperty("upd_tme")
     private String updTme;

     /** 用户名*/
     @JsonProperty("usr_nme")
     private String usrNme;

     /** 用户编号*/
     @JsonProperty("usr_id")
     private String usrId;

     /** 缴费用户手机号*/
     @JsonProperty("usr_tel_no")
     private String usrTelNo;

     /** 支付渠道账号【如:微信号、支付宝账号】*/
     @JsonProperty("pay_chn_usr_id")
     private String payChnUsrId;

     /** 支付渠道用户名【如：微信名称、支付宝名称】*/
     @JsonProperty("pay_chn_usr_nme")
     private String payChnUsrNme;

     /** 回调状态*/
     @JsonProperty("is_callback_sts")
     private String isCallbackSts;

     /** 回调标识【01-本系统 02-支付系统 03-定时任务】*/
     @JsonProperty("is_callback_flg")
     private String isCallbackFlg;

     /** 对账状态*/
     @JsonProperty("acc_chk_sts")
     private String accChkSts;

     /** 对账标识*/
     @JsonProperty("acc_chk_flg")
     private String accChkFlg;

     /** 对账日期*/
     @JsonProperty("acc_dte")
     private String accDte;

     /** 币种*/
     @JsonProperty("ccy")
     private String ccy;

     /** 下单类型【bill-查缴类|direct-直缴类|merge-合并缴费类】*/
     @JsonProperty("type")
     private String type;

     /** 业务数据，内容与格式不做处理*/
     @JsonProperty("detail")
     private String detail;

     /** 备用字段*/
     @JsonProperty("rsv_fld")
     private String rsvFld;

     /** 备用字段1*/
     @JsonProperty("rsv_fld1")
     private String rsvFld1;

     /** 备用字段2*/
     @JsonProperty("rsv_fld2")
     private String rsvFld2;

     /** 通知状态1-成功；0-通知失败，放弃通知*/
     @JsonProperty("notic_sts")
     private String noticSts;

     /** 通知标识【01-本系统|02-回调通知|03-定时任务】*/
     @JsonProperty("notic_flg")
     private String noticFlg;

     /** 支付策略*/
     @JsonProperty("pay_stg")
     private String payStg;

     /** 支付接口编号*/
     @JsonProperty("txn_chn")
     private String txnChn;

     /** 缴费用户证件号码*/
     @JsonProperty("id_no")
     private String idNo;

     /** 二维码ID*/
     @JsonProperty("qr_id")
     private String qrId;

     /** 项目数量*/
     @JsonProperty("pay_itm_num")
     private String payItmNum;

     /** 业务类型*/
     @JsonProperty("bus_type")
     private String busType;

     /** 第三方平台编号*/
     @JsonProperty("thd_plt_id")
     private String thdPltId;

     /** 第三方请求编号*/
     @JsonProperty("thd_sqn")
     private String thdSqn;

     /** 收款识别码*/
     @JsonProperty("id_id_no")
     private String idIdNo;

     /** 快捷支付协议编号*/
     @JsonProperty("agr_no")
     private String agrNo;

     /** 维护人编号*/
     @JsonProperty("opr_cus_id")
     private String oprCusId;

     /** 维护人姓名*/
     @JsonProperty("opr_cus_nme")
     private String oprCusNme;

     /** 手续费*/
     @JsonProperty("txn_fee")
     private String txnFee;

     /** 核心下发会计流水号*/
     @JsonProperty("vch_no")
     private String vchNo;

     /** ONLINE-线上 、OFFLINE-线下*/
     @JsonProperty("location")
     private String location;

     /** 批量明细id*/
     @JsonProperty("bat_detail_id")
     private String batDetailId;

     /** 主订单号*/
     @JsonProperty("parent_ode_no")
     private String parentOdeNo;

     /** 二级入账户*/
     @JsonProperty("sub_ac")
     private String subAc;

     /** 缴款人名称*/
     @JsonProperty("pay_usr_nme")
     private String payUsrNme;

     /** 缴款账户*/
     @JsonProperty("pay_ac")
     private String payAc;

     /** 是否已经二次清分 0-是 1-否*/
     @JsonProperty("snd_clr_flg")
     private String sndClrFlg;

     /** 二维码文本*/
     @JsonProperty("display_code_text")
     private String displayCodeText;

     /** 退款金额*/
     @JsonProperty("rfu_amt")
     private String rfuAmt;

     /** 退款状态R:全额退   */
     @JsonProperty("rfu_sts")
     private String rfuSts;

     /** 业务传票摘要区*/
     @JsonProperty("vch_bus_rmk")
     private String vchBusRmk;

     /** 商户流水号*/
     @JsonProperty("bank_tran_no")
     private String bankTranNo;

     /** 分行特色流水：1-分行特色流水；其他总行产品*/
     @JsonProperty("is_br")
     private String isBr;

     /** 商品编号*/
     @JsonProperty("code")
     private String code;

     /** 发票状态：S-成功；F-失败*/
     @JsonProperty("inv_sts")
     private String invSts;

     /** 微信支付宝等交易单号*/
     @JsonProperty("pay_id")
     private String payId;

	public String getCommunicationUrl() {
		return communicationUrl;
	}

	public void setCommunicationUrl(String communicationUrl) {
		this.communicationUrl = communicationUrl;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getSqn() {
		return sqn;
	}

	public void setSqn(String sqn) {
		this.sqn = sqn;
	}
	public String getReqChn() {
		return reqChn;
	}

	public void setReqChn(String reqChn) {
		this.reqChn = reqChn;
	}
	public String getReqSysCde() {
		return reqSysCde;
	}

	public void setReqSysCde(String reqSysCde) {
		this.reqSysCde = reqSysCde;
	}
	public String getPayChn() {
		return payChn;
	}

	public void setPayChn(String payChn) {
		this.payChn = payChn;
	}
	public String getOdeNo() {
		return odeNo;
	}

	public void setOdeNo(String odeNo) {
		this.odeNo = odeNo;
	}
	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public String getAppNme() {
		return appNme;
	}

	public void setAppNme(String appNme) {
		this.appNme = appNme;
	}
	public String getAthId() {
		return athId;
	}

	public void setAthId(String athId) {
		this.athId = athId;
	}
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgNme() {
		return orgNme;
	}

	public void setOrgNme(String orgNme) {
		this.orgNme = orgNme;
	}
	public String getPayItmId() {
		return payItmId;
	}

	public void setPayItmId(String payItmId) {
		this.payItmId = payItmId;
	}
	public String getPayItmNme() {
		return payItmNme;
	}

	public void setPayItmNme(String payItmNme) {
		this.payItmNme = payItmNme;
	}
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBills() {
		return bills;
	}

	public void setBills(String bills) {
		this.bills = bills;
	}
	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
	public String getUpdTme() {
		return updTme;
	}

	public void setUpdTme(String updTme) {
		this.updTme = updTme;
	}
	public String getUsrNme() {
		return usrNme;
	}

	public void setUsrNme(String usrNme) {
		this.usrNme = usrNme;
	}
	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getUsrTelNo() {
		return usrTelNo;
	}

	public void setUsrTelNo(String usrTelNo) {
		this.usrTelNo = usrTelNo;
	}
	public String getPayChnUsrId() {
		return payChnUsrId;
	}

	public void setPayChnUsrId(String payChnUsrId) {
		this.payChnUsrId = payChnUsrId;
	}
	public String getPayChnUsrNme() {
		return payChnUsrNme;
	}

	public void setPayChnUsrNme(String payChnUsrNme) {
		this.payChnUsrNme = payChnUsrNme;
	}
	public String getIsCallbackSts() {
		return isCallbackSts;
	}

	public void setIsCallbackSts(String isCallbackSts) {
		this.isCallbackSts = isCallbackSts;
	}
	public String getIsCallbackFlg() {
		return isCallbackFlg;
	}

	public void setIsCallbackFlg(String isCallbackFlg) {
		this.isCallbackFlg = isCallbackFlg;
	}
	public String getAccChkSts() {
		return accChkSts;
	}

	public void setAccChkSts(String accChkSts) {
		this.accChkSts = accChkSts;
	}
	public String getAccChkFlg() {
		return accChkFlg;
	}

	public void setAccChkFlg(String accChkFlg) {
		this.accChkFlg = accChkFlg;
	}
	public String getAccDte() {
		return accDte;
	}

	public void setAccDte(String accDte) {
		this.accDte = accDte;
	}
	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getRsvFld() {
		return rsvFld;
	}

	public void setRsvFld(String rsvFld) {
		this.rsvFld = rsvFld;
	}
	public String getRsvFld1() {
		return rsvFld1;
	}

	public void setRsvFld1(String rsvFld1) {
		this.rsvFld1 = rsvFld1;
	}
	public String getRsvFld2() {
		return rsvFld2;
	}

	public void setRsvFld2(String rsvFld2) {
		this.rsvFld2 = rsvFld2;
	}
	public String getNoticSts() {
		return noticSts;
	}

	public void setNoticSts(String noticSts) {
		this.noticSts = noticSts;
	}
	public String getNoticFlg() {
		return noticFlg;
	}

	public void setNoticFlg(String noticFlg) {
		this.noticFlg = noticFlg;
	}
	public String getPayStg() {
		return payStg;
	}

	public void setPayStg(String payStg) {
		this.payStg = payStg;
	}
	public String getTxnChn() {
		return txnChn;
	}

	public void setTxnChn(String txnChn) {
		this.txnChn = txnChn;
	}
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getQrId() {
		return qrId;
	}

	public void setQrId(String qrId) {
		this.qrId = qrId;
	}
	public String getPayItmNum() {
		return payItmNum;
	}

	public void setPayItmNum(String payItmNum) {
		this.payItmNum = payItmNum;
	}
	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getThdPltId() {
		return thdPltId;
	}

	public void setThdPltId(String thdPltId) {
		this.thdPltId = thdPltId;
	}
	public String getThdSqn() {
		return thdSqn;
	}

	public void setThdSqn(String thdSqn) {
		this.thdSqn = thdSqn;
	}
	public String getIdIdNo() {
		return idIdNo;
	}

	public void setIdIdNo(String idIdNo) {
		this.idIdNo = idIdNo;
	}
	public String getAgrNo() {
		return agrNo;
	}

	public void setAgrNo(String agrNo) {
		this.agrNo = agrNo;
	}
	public String getOprCusId() {
		return oprCusId;
	}

	public void setOprCusId(String oprCusId) {
		this.oprCusId = oprCusId;
	}
	public String getOprCusNme() {
		return oprCusNme;
	}

	public void setOprCusNme(String oprCusNme) {
		this.oprCusNme = oprCusNme;
	}
	public String getTxnFee() {
		return txnFee;
	}

	public void setTxnFee(String txnFee) {
		this.txnFee = txnFee;
	}
	public String getVchNo() {
		return vchNo;
	}

	public void setVchNo(String vchNo) {
		this.vchNo = vchNo;
	}
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	public String getBatDetailId() {
		return batDetailId;
	}

	public void setBatDetailId(String batDetailId) {
		this.batDetailId = batDetailId;
	}
	public String getParentOdeNo() {
		return parentOdeNo;
	}

	public void setParentOdeNo(String parentOdeNo) {
		this.parentOdeNo = parentOdeNo;
	}
	public String getSubAc() {
		return subAc;
	}

	public void setSubAc(String subAc) {
		this.subAc = subAc;
	}
	public String getPayUsrNme() {
		return payUsrNme;
	}

	public void setPayUsrNme(String payUsrNme) {
		this.payUsrNme = payUsrNme;
	}
	public String getPayAc() {
		return payAc;
	}

	public void setPayAc(String payAc) {
		this.payAc = payAc;
	}
	public String getSndClrFlg() {
		return sndClrFlg;
	}

	public void setSndClrFlg(String sndClrFlg) {
		this.sndClrFlg = sndClrFlg;
	}
	public String getDisplayCodeText() {
		return displayCodeText;
	}

	public void setDisplayCodeText(String displayCodeText) {
		this.displayCodeText = displayCodeText;
	}
	public String getRfuAmt() {
		return rfuAmt;
	}

	public void setRfuAmt(String rfuAmt) {
		this.rfuAmt = rfuAmt;
	}
	public String getRfuSts() {
		return rfuSts;
	}

	public void setRfuSts(String rfuSts) {
		this.rfuSts = rfuSts;
	}
	public String getVchBusRmk() {
		return vchBusRmk;
	}

	public void setVchBusRmk(String vchBusRmk) {
		this.vchBusRmk = vchBusRmk;
	}
	public String getBankTranNo() {
		return bankTranNo;
	}

	public void setBankTranNo(String bankTranNo) {
		this.bankTranNo = bankTranNo;
	}
	public String getIsBr() {
		return isBr;
	}

	public void setIsBr(String isBr) {
		this.isBr = isBr;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getInvSts() {
		return invSts;
	}

	public void setInvSts(String invSts) {
		this.invSts = invSts;
	}
	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}
}
}