package com.newtouch.uctp.module.business.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.dal.mysql.ContractMapper;
import com.newtouch.uctp.module.business.enums.QysContractStatus;
import com.newtouch.uctp.module.business.service.ContractService;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.qiyuesuo.sdk.v2.bean.Contract;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;

/**
 * 车辆合同 Service 实现类
 *
 * @author lc
 */
@Service
@Validated
public class ContractServiceImpl implements ContractService {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private QiyuesuoClientFactory qiyuesuoClientFactory;
    @Resource
    private FileApi fileApi;
    @Resource
    @Lazy
    private QysConfigService qysConfigService;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public List<AppContractarVO> getContractInfo(String carID) {
        List<AppContractarVO> contractInfo = contractMapper.getContractInfo(carID);

        List<AppContractarVO> contractInfo1 =new ArrayList<>();
        for (AppContractarVO appContractarVO : contractInfo) {
            contractInfo1.add(setContractUrl(appContractarVO));
        }
        return contractInfo1;
    }


    @Override
    public String updateContractStatas(CarDCVo carDCVo) {
        String result="更新失败";
        int i=contractMapper.updateContractStatas(carDCVo);
        if (i>0)
            result="更新失败";
        return result;
    }

    @Override
    public List<CarDCVo> getContractIds(String contractID) {
        return contractMapper.getContractIds(contractID);
    }

    @Override
    public List<ContractDO> getContractListByCarId(Long id) {
        return contractMapper.selectByCarID(id);
    }

    @Override
    public ContractDO getById(Long id) {
        return contractMapper.selectById(id);
    }

    @Transactional
    @Override
    public void contractInvalid(Long id,String reason) {
        ContractDO contractDO = contractMapper.selectById(id);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        //修改契约锁合同状态已作废
        QysConfigDO qysConfigDO = qysConfigService.getByDeptId(contractDO.getBusinessId());
        if (ObjectUtil.isNull(qysConfigDO) || StrUtil.isBlank(qysConfigDO.getAccessKey())) {
            throw exception(QYS_CONFIG_AUTH_ERROR);
        }
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        Contract contract = client.defaultContractDetail(contractDO.getContractId()).getCheckedData();
        if (StrUtil.equals(contract.getStatus(), QysContractStatus.DRAFT.value())) {
            client.defaultContractInvalid(contractDO.getContractId());
        }
        if (StrUtil.equals(contract.getStatus(), QysContractStatus.SIGNING.value())) {
            client.defaultContractInvalid(contractDO.getContractId(),reason);
        }
        if (StrUtil.equals(contract.getStatus(), QysContractStatus.COMPLETE.value())) {
            client.defaultContractInvalid(contractDO.getContractId(),null,reason);
        }
        //修改合同状态为已作废
        contractDO.setStatus(2);
        contractMapper.updateById(contractDO);
    }

    @Override
    public ContractDO getCollectDraft(Long carId, Integer contractType, Long tenantId) {
        return contractMapper.findCollectDraft(carId, contractType, tenantId);
    }

    @Override
    public void update(ContractDO contractDO) {
        if (ObjectUtil.isNotNull(contractDO)) {
            contractMapper.updateById(contractDO);
        }
    }

    @Override
    public ContractDO getByContractId(Long contractId) {
        return contractMapper.selectOne("CONTRACT_ID",contractId);
    }


    /**
     * 将合同的url放到实体中
     */
    public AppContractarVO setContractUrl(AppContractarVO appContractarVO){

        CommonResult<List<FileRespDTO>> listContractar =null;

        List<Long> contractList=new ArrayList<>();
        List<CarDCVo> contractIds= getContractIds(appContractarVO.getContractID()) ;//一条合同数据的ids;正常情况一个合同只会有一个pdf文件
        for (CarDCVo contractId : contractIds) {
            contractList.add(contractId.getLongId());
        }
        listContractar= fileApi.fileList(contractList);
        if(listContractar.getData()!=null) {
            for (FileRespDTO datum : listContractar.getData()) {

                appContractarVO.setUrl(datum.getUrl());
            }
        }
        return appContractarVO;
    }

    private String GenerateCode(Integer type){
        String busiTypeCode = "";
        switch (type){
//            1收车委托合同   2收车合同  3卖车委托合同  4卖车合同
            case 1:
                busiTypeCode = "BCV";
                break;
            case 2:
                busiTypeCode = "BC";
                break;
            case 3:
                busiTypeCode = "CSV";
                break;
            case 4:
                busiTypeCode = "CS";
                break;
            default:
                throw exception(CONTRACT_TYPE_UNKNOWN);
        }
        String serialNoPrefix = busiTypeCode.concat(DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now()));
        Long index = this.redisTemplate.opsForValue().increment(serialNoPrefix, 1L);
        return serialNoPrefix.concat(String.format("%04d", index.intValue()));
    }

}
