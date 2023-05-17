package com.newtouch.uctp.module.business.service.bank.response;

   import com.fasterxml.jackson.annotation.JsonProperty;
   import com.bocom.api.BocomResponse;
   import java.util.List;
   public class QueryTodayPayResultResponseV1 extends BocomResponse {
	/** ""*/
	@JsonProperty("ord_pay_jnl_list")
	private List<OrdPayJnl> ordPayJnlList;

	public static class OrdPayJnl {
     /** 交易流水号*/
     @JsonProperty("sqn")
     private String sqn;

     /** 订单号*/
     @JsonProperty("ode_no")
     private String odeNo;

     /** 记账渠道类型*/
     @JsonProperty("pay_chl")
     private String payChl;

     /** 机构编号*/
     @JsonProperty("org_id")
     private String orgId;

     /** 缴费机构名称*/
     @JsonProperty("org_nme")
     private String orgNme;

     /** 批次号*/
     @JsonProperty("bat_no")
     private String batNo;

     /** 客户编号*/
     @JsonProperty("cus_id")
     private String cusId;

     /** 客户姓名*/
     @JsonProperty("cus_nme")
     private String cusNme;

     /** 客户手机号*/
     @JsonProperty("cus_tel_no")
     private String cusTelNo;

     /** 二级入账户*/
     @JsonProperty("sub_ac")
     private String subAc;

     /** 缴款人名称*/
     @JsonProperty("pay_usr_nme")
     private String payUsrNme;

     /** 付款账号*/
     @JsonProperty("pay_ac")
     private String payAc;

     /** 交易金额*/
     @JsonProperty("txn_amt")
     private String txnAmt;

     /** 交易时间*/
     @JsonProperty("txn_tme")
     private String txnTme;

     /** 交易状态*/
     @JsonProperty("txn_sts")
     private String txnSts;

     /** 业务数据*/
     @JsonProperty("detail")
     private String detail;

     /** 维护人编号*/
     @JsonProperty("opr_cus_id")
     private String oprCusId;

     /** 业务传票摘要区*/
     @JsonProperty("vch_bus_rmk")
     private String vchBusRmk;

     /** 维护人姓名*/
     @JsonProperty("opr_cus_nme")
     private String oprCusNme;

     /** 转账收款识别码*/
     @JsonProperty("id_id_no")
     private String idIdNo;

     /** 备用字段*/
     @JsonProperty("rsv_fld")
     private String rsvFld;

	public String getSqn() {
		return sqn;
	}

	public void setSqn(String sqn) {
		this.sqn = sqn;
	}
	public String getOdeNo() {
		return odeNo;
	}

	public void setOdeNo(String odeNo) {
		this.odeNo = odeNo;
	}
	public String getPayChl() {
		return payChl;
	}

	public void setPayChl(String payChl) {
		this.payChl = payChl;
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
	public String getBatNo() {
		return batNo;
	}

	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}
	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getCusNme() {
		return cusNme;
	}

	public void setCusNme(String cusNme) {
		this.cusNme = cusNme;
	}
	public String getCusTelNo() {
		return cusTelNo;
	}

	public void setCusTelNo(String cusTelNo) {
		this.cusTelNo = cusTelNo;
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
	public String getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getTxnTme() {
		return txnTme;
	}

	public void setTxnTme(String txnTme) {
		this.txnTme = txnTme;
	}
	public String getTxnSts() {
		return txnSts;
	}

	public void setTxnSts(String txnSts) {
		this.txnSts = txnSts;
	}
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getOprCusId() {
		return oprCusId;
	}

	public void setOprCusId(String oprCusId) {
		this.oprCusId = oprCusId;
	}
	public String getVchBusRmk() {
		return vchBusRmk;
	}

	public void setVchBusRmk(String vchBusRmk) {
		this.vchBusRmk = vchBusRmk;
	}
	public String getOprCusNme() {
		return oprCusNme;
	}

	public void setOprCusNme(String oprCusNme) {
		this.oprCusNme = oprCusNme;
	}
	public String getIdIdNo() {
		return idIdNo;
	}

	public void setIdIdNo(String idIdNo) {
		this.idIdNo = idIdNo;
	}
	public String getRsvFld() {
		return rsvFld;
	}

	public void setRsvFld(String rsvFld) {
		this.rsvFld = rsvFld;
	}
}     /** 总记录数*/
     @JsonProperty("pageableresponse_totalelements")
     private String pageableresponseTotalelements;

     /** 总页数*/
     @JsonProperty("pageableresponse_totalpages")
     private String pageableresponseTotalpages;

	public List<OrdPayJnl> getOrdPayJnlList() {
		return ordPayJnlList;
	}

	public void setOrdPayJnlList(List<OrdPayJnl> ordPayJnlList) {
		this.ordPayJnlList = ordPayJnlList;
	}
	public String getPageableresponseTotalelements() {
		return pageableresponseTotalelements;
	}

	public void setPageableresponseTotalelements(String pageableresponseTotalelements) {
		this.pageableresponseTotalelements = pageableresponseTotalelements;
	}
	public String getPageableresponseTotalpages() {
		return pageableresponseTotalpages;
	}

	public void setPageableresponseTotalpages(String pageableresponseTotalpages) {
		this.pageableresponseTotalpages = pageableresponseTotalpages;
	}
}