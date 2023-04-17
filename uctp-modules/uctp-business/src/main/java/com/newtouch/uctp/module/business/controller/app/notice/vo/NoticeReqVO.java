package com.newtouch.uctp.module.business.controller.app.notice.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class NoticeReqVO extends BaseDO {

   /* private String id;

    private String title;

    private String content;



    private String phone;

    private String businessId;

    private String tenantId;

    private String status;

    private String url;

    private String pushStatus;*/
//-----------------------

    private String type;

    private String  contentType;


    private String  reason;
//----
   /* private String  contractNo;

    private String  contractName;

    private String price;*/
//-----
    private String data;
}
