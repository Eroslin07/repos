package com.newtouch.uctp.module.business.service.qys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.convert.qys.QysConfigConvert;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysCallbackDO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.dal.mysql.dept.DeptMapper;
import com.newtouch.uctp.module.business.dal.mysql.qys.QysCallbackMapper;
import com.newtouch.uctp.module.business.dal.mysql.qys.QysConfigMapper;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.business.util.Byte2StrUtil;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.utils.CryptUtils;
import com.qiyuesuo.sdk.v2.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_NOT_EXISTS;
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
    @Resource
    private QiyuesuoClientFactory qiyuesuoClientFactory;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private QysCallbackMapper qysCallbackMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @PostConstruct
    @Override
    public void initLocalCache() {
        // 第一步：查询数据
        List<QysConfigDO> configDOS = qysConfigMapper.selectList();
        log.info("[initLocalCache][缓存契约锁client，数量为:{}]", configDOS.size());

        // 第二步：构建缓存：创建或更新短信 Client
//        List<QiyuesuoChannelProperties> propertiesList = QysConfigConvert.INSTANCE.convert01(configDOS);
//        propertiesList.forEach(properties -> qiyuesuoClientFactory.createOrUpdateQiyuesuoClient(properties));
        this.createOrUpdateQiyuesuoClient(configDOS);
    }

    private void createOrUpdateQiyuesuoClient(List<QysConfigDO> configDOS){
        List<QiyuesuoChannelProperties> propertiesList = QysConfigConvert.INSTANCE.convert01(configDOS);
        propertiesList.forEach(properties -> qiyuesuoClientFactory.createOrUpdateQiyuesuoClient(properties));
    }

    @Override
    public Long createQysConfig(QysConfigCreateReqVO createReqVO) {
        // 插入
        QysConfigDO qysConfig = QysConfigConvert.INSTANCE.convert(createReqVO);
        qysConfigMapper.insert(qysConfig);
        //刷新client
        this.createOrUpdateQiyuesuoClient(Arrays.asList(qysConfig));
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
        //刷新client
        this.createOrUpdateQiyuesuoClient(Arrays.asList(updateObj));
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
        log.info("[certification]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        JSONObject jsonObject = JSON.parseObject(json);
        String companyName = jsonObject.getString("companyName");
        String status = jsonObject.getString("status");
        QysCallbackDO qysCallbackDO = new QysCallbackDO();
        qysCallbackDO.setContent(json);
        //目前根据saas文档来的
        qysCallbackDO.setType(1);
        //查询企业,后面确认下工商注册号是不是我们的一样
        List<DeptDO> deptDOS = deptMapper.selectByName(companyName);
        DeptDO deptDO = null;
        if (CollUtil.isNotEmpty(deptDOS)) {
            log.warn("[certification]根据返回的公司名称未查询到数据,companyName:{}",companyName);
            deptDO = deptDOS.get(0);
            if (ObjectUtil.isNotNull(deptDO)) {
                qysCallbackDO.setMainId(deptDO.getId());
                deptDO.setAuth(Integer.valueOf(status));
            }
        }
        qysCallbackMapper.insert(qysCallbackDO);
        if (ObjectUtil.isNotNull(deptDO)) {
            deptMapper.updateById(deptDO);
        }
        return "success";
    }

    @Override
    public String status(String signature, String timestamp, String content) throws Exception {
        log.info("[status]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        //TODO 处理业务
        return "success";
    }

    @Override
    public String verification(String ticket) throws Exception {
        log.info("[verification]电子签CAS校验：ticket【{}】",ticket);
        //拿到 aes 密钥
//        redisTemplate.opsForValue().set("ticket:"+ticket,"密钥");
        String aesKeyStr = redisTemplate.opsForValue().get("ticket:"+ticket);
        byte[] aesKey = Byte2StrUtil.toByteArray(aesKeyStr);
        AES aes = SecureUtil.aes(aesKey);
        //解密 获取到userId
        String userId = aes.decryptStr(ticket, CharsetUtil.CHARSET_UTF_8);
        //查询到用户数据并返回

        return "success";
    }

    @Override
    public String login(String signature, String timestamp, String content) throws Exception {
        log.info("[login]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        //TODO 处理业务
        return "success";
    }

    @Override
    public void test() {
        QiyuesuoClient qiyuesuoClient = qiyuesuoClientFactory.getQiyuesuoClient(1648231591874347009L);
        QiyuesuoSaasClient qiyuesuoSaasClient = qiyuesuoClientFactory.getQiyuesuoSaasClient(1648231591874347009L);
        qiyuesuoClient.defaultSend(null);
        qiyuesuoSaasClient.saasCompanyAuthPageUrl(null);
    }

    @Override
    public void send(Long carId) {
        CarInfoDO carInfo = carInfoService.getCarInfo(carId);
        if (ObjectUtil.isNull(carInfo)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", loginUser.getDeptId());
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        QiyuesuoCommonResult<Contract> result = client.defaultSend(null);

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

    private String decryptMessage(String content) throws Exception {
        return CryptUtils.aesDerypt(content, SECRET);
    }


    public static void main(String[] args) {

        /*try {
            String md5 = MD5.toMD5("1681907332818" + SECRET);
            if (StrUtil.equals("03fb7492aa65106be0b6ab14a22cc781",md5)) {
                System.out.println("666");
            }else {
                System.out.println("666");
            }
            //解密消息
            String content = "dn9AJvP1tgxz+hrH7KLE5E1xQowx3dRnZn/vVOyFpG7qUZ7bUieKk1404aFK5NTR5o4SX44AMc2TiCZTOxTmDEjbAwjC7UEMMJjSxlKuCsSubptFjiCI0CrPeSjO66zNzIdsQMJCrkJ+IjjTFh9+91tH0miMVPrDj3tuv/MRtxBaO4ENI5HPCDn7cJnlM+dVO4kCcvogcDquj1aTy/B3aGHgygpkbKOMJa7D40kw7m+9XNixvwot3afXisOU1ta2woWO7XYqhGBNZ9RZ39WDr7wKAbkhvK7uk+B2elPHz78=";
            String s = CryptUtils.aesDerypt(content, SECRET);
//            {"companyId":"3086283524031066663","requestId":"09dfb74d-63f1-4f9d-957b-c2840ba6c806","companyName":"西瓜科技公司","legalPerson":"罗风","actionEvent":7,"status":1,"registerNo":"916100007869672125638"}
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        String content = "test中文";

// 随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
// 构建
        AES aes = SecureUtil.aes(key);



// 加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        System.out.println(encryptHex);
// 解密为字符串
//        Byte2StrUtil.toByteArray("");
        String s = Byte2StrUtil.toHexString(key);
        byte[] key1 = Byte2StrUtil.toByteArray(s);
        AES aes1 = SecureUtil.aes(key1);
        String decryptStr = aes1.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);
    }

}
