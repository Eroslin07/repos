package com.newtouch.uctp.module.business.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import com.newtouch.uctp.module.business.dal.mysql.NoticeMapper;
import com.newtouch.uctp.module.business.service.NoticeService;
import com.newtouch.uctp.module.business.util.ListUtil;
import com.newtouch.uctp.module.business.util.MsgContentUtil;


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
    public List<NoticeInfoDO> getNotices(String businessID) {
        List<NoticeInfoDO> notices = noticeMapper.getNotices(businessID);
        int unreadCount = noticeMapper.getUnreadNoticeCount(businessID);
        return notices;
    }

    @Override
    public void updateNoticeStatus(NoticeVO noticeVO) {
        noticeMapper.updateNoticeStatus(noticeVO);
    }

    @Override
    public void updateAllNoticeStatus(String id) {
        noticeMapper.updateAllNoticeStatus(id);

    }

    @Override
    public void updateAllNoticeStatus(List<String> list) {

        if (list.size()>100) {
            Map<String, List<String>> map = ListUtil.groupList(list);

            for (int i = 0; i < map.size(); i++) {
                String ids = "";
                for (int a = 0; a < map.get("keyName" + i).size(); a++) {
                    ids += "'" + map.get("keyName" + i).get(a) + "',";
                }
                ids += "'#'";
                noticeMapper.updateAllNoticeStatus(ids);
            }
        }else {
            String ids = "";
            for (int i = 0; i < list.size(); i++) {

                    ids += "'" + list.get(i) + "',";
            }
            ids += "'#'";
            noticeMapper.updateAllNoticeStatus(ids);
        }
    }

    @Override
    public void deleteAllNoticeStatus(List<String> list) {
        String ids = "";
        for (int i = 0; i < list.size(); i++) {

            ids += "'" + list.get(i) + "',";
        }
        ids += "'#'";
        noticeMapper.deleteAllNoticeStatus(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveTaskNotice(String type, String contentType, String reason, BpmFormResVO bpmFormMainVO) {
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
                infoDO.setUrl("/subPages/home/bycar/agreement");
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
                map.put("url","/subPages/home/bycar/index");
                //公允审批不通过的跳转路径
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
        if (insert>0) {
            result = "写入数据成功";
        }
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
        if (map.get("url")!=null) {
            infoDO.setUrl(map.get("url"));
        }
        //默认状态为未推送
        infoDO.setPushStatus("0");
        infoDO.setType(map.get("type"));

        String result="写入数据失败";
        int insert = noticeMapper.insert(infoDO);
        if (insert>0) {
            result = "写入数据成功";
        }
        return result;
    }

    @Override
    public int getUnreadNoticeCount(String businessID) {
        return noticeMapper.getUnreadNoticeCount(businessID);
    }
}
