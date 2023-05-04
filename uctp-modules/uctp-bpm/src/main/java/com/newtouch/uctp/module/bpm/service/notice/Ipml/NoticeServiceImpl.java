package com.newtouch.uctp.module.bpm.service.notice.Ipml;


import com.newtouch.uctp.module.bpm.util.MsgSendUtil;
import io.seata.spring.annotation.GlobalTransactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDetailsDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.notice.NoticeInfoDO;
import com.newtouch.uctp.module.bpm.dal.mysql.notice.NoticeMapper;
import com.newtouch.uctp.module.bpm.service.notice.NoticeService;
import com.newtouch.uctp.module.bpm.util.MsgContentUtil;


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
   // @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String saveTaskNotice(String type, String contentType, String reason, BpmFormMainVO bpmFormMainVO) {
        //保存消息时根据类型处理为对应map
        Map<String ,String> map =getContentMaps(type,contentType,reason,bpmFormMainVO);
        map.put("type",type);
        map.put("contentType",contentType);
        map.put("reason",reason);
        NoticeInfoDO infoDO=new NoticeInfoDO();
        infoDO.setId(UUID.randomUUID().toString());
        //根据不同的内容类型选择不同的内容模版
        Map<String, String> contentMap = MsgContentUtil.getContent(map);
        infoDO.setTitle(contentMap.get("title"));
        infoDO.setContent(contentMap.get("content"));
        //取上下文租户号
        Long tenantId = TenantContextHolder.getTenantId();
        infoDO.setTenantId(tenantId);
        infoDO.setPhone(map.get("phone"));
        infoDO.setBusinessId(bpmFormMainVO.getMerchantId().toString());
        infoDO.setStatus("0");
        if (type.equals("0")&&map.get("url")!=null) {
            infoDO.setUrl(map.get("url"));
        }
        infoDO.setPushStatus("0");
        //短信发送
        if (type.equals("1")){
            Map<String, String> message = MsgSendUtil.sendMessage(map);
            if (message.get("flags").equals("FALSE")){
                infoDO.setErrorMsg(message.get("msg"));
                infoDO.setErrorNum(message.get("errorNum"));
                infoDO.setPushStatus("1");
            }
        }

        infoDO.setType(type);
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
        infoDO.setPhone(map.get("phone"));
        infoDO.setBusinessId(map.get("businessId"));
        infoDO.setStatus("0");
        if (map.get("type").equals("0")&&map.get("url")!=null) {
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


    /**
     * 保存消息时根据类型处理为对应map
     * 目前就公允价值判断以及注册判断
     * */
    private Map<String,String> getContentMaps(String type, String contentType, String reason, BpmFormMainVO bpmFormMainVO){
        JSONObject jsonObject = bpmFormMainVO.getFormDataJson();
        Map<String,String> map=new HashMap<>();
        map.put("type",type);
        map.put("contentType",contentType);
        map.put("reason",reason);
        //公允审批通过判断 type=0，contentType=12 收车通过； type=0，contentType=22 卖车通过；
        if(type.equals("0")){
                CarInfoDetailsDO carInfoDetails = JSON.toJavaObject((JSON)jsonObject.get("carInfoDetails"),CarInfoDetailsDO.class);
                CarInfoDO carInfo = JSON.toJavaObject((JSON)jsonObject.get("carInfo"),CarInfoDO.class);

            //CarInfoDO carInfo = (CarInfoDO) jsonObject.get("carInfo");

            //收车公允值通过需要添加跳转路径
            if (contentType.equals("12")){
                if (carInfo.getVehicleReceiptAmount()!=null) {
                    map.put("vehicleReceiptAmount", carInfo.getVehicleReceiptAmount().toString());
                }
                map.put("buyType","1");//收车
                map.put("phone",carInfoDetails.getSellerTel());
                map.put("url","/subPages/home/bycar/agreement?type='1'&carId="+carInfoDetails.getCarId());
            }else if(contentType.equals("22")){
                //卖车公允值通过需要添加跳转路径
                if (carInfo.getSellAmount()!=null) {
                    map.put("sellAmount", carInfo.getSellAmount().toString());
                }
                map.put("buyType","2");//卖车
                map.put("phone",carInfoDetails.getBuyerTel());
                map.put("url","/subPages/home/sellingCar/agreement?type='1'&carId="+carInfoDetails.getCarId());
            }

        }
        else if(type.equals("1")){
            CarInfoDetailsDO carInfoDetails = JSON.toJavaObject((JSON)jsonObject.get("carInfoDetails"),CarInfoDetailsDO.class);
            CarInfoDO carInfo = JSON.toJavaObject((JSON)jsonObject.get("carInfo"),CarInfoDO.class);

            //map.put("businessId",bpmFormMainVO.getMerchantId().toString());
            map.put("phone",jsonObject.getString("phone"));
            //收车公允审批不通过
            if (contentType.equals("21")){
                map.put("type","0");
                map.put("contentType","11");
                map.put("buyType","1");
                map.put("phone",carInfoDetails.getSellerTel());
                if (carInfo.getVehicleReceiptAmount()!=null) {
                    map.put("vehicleReceiptAmount", carInfo.getVehicleReceiptAmount().toString());
                }
                //公允审批不通过的跳转路径
                map.put("url","/subPages/home/bycar/index?type='1'&carId="+carInfoDetails.getCarId());
                //添加站内消息
                saveNotice(map);
            }else if (contentType.equals("31")){
                map.put("type","0");
                map.put("contentType","21");
                map.put("buyType","2");
                if (carInfo.getSellAmount()!=null) {
                    map.put("sellAmount", carInfo.getSellAmount().toString());
                }
                map.put("phone",carInfoDetails.getBuyerTel());
                //卖车公允审批不通过的跳转路径
                map.put("url","/subPages/home/sellingCar/carInfo?type='1'&carId="+carInfoDetails.getCarId());
                //添加站内消息
                saveNotice(map);
            } else if (contentType.equals("12")){
                //注册成功
                map.put("phone",jsonObject.getString("phone"));
            } else if (contentType.equals("11")){
                //注册失败
                map.put("phone",jsonObject.getString("phone"));
            }
        }

        return map;
    }


   /* public String saveTaskNotice(String type, String contentType, String reason, BpmFormMainVO bpmFormMainVO) {
        Map<String ,String> map =getContentMaps(type,contentType,reason,bpmFormMainVO);
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
        infoDO.setPhone(jsonObject.getString("phone"));
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
            map.put("phone",jsonObject.getString("phone"));
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
    }*/
}
