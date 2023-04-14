package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInvoiceVo;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * 车辆发票 Service 接口
 *
 * @author hr
 */

public interface InvoicesService {

    /**
     * 获得车辆发票信息
     *
     * @param carID
     * @return 车辆发票信息
     */
    List<AppCarInvoiceVo> getInvoicesInfo(String carID);


    /**
     * 获得发票在业务上传表中的id集合（查询url使用）
     *
     * @param id
     * @return 发票在业务上传表中的id集合
     */
    List<CarDCVo> getInvoiceIds(String id);

}
