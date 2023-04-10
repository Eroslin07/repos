package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarCostVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoAndDetailVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.domain.app.InvoicesInfoDO;
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
import com.newtouch.uctp.module.system.api.dict.DictDataApi;
import com.newtouch.uctp.module.system.api.dict.dto.DictDataRespDTO;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_NOT_EXISTS;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_SELL_AMOUNT_ERROR;
import static com.newtouch.uctp.module.system.enums.DictTypeConstants.*;
import static com.newtouch.uctp.module.system.enums.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;

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
    private CarInfoDetailsService carInfoDetailsService;
    @Resource
    private DictDataApi dictDataApi;

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
        infoDO.setBusinessId(Long.valueOf("130"));
        infoDO.setStatus(31);//草稿
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
        carInfoDetailsService.insertCarInfoDetail(detailsDO);

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
        return String.valueOf(detailsDO.getId());
    }

    @Override
    @Transactional
    public String insertSellerInfo(AppSellerInfoReqVO reqVO) {
        Long id = reqVO.getId();
        CarInfoDetailsDO infoDetails = carInfoDetailsService.getCarInfoDetails(id);
        infoDetails.setSellerName(reqVO.getSellerName());
        infoDetails.setCollection(reqVO.getCollection());
        infoDetails.setSellerIdCard(reqVO.getSellerIdCard());
        infoDetails.setSellerTel(reqVO.getSellerTel());
        infoDetails.setPayType(reqVO.getPayType());
        infoDetails.setBankCard(reqVO.getBankCard());

        infoDetails.setThirdSellerName(reqVO.getThirdSellerName());
        infoDetails.setThirdBankCard(reqVO.getThirdBankCard());
        carInfoDetailsService.updateCarInfoDetail(infoDetails);
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
        page = carInfoMapper.selectAppHomePage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<Map<String, Object>> getCarCountGroupByStatus() {
        List<Map<String, Object>> maps = carInfoMapper.selectCarCountGroupByStatus();
        return this.initRetMap(maps);
    }

    @Override
    public PageResult<AppSellCarInfoPageRespVO> getSellCarInfoPage(AppSellCarInfoPageReqVO pageVO) {
        Page<AppSellCarInfoPageRespVO> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize());
        page = carInfoMapper.selectAppCellCarPage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public AppSellCarInfoRespVO getSellCarInfo(Long id) {
        CarInfoDO carInfo = this.getCarInfo(id);
        if (ObjectUtil.isNull(carInfo)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        CarInfoDetailsDO carInfoDetailsDO = carInfoDetailsService.getCarInfoDetailsByCarId(id);
        if (ObjectUtil.isNull(carInfoDetailsDO)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        //获取车辆相关图片
        List<FileRespDTO> fileList = businessFileService.getDTOByMainIdAndType(id,null);
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
        AppSellCarInfoRespVO carInfoRespVO = CarInfoConvert.INSTANCE.convertSell(carInfo,carPicList,drivingPicList,registerPicList,carInfoDetailsDO);
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
        carInfo.setSellAmount(sellAmount);
        //卖车金额 >= 收车金额
        if (sellAmount.compareTo(carInfo.getVehicleReceiptAmount()) == -1) {
            throw exception(CAR_INFO_SELL_AMOUNT_ERROR);
        }
        //获取默认字典表配置的车辆金额费用配置项
        CommonResult<List<DictDataRespDTO>> dictDataRes = dictDataApi.getDictDataList(DictTypeConstants.CAR_EXPENSE_CONFIG_DEFAULT, null);
        if (!dictDataRes.getCode().equals(0)) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        List<DictDataRespDTO> dictDataDTOList = dictDataRes.getData();
        if (CollUtil.isEmpty(dictDataDTOList)) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        return this.calculation(carInfo,dictDataDTOList);
    }

    private AppCarInfoAmountRespVO calculation(CarInfoDO carInfo,List<DictDataRespDTO> dictDataDTOList){
        AppCarInfoAmountRespVO vo = new AppCarInfoAmountRespVO();
        vo.setVehicleReceiptAmount(carInfo.getVehicleReceiptAmount());
        vo.setSellAmount(carInfo.getSellAmount());
        dictDataDTOList.forEach(dto -> {
            if (dto.getValue().equalsIgnoreCase(CAR_TAX_VALUE_ADDED)) {
                //税费 = 卖车金额 * 税率
                BigDecimal vat = carInfo.getSellAmount().multiply(new BigDecimal(dto.getLabel()));
                vo.setVat(vat);
            } else if (dto.getValue().equalsIgnoreCase(CAR_SERVICE_OPERATION)) {
                vo.setOperation(new BigDecimal(dto.getLabel()));
            } else if (dto.getValue().equalsIgnoreCase(CAR_SERVICE_TRANSFER_SELL)) {
                vo.setTransferSell(new BigDecimal(dto.getLabel()));
            } else if (dto.getValue().equalsIgnoreCase(CAR_SERVICE_TRANSFER_BUY)) {
                vo.setTransferBuy(new BigDecimal(dto.getLabel()));
            }
        });
        log.info("卖车详情的明细费用", vo);

        //费用合计 = 所有服务费+税费
        vo.setTotal(vo.getVat()
                .add(vo.getOperation())
                .add(vo.getTransferBuy())
                .add(vo.getTransferSell()));
        //利润=卖车金额-收车金额-税-服务费
        //利润=卖车金额-收车金额-费用合计
        vo.setProfit(vo.getSellAmount()
                .subtract(vo.getVehicleReceiptAmount())
                .subtract(vo.getTotal()));
        return vo;
    }

    @Transactional
    @Override
    public void saveSellCarInfo(AppSellCarInfoReqVO reqVO) {
        validateCarInfoExists(reqVO.getId());
        // 更新卖车填写数据
        CarInfoDO updateObj = CarInfoConvert.INSTANCE.convert(reqVO);
        carInfoMapper.updateById(updateObj);
        //保存卖车上传的身份证正反面图片
        List<BusinessFileDO> businessFileList = businessFileService.getByMainIdAndType(updateObj.getId(), "5");
        if (CollUtil.isNotEmpty(businessFileList)) {
            //这里其实可以不用查询，直接删除
            businessFileService.deleteByMainIdAndType(updateObj.getId(),"5");
        }
        reqVO.getIdCardIds().forEach(fileId -> {
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(fileId);
            businessFileDO.setFileType("5");
            businessFileDO.setMainId(updateObj.getId());
            businessFileService.insert(businessFileDO);
        });

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
            for (int i = 0; i <= 3; i++) {
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
            List<Integer> list = Arrays.asList(0,1, 2, 3);
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
    @Override
    public AppCarInfoAndDetailVO getCarInfoAndDetails(String id) {
        return carInfoMapper.getCarInfoAndDetails(id);
    }

    @Override
    public PicResp getCarDCDetails(String id) {
        return carInfoMapper.getCarDCDetails(id);
    }

    @Override
    public AppCarCostVO getCarCosts(String id) {
        return carInfoMapper.getCarCosts(id);
    }

    @Override
    public List<AppContractarVO> getContractInfo(String carID) {
        return carInfoMapper.getContractInfo(carID);
    }

    @Override
    public String updateContractStatas(CarDCVo carDCVo) {
        String result="更新失败";
        int i=carInfoMapper.updateContractStatas(carDCVo);
        if (i>0)
            result="更新失败";
        return result;
    }

    @Override
    public List<InvoicesInfoDO> getInvoicesInfo(String id) {
        return carInfoMapper.getInvoicesInfo(id);
    }

    @Override
    public CarDCVo getCarDC(String carID) {
        return carInfoMapper.getCarDC(carID);
    }

    @Override
    public List<CarDCVo> getCarIds(String carID) {
        return carInfoMapper.getCarIds(carID);
    }

    @Override
    public List<CarDCVo> getDrivingLicenseIds(String carID) {
        return carInfoMapper.getDrivingLicenseIds(carID);
    }

    @Override
    public List<CarDCVo> getCertificateIds(String carID) {
        return carInfoMapper.getCertificateIds(carID);
    }

    @Override
    public List<CarDCVo> getContractIds(String contractID) {
        return carInfoMapper.getContractIds(contractID);
    }

    @Override
    public PeopleVo getPeopelInfo(String carID) {
        return carInfoMapper.getPeopelInfo(carID);
    }

}
