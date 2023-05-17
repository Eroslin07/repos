package com.newtouch.uctp.module.business.service.bank.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.bocom.api.utils.enums.SecurityLevel;
import com.newtouch.uctp.module.business.service.bank.response.ForwardGetTokenResponseV1;

import java.util.List;


public class ForwardGetTokenRequestV1 extends AbstractBocomRequest<ForwardGetTokenResponseV1> {

  @Override
  public Class<ForwardGetTokenResponseV1> getResponseClass() {
    return ForwardGetTokenResponseV1.class;
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
    return ForwardGetTokenRequestV1Biz.class;
  }



  public static class ForwardGetTokenRequestV1Biz implements BizContent {

     /** 机构编号*/
     @JsonProperty("org_id")
     private String orgId;

     /** 转账收款识别码*/
     @JsonProperty("id_id_no")
     private String idIdNo;

     /** 收款识别码名称*/
     @JsonProperty("id_no_nme")
     private String idNoNme;

     /** 收款金额*/
     @JsonProperty("amount")
     private String amount;

     /** 交易场景码*/
     @JsonProperty("ser_scene_code")
     private String serSceneCode;

     /** 交易场景中文名*/
     @JsonProperty("ser_scene_c_n_name")
     private String serSceneCNName;

     /** 第三方订单号*/
     @JsonProperty("thd_sqn")
     private String thdSqn;

     /** 商户转账摘要*/
     @JsonProperty("mcht_abstract")
     private String mchtAbstract;

     /** 备用字段*/
     @JsonProperty("rsv_fld")
     private String rsvFld;

     /** 交易渠道*/
     @JsonProperty("requesthead_chn")
     private String requestheadChn;

     /** 请求系统标志*/
     @JsonProperty("requesthead_reqsyscde")
     private String requestheadReqsyscde;

     /** 目标系统码*/
     @JsonProperty("requesthead_syscde")
     private String requestheadSyscde;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
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
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSerSceneCode() {
		return serSceneCode;
	}

	public void setSerSceneCode(String serSceneCode) {
		this.serSceneCode = serSceneCode;
	}
	public String getSerSceneCNName() {
		return serSceneCNName;
	}

	public void setSerSceneCNName(String serSceneCNName) {
		this.serSceneCNName = serSceneCNName;
	}
	public String getThdSqn() {
		return thdSqn;
	}

	public void setThdSqn(String thdSqn) {
		this.thdSqn = thdSqn;
	}
	public String getMchtAbstract() {
		return mchtAbstract;
	}

	public void setMchtAbstract(String mchtAbstract) {
		this.mchtAbstract = mchtAbstract;
	}
	public String getRsvFld() {
		return rsvFld;
	}

	public void setRsvFld(String rsvFld) {
		this.rsvFld = rsvFld;
	}
	public String getRequestheadChn() {
		return requestheadChn;
	}

	public void setRequestheadChn(String requestheadChn) {
		this.requestheadChn = requestheadChn;
	}
	public String getRequestheadReqsyscde() {
		return requestheadReqsyscde;
	}

	public void setRequestheadReqsyscde(String requestheadReqsyscde) {
		this.requestheadReqsyscde = requestheadReqsyscde;
	}
	public String getRequestheadSyscde() {
		return requestheadSyscde;
	}

	public void setRequestheadSyscde(String requestheadSyscde) {
		this.requestheadSyscde = requestheadSyscde;
	}
}
}