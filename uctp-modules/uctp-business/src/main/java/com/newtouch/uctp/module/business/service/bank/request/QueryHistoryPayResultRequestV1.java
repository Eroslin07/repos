package com.newtouch.uctp.module.business.service.bank.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.bocom.api.utils.enums.SecurityLevel;
import com.newtouch.uctp.module.business.service.bank.response.QueryHistoryPayResultResponseV1;

import java.util.List;


public class QueryHistoryPayResultRequestV1 extends AbstractBocomRequest<QueryHistoryPayResultResponseV1> {

  @Override
  public Class<QueryHistoryPayResultResponseV1> getResponseClass() {
    return QueryHistoryPayResultResponseV1.class;
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
    return QueryHistoryPayResultRequestV1Biz.class;
  }



  public static class QueryHistoryPayResultRequestV1Biz implements BizContent {

     /** 产品类型*/
     @JsonProperty("prd_typ")
     private String prdTyp;

     /** 机构编号*/
     @JsonProperty("org_id")
     private String orgId;

     /** 查询起始日期*/
     @JsonProperty("begin_date")
     private String beginDate;

     /** 查询截止日期*/
     @JsonProperty("end_date")
     private String endDate;

     /** 转账收款识别码*/
     @JsonProperty("id_id_no")
     private String idIdNo;

     /** 扩展域*/
     @JsonProperty("extfld")
     private String extfld;

     /** 第三方编号*/
     @JsonProperty("thd_id")
     private String thdId;

     /** 页数*/
     @JsonProperty("pageablerequest_pagenum")
     private String pageablerequestPagenum;

     /** 每页显示数据数*/
     @JsonProperty("pageablerequest_pagesize")
     private String pageablerequestPagesize;

	/** ""*/
	@JsonProperty("sorting_info")
	private List<SortingInfo> sortingInfo;

	public static class SortingInfo {
     /** 排序数据域*/
     @JsonProperty("property")
     private String property;

     /** 排序方式*/
     @JsonProperty("direction")
     private String direction;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}	public String getPrdTyp() {
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
	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIdIdNo() {
		return idIdNo;
	}

	public void setIdIdNo(String idIdNo) {
		this.idIdNo = idIdNo;
	}
	public String getExtfld() {
		return extfld;
	}

	public void setExtfld(String extfld) {
		this.extfld = extfld;
	}
	public String getThdId() {
		return thdId;
	}

	public void setThdId(String thdId) {
		this.thdId = thdId;
	}
	public String getPageablerequestPagenum() {
		return pageablerequestPagenum;
	}

	public void setPageablerequestPagenum(String pageablerequestPagenum) {
		this.pageablerequestPagenum = pageablerequestPagenum;
	}
	public String getPageablerequestPagesize() {
		return pageablerequestPagesize;
	}

	public void setPageablerequestPagesize(String pageablerequestPagesize) {
		this.pageablerequestPagesize = pageablerequestPagesize;
	}
	public List<SortingInfo> getSortingInfo() {
		return sortingInfo;
	}

	public void setSortingInfo(List<SortingInfo> sortingInfo) {
		this.sortingInfo = sortingInfo;
	}
}
}