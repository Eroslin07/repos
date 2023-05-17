package com.newtouch.uctp.module.business.service.bank.request;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.newtouch.uctp.module.business.service.bank.response.CreateTrsCusAcResponseV1;


public class CreateTrsCusAcRequestV1 extends AbstractBocomRequest<CreateTrsCusAcResponseV1> {

  @Override
  public Class<CreateTrsCusAcResponseV1> getResponseClass() {
    return CreateTrsCusAcResponseV1.class;
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
    return CreateTrsCusAcRequestV1Biz.class;
  }



  public static class CreateTrsCusAcRequestV1Biz implements BizContent {

     /** 产品类型*/
     @JsonProperty("prd_typ")
     private String prdTyp;

     /** 机构编号*/
     @JsonProperty("org_id")
     private String orgId;

     /** 支付渠道*/
     @JsonProperty("pay_chn")
     private String payChn;

     /** 业务数据*/
     @JsonProperty("detail")
     private String detail;

     /** 转账收款识别码*/
     @JsonProperty("id_id_no")
     private String idIdNo;

     /** 转账分组编号*/
     @JsonProperty("sub_grp_no")
     private String subGrpNo;

     /** 收款识别码名称*/
     @JsonProperty("id_no_nme")
     private String idNoNme;

     /** 收款账号*/
     @JsonProperty("cus_ac")
     private String cusAc;

     /** 限定收款金额*/
     @JsonProperty("lmt_rcv_mny_amt")
     private String lmtRcvMnyAmt;

     /** 最小收款金额*/
     @JsonProperty("min_rcv_mnv_amt")
     private String minRcvMnvAmt;

     /** 最大收款金额*/
     @JsonProperty("max_rcv_mny_amt")
     private String maxRcvMnyAmt;

     /** 限定付款账号*/
     @JsonProperty("lmt_pay_ac")
     private String lmtPayAc;

     /** 付款账号*/
     @JsonProperty("pay_ac")
     private String payAc;

     /** 付款账户名(150)*/
     @JsonProperty("pay_ac_nme150")
     private String payAcNme150;

     /** 限定收款日期*/
     @JsonProperty("lmt_rcv_amt_dte")
     private String lmtRcvAmtDte;

     /** 起始收款日期*/
     @JsonProperty("rcv_amt_sdt")
     private String rcvAmtSdt;

     /** 截止收款日期*/
     @JsonProperty("rcv_amt_edt")
     private String rcvAmtEdt;

     /** 收款识别码标记码*/
     @JsonProperty("uni_idt_str")
     private String uniIdtStr;

     /** 第三方编号*/
     @JsonProperty("thd_id")
     private String thdId;

     /** 识别号备注*/
     @JsonProperty("id_no_rmk")
     private String idNoRmk;

     /** 备用字段*/
     @JsonProperty("rsv_fld")
     private String rsvFld;

	public String getPrdTyp() {
		return prdTyp;
	}

	public void setPrdTyp(String prdTyp) {
		this.prdTyp = prdTyp;
	}
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getPayChn() {
		return payChn;
	}

	public void setPayChn(String payChn) {
		this.payChn = payChn;
	}
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getIdIdNo() {
		return idIdNo;
	}

	public void setIdIdNo(String idIdNo) {
		this.idIdNo = idIdNo;
	}
	public String getSubGrpNo() {
		return subGrpNo;
	}

	public void setSubGrpNo(String subGrpNo) {
		this.subGrpNo = subGrpNo;
	}
	public String getIdNoNme() {
		return idNoNme;
	}

	public void setIdNoNme(String idNoNme) {
		this.idNoNme = idNoNme;
	}
	public String getCusAc() {
		return cusAc;
	}

	public void setCusAc(String cusAc) {
		this.cusAc = cusAc;
	}
	public String getLmtRcvMnyAmt() {
		return lmtRcvMnyAmt;
	}

	public void setLmtRcvMnyAmt(String lmtRcvMnyAmt) {
		this.lmtRcvMnyAmt = lmtRcvMnyAmt;
	}
	public String getMinRcvMnvAmt() {
		return minRcvMnvAmt;
	}

	public void setMinRcvMnvAmt(String minRcvMnvAmt) {
		this.minRcvMnvAmt = minRcvMnvAmt;
	}
	public String getMaxRcvMnyAmt() {
		return maxRcvMnyAmt;
	}

	public void setMaxRcvMnyAmt(String maxRcvMnyAmt) {
		this.maxRcvMnyAmt = maxRcvMnyAmt;
	}
	public String getLmtPayAc() {
		return lmtPayAc;
	}

	public void setLmtPayAc(String lmtPayAc) {
		this.lmtPayAc = lmtPayAc;
	}
	public String getPayAc() {
		return payAc;
	}

	public void setPayAc(String payAc) {
		this.payAc = payAc;
	}
	public String getPayAcNme150() {
		return payAcNme150;
	}

	public void setPayAcNme150(String payAcNme150) {
		this.payAcNme150 = payAcNme150;
	}
	public String getLmtRcvAmtDte() {
		return lmtRcvAmtDte;
	}

	public void setLmtRcvAmtDte(String lmtRcvAmtDte) {
		this.lmtRcvAmtDte = lmtRcvAmtDte;
	}
	public String getRcvAmtSdt() {
		return rcvAmtSdt;
	}

	public void setRcvAmtSdt(String rcvAmtSdt) {
		this.rcvAmtSdt = rcvAmtSdt;
	}
	public String getRcvAmtEdt() {
		return rcvAmtEdt;
	}

	public void setRcvAmtEdt(String rcvAmtEdt) {
		this.rcvAmtEdt = rcvAmtEdt;
	}
	public String getUniIdtStr() {
		return uniIdtStr;
	}

	public void setUniIdtStr(String uniIdtStr) {
		this.uniIdtStr = uniIdtStr;
	}
	public String getThdId() {
		return thdId;
	}

	public void setThdId(String thdId) {
		this.thdId = thdId;
	}
	public String getIdNoRmk() {
		return idNoRmk;
	}

	public void setIdNoRmk(String idNoRmk) {
		this.idNoRmk = idNoRmk;
	}
	public String getRsvFld() {
		return rsvFld;
	}

	public void setRsvFld(String rsvFld) {
		this.rsvFld = rsvFld;
	}
}
}