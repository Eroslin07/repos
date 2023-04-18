package com.newtouch.uctp.module.bpm.service.notice.Ipml;


import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.bpm.dal.dataobject.notice.NoticeInfoDO;
import com.newtouch.uctp.module.bpm.dal.mysql.notice.NoticeMapper;
import com.newtouch.uctp.module.bpm.service.notice.NoticeService;
import com.newtouch.uctp.module.bpm.util.MsgContentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 车辆主表 Service 实现类
 *
 * @author lc
 */
@Service
@Validated
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveTaskNotice(String type, String contentType, String reason, BpmFormMainVO bpmFormMainVO) {
        Map<String ,String> map =new HashMap<>();
        map.put("type",type);
        map.put("contentType",contentType);
        map.put("reason",reason);
        NoticeInfoDO infoDO=new NoticeInfoDO();
        JSONObject jsonObject = bpmFormMainVO.getFormDataJson();
        map.put("vehicleReceiptAmount",jsonObject.getString("vehicleReceiptAmount"));
        map.put("sellAmount",jsonObject.getString("sellAmount"));
        map.put("contractId",jsonObject.getString("contractId"));
        infoDO.setId(UUID.randomUUID().toString());
        //根据不同的内容类型选择不同的内容模版
        Map<String, String> contentMap = MsgContentUtil.getContent(map);
        infoDO.setTitle(contentMap.get("title"));
        infoDO.setContent(contentMap.get("content"));
        //取上下文租户号
        Long tenantId = TenantContextHolder.getTenantId();
        infoDO.setTenantId(tenantId);
        infoDO.setPhone(jsonObject.get("phone").toString());
        infoDO.setBusinessId(bpmFormMainVO.getMerchantId().toString());
        infoDO.setStatus("0");
        if (type.equals("0")) {
            //收车公允值通过需要添加跳转路径
            if (contentType.equals("12")){
                infoDO.setUrl("/subPages/home/bycar/agreement");//?type='1'&carId=
            }else if(contentType.equals("22")){
            //卖车公允值通过需要添加跳转路径
                infoDO.setUrl("/subPages/home/sellingCar/agreement");
            }
        }
        infoDO.setPushStatus("0");
        infoDO.setType(type);
        if (type.equals("1")) {

            map.put("businessId",bpmFormMainVO.getMerchantId().toString());
            map.put("phone",jsonObject.get("phone").toString());
            //收车公允审批不通过
            if (contentType.equals("21")){
                map.put("type","0");
                map.put("contentType","11");
                //公允审批不通过的跳转路径
                map.put("url","/subPages/home/bycar/index");
                //添加站内消息
                saveNotice(map);
            }else if (contentType.equals("31")){
                map.put("type","0");
                map.put("contentType","21");
                //卖车公允审批不通过的跳转路径
                map.put("url","/subPages/home/sellingCar/carInfo");
                //添加站内消息
                saveNotice(map);
            }
        }
        String result="写入数据失败";
        int insert = noticeMapper.insert(infoDO);
        if (insert>0)
            result="写入数据成功";
        return result;
    }




    @Override
    public String saveNotice(Map<String,String> map) {

        NoticeInfoDO infoDO=new NoticeInfoDO();

        infoDO.setId(UUID.randomUUID().toString());

        //根据不同的内容类型选择不同的内容模版
        Map<String, String> contentMap = MsgContentUtil.getContent(map);
        infoDO.setTitle(contentMap.get("title"));
        infoDO.setContent(contentMap.get("content"));
        //取上下文租户号
        Long tenantId = TenantContextHolder.getTenantId();
        infoDO.setTenantId(tenantId);
        infoDO.setPhone( map.get("phone"));
        infoDO.setBusinessId(map.get("businessId"));
        infoDO.setStatus("0");
        if (map.get("url")!=null)
            infoDO.setUrl(map.get("url"));
        //默认状态为未推送
        infoDO.setPushStatus("0");
        infoDO.setType(map.get("type"));

        String result="写入数据失败";
        int insert = noticeMapper.insert(infoDO);
        if (insert>0)
            result="写入数据成功";
        return result;
    }

}
