package com.newtouch.uctp.module.business.service.bank.response;

   import com.fasterxml.jackson.annotation.JsonProperty;
   import com.bocom.api.BocomResponse;
   import java.util.List;
   public class UpdateIdIdNoResponseV1 extends BocomResponse {
     /** 转账收款识别码*/
     @JsonProperty("id_id_no")
     private String idIdNo;

     /** 收款识别码名称*/
     @JsonProperty("id_no_nme")
     private String idNoNme;

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

     /** 转账分组编号*/
     @JsonProperty("sub_grp_no")
     private String subGrpNo;

	public String getIdIdNo() {
		return idIdNo;
	}

	public void setIdIdNo(String idIdNo) {
		this.idIdNo = idIdNo;
	}
	public String getIdNoNme() {
		return idNoNme;
	}

	public void setIdNoNme(String idNoNme) {
		this.idNoNme = idNoNme;
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
	public String getSubGrpNo() {
		return subGrpNo;
	}

	public void setSubGrpNo(String subGrpNo) {
		this.subGrpNo = subGrpNo;
	}
}