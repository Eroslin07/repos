package com.newtouch.uctp.module.business.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import com.newtouch.uctp.module.business.dal.mysql.NoticeMapper;
import com.newtouch.uctp.module.business.service.NoticeService;
import com.newtouch.uctp.module.business.util.ListUtil;
import com.newtouch.uctp.module.business.util.MsgContentUtil;
import liquibase.pro.packaged.S;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
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
        //infoDO.setTitle(noticeInfoDO.getTitle());
        //根据不同的内容类型选择不同的内容模版
        Map<String, String> contentMap = MsgContentUtil.getContent(map);
        infoDO.setTitle(contentMap.get("title"));
        infoDO.setContent(contentMap.get("content"));
        //取上下文租户号
        Long tenantId = TenantContextHolder.getTenantId();
        infoDO.setTenantId(tenantId);
        infoDO.setPhone(jsonObject.get("phone").toString());
        infoDO.setBusinessId(bpmFormMainVO.getMerchantId().toString());
       /* infoDO.setTenantId("1");
        infoDO.setCreator(map.getCreator());
        if (map.getUpdater()!=null) {
            infoDO.setUpdater(map.getUpdater());
        }else {
            infoDO.setUpdateTime(null);
        }*/

        infoDO.setStatus("0");
        /*if ((String) jsonMap.get("url")!=null)
            infoDO.setUrl(jsonMap.get("url"));*/
        //默认状态为未推送
        infoDO.setPushStatus("0");
        infoDO.setType(type);

        String result="写入数据失败";
        int insert = noticeMapper.insert(infoDO);
        if (insert>0)
            result="写入数据成功";
        return result;
    }


    public String saveTaskNotice1(String type, String contentType, String reason, BpmFormMainVO bpmFormMainVO) {

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
        //infoDO.setTitle(noticeInfoDO.getTitle());
        //根据不同的内容类型选择不同的内容模版
        Map<String, String> contentMap = MsgContentUtil.getContent(map);
        infoDO.setTitle(contentMap.get("title"));
        infoDO.setContent(contentMap.get("content"));
        //取上下文租户号
        Long tenantId = TenantContextHolder.getTenantId();
        infoDO.setTenantId(tenantId);
        infoDO.setPhone(jsonObject.get("phone").toString());
        infoDO.setBusinessId(bpmFormMainVO.getMerchantId().toString());
       /* infoDO.setTenantId("1");
        infoDO.setCreator(map.getCreator());
        if (map.getUpdater()!=null) {
            infoDO.setUpdater(map.getUpdater());
        }else {
            infoDO.setUpdateTime(null);
        }*/

        infoDO.setStatus("0");
        /*if ((String) jsonMap.get("url")!=null)
            infoDO.setUrl(jsonMap.get("url"));*/
        //默认状态为未推送
        infoDO.setPushStatus("0");
        infoDO.setType(type);

        String result="写入数据失败";
        int insert = noticeMapper.insert(infoDO);
        if (insert>0)
            result="写入数据成功";
        return result;
    }



    @Override
    public String saveNotice(Map<String,String> map) {
       /* Map<String,String> map=new HashMap<>();
        map.put("type",noticeInfoDO.getType());
        map.put("contentType",noticeInfoDO.getContentType());
        map.put("reason",noticeInfoDO.getReason());
        //map.put("price",noticeInfoDO.getPrice());
        //map.put("contractNo",noticeInfoDO.getContractNo());
       // map.put("contractName",noticeInfoDO.getContractName());
        NoticeInfoDO infoDO=new NoticeInfoDO();


        infoDO.setId(UUID.randomUUID().toString());
        infoDO.setTitle(noticeInfoDO.getTitle());
        //根据不同的内容类型选择不同的内容模版
        infoDO.setContent(MsgContentUtil.getContent(noticeInfoDO));
        //先写死
        infoDO.setTenantId("1");
        infoDO.setPhone(noticeInfoDO.getPhone());
        infoDO.setBusinessId(noticeInfoDO.getBusinessId());
        infoDO.setTenantId(noticeInfoDO.getTenantId());
        infoDO.setCreator(noticeInfoDO.getCreator());
        if (noticeInfoDO.getUpdater()!=null) {
            infoDO.setUpdater(noticeInfoDO.getUpdater());
        }else {
            infoDO.setUpdateTime(null);
        }

        infoDO.setStatus("0");
        if (noticeInfoDO.getUrl()!=null)
            infoDO.setUrl(noticeInfoDO.getUrl());
        //默认状态为未推送
        infoDO.setPushStatus("0");*/

        NoticeInfoDO infoDO=new NoticeInfoDO();
       // JSONObject jsonObject = JSON.parseObject(map.get("data"));

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
        /*if ((String) jsonMap.get("url")!=null)
            infoDO.setUrl(jsonMap.get("url"));*/
        //默认状态为未推送
        infoDO.setPushStatus("0");
        infoDO.setType(map.get("type"));

        String result="写入数据失败";
        int insert = noticeMapper.insert(infoDO);
        if (insert>0)
            result="写入数据成功";
        return result;
    }

    @Override
    public int getUnreadNoticeCount(String businessID) {
        return noticeMapper.getUnreadNoticeCount(businessID);
    }
}
