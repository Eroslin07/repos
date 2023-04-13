package com.newtouch.uctp.module.business.service.impl;


import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInvoiceVo;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.CostExample;
import com.newtouch.uctp.module.business.dal.mysql.CostMapper;
import com.newtouch.uctp.module.business.dal.mysql.InvoicesMapper;
import com.newtouch.uctp.module.business.service.CostService;
import com.newtouch.uctp.module.business.service.InvoicesService;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 车辆主表 Service 实现类
 *
 * @author lc
 */
@Service
@Validated
public class InvoicesServiceImpl implements InvoicesService {

    @Resource
    private InvoicesMapper invoicesMapper;

    @Resource
    private FileApi fileApi;


    @Override
    public List<AppCarInvoiceVo> getInvoicesInfo(String carID) {
        List<AppCarInvoiceVo> invoicesInfosList = invoicesMapper.getInvoicesInfo(carID);
        List<AppCarInvoiceVo> invoicesInfos= new ArrayList<>();
        for (AppCarInvoiceVo appCarInvoiceVo : invoicesInfosList) {
            invoicesInfos.add(setInvoiceUrl(appCarInvoiceVo));
        }
        return invoicesInfosList;
    }

    @Override
    public List<CarDCVo> getInvoiceIds(String id) {
        return invoicesMapper.getInvoiceIds(id);
    }


    /**
     * 将发票的url放到实体中
     */
    private AppCarInvoiceVo setInvoiceUrl(AppCarInvoiceVo  VO){

        CommonResult<List<FileRespDTO>> listInvoice =null;

        List<Long> invoiceList=new ArrayList<>();
        List<CarDCVo> invoiceIds= getInvoiceIds(VO.getInvoiceId()) ;//目前一个发票只有一个路径

        for (CarDCVo invoiceId : invoiceIds) {
            invoiceList.add(invoiceId.getLongId());
        }
        listInvoice= fileApi.fileList(invoiceList);
        if(listInvoice.getData()!=null) {
            for (FileRespDTO datum : listInvoice.getData()) {
                VO.setUrl(datum.getUrl());
            }
        }
        return VO;
    }
}
