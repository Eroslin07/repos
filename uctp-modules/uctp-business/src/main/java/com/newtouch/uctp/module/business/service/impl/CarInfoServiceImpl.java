package com.newtouch.uctp.module.business.service.impl;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.domain.app.CarInfoDO;
import com.newtouch.uctp.module.business.mapper.CarInfoMapper;
import com.newtouch.uctp.module.business.service.CarInfoService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_NOT_EXISTS;

/**
 * 车辆主表 Service 实现类
 *
 * @author lc
 */
@Service
@Validated
public class CarInfoServiceImpl implements CarInfoService {

    @Resource
    private CarInfoMapper carInfoMapper;



    private void validateCarInfoExists(Long id) {
        if (carInfoMapper.selectById(id) == null) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
    }
    @Override
    public PageResult<CarInfoDO> getCarInfoPage(AppCarInfoPageReqVO pageReqVO) {
        return carInfoMapper.selectPage(pageReqVO);
    }

}
