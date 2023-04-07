package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.convert.app.CarInfoConvert;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoMapper;
import com.newtouch.uctp.module.business.service.CarInfoService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    @Override
    public Long createCarInfo(AppCarInfoCreateReqVO createReqVO) {
        // 插入
        CarInfoDO carInfo = CarInfoConvert.INSTANCE.convert(createReqVO);
        carInfoMapper.insert(carInfo);
        // 返回
        return carInfo.getId();
    }

    @Override
    public void updateCarInfo(AppCarInfoUpdateReqVO updateReqVO) {
        // 校验存在
        validateCarInfoExists(updateReqVO.getId());
        // 更新
        CarInfoDO updateObj = CarInfoConvert.INSTANCE.convert(updateReqVO);
        carInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteCarInfo(Long id) {
        // 校验存在
        validateCarInfoExists(id);
        // 删除
        carInfoMapper.deleteById(id);
    }

    private void validateCarInfoExists(Long id) {
        if (carInfoMapper.selectById(id) == null) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
    }

    @Override
    public CarInfoDO getCarInfo(Long id) {
        return carInfoMapper.selectById(id);
    }

    @Override
    public List<CarInfoDO> getCarInfoList(Collection<Long> ids) {
        return carInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CarInfoDO> getCarInfoPage(AppCarInfoPageReqVO pageReqVO) {
        return carInfoMapper.selectPage(pageReqVO);
    }


    @Override
    public PageResult<AppHomeCarInfoRespVO> getHomeCarInfoPage(AppHomeCarInfoPageReqVO pageVO) {
        Page<AppHomeCarInfoRespVO> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize());
        page = carInfoMapper.selectAppHomePage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<Map<String, Object>> getCarCountGroupByStatus() {
        List<Map<String, Object>> maps = carInfoMapper.selectCarCountGroupByStatus();
        return this.initRetMap(maps);
    }

    /**
     * 系统初始化时，可能数据不全，这里进行补齐
     * @param maps
     * @return
     */
    private List<Map<String, Object>> initRetMap(List<Map<String, Object>> maps){
        if (maps.size() == 4) {
            //这里以后增加了状态删除即可
            return maps;
        }
        if (CollUtil.isEmpty(maps)) {
            for (int i = 1; i <= 4; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("salesStatus", i);
                map.put("num", 0);
                maps.add(map);
            }
        }else {
            List<Integer> salesStatusList = new ArrayList<>();
            maps.forEach(map->{
                salesStatusList.add((Integer) map.get("salesStatus"));
            });
            List<Integer> list = Arrays.asList(1, 2, 3, 4);
            List<Integer> resList = list.stream().filter(i -> !salesStatusList.contains(i)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(resList)) {
                for (Integer i : resList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("salesStatus", i);
                    map.put("num", 0);
                    maps.add(map);
                }
            }
        }
        return maps;
    }
}
