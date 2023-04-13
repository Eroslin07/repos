package com.newtouch.uctp.module.business.service.impl;


import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInvoiceVo;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.dal.mysql.ContractMapper;
import com.newtouch.uctp.module.business.dal.mysql.InvoicesMapper;
import com.newtouch.uctp.module.business.service.ContractService;
import com.newtouch.uctp.module.business.service.InvoicesService;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private FileApi fileApi;


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

}
