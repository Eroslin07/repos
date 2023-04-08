package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.convert.app.CarInfoConvert;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoMapper;
import com.newtouch.uctp.module.business.service.BusinessFileService;
import com.newtouch.uctp.module.business.service.CarInfoDetailsService;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Slf4j
public class CarInfoServiceImpl implements CarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private BusinessFileService businessFileService;
    @Resource
    private CarInfoDetailsService infoDetailsService;

    @Override
    public Long createCarInfo(AppCarInfoCreateReqVO createReqVO) {
        // 插入
        CarInfoDO carInfo = CarInfoConvert.INSTANCE.convert(createReqVO);
        carInfoMapper.insert(carInfo);
        // 返回
        return carInfo.getId();
    }

    @Override
    @Transactional
    public String insertCarInfo(AppCarInfoCreateReqVO createReqVO) {
//        SecurityFrameworkUtils.getLoginUserId();
        //车辆主表信息
        CarInfoDO infoDO = new CarInfoDO();
        infoDO.setBrand(createReqVO.getBrand());
        infoDO.setVin(createReqVO.getVin());
        infoDO.setYear(createReqVO.getYear());
        infoDO.setModel(createReqVO.getModel());
        infoDO.setEngineNum(createReqVO.getEngineNum());
        infoDO.setVehicleReceiptAmount(createReqVO.getVehicleReceiptAmount());
        infoDO.setRemarks(createReqVO.getRemarks());
        infoDO.setSalesStatus(0);
        LocalDateTime current = LocalDateTime.now();
        infoDO.setPickUpTime(current);
        infoDO.setCheckStatus(0);
        carInfoMapper.insert(infoDO);

        //车辆明细数据
        CarInfoDetailsDO detailsDO = new CarInfoDetailsDO();
        detailsDO.setCarId(infoDO.getId());
        detailsDO.setMileage(createReqVO.getMileage());
        detailsDO.setAccidentVehicle(0);
        detailsDO.setSoakingCar(0);
        detailsDO.setBurnCar(0);
        detailsDO.setFirstRegistDate(createReqVO.getFirstRegistDate());
        detailsDO.setDrivingLicense(createReqVO.getDrivingLicense());
        infoDetailsService.insertCarInfoDetail(detailsDO);

        //保存图片到中间表
        List<String> carUrl = createReqVO.getCarUrl();
        for(int a=0;a<carUrl.size();a++){//车辆图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(carUrl.get(a)));
            businessFileDO.setMainId(infoDO.getId());
            businessFileDO.setFileType("1");
            businessFileService.insert(businessFileDO);
        }

        List<String> licenseUrl = createReqVO.getDrivingLicenseUrl();
        for(int a=0;a<licenseUrl.size();a++){//行驶证图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(licenseUrl.get(a)));
            businessFileDO.setMainId(infoDO.getId());
            businessFileDO.setFileType("2");
            businessFileService.insert(businessFileDO);
        }

        List<String> certificateUrl = createReqVO.getCertificateUrl();
        for(int a=0;a<licenseUrl.size();a++){//登记证书图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(certificateUrl.get(a)));
            businessFileDO.setMainId(infoDO.getId());
            businessFileDO.setFileType("3");
            businessFileService.insert(businessFileDO);
        }

        return String.valueOf(infoDO.getId());
    }

    @Override
    @Transactional
    public String insertSellerInfo(AppSellerInfoReqVO reqVO) {
        Long id = reqVO.getId();
        CarInfoDetailsDO infoDetails = infoDetailsService.getCarInfoDetails(id);
        infoDetails.setSellerName(reqVO.getSellerName());
        infoDetails.setCollection(reqVO.getCollection());
        infoDetails.setSellerIdCard(reqVO.getSellerIdCard());
        infoDetails.setSellerTel(reqVO.getSellerTel());
        infoDetails.setPayType(reqVO.getPayType());
        infoDetails.setBankCard(reqVO.getBankCard());

        infoDetails.setThirdSellerName(reqVO.getThirdSellerName());
        infoDetails.setThirdBankCard(reqVO.getThirdBankCard());
        infoDetailsService.updateCarInfoDetail(infoDetails);
        return "success";
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
        pageVO.formatLocalDateTime();
        page = carInfoMapper.selectAppHomePage(pageVO);
//        page = carInfoMapper.selectAppHomePage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<Map<String, Object>> getCarCountGroupByStatus() {
        List<Map<String, Object>> maps = carInfoMapper.selectCarCountGroupByStatus();
        return this.initRetMap(maps);
    }

    @Override
    public PageResult<AppSellCarInfoPageRespVO> getSellCarInfoPage(AppSellCarInfoPageReqVO pageVO) {
        return carInfoMapper.selectAppCellCarPage(pageVO);
    }

    @Override
    public AppSellCarInfoRespVO getSellCarInfo(Long id) {
        CarInfoDO carInfo = this.getCarInfo(id);
        if (ObjectUtil.isNull(carInfo)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        List<FileRespDTO> fileList = businessFileService.getFileByMainId(id,"");
        List<String> carPicList = Lists.newArrayList();
        List<String> drivingPicList = Lists.newArrayList();
        List<String> registerPicList = Lists.newArrayList();
        for (FileRespDTO dto : fileList) {
//            1车辆图片 2行驶证 3登记证书 4卖家身份证 5买家身份证
            switch (dto.getFileType()){
                case "1":
                    carPicList.add(dto.getUrl());
                    break;
                case "2":
                    drivingPicList.add(dto.getUrl());
                    break;
                case "3":
                    registerPicList.add(dto.getUrl());
                    break;
                default:
                    break;
            }
        }
        //TODO 获取预计费用和利润
        BigDecimal estimatedCost = new BigDecimal("666");
        BigDecimal profit = new BigDecimal("999");
        AppSellCarInfoRespVO carInfoRespVO = CarInfoConvert.INSTANCE.convertSell(carInfo,carPicList,drivingPicList,registerPicList,estimatedCost,profit);
        return carInfoRespVO;
    }

    @Override
    public AppCarInfoAmountRespVO getCarInfoAmount(Long id,BigDecimal sellAmount) {
//        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        //税配置100，服务费配置200，然后合计费用300；利润=卖车金额-收车金额-税-服务费
        //杂税不配置税率，增值税配置0.5%。税费等于卖车合同*税率
        CarInfoDO carInfo = this.getCarInfo(id);
        if (ObjectUtil.isNull(carInfo)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        //TODO 这里从字典数据取配置的值
        AppCarInfoAmountRespVO respVO = new AppCarInfoAmountRespVO();
        //TODO 这里卖车金额需要前端传递过来（优先去前端传递数据），除非他已经存过一次
        respVO.calculation(carInfo.getVehicleReceiptAmount(),ObjectUtil.isNotNull(sellAmount)?sellAmount:carInfo.getSellAmount());
        return respVO;
    }

    @Override
    public void saveSellCarInfo(AppSellCarInfoReqVO reqVO) {
        validateCarInfoExists(reqVO.getId());
        // 更新
        CarInfoDO updateObj = CarInfoConvert.INSTANCE.convert(reqVO);
        carInfoMapper.updateById(updateObj);
    }

    /**
     * 系统初始化时，首页统计数据可能数据不全，这里进行补齐
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
