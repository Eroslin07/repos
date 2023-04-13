package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarCostVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoAndDetailVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.convert.app.CarInfoConvert;
import com.newtouch.uctp.module.business.convert.app.CarInfoDetailsConvert;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoMapper;
import com.newtouch.uctp.module.business.service.BusinessFileService;
import com.newtouch.uctp.module.business.service.CarInfoDetailsService;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.newtouch.uctp.module.system.api.dict.DictDataApi;
import com.newtouch.uctp.module.system.api.dict.dto.DictDataRespDTO;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
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
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;
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


    @Resource
    private FileApi fileApi;

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
    public AppBpmCarInfoRespVO insertCarInfo(AppCarInfoCreateReqVO createReqVO) {

//        CarInfoDO carInfoDO = carInfoMapper.selectByVin(createReqVO.getVin(),"11");//11收车中草稿
//        if (ObjectUtil.isNull(carInfoDO)) {
//            throw exception(CAR_INFO_IS_EXISTS);
//        }
        //车辆主表信息
        CarInfoDO infoDO = new CarInfoDO();
        infoDO.setBrand(createReqVO.getBrand());
        infoDO.setVin(createReqVO.getVin());
        infoDO.setBrand(createReqVO.getBrand());//车辆品牌
        infoDO.setCarType(createReqVO.getCarType());//车辆类型
        infoDO.setBrandType(createReqVO.getBrandType());//品牌型号
        infoDO.setModel(createReqVO.getModel());//品牌/车型
        infoDO.setPlateNum(createReqVO.getPlateNum());
        infoDO.setEngineNum(createReqVO.getEngineNum());
        infoDO.setRemarks(createReqVO.getRemarks());

        LocalDateTime current = LocalDateTime.now();
        infoDO.setPickUpTime(current);
        infoDO.setCheckStatus(0);
        infoDO.setBusinessId(createReqVO.getDeptId());
        infoDO.setTenantId(createReqVO.getTenantId());

        infoDO.setScrapDate(createReqVO.getScrapDate());
        infoDO.setAnnualInspectionDate(createReqVO.getAnnualInspectionDate());
        infoDO.setInsurance(createReqVO.getInsurance());
        infoDO.setInsuranceEndData(createReqVO.getInsuranceEndData());

        infoDO.setSalesStatus(1);//收车中
        infoDO.setStatus(11);//草稿
        infoDO.setStatusThree(111);
        carInfoMapper.insert(infoDO);

        //车辆明细数据
        CarInfoDetailsDO detailsDO = new CarInfoDetailsDO();
        detailsDO.setCarId(infoDO.getId());
        detailsDO.setMileage(createReqVO.getMileage());
        detailsDO.setColour(createReqVO.getColour());
        detailsDO.setAccidentVehicle(0);
        detailsDO.setSoakingCar(0);
        detailsDO.setNatureOfOperat(createReqVO.getNatureOfOperat());
        detailsDO.setBurnCar(0);
        detailsDO.setFirstRegistDate(createReqVO.getFirstRegistDate());
        detailsDO.setDrivingLicense(createReqVO.getDrivingLicense());
        detailsDO.setTenantId(createReqVO.getTenantId());
        detailsDO.setProceduresAndSpareParts(createReqVO.getProceduresAndSpareParts());
        carInfoDetailsService.insertCarInfoDetail(detailsDO);

        //保存图片到中间表
        List<String> carUrl = createReqVO.getCarUrl();
        for(int a=0;a<carUrl.size();a++){//车辆图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(carUrl.get(a)));
            businessFileDO.setMainId(infoDO.getId());
            if(a==0){
                businessFileDO.setFileType("1-1");//1-1 车辆图片首页图
            }else{
                businessFileDO.setFileType("1");
            }
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

        return this.buildBmpVO(infoDO,detailsDO);
    }

    @Override
    @Transactional
    public String insertSellerInfo(AppSellerInfoReqVO reqVO) {
        //更新车辆明细表
        Long id = reqVO.getId();
        CarInfoDetailsDO infoDetails = carInfoDetailsService.getCarInfoDetails(id);
        infoDetails.setSellerName(reqVO.getSellerName());
        infoDetails.setCollection(reqVO.getCollection());//是否第三方代收
        infoDetails.setSellerIdCard(reqVO.getSellerIdCard());
        infoDetails.setSellerTel(reqVO.getSellerTel());
        infoDetails.setRemitType(reqVO.getRemitType());//卖家收款方式
        infoDetails.setPayType(reqVO.getPayType());//平台付款方式
        infoDetails.setBankCard(reqVO.getBankCard());
        infoDetails.setBankName(reqVO.getBankName());
        infoDetails.setThirdSellerName(reqVO.getThirdSellerName());
        infoDetails.setThirdBankCard(reqVO.getThirdBankCard());
        carInfoDetailsService.updateCarInfoDetail(infoDetails);


        //更新车辆主表
        CarInfoDO carInfoDO = carInfoMapper.selectById(infoDetails.getCarId());
        carInfoDO.setVehicleReceiptAmount(reqVO.getVehicleReceiptAmount());
        carInfoMapper.updateById(carInfoDO);

        for(int a=0;a<reqVO.getIdCardUrl().size();a++){//卖家身份证图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(reqVO.getIdCardUrl().get(a)));
            businessFileDO.setMainId(id);//车辆明细表id
            businessFileDO.setFileType("4");
            businessFileService.insert(businessFileDO);
        }

        carInfoDetailsService.updateCarInfoDetail(infoDetails);
        return "success";
    }

    @Override
    public CarInfoDO selectCarInfoByID(String id) {
        CarInfoDetailsDO infoDetails = carInfoDetailsService.getCarInfoDetails(Long.valueOf(id));
        return carInfoMapper.selectById(infoDetails.getCarId());
    }

    @Override
    public CarInfoDetailsDO seleCarInfoDetail(String id) {
        return carInfoDetailsService.getCarInfoDetails(Long.valueOf(id));
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
    public AppSellCarInfoRespVO getCarInfoByVIN(String vin) {
        CarInfoDO carInfoDO = carInfoMapper.selectByVin(vin,"11");//11收车中草稿
        Long id = carInfoDO.getId();
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
    public AppBpmCarInfoRespVO saveSellCarInfo(AppSellCarInfoReqVO reqVO) {
        validateCarInfoExists(reqVO.getId());
        // 更新卖车填写数据
        CarInfoDO carInfo = CarInfoConvert.INSTANCE.convert(reqVO);
        CarInfoDetailsDO carInfoDetails = CarInfoDetailsConvert.INSTANCE.convert(reqVO);
        carInfoMapper.updateById(carInfo);
        carInfoDetailsService.updateCarInfoDetail(carInfoDetails);
        //保存卖车上传的身份证正反面图片
        List<BusinessFileDO> businessFileList = businessFileService.getByMainIdAndType(carInfo.getId(), "5");
        if (CollUtil.isNotEmpty(businessFileList)) {
            //这里其实可以不用查询，直接删除
            businessFileService.deleteByMainIdAndType(carInfo.getId(),"5");
        }
        reqVO.getIdCardIds().forEach(fileId -> {
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(fileId);
            businessFileDO.setFileType("5");
            businessFileDO.setMainId(carInfo.getId());
            businessFileService.insert(businessFileDO);
        });
        //返回流程需要的对象
        return this.buildBmpVO(carInfo,carInfoDetails);
    }

    private AppBpmCarInfoRespVO buildBmpVO(CarInfoDO carInfo,CarInfoDetailsDO carInfoDetails){
        AppBpmCarInfoRespVO vo = new AppBpmCarInfoRespVO();
        vo.setCarInfo(carInfo);
        vo.setCarInfoDetails(carInfoDetails);
        List<FileRespDTO> fileList = businessFileService.getDTOByMainId(carInfo.getId());
        //1车辆图片 2行驶证 3登记证书 4卖家身份证 5买家身份证  6银行卡 7发票 8商户身份证
        fileList.forEach(file->{
            switch (file.getFileType()){
                case "1":
                case "1-1":
                    //1-1 表是车辆第一张图片
                    vo.getFileA().add(new AppSimpleFileVO(file));
                    break;
                case "2":
                    vo.getFileB().add(new AppSimpleFileVO(file));
                    break;
                case "3":
                    vo.getFileC().add(new AppSimpleFileVO(file));
                    break;
                case "4":
                    vo.getFileD().add(new AppSimpleFileVO(file));
                    break;
                case "5":
                    vo.getFileE().add(new AppSimpleFileVO(file));
                    break;
                default:
                    break;
            }
        });
        return vo;
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
    public AppCarInfoAndDetailVO getCarInfoAndDetail(String id) {
        return carInfoMapper.getCarInfoAndDetails(id);
    }

    @Override
    public CarDetailRespVO  getCarInfoAndDetails(CarDCVo carDCVo) {

        CarDetailRespVO carDetailRespVO=new CarDetailRespVO();
        List<CarDCVo> carIds = getCarIds(carDCVo.getId());
        List<Long> cars = new ArrayList<>();

        for (CarDCVo carId : carIds) {
            cars.add(carId.getLongId());
        }

        CommonResult<List<FileRespDTO>> listcarIds = fileApi.fileList(cars);
        //车辆图片
        List<String> carPics = new ArrayList<>();
        if (listcarIds.getData()!=null) {
            for (FileRespDTO datum : listcarIds.getData()) {
                carPics.add(datum.getUrl());
            }
        }
        //行驶证和驾驶证还需要进行状态的判断来进行分表查询是否新数据 ToDO
        PicResp pics = getPics(carDCVo);
        AppCarInfoAndDetailVO appCarInfoAndDetailVO = getCarInfoAndDetail(carDCVo.getId());
        appCarInfoAndDetailVO.setCarPic(carPics);
        //写入行驶证驾驶证图片
        appCarInfoAndDetailVO.setPics(pics);
        //写入车辆信息
        carDetailRespVO.setCarInfoAndDetailVO(appCarInfoAndDetailVO);
        //合同信息
        List<AppContractarVO> contractInfo = getContractInfo(carDCVo.getId());
        List<AppContractarVO> contractInfo1 =new ArrayList<>();
        for (AppContractarVO appContractarVO : contractInfo) {
            contractInfo1.add(setContractUrl(appContractarVO));
        }
        carDetailRespVO.setContractarVO(contractInfo1);

        // 资金信息
        carDetailRespVO.setCarCostVO(getCarCosts(carDCVo.getId()));
        //发票信息
        List<AppCarInvoiceVo> invoicesInfosList= getInvoicesInfo(carDCVo.getId());
        List<AppCarInvoiceVo> invoicesInfos= new ArrayList<>();
        for (AppCarInvoiceVo appCarInvoiceVo : invoicesInfosList) {
            invoicesInfos.add(setInvoiceUrl(appCarInvoiceVo));
        }
        carDetailRespVO.setCarInvoiceVO(invoicesInfos);

        return carDetailRespVO;
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
    public List<AppCarInvoiceVo> getInvoicesInfo(String id) {
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

    @Override
    public List<CarDCVo> getInvoiceIds(String id) {
        return carInfoMapper.getInvoiceIds(id);
    }

    @Transactional
    @Override
    public void deleteSell(Long id) {
        this.validateCarInfoExists(id);
        CarInfoDO carInfo = this.getCarInfo(id);
        //判断第二级状态不为草稿状态，禁止删除
        if (!"31".equals(carInfo.getStatus())) {
            throw exception(CAR_INFO_STATUS_ERROR);
        }
        carInfo.setRemarks(null);
        carInfo.setSellAmount(null);
        carInfo.setSellType(null);
        carInfo.setRemarks(null);
        CarInfoDetailsDO carInfoDetails = carInfoDetailsService.getCarInfoDetailsByCarId(id);
        carInfoDetails.setBuyerAdder(null);
        carInfoDetails.setBuyerIdCard(null);
        carInfoDetails.setBuyerName(null);
        carInfoDetails.setBuyerTel(null);
        carInfoDetails.setTransManageName(null);
        carInfoMapper.updateById(carInfo);
        carInfoDetailsService.updateCarInfoDetail(carInfoDetails);
    }


    public PicResp getPics( CarDCVo carDCVo) {
        //获取行驶证，驾驶证号
       // CarDCVo carDC =getCarDC(carDCVo.getId());
       // List<CarDCVo>  certificatePic=getCertificateIds(carDC.getCertificateNo());
        //List<CarDCVo> drivingPic =getDrivingLicenseIds(carDC.getDrivingLicense());
        List<CarDCVo>  certificatePic=getCertificateIds(carDCVo.getId());
        List<CarDCVo> drivingPic =getDrivingLicenseIds(carDCVo.getId());
        List<Long> certificates=new ArrayList<>();
        List<Long> drivers=new ArrayList<>();

        for (CarDCVo carDCVo1 : certificatePic) {
            certificates.add(carDCVo1.getLongId());
        }
        for (CarDCVo carDCVo1 : drivingPic) {
            drivers.add(carDCVo1.getLongId());
        }
        CommonResult<List<FileRespDTO>> listCertificates = fileApi.fileList(certificates);
        CommonResult<List<FileRespDTO>> listDrivers = fileApi.fileList(drivers);

        //行驶证图片
        List<String> drivingPics=new ArrayList<>();
        //驾驶证图片
        List<String> certificatePics=new ArrayList<>();
        if (listCertificates.getData()!=null) {
            for (FileRespDTO datum : listCertificates.getData()) {
                certificatePics.add(datum.getUrl());
            }
        }
        if (listDrivers.getData()!=null) {
            for (FileRespDTO datum : listDrivers.getData()) {
                drivingPics.add(datum.getUrl());
            }
        }
       // PicResp picResp =getCarDCDetails(carDCVo.getId().toString());
        PicResp picResp =new PicResp();
        picResp.setDrivingPics(drivingPics);
        picResp.setCertificatePics(certificatePics);
        return picResp;
    }



    /**
     * 将合同的url放到实体中
     */
    private AppContractarVO setContractUrl(AppContractarVO appContractarVO){

        CommonResult<List<FileRespDTO>> listContractar =null;

        List<Long> contractList=new ArrayList<>();
        List<CarDCVo> contractIds= getContractIds(appContractarVO.getContractID()) ;//一条合同数据的ids;正常情况一个合同只会有一个pdf文件
        for (CarDCVo contractId : contractIds) {
            contractList.add(contractId.getLongId());
        }
        listContractar= fileApi.fileList(contractList);
        if(listContractar.getData()!=null) {
            for (FileRespDTO datum : listContractar.getData()) {

                appContractarVO.setUrl(datum.getUrl());
            }
        }
        return appContractarVO;
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
