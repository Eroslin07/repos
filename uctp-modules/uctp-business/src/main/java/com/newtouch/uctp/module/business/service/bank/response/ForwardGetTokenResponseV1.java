package com.newtouch.uctp.module.business.service.bank.response;

   import com.fasterxml.jackson.annotation.JsonProperty;
   import com.bocom.api.BocomResponse;
   import java.util.List;
   public class ForwardGetTokenResponseV1 extends BocomResponse {
     /** token标识*/
     @JsonProperty("token_id")
     private String tokenId;

     /** 创建时间*/
     @JsonProperty("create_time")
     private String createTime;

     /** Token有效期*/
     @JsonProperty("valid_time")
     private String validTime;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
}