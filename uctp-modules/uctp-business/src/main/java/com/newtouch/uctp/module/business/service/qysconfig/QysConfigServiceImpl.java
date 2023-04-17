package com.newtouch.uctp.module.business.service.qysconfig;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.convert.qysconfig.QysConfigConvert;
import com.newtouch.uctp.module.business.dal.dataobject.qysconfig.QysConfigDO;
import com.newtouch.uctp.module.business.dal.mysql.qysconfig.QysConfigMapper;
import com.qiyuesuo.sdk.v2.utils.CryptUtils;
import com.qiyuesuo.sdk.v2.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.QYS_CONFIG_NOT_EXISTS;
import static com.newtouch.uctp.module.business.enums.QysConstants.SECRET;

/**
 * 契约锁 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class QysConfigServiceImpl implements QysConfigService {

    @Resource
    private QysConfigMapper qysConfigMapper;

    @Override
    public Long createQysConfig(QysConfigCreateReqVO createReqVO) {
        // 插入
        QysConfigDO qysConfig = QysConfigConvert.INSTANCE.convert(createReqVO);
        qysConfigMapper.insert(qysConfig);
        // 返回
        return qysConfig.getId();
    }

    @Override
    public void updateQysConfig(QysConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateQysConfigExists(updateReqVO.getId());
        // 更新
        QysConfigDO updateObj = QysConfigConvert.INSTANCE.convert(updateReqVO);
        qysConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteQysConfig(Long id) {
        // 校验存在
        validateQysConfigExists(id);
        // 删除
        qysConfigMapper.deleteById(id);
    }

    private void validateQysConfigExists(Long id) {
        if (qysConfigMapper.selectById(id) == null) {
            throw exception(QYS_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public QysConfigDO getQysConfig(Long id) {
        return qysConfigMapper.selectById(id);
    }

    @Override
    public List<QysConfigDO> getQysConfigList(Collection<Long> ids) {
        return qysConfigMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<QysConfigDO> getQysConfigPage(QysConfigPageReqVO pageReqVO) {
        return qysConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public String certification(String signature, String timestamp, String content) throws Exception {
        log.info("电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        JSONObject jsonObject = this.decryptMessage(content);
        //TODO 处理业务
        return "success";
    }

    @Override
    public String status(String signature, String timestamp, String content) throws Exception {
        log.info("电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        JSONObject jsonObject = this.decryptMessage(content);
        //TODO 处理业务
        return "success";
    }

    @Override
    public String verification(String signature, String timestamp, String content) throws Exception {
        log.info("电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        JSONObject jsonObject = this.decryptMessage(content);
        //TODO 处理业务
        return "success";
    }

    @Override
    public String login(String signature, String timestamp, String content) throws Exception {
        log.info("电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        JSONObject jsonObject = this.decryptMessage(content);
        //TODO 处理业务
        return "success";
    }

    private Boolean verificationSignature(String signature, String timestamp){
        String md5 = MD5.toMD5(timestamp + SECRET);
        if (!StrUtil.equals(signature,md5)) {
            log.error("验证签名不一致，md5【{}】,signature【{}】",md5,signature);
            System.out.println("签名不一致");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private JSONObject decryptMessage(String content) throws Exception {
        String contentJson = CryptUtils.aesDerypt(content, SECRET);
        JSONObject json = JSONObject.parseObject(contentJson);
        log.info("解密消息，json【{}】",json);
        return json;
//        Map<String, Object> map = JSONUtils.toMap(contentJson);
    }


}
