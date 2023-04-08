package com.newtouch.uctp.module.uctp.service;

import cn.hutool.core.util.IdUtil;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.test.core.ut.BaseDbUnitTest;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoMapper;
import com.newtouch.uctp.module.business.service.impl.CarInfoServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;

import javax.annotation.Resource;

import static com.newtouch.uctp.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static com.newtouch.uctp.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.newtouch.uctp.framework.test.core.util.AssertUtils.assertServiceException;
import static com.newtouch.uctp.framework.test.core.util.RandomUtils.randomPojo;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CarInfoServiceImpl} 的单元测试类
 *
 * @author lc
 */
@Import(CarInfoServiceImpl.class)
public class CarInfoServiceImplTest extends BaseDbUnitTest {
    @Resource
    private CarInfoServiceImpl carInfoService;

    @Resource
    private CarInfoMapper carInfoMapper;


    @Test
    @Commit
    public void testCreateCarInfo_success() {
        // 准备参数
        AppCarInfoCreateReqVO reqVO = randomPojo(AppCarInfoCreateReqVO.class);

        // 调用
        Long carInfoId = carInfoService.createCarInfo(reqVO);
        // 断言
        assertNotNull(carInfoId);
        // 校验记录的属性是否正确
        CarInfoDO carInfo = carInfoMapper.selectById(carInfoId);
        assertPojoEquals(reqVO, carInfo);
    }

    @Test
    public void testUpdateCarInfo_success() {
        // mock 数据
        CarInfoDO dbCarInfo = randomPojo(CarInfoDO.class);
        carInfoMapper.insert(dbCarInfo);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCarInfoUpdateReqVO reqVO = randomPojo(AppCarInfoUpdateReqVO.class, o -> {
            o.setId(dbCarInfo.getId()); // 设置更新的 ID
        });

        // 调用
        carInfoService.updateCarInfo(reqVO);
        // 校验是否更新正确
        CarInfoDO carInfo = carInfoMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, carInfo);
    }

    @Test
    public void testUpdateCarInfo_notExists() {
        // 准备参数
        AppCarInfoUpdateReqVO reqVO = randomPojo(AppCarInfoUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> carInfoService.updateCarInfo(reqVO), CAR_INFO_NOT_EXISTS);
    }

    @Test
    public void testDeleteCarInfo_success() {
        // mock 数据
        CarInfoDO dbCarInfo = randomPojo(CarInfoDO.class);
        carInfoMapper.insert(dbCarInfo);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCarInfo.getId();

        // 调用
        carInfoService.deleteCarInfo(id);
        // 校验数据不存在了
        assertNull(carInfoMapper.selectById(id));
    }

    @Test
    public void testDeleteCarInfo_notExists() {
        // 准备参数
        Long id = randomStringId();

        // 调用, 并断言异常
        assertServiceException(() -> carInfoService.deleteCarInfo(id), CAR_INFO_NOT_EXISTS);
    }

    private Long randomStringId() {
        return IdUtil.getSnowflakeNextId();
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCarInfoPage() {
        // mock 数据
        CarInfoDO dbCarInfo = randomPojo(CarInfoDO.class, o -> { // 等会查询到
            o.setVin(null);
            o.setBrand(null);
            o.setYear(null);
            o.setModel(null);
            o.setEngineNum(null);
            o.setVehicleReceiptAmount(null);
            o.setRemarks(null);
            o.setRevision(null);
            o.setCreateTime(null);
            o.setSalesStatus(null);
            o.setCheckStatus(null);
            o.setBusinessId(null);
        });
        carInfoMapper.insert(dbCarInfo);
        // 测试 vin 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setVin(null)));
        // 测试 brand 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setBrand(null)));
        // 测试 year 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setYear(null)));
        // 测试 style 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setModel(null)));
        // 测试 engineNum 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setEngineNum(null)));
        // 测试 vehicleReceiptAmount 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setVehicleReceiptAmount(null)));
        // 测试 remarks 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setRemarks(null)));
        // 测试 revision 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setRevision(null)));
        // 测试 createTime 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setCreateTime(null)));
        // 测试 salesStatus 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setSalesStatus(null)));
        // 测试 checkStatus 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setCheckStatus(null)));
        // 测试 businessId 不匹配
        carInfoMapper.insert(cloneIgnoreId(dbCarInfo, o -> o.setBusinessId(null)));
        // 准备参数
        AppCarInfoPageReqVO reqVO = new AppCarInfoPageReqVO();
        reqVO.setVin(null);
        reqVO.setBrand(null);
//        reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
//        reqVO.setUpdatedTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
        reqVO.setSalesStatus(null);
        reqVO.setCheckStatus(null);
        // 调用
        PageResult<CarInfoDO> pageResult = carInfoService.getCarInfoPage(reqVO);
        // 断言
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertPojoEquals(dbCarInfo, pageResult.getList().get(0));
    }

}
