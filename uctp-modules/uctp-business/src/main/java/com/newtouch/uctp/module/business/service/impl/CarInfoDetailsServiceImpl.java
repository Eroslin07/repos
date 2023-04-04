package com.newtouch.uctp.module.business.service.impl;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsUpdateReqVO;
import com.newtouch.uctp.module.business.convert.app.CarInfoDetailsConvert;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoDetailsMapper;
import com.newtouch.uctp.module.business.service.CarInfoDetailsService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_DETAILS_NOT_EXISTS;

public class CarInfoDetailsServiceImpl implements CarInfoDetailsService {
    @Resource
    private CarInfoDetailsMapper carInfoDetailsMapper;

    @Override
    public String createCarInfoDetails(CarInfoDetailsCreateReqVO createReqVO) {
        // 插入
        CarInfoDetailsDO carInfoDetails = CarInfoDetailsConvert.INSTANCE.convert(createReqVO);
        carInfoDetailsMapper.insert(carInfoDetails);
        // 返回
        return carInfoDetails.getId();
    }

    @Override
    public void updateCarInfoDetails(CarInfoDetailsUpdateReqVO updateReqVO) {
        // 校验存在
        validateCarInfoDetailsExists(updateReqVO.getId());
        // 更新
        CarInfoDetailsDO updateObj = CarInfoDetailsConvert.INSTANCE.convert(updateReqVO);
        carInfoDetailsMapper.updateById(updateObj);
    }

    @Override
    public void deleteCarInfoDetails(String id) {
        // 校验存在
        validateCarInfoDetailsExists(id);
        // 删除
        carInfoDetailsMapper.deleteById(id);
    }

    private void validateCarInfoDetailsExists(String id) {
        if (carInfoDetailsMapper.selectById(id) == null) {
            throw exception(CAR_INFO_DETAILS_NOT_EXISTS);
        }
    }

    @Override
    public CarInfoDetailsDO getCarInfoDetails(String id) {
        return carInfoDetailsMapper.selectById(id);
    }

    @Override
    public List<CarInfoDetailsDO> getCarInfoDetailsList(Collection<String> ids) {
        return carInfoDetailsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CarInfoDetailsDO> getCarInfoDetailsPage(CarInfoDetailsPageReqVO pageReqVO) {
        return carInfoDetailsMapper.selectPage(pageReqVO);
    }


}
