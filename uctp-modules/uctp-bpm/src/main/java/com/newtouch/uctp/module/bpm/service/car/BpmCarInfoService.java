package com.newtouch.uctp.module.bpm.service.car;

import com.newtouch.uctp.module.bpm.service.car.dto.AppBpmCarInfoRespDTO;

/**
 * @author helong
 * @date 2023/4/21 17:19
 */

public interface BpmCarInfoService {
    /**
     * 根据车辆ID获取收车信息
     * @param carId  车辆ID
     * @return
     */
    AppBpmCarInfoRespDTO getCollectTheCarInfo(Long carId);
}
