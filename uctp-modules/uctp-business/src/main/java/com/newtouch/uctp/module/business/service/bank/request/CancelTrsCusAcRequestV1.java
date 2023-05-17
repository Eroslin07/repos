package com.newtouch.uctp.module.business.service.bank.request;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.newtouch.uctp.module.business.service.bank.response.CancelTrsCusAcResponseV1;


public class CancelTrsCusAcRequestV1 extends AbstractBocomRequest<CancelTrsCusAcResponseV1> {

  @Override
  public Class<CancelTrsCusAcResponseV1> getResponseClass() {
    return CancelTrsCusAcResponseV1.class;
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
    return CancelTrsCusAcRequestV1Biz.class;
  }



  public static class CancelTrsCusAcRequestV1Biz implements BizContent {

     /** 产品类型*/
     @JsonProperty("prd_typ")
     private String prdTyp;

     /** 机构编号*/
     @JsonProperty("org_id")
     private String orgId;

     /** 第三方编号*/
     @JsonProperty("thd_id")
     private String thdId;

     /** 转账收款识别码*/
     @JsonProperty("id_id_no")
     private String idIdNo;

     /** 收款识别码名称*/
     @JsonProperty("id_no_nme")
     private String idNoNme;

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
	public String getThdId() {
		return thdId;
	}

	public void setThdId(String thdId) {
		this.thdId = thdId;
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
}
}