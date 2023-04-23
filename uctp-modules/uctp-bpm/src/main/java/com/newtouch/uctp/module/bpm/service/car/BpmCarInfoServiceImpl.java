package com.newtouch.uctp.module.bpm.service.car;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDetailsDO;
import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoDetailsMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoMapper;
import com.newtouch.uctp.module.bpm.service.car.dto.AppBpmCarInfoRespDTO;

/**
 * @author helong
 * @date 2023/4/21 17:19
 */
@Service
@Validated
public class BpmCarInfoServiceImpl implements BpmCarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private CarInfoDetailsMapper carInfoDetailsMapper;


    @Override
    public AppBpmCarInfoRespDTO getCollectTheCarInfo(Long carId) {
        AppBpmCarInfoRespDTO dto = new AppBpmCarInfoRespDTO();
        CarInfoDO carInfoDO = carInfoMapper.selectById(carId);
        CarInfoDetailsDO carInfoDetailsDO = carInfoDetailsMapper.selectOne(CarInfoDetailsDO::getCarId, carId);
        dto.setCarInfo(carInfoDO);
        dto.setCarInfoDetails(carInfoDetailsDO);


        return null;
    }
}
