package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.newtouch.uctp.module.business.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.business.dal.mysql.user.UserMapper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.convert.carInfo.CarInfoConvert;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoDetailsMapper;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoMapper;
import com.newtouch.uctp.module.business.dal.mysql.ContractMapper;
import com.newtouch.uctp.module.business.enums.CarStatus;
import com.newtouch.uctp.module.business.service.*;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.newtouch.uctp.module.system.api.dict.DictDataApi;
import com.newtouch.uctp.module.system.api.dict.dto.DictDataRespDTO;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_NOT_EXISTS;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.CAR_INFO_STATUS_ERROR;
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
    private CarInfoDetailsMapper carInfoDetailsMapper;
    @Resource
    private BusinessFileService businessFileService;
    @Resource
    private CarInfoDetailsService carInfoDetailsService;
    @Resource
    private DictDataApi dictDataApi;
    @Autowired
    @Lazy
    private InvoicesService invoicesService;

    @Resource
    private CostService costService;

    @Resource
    private ContractService contractService;

    @Resource
    private FileApi fileApi;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private UserMapper userMapper;

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
        CarInfoDO infoDO = new CarInfoDO();
        CarInfoDetailsDO detailsDO = new CarInfoDetailsDO();


        if(null!=createReqVO.getId()){
            //保存之前查看是否存在草稿
            List<CarInfoDO> carInfoDOS = carInfoMapper.selectIsExist(createReqVO.getVin(), 1, 11);
            infoDO = carInfoDOS.get(0);
            Long id = infoDO.getId();
            detailsDO = carInfoDetailsService.getCarInfoDetailsByCarId(id);

            //车辆主表信息
            infoDO.setBrand(createReqVO.getBrand());
            infoDO.setVin(createReqVO.getVin());
            infoDO.setBrand(createReqVO.getBrand());//车辆品牌
            infoDO.setCarType(createReqVO.getCarType());//车辆类型
            infoDO.setBrandType(createReqVO.getBrandType());//品牌型号
            infoDO.setModel(createReqVO.getModel());//品牌/车型
            infoDO.setModelId(createReqVO.getModelId());//车型id
            infoDO.setPlateNum(createReqVO.getPlateNum());
            infoDO.setEngineNum(createReqVO.getEngineNum());
            infoDO.setRemarks(createReqVO.getRemarks());
            LocalDateTime current = LocalDateTime.now();
            infoDO.setPickUpTime(current);
            infoDO.setCheckStatus(0);
            infoDO.setBusinessId(createReqVO.getDeptId());
            infoDO.setTenantId(createReqVO.getTenantId());
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String scrapDate = createReqVO.getScrapDate() + " 00:00:00";
            String annualInspectionDate = createReqVO.getAnnualInspectionDate()+" 00:00:00";
            infoDO.setScrapDate(LocalDateTime.parse(scrapDate,df));
            infoDO.setAnnualInspectionDate(LocalDateTime.parse(annualInspectionDate,df));
            infoDO.setInsurance(createReqVO.getInsurance());
            infoDO.setInsuranceEndData(createReqVO.getInsuranceEndData());
            infoDO.setSalesStatus(CarStatus.COLLECT.value());//收车中
            infoDO.setStatus(CarStatus.COLLECT_A.value());//草稿
            infoDO.setStatusThree(CarStatus.COLLECT_A_A.value());
            carInfoMapper.updateById(infoDO);
            //车辆明细数据
            detailsDO.setMileage(createReqVO.getMileage());
            detailsDO.setColour(createReqVO.getColour());
            detailsDO.setNatureOfOperat(createReqVO.getNatureOfOperat());
            detailsDO.setCertificateNo(createReqVO.getCertificateNo());
            detailsDO.setFirstRegistDate(createReqVO.getFirstRegistDate());
            detailsDO.setDrivingLicense(createReqVO.getDrivingLicense());
            detailsDO.setTenantId(createReqVO.getTenantId());
            detailsDO.setProceduresAndSpareParts(createReqVO.getProceduresAndSpareParts());
            carInfoDetailsService.updateCarInfoDetail(detailsDO);
        }else{
            //车辆主表信息
            infoDO.setBrand(createReqVO.getBrand());
            infoDO.setVin(createReqVO.getVin());
            infoDO.setBrand(createReqVO.getBrand());//车辆品牌
            infoDO.setCarType(createReqVO.getCarType());//车辆类型
            infoDO.setBrandType(createReqVO.getBrandType());//品牌型号
            infoDO.setModel(createReqVO.getModel());//品牌/车型
            infoDO.setModelId(createReqVO.getModelId());//车型id
            infoDO.setPlateNum(createReqVO.getPlateNum());
            infoDO.setEngineNum(createReqVO.getEngineNum());
            infoDO.setRemarks(createReqVO.getRemarks());
            LocalDateTime current = LocalDateTime.now();
            infoDO.setPickUpTime(current);
            infoDO.setCheckStatus(0);
            infoDO.setBusinessId(createReqVO.getDeptId());
            infoDO.setTenantId(createReqVO.getTenantId());
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String scrapDate = createReqVO.getScrapDate() + " 00:00:00";
            String annualInspectionDate = createReqVO.getAnnualInspectionDate()+" 00:00:00";
            infoDO.setScrapDate(LocalDateTime.parse(scrapDate,df));
            infoDO.setAnnualInspectionDate(LocalDateTime.parse(annualInspectionDate,df));
            infoDO.setInsurance(createReqVO.getInsurance());
            infoDO.setInsuranceEndData(createReqVO.getInsuranceEndData());
            infoDO.setSalesStatus(CarStatus.COLLECT.value());//收车中
            infoDO.setStatus(CarStatus.COLLECT_A.value());//草稿
            infoDO.setStatusThree(CarStatus.COLLECT_A_A.value());

            //车辆明细数据
            detailsDO.setMileage(createReqVO.getMileage());
            detailsDO.setColour(createReqVO.getColour());
            detailsDO.setNatureOfOperat(createReqVO.getNatureOfOperat());
            detailsDO.setCertificateNo(createReqVO.getCertificateNo());
            detailsDO.setFirstRegistDate(createReqVO.getFirstRegistDate());
            detailsDO.setDrivingLicense(createReqVO.getDrivingLicense());
            detailsDO.setTenantId(createReqVO.getTenantId());
            detailsDO.setProceduresAndSpareParts(createReqVO.getProceduresAndSpareParts());

            carInfoMapper.insert(infoDO);
            detailsDO.setCarId(infoDO.getId());
            carInfoDetailsService.insertCarInfoDetail(detailsDO);

        }
        //保存图片到中间表
        List<String> carUrl = createReqVO.getCarUrl();
        businessFileService.deleteByMainIdAndType(infoDO.getId(),"1-1");
        businessFileService.deleteByMainIdAndType(infoDO.getId(),"1");
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
        businessFileService.deleteByMainIdAndType(infoDO.getId(),"2");
        for(int a=0;a<licenseUrl.size();a++){//行驶证图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(licenseUrl.get(a)));
            businessFileDO.setMainId(infoDO.getId());
            businessFileDO.setFileType("2");
            businessFileService.insert(businessFileDO);
        }

        List<String> certificateUrl = createReqVO.getCertificateUrl();
        businessFileService.deleteByMainIdAndType(infoDO.getId(),"3");
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
    public AppBpmCarInfoRespVO insertSellerInfo(AppSellerInfoReqVO reqVO) {
        //更新车辆明细表
        Long id = reqVO.getId();
        CarInfoDetailsDO infoDetails = carInfoDetailsService.getCarInfoDetails(id);
        infoDetails.setSellerName(reqVO.getSellerName());
        infoDetails.setTransManageName(reqVO.getTransManageName());
        infoDetails.setCollection(reqVO.getCollection());//是否第三方代收
        infoDetails.setSellerIdCard(reqVO.getSellerIdCard());
        infoDetails.setSellerTel(reqVO.getSellerTel());
        infoDetails.setRemitType(reqVO.getRemitType());//卖家收款方式
        infoDetails.setPayType(reqVO.getPayType());//平台付款方式
        infoDetails.setBankCard(reqVO.getBankCard());
        infoDetails.setSellerAdder(reqVO.getSellerAdder());
        infoDetails.setBankName(reqVO.getBankName());
        infoDetails.setThirdSellerName(reqVO.getThirdSellerName());
        infoDetails.setThirdBankCard(reqVO.getThirdBankCard());
        carInfoDetailsService.updateCarInfoDetail(infoDetails);


        //更新车辆主表
        CarInfoDO carInfoDO = carInfoMapper.selectById(infoDetails.getCarId());
        carInfoDO.setVehicleReceiptAmount(reqVO.getVehicleReceiptAmount());
        carInfoMapper.updateById(carInfoDO);

        businessFileService.deleteByMainIdAndType(carInfoDO.getId(),"4");
        for(int a=0;a<reqVO.getIdCardUrl().size();a++){//卖家身份证图片
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setId(Long.valueOf(reqVO.getIdCardUrl().get(a)));
            businessFileDO.setMainId(infoDetails.getCarId());//车辆明细表id
            businessFileDO.setFileType("4");
            businessFileService.insert(businessFileDO);
        }

        carInfoDetailsService.updateCarInfoDetail(infoDetails);
        return this.buildBmpVO(carInfoDO,infoDetails);
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

    @Override
    public void delCarInfoWithCollect(Long id) {
        // 校验存在
        validateCarInfoExists(id);
        // 删除车辆主表
        carInfoMapper.deleteById(id);
        //删除车辆明细表
        carInfoDetailsService.deleteByCarId(id);
        businessFileService.deleteByMainId(id);

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
    public List<HomeCountVO> getCarCountGroupByStatus() {
        List<Map<String, Object>> maps = carInfoMapper.selectCarCountGroupByStatus();
        List<Map<String, Object>> maps1 = this.initRetMap(maps);
        return this.buildHomeCountRespVO(maps1);
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
                case "1-1":
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
    public Map getCarInfoByVIN(String vin) {
        Map map = new HashMap<>();
        //查询除已卖状态以外的车
        List<CarInfoDO> carInfoDOS = carInfoMapper.selectIsSell(vin,CarStatus.SOLD_C_A.value());
        //除已卖状态外 有且只有一条数据
        if(carInfoDOS.size()>0){
            for (CarInfoDO carInfoDO:carInfoDOS) {
                if(carInfoDO.getStatus()==11){
//                  //明细表数据
                    CarInfoDetailsDO carInfoDetailsDO = carInfoDetailsService.getCarInfoDetailsByCarId(carInfoDO.getId());
                    //拼装已有数据进行回显
                    AppBpmCarInfoRespVO appBpmCarInfoRespVO = this.buildBmpVO(carInfoDO, carInfoDetailsDO);
                    map.put("1",appBpmCarInfoRespVO);
                }else {
                    map.put("2","该车辆已存在");
                }
                break;
            }
        }else{
            map.put("3","无草稿数据");
        }
        return map;
    }

    @Override
    public AppBpmCarInfoRespVO getCarInfoByID(Long id) {
        CarInfoDO carInfo = this.getCarInfo(id);

        CarInfoDetailsDO carInfoDetailsDO = carInfoDetailsService.getCarInfoDetailsByCarId(id);

       return this.buildBmpVO(carInfo, carInfoDetailsDO);

    }

    @Override
    public AppCarInfoCardRespVO getCardByID(Long id) {
        CarInfoDO carInfo = this.getCarInfo(id);
        if (ObjectUtil.isNull(carInfo)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        AdminUserDO adminUserDO = userMapper.selectById(carInfo.getCreator());
        carInfo.setCreator(adminUserDO.getNickname());
        CarInfoDetailsDO carInfoDetailsDO = carInfoDetailsService.getCarInfoDetailsByCarId(id);
        if (ObjectUtil.isNull(carInfoDetailsDO)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        List<ContractDO> contractDOS = contractService.getContractListByCarId(id);
        AppCarInfoCardRespVO appCarInfoCardRespVO = this.buildCardVO(carInfo, carInfoDetailsDO,contractDOS);
        return appCarInfoCardRespVO;
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
//        if (sellAmount.compareTo(carInfo.getVehicleReceiptAmount()) == -1) {
//            throw exception(CAR_INFO_SELL_AMOUNT_ERROR);
//        }
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
                if(null!=carInfo.getSellAmount()){
                    //税费 = 卖车金额 * 税率
                    BigDecimal vat = carInfo.getSellAmount().multiply(new BigDecimal(dto.getLabel()));
                    vo.setVat(vat);
                }
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
        if(null!=vo.getVat()){
            vo.setTotal(vo.getVat()
                    .add(vo.getOperation())
                    .add(vo.getTransferBuy())
                    .add(vo.getTransferSell()));
        }else{
            vo.setTotal(new BigDecimal(0));
        }

        //利润=卖车金额-收车金额-税-服务费
        //利润=卖车金额-收车金额-费用合计
        if(null!=vo.getSellAmount()){
            vo.setProfit(vo.getSellAmount()
                    .subtract(vo.getVehicleReceiptAmount())
                    .subtract(vo.getTotal()));
        }else{
            vo.setProfit(new BigDecimal(0));
        }
        return vo;
    }

    @Transactional
    @Override
    public AppBpmCarInfoRespVO saveSellCarInfo(AppSellCarInfoReqVO reqVO) {
        validateCarInfoExists(reqVO.getId());
        // 更新卖车填写数据
        CarInfoDO carInfo = carInfoMapper.selectById(reqVO.getId());
        carInfo.setRemarks( reqVO.getRemarks() );
        carInfo.setSellAmount( reqVO.getSellAmount() );
        carInfo.setSellType( reqVO.getSellType() );
        carInfo.setOther(reqVO.getOther());
        //此时状态为 买车中草稿
        carInfo.setSalesStatus(CarStatus.SELL.value());
        carInfo.setStatus(CarStatus.SELL_A.value());
        carInfo.setStatusThree(CarStatus.SELL_A_A.value());
        carInfoMapper.updateById(carInfo);
        CarInfoDetailsDO carInfoDetails = carInfoDetailsService.getCarInfoDetailsByCarId(reqVO.getId());
        carInfoDetails.setBuyerTel( reqVO.getBuyerTel() );
        carInfoDetails.setBuyerIdCard( reqVO.getBuyerIdCard() );
        carInfoDetails.setBuyerName( reqVO.getBuyerName() );
        carInfoDetails.setBuyerAdder( reqVO.getBuyerAdder() );
        carInfoDetails.setTransManageName( reqVO.getTransManageName() );
        carInfoDetails.setFeesAndCommitments( reqVO.getFeesAndCommitments() );
        carInfoDetails.setProceduresAndSpareSell(reqVO.getProceduresAndSpareSell());
        carInfoDetails.setVehicleProblem(reqVO.getVehicleProblem());
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
        if (ObjectUtil.isNull(carInfo)) {
            return vo;
        }
        List<FileRespDTO> fileList = businessFileService.getDTOByMainId(carInfo.getId());
        if (CollUtil.isNotEmpty(fileList)) {
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
        }

        return vo;
    }

    private AppCarInfoCardRespVO buildCardVO(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetails, List<ContractDO> contractList){
        AppCarInfoCardRespVO vo = new AppCarInfoCardRespVO();
        //车辆主表信息
        vo.setCarInfo(carInfo);
        //车辆明细表数据
        vo.setCarInfoDetails(carInfoDetails);
        //合同数据
        List<APPContractCardVO> list = new ArrayList<>();
        List<APPContractCardVO> listN = new ArrayList<>();

        for (ContractDO contractDO:contractList) {//循环合同信息，查询中间表拿到文件url
            List<FileRespDTO> fileList = businessFileService.getDTOByMainId(contractDO.getId());
            if(fileList.size()>0){
                if(contractDO.getStatus()==2){
                    APPContractCardVO contractCardVO = new APPContractCardVO();
                    FileRespDTO fileRespDTO = fileList.get(0);
                    contractCardVO.setContractDO(contractDO);
                    contractCardVO.setPath(fileRespDTO.getPath());
                    contractCardVO.setUrl(fileRespDTO.getUrl());
                    listN.add(contractCardVO);
                }else{
                    APPContractCardVO contractCardVO = new APPContractCardVO();
                    FileRespDTO fileRespDTO = fileList.get(0);
                    contractCardVO.setContractDO(contractDO);
                    contractCardVO.setPath(fileRespDTO.getPath());
                    contractCardVO.setUrl(fileRespDTO.getUrl());
                    list.add(contractCardVO);
                }

            }
        }
        vo.setContractCardVOS(list);
        vo.setContractCardNOS(listN);
        //资金信息
        CommonResult<List<DictDataRespDTO>> dictDataRes = dictDataApi.getDictDataList(DictTypeConstants.CAR_EXPENSE_CONFIG_DEFAULT, null);
        if (!dictDataRes.getCode().equals(0)) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        List<DictDataRespDTO> dictDataDTOList = dictDataRes.getData();
        if (CollUtil.isEmpty(dictDataDTOList)) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        AppCarInfoAmountRespVO calculation = calculation(carInfo, dictDataDTOList);
        calculation.setVehicleReceiptAmount(carInfo.getVehicleReceiptAmount());
        vo.setAppCarInfoAmountRespVO(calculation);

        //车辆图片相关数据
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
                case "14":
                    vo.getFileF().add(new AppSimpleFileVO(file));
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
        if (maps.size() == 12) {
            //这里以后增加了状态删除即可
            return maps;
        }
        //获取状态索引
        Map<Integer, Integer> mapIdx = new HashMap<>();
        for (int i = 0; i < maps.size(); i++) {
            mapIdx.put((Integer)maps.get(i).get("statusTwo"), i);
        }
        //填充未查询到的状态
        List<Map<String, Object>> mapList = Lists.newArrayList();
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 3; j++) {
                Map<String,Object> map = new HashMap<>();
                Integer statusTwo = 10 * i + j;
                if (mapIdx.containsKey(statusTwo)) {
                    Map<String, Object> dbMap = maps.get(mapIdx.get(statusTwo));
                    mapList.add(dbMap);
                } else {
                    map.put("statusOne", i);
                    map.put("statusTwo", statusTwo);
                    map.put("num", 0);
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }
    private List<HomeCountVO> buildHomeCountRespVO(List<Map<String, Object>> mapList){
        //按照第一级状态分组
        Map<Integer, List<Map<String, Object>>> mapGroup = mapList.stream().collect(Collectors.groupingBy(map -> (Integer) map.get("statusOne")));
        List<HomeCountVO> retList = Lists.newArrayList();
        mapGroup.keySet().forEach(key -> {
            HomeCountVO homeCount = new HomeCountVO();
            homeCount.setStatus(key);
            homeCount.setLabel(CarStatus.toType(key).text());
            Long num = 0L;
            List<Map<String, Object>> mapChildList = mapGroup.get(key);
            for (Map<String, Object> child : mapChildList) {
                HomeCountVO homeCountChild = new HomeCountVO();
                Integer statusTwo = (Integer) child.get("statusTwo");
                homeCountChild.setStatus(statusTwo);
                homeCountChild.setLabel(CarStatus.toType(statusTwo).text());
                Long numSon = Long.valueOf(child.get("num").toString());
                homeCountChild.setNum(numSon);
                homeCountChild.setChild(null);
                homeCount.getChild().add(homeCountChild);
                num += numSon;
            }
            homeCount.setNum(num);
            retList.add(homeCount);
        });
        return retList;
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
        List<AppContractarVO> contractInfo = contractService.getContractInfo(carDCVo.getId());
        /*List<AppContractarVO> contractInfo1 =new ArrayList<>();
        for (AppContractarVO appContractarVO : contractInfo) {
            contractInfo1.add(contractService.setContractUrl(appContractarVO));
        }*/
        carDetailRespVO.setContractarVO(contractInfo);

        // 资金信息
        carDetailRespVO.setCarCostVO(costService.getCarCosts(carDCVo.getId()));
        //发票信息
        List<AppCarInvoiceVo> invoicesInfosList= invoicesService.getInvoicesInfo(carDCVo.getId());
        /*List<AppCarInvoiceVo> invoicesInfos= new ArrayList<>();
        for (AppCarInvoiceVo appCarInvoiceVo : invoicesInfosList) {
            invoicesInfos.add(setInvoiceUrl(appCarInvoiceVo));
        }*/
        carDetailRespVO.setCarInvoiceVO(invoicesInfosList);

        return carDetailRespVO;
    }

    @Override
    public PicResp getCarDCDetails(String id) {
        return carInfoMapper.getCarDCDetails(id);
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
    public PeopleVo getPeopelInfo(String carID) {
        return carInfoMapper.getPeopelInfo(carID);
    }


    @Transactional
    @Override
    public void deleteSell(Long id) {
        this.validateCarInfoExists(id);
        CarInfoDO carInfo = this.getCarInfo(id);
        //判断第二级状态不为草稿状态，禁止删除
        if (!ObjectUtil.equals(31,carInfo.getStatus())) {
            throw exception(CAR_INFO_STATUS_ERROR);
        }
        carInfo.setRemarks(null);
        carInfo.setSellAmount(null);
        carInfo.setSellType(null);
        carInfo.setRemarks(null);
        //此时状态为 待售中-已检测
        carInfo.setSalesStatus(CarStatus.SALE.value());
        carInfo.setStatus(CarStatus.SALE_A_C.value());
        carInfo.setStatusThree(CarStatus.SALE_A_C_A.value());
        CarInfoDetailsDO carInfoDetails = carInfoDetailsService.getCarInfoDetailsByCarId(id);
        carInfoDetails.setBuyerAdder(null);
        carInfoDetails.setBuyerIdCard(null);
        carInfoDetails.setBuyerName(null);
        carInfoDetails.setBuyerTel(null);
        carInfoDetails.setTransManageName(null);
        carInfoDetails.setFeesAndCommitments(null);
        carInfoMapper.updateById(carInfo);
        carInfoDetailsService.updateCarInfoDetail(carInfoDetails);
    }

    @Override
    public CarTransferInfoVO getTransferInfo(Long carId, String procDefKey) {
        CarTransferInfoVO carTransferInfoVO = new CarTransferInfoVO();
        // 1.根据车辆ID获取车辆主信息
        CarInfoDO carInfoDO = carInfoMapper.selectById(carId);
        // 2.根据车辆ID获取车辆扩展子表明细信息
        CarInfoDetailsDO infoDetails = carInfoDetailsMapper.selectOne(CarInfoDetailsDO::getCarId, carId);
        carTransferInfoVO.setCarInfo(carInfoDO);
        carTransferInfoVO.setCarInfoDetails(infoDetails);
        // 3.处理车辆的图片信息
        this.buildBmpVO(carInfoDO,infoDetails);
        // 4.处理合同   合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
        if (ObjectUtil.equals("SCGH", procDefKey)) {
            // 收车过户
            List<ContractApprovalShowVO> contractList = com.google.common.collect.Lists.newArrayList();
            contractList.add(this.getContractApprovalShowInfo(carId, 1));
            contractList.add(this.getContractApprovalShowInfo(carId, 2));
            String contractCode = ObjectUtil.isNotNull(contractList.get(1).getContractId()) ? String.valueOf(contractList.get(1).getContractId()) : "";
            carTransferInfoVO.setContractCode(contractCode);
            carTransferInfoVO.setContractList(contractList);
        } else if (ObjectUtil.equals("MCGH", procDefKey)) {
            // 卖车过户
            List<ContractApprovalShowVO> contractList = com.google.common.collect.Lists.newArrayList();
            contractList.add(this.getContractApprovalShowInfo(carId, 3));
            contractList.add(this.getContractApprovalShowInfo(carId, 4));
            String contractCode = ObjectUtil.isNotNull(contractList.get(1).getContractId()) ? String.valueOf(contractList.get(1).getContractId()) : "";
            carTransferInfoVO.setContractCode(contractCode);
            carTransferInfoVO.setContractList(contractList);
        }

        return carTransferInfoVO;
    }

    /**
     * 根据车辆ID和合同类型查询合同附件信息
     * @param carId  车辆ID
     * @param contractType 合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
     * @return
     */
    private ContractApprovalShowVO getContractApprovalShowInfo(Long carId, Integer contractType) {
        ContractApprovalShowVO contractApprovalShowVO = new ContractApprovalShowVO();
        contractApprovalShowVO.setContractType(contractType);
        ContractDO contractDO = this.contractMapper.selectOne(ContractDO::getCarId, carId, ContractDO::getContractType, contractType);
        if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getContractId())) {
            return contractApprovalShowVO;
        }
        contractApprovalShowVO.setContractId(contractDO.getContractId());
        contractApprovalShowVO.setContractName(contractDO.getContractName());
        List<FileRespDTO> fileList = businessFileService.getDTOByMainId(contractDO.getContractId());
        if (!CollectionUtils.isEmpty(fileList)) {
            FileRespDTO fileRespDTO = fileList.get(0);
            contractApprovalShowVO.setContractFileId(String.valueOf(fileRespDTO.getId()));
            contractApprovalShowVO.setContractFilePath(fileRespDTO.getPath());
            contractApprovalShowVO.setContractFileUrl(fileRespDTO.getUrl());
        }

        return contractApprovalShowVO;
    }


    /**
     * 获取汽车驾驶证行驶证图片
     */
    private PicResp getPics( CarDCVo carDCVo) {
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






}
