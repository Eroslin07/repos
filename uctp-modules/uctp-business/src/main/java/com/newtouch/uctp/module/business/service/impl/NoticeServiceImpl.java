package com.newtouch.uctp.module.business.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import com.newtouch.uctp.module.business.util.MsgSendUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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


    public String saveTaskNotice1(String type, String contentType, String reason, BpmFormResVO bpmFormMainVO) {
        if (bpmFormMainVO!=null) {
            Map<String, String> map = new HashMap<>();
            map.put("type", type);
            map.put("contentType", contentType);
            map.put("reason", reason);
            NoticeInfoDO infoDO = new NoticeInfoDO();
            JSONObject jsonObject = bpmFormMainVO.getFormDataJson();
            map.put("vehicleReceiptAmount", jsonObject.getString("vehicleReceiptAmount"));
            map.put("sellAmount", jsonObject.getString("sellAmount"));
            map.put("contractId", jsonObject.getString("contractId"));
            infoDO.setId(UUID.randomUUID().toString());
            //根据不同的内容类型选择不同的内容模版
            Map<String, String> contentMap = MsgContentUtil.getContent(map);
            infoDO.setTitle(contentMap.get("title"));
            infoDO.setContent(contentMap.get("content"));
            //取上下文租户号
            Long tenantId = TenantContextHolder.getTenantId();
            infoDO.setTenantId(tenantId);
            infoDO.setPhone(String.valueOf(jsonObject.get("phone")));
            infoDO.setBusinessId(String.valueOf(bpmFormMainVO.getMerchantId()));
            infoDO.setStatus("0");
            if (type.equals("0")) {
                //收车公允值通过需要添加跳转路径
                if (contentType.equals("12")) {
                    infoDO.setUrl("/subPages/home/bycar/agreement");
                    map.put("buyType", "1");
                } else if (contentType.equals("22")) {
                    //卖车公允值通过需要添加跳转路径
                    infoDO.setUrl("/subPages/home/sellingCar/agreement");
                    map.put("buyType", "12");
                }
            }
            //默认状态为推送成功
            infoDO.setPushStatus("0");
            infoDO.setType(type);
            if (type.equals("1")) {

                map.put("businessId", String.valueOf(bpmFormMainVO.getMerchantId()));
                map.put("phone", String.valueOf(jsonObject.get("phone")));
                //收车公允审批不通过
                if (contentType.equals("21")) {
                    map.put("type", "0");
                    map.put("contentType", "11");
                    map.put("url", "/subPages/home/bycar/index");
                    map.put("buyType", "1");
                    //公允审批不通过的跳转路径
                    //添加站内消息
                    saveNotice(map);
                } else if (contentType.equals("31")) {
                    map.put("type", "0");
                    map.put("contentType", "21");
                    map.put("buyType", "2");
                    //卖车公允审批不通过的跳转路径
                    map.put("url", "/subPages/home/sellingCar/carInfo");
                    //添加站内消息
                    saveNotice(map);
                }
            }
            if (type.equals("1")) {
                Map<String, String> message = MsgSendUtil.sendMessage(map);
                if (message.get("flags").equals("FALSE")) {
                    infoDO.setErrorMsg(message.get("msg"));
                    infoDO.setErrorNum(message.get("errorNum"));
                    infoDO.setPushStatus("1");
                }
            }

            String result = "写入数据失败";
            int insert = noticeMapper.insert(infoDO);
            if (insert > 0) {
                result = "写入数据成功";
            }
            return result;
        }
        return "流程VO为null，请检查参数";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveTaskNotice(String type, String contentType, String reason, BpmFormResVO bpmFormMainVO) {
        //保存消息时根据类型处理为对应map
        Map<String ,String> map =getContentMaps(type,contentType,reason,bpmFormMainVO);
        if (map.get("bpm").equals("0")) {
            map.put("type", type);
            map.put("contentType", contentType);
            map.put("reason", reason);
            NoticeInfoDO infoDO = new NoticeInfoDO();
            infoDO.setId(UUID.randomUUID().toString());
            //根据不同的内容类型选择不同的内容模版
            Map<String, String> contentMap = MsgContentUtil.getContent(map);
            infoDO.setTitle(contentMap.get("title"));
            infoDO.setContent(contentMap.get("content"));
            map.put("content",contentMap.get("content"));
            //取上下文租户号
            Long tenantId = TenantContextHolder.getTenantId();
            infoDO.setTenantId(tenantId);
            infoDO.setPhone(map.get("phone"));
            infoDO.setBusinessId(String.valueOf(bpmFormMainVO.getMerchantId()));
            infoDO.setStatus("0");
            if (type.equals("0") && map.get("url") != null) {
                infoDO.setUrl(map.get("url"));
            }
            infoDO.setPushStatus("0");
            //短信发送
            if (type.equals("1")) {
                Map<String, String> message = MsgSendUtil.sendMessage(map);
                if (message.get("flags").equals("FALSE")) {
                    infoDO.setErrorMsg(message.get("msg"));
                    infoDO.setErrorNum(message.get("errorNum"));
                    infoDO.setPushStatus("1");
                }
            }

            infoDO.setType(type);
            String result = "写入数据失败";
            int insert = noticeMapper.insert(infoDO);
            if (insert > 0) {
                result = "写入数据成功";
            }
            return result;
        }
        return "bpm实体为空，请检查参数";
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
        //默认状态为推送成功
        infoDO.setPushStatus("0");
        infoDO.setType(map.get("type"));
        if (map.get("type").equals("1")){
            Map<String, String> message = MsgSendUtil.sendMessage(map);
            if (message.get("flags").equals("FALSE")){
                infoDO.setErrorMsg(message.get("msg"));
                infoDO.setErrorNum(message.get("errorNum"));
                infoDO.setPushStatus("1");
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
    public int getUnreadNoticeCount(String businessID) {
        return noticeMapper.getUnreadNoticeCount(businessID);
    }

    /**
     * 保存消息时根据类型处理为对应map
     * 目前就公允价值判断以及注册判断
     * */
    private Map<String,String> getContentMaps(String type, String contentType, String reason, BpmFormResVO bpmFormMainVO){
        Map<String, String> map = new HashMap<>();
        if (bpmFormMainVO!=null) {
            map.put("bpm","0");
            JSONObject jsonObject = bpmFormMainVO.getFormDataJson();
            map.put("type", type);
            map.put("contentType", contentType);
            map.put("reason", reason);
            //公允审批通过判断 type=0，contentType=12 收车通过； type=0，contentType=22 卖车通过；
            if (type.equals("0")) {
                CarInfoDetailsDO carInfoDetails = JSON.toJavaObject((JSON) jsonObject.get("carInfoDetails"), CarInfoDetailsDO.class);
                CarInfoDO carInfo = JSON.toJavaObject((JSON) jsonObject.get("carInfo"), CarInfoDO.class);

                //CarInfoDO carInfo = (CarInfoDO) jsonObject.get("carInfo");

                //收车公允值通过需要添加跳转路径
                if (contentType.equals("12")) {
                    if (carInfo.getVehicleReceiptAmount() != null) {
                        map.put("vehicleReceiptAmount", String.valueOf(carInfo.getVehicleReceiptAmount()));
                    }
                    map.put("buyType", "1");//收车
                    map.put("phone", carInfoDetails.getSellerTel());
                    map.put("url", "/subPages/home/bycar/agreement?type='1'&carId=" + carInfoDetails.getCarId());
                } else if (contentType.equals("22")) {
                    //卖车公允值通过需要添加跳转路径
                    if (carInfo.getSellAmount() != null) {
                        map.put("sellAmount", String.valueOf(carInfo.getSellAmount()));
                    }
                    map.put("buyType", "2");//卖车
                    map.put("phone", carInfoDetails.getBuyerTel());
                    map.put("url", "/subPages/home/sellingCar/agreement?type='1'&carId=" + carInfoDetails.getCarId());
                }

            } else if (type.equals("1")) {
                //CarInfoDetailsDO carInfoDetails = JSON.toJavaObject((JSON) jsonObject.get("carInfoDetails"), CarInfoDetailsDO.class);
                CarInfoDetailsDO carInfoDetails= JSON.parseObject(JSON.toJSONString(jsonObject.get("carInfoDetails")), new TypeReference<CarInfoDetailsDO>() {});
                //CarInfoDetailsDO carInfoDetails = (CarInfoDetailsDO) jsonObject.get("carInfoDetails");
               // CarInfoDO carInfo = JSON.toJavaObject((JSON) jsonObject.get("carInfo"), CarInfoDO.class);
                CarInfoDO carInfo = JSON.parseObject(JSON.toJSONString(jsonObject.get("carInfo")), new TypeReference<CarInfoDO>() {});
                //map.put("businessId",String.valueOf(bpmFormMainVO.getMerchantId()));
                map.put("phone", jsonObject.getString("phone"));
                //收车公允审批不通过
                if (contentType.equals("21")) {
                    map.put("type", "0");
                    map.put("contentType", "11");
                    map.put("buyType", "1");
                    map.put("phone", carInfoDetails.getSellerTel());
                    if (carInfo.getVehicleReceiptAmount() != null) {
                        map.put("vehicleReceiptAmount", String.valueOf(carInfo.getVehicleReceiptAmount()));
                    }
                    //公允审批不通过的跳转路径
                    map.put("url", "/subPages/home/bycar/index?type='1'&carId=" + carInfoDetails.getCarId());
                    //添加站内消息
                    saveNotice(map);
                } else if (contentType.equals("31")) {
                    map.put("type", "0");
                    map.put("contentType", "21");
                    map.put("buyType", "2");
                    if (carInfo.getSellAmount() != null) {
                        map.put("sellAmount", String.valueOf(carInfo.getSellAmount()));
                    }
                    map.put("phone", carInfoDetails.getBuyerTel());
                    //卖车公允审批不通过的跳转路径
                    map.put("url", "/subPages/home/sellingCar/carInfo?type='1'&carId=" + carInfoDetails.getCarId());
                    //添加站内消息
                    saveNotice(map);
                } else if (contentType.equals("12")) {
                    //注册成功
                    map.put("phone", jsonObject.getString("phone"));
                } else if (contentType.equals("11")) {
                    //注册失败
                    map.put("phone", jsonObject.getString("phone"));
                }
            }
        }else {
            map.put("bpm","1");
        }
        return map;
    }


}
