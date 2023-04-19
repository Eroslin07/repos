package com.newtouch.uctp.module.business.controller.app.carInfo;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.convert.carInfo.CarInfoConvert;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.business.service.ContractService;
import com.newtouch.uctp.module.business.util.DownLoadUtils;
import com.newtouch.uctp.module.business.util.UctpCarInfoSearchUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "App管理 - 车辆管理")
@RestController
@RequestMapping("/uctp/car-info")
@Validated
public class AppCarInfoController {
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private ContractService contractService;



    @PostMapping("/create")
    @Operation(summary = "创建车辆主表")
    public CommonResult<Long> createCarInfo(@Valid @RequestBody AppCarInfoCreateReqVO createReqVO) {
        return success(carInfoService.createCarInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新车辆主表")
    public CommonResult<Boolean> updateCarInfo(@Valid @RequestBody AppCarInfoUpdateReqVO updateReqVO) {
        carInfoService.updateCarInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除车辆主表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteCarInfo(@RequestParam("id") Long id) {
        carInfoService.deleteCarInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得车辆主表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCarInfoRespVO> getCarInfo(@RequestParam("id") Long id) {
        CarInfoDO carInfo = carInfoService.getCarInfo(id);
        return success(CarInfoConvert.INSTANCE.convert(carInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得车辆主表列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024")
    public CommonResult<List<AppCarInfoRespVO>> getCarInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<CarInfoDO> list = carInfoService.getCarInfoList(ids);
        return success(CarInfoConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/page")
    @Operation(summary = "获得车辆主表分页")
    public CommonResult<PageResult<AppCarInfoRespVO>> getCarInfoPage(@RequestBody @Valid AppCarInfoPageReqVO pageVO) {
        PageResult<CarInfoDO> pageResult = carInfoService.getCarInfoPage(pageVO);
        return success(CarInfoConvert.INSTANCE.convertPage(pageResult));
    }
    @GetMapping("/home/page")
    @Operation(summary = "获得APP首页分页")
    public CommonResult<PageResult<AppHomeCarInfoRespVO>> getHomeCarInfoPage(@Valid AppHomeCarInfoPageReqVO pageVO) {
        PageResult<AppHomeCarInfoRespVO> pageResult = carInfoService.getHomeCarInfoPage(pageVO);
        return success(pageResult);

    }

    @GetMapping("/home/count")
    @Operation(summary = "获得APP首页统计")
    public CommonResult<List<HomeCountVO>> getCarCountGroupByStatus() {
        List<HomeCountVO> list = carInfoService.getCarCountGroupByStatus();
        return success(list);
    }

    @GetMapping("/sell/page")
    @Operation(summary = "获得APP卖车分页")
    public CommonResult<PageResult<AppSellCarInfoPageRespVO>> getSellCarInfoPage(@Valid AppSellCarInfoPageReqVO pageVO) {
        PageResult<AppSellCarInfoPageRespVO> pageResult = carInfoService.getSellCarInfoPage(pageVO);
        System.out.println(pageResult);
        return success(pageResult);
    }

    @GetMapping("/get/sell")
    @Operation(summary = "获得APP卖车详情页")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppSellCarInfoRespVO> getSellCarInfo(@RequestParam("id") Long id) {
        return success(carInfoService.getSellCarInfo(id));
    }

    @GetMapping("/amount")
    @Operation(summary = "获得APP卖车详情页中车辆明细金额数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @Parameter(name = "sellAmount", description = "卖车金额", required = true, example = "666")
    public CommonResult<AppCarInfoAmountRespVO> getCarInfoAmount(@RequestParam("id") Long id,
                                                                 @RequestParam("sellAmount") BigDecimal sellAmount) {
        AppCarInfoAmountRespVO vo = carInfoService.getCarInfoAmount(id,sellAmount);
        return success(vo);
    }

    @PostMapping("/save/sell")
    @Operation(summary = "保存APP卖车填写数据")
    public CommonResult<AppBpmCarInfoRespVO> saveSellCarInfo(@Valid @RequestBody AppSellCarInfoReqVO reqVO) {
        carInfoService.saveSellCarInfo(reqVO);
        return success(carInfoService.saveSellCarInfo(reqVO));
    }

    @GetMapping("/delete/sell/{id}")
    @Operation(summary = "删除卖车的草稿数据")
    public CommonResult<Boolean> deleteSell(@PathVariable @NotNull(message = "请传入Id") Long id) {
        carInfoService.deleteSell(id);
        return success(true);
    }

    @PostMapping("/insertCarInfo")
    @Operation(summary = "新增车辆信息")
    public CommonResult<AppBpmCarInfoRespVO> insertCarInfo(@Valid @RequestBody AppCarInfoCreateReqVO createReqVO) {
        return success(carInfoService.insertCarInfo(createReqVO));
    }

    @PostMapping("/insertSellerInfo")
    @Operation(summary = "新增卖家信息")
    public CommonResult<AppBpmCarInfoRespVO> insertSellerInfo(@Valid @RequestBody AppSellerInfoReqVO reqVO) {
        return success(carInfoService.insertSellerInfo(reqVO));
    }


    @GetMapping("/getCarInfoByVIN")
    @Operation(summary = "根据VIN获取回显车辆信息")
    @Parameter(name = "VIN", description = "编号", required = true, example = "1024")
    public CommonResult<Map> getCarInfoByVIN(@RequestParam("VIN") String VIN) {
        return success(carInfoService.getCarInfoByVIN(VIN));
    }


    @DeleteMapping("/delCarInfoWithCollect")
    @Operation(summary = "收车删除车辆信息")
    @Parameter(name = "id", description = "编号", required = true, example = "150")
    public CommonResult<Boolean> delCarInfoWithCollect(@RequestParam("id") Long id) {
        carInfoService.delCarInfoWithCollect(id);
        return success(true);
    }



    @GetMapping("/getDetailds")
    @Operation(summary = "获得车辆明细信息")
    public CommonResult<CarDetailRespVO> getCarInfoAndDetails(@Valid CarDCVo carDCVo) {
            CarDetailRespVO pageResult = carInfoService.getCarInfoAndDetails(carDCVo);
            return success(pageResult);
    }




    @PostMapping("/updateContractStatas")
    @Operation(summary = "作废合同状态")
    public CommonResult<String> updateContractStatas(@RequestBody  CarDCVo carDCVo) {
        return success(contractService.updateContractStatas(carDCVo));
    }


    @PostMapping("/downloadFile")
    @Operation(summary = "通过路径下载单个文件/多文件下载需前端轮询")
    public void downLoadone(@RequestBody DownloadExample example, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = DownLoadUtils.getResourceFile(example.getUrl());
        DownLoadUtils.outFileByFile(example.getName()+"."+example.getType(),file,false,request,response);
    }
/*
    @PostMapping ("/download-more")
    @Operation(summary = "多文件下载")
    public void downLoadmore(@RequestBody DownloadMoreExample example, HttpServletRequest request, HttpServletResponse response) throws IOException {
       // String url="http://61.172.179.54:9000/uctp-cloud/9be70b12034965ccfeabfbd36965720787ceca730afaa2ae915ef7e6ffb1f850.jpg";
        for (DownloadExample exampleExample : example.getExamples()) {
            File file = DownLoadUtils.getResourceFile(exampleExample.getUrl());
            DownLoadUtils.outFileByFile(exampleExample.getName()+"."+exampleExample.getType(),file,false,request,response);
        }
    }




    @GetMapping("/getCarCosts/{id}")
    @Operation(summary = "获得资金信息")
    public CommonResult<AppCarCostVO> getCarCosts(@PathVariable String id) {
        AppCarCostVO pageResult = carInfoService.getCarCosts(id);
        return success(pageResult);
    }

    @GetMapping("/getContractInfo")
    @Operation(summary = "获得合同主表信息")
    public CommonResult<List<AppContractarVO>> getContractInfo(@RequestParam("carID")  String carID) {
        List<AppContractarVO> pageResult = carInfoService.getContractInfo(carID);
        List<AppContractarVO> pageResult1 =null;
        for (AppContractarVO appContractarVO : pageResult) {
            pageResult1.add(setContractUrl(appContractarVO));
        }

        return success(pageResult1);
    }

    @GetMapping("/getPeopleInfo")
    @Operation(summary = "卖家/买家信息查询")
    public CommonResult<PeopleVo> getPeopleInfo(@RequestParam("carID")  String carID) {
        PeopleVo pageResult = carInfoService.getPeopelInfo(carID);
        return success(pageResult);
    }



    @GetMapping("/getDetailds1")
    @Operation(summary = "获得车辆明细信息")
    public CommonResult<AppCarInfoAndDetailVO> getCarInfoAndDetails1(@Valid CarDCVo carDCVo) {


        try {

            //CarDCVo carDC = carInfoService.getCarDC("1644597512931184642");
            List<CarDCVo> carIds = carInfoService.getCarIds(carDCVo.getId().toString());
            List<Long> cars = new ArrayList<>();

            for (CarDCVo carId : carIds) {
                cars.add(carId.getLongId());
            }

            CommonResult<List<FileRespDTO>> listcarIds = fileApi.fileList(cars);
            //车辆图片
            List<String> carPics = new ArrayList<>();
            for (FileRespDTO datum : listcarIds.getData()) {
                carPics.add(datum.getUrl());
            }
            AppCarInfoAndDetailVO pageResult = carInfoService.getCarInfoAndDetail(carDCVo.getId().toString());
            pageResult.setCarPic(carPics);
            return success(pageResult);
        }catch (NullPointerException e){
            e.printStackTrace();

        }
        return error(500,"服务器内部错误，请联系管理员处理");
    }

    @GetMapping("/getPics")
    @Operation(summary = "获得车辆相关图片信息")
    public CommonResult<PicResp>  getPics(@Valid CarDCVo carDCVo) {
        //可以传car_id 和传行驶证，驾驶证号
        CarDCVo carDC =carInfoService.getCarDC(carDCVo.getCertificateNo());
        List<CarDCVo>  certificatePic=carInfoService.getCertificateIds(carDC.getCertificateNo());
        List<CarDCVo> drivingPic =carInfoService.getDrivingLicenseIds(carDC.getDrivingLicense());
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

        for (FileRespDTO datum : listCertificates.getData()) {
            certificatePics.add(datum.getUrl());
        }

        for (FileRespDTO datum : listDrivers.getData()) {
            drivingPics.add(datum.getUrl());
        }

        PicResp picResp =carInfoService.getCarDCDetails(carDCVo.getId().toString());
        picResp.setDrivingPics(drivingPics);
        picResp.setCertificatePics(certificatePics);
        return success(picResp);
    }

    //将合同的url放到实体中

    private AppContractarVO setContractUrl(AppContractarVO appContractarVO){

        CommonResult<List<FileRespDTO>> listCertificates =null;

        List<Long> contractList=new ArrayList<>();
        List<CarDCVo> contractIds= carInfoService.getContractIds(appContractarVO.getContractID()) ;//一条合同数据的ids;正常情况一个合同只会有一个pdf文件
        for (CarDCVo contractId : contractIds) {
            contractList.add(contractId.getLongId());
        }
        listCertificates= fileApi.fileList(contractList);
        for (FileRespDTO datum : listCertificates.getData()) {

            appContractarVO.setUrl(datum.getUrl());
        }
        return appContractarVO;
    }
*/

    @PostMapping("/getCarBrandList")
    @PermitAll
    @Operation(summary = "品牌查询")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public Map getCarBrandList(@RequestBody Map map) {
        String brand_name = String.valueOf(map.get("brand_name"));//品牌
        Map SeriesMap=new HashMap<>();
        try {
            String url = "http://testapi.che300.com/service/getCarBrandList";
            String token = "61f499b086392005f92009b91f8f966a";
            SeriesMap = UctpCarInfoSearchUtils.getCarBrandList(token, brand_name, url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SeriesMap;
    }


    @PostMapping("/getCarSeriesList")
    @PermitAll
    @Operation(summary = "查询车系")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public Map getCarSeriesList(@RequestBody Map map) {
        String brand_id = String.valueOf(map.get("brand_id"));//品牌id
        Map SeriesMap=new HashMap<>();
        try {
            String url = "http://testapi.che300.com/service/getCarSeriesList";
            String token = "61f499b086392005f92009b91f8f966a";
            SeriesMap = UctpCarInfoSearchUtils.getCarSeriesList(token, brand_id, url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SeriesMap;
    }


    @PostMapping("/getCarModelList")
    @PermitAll
    @Operation(summary = "查询车型")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public Map getCarModelList(@RequestBody Map map) {
        String seriesId = String.valueOf(map.get("seriesId"));//车系
        Map SeriesMap=new HashMap<>();
        try {
            String url = "http://testapi.che300.com/service/getCarModelList";
            String token = "61f499b086392005f92009b91f8f966a";
            SeriesMap = UctpCarInfoSearchUtils.getCarModelList(token, seriesId, url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SeriesMap;
    }


    @PostMapping("/searchFairValue")
    @PermitAll
    @Operation(summary = "查询公允价值")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public HashMap searchFairValue(@RequestBody Map map) {
//        String vehicleReceiptAmount = String.valueOf(map.get("vehicleReceiptAmount"));//收车金额
//        String carId = String.valueOf(map.get("carId"));
//
//        CarInfoDO carInfoDO = carInfoService.selectCarInfoByID(carId);
//        CarInfoDetailsDO carInfoDetailsDO = carInfoService.seleCarInfoDetail(carId);
        HashMap fairValue = null;
        try {
            String token = "61f499b086392005f92009b91f8f966a";
            String modelId = String.valueOf(map.get("modelId"));//车型id
//            String zone =carInfoDO.getPlateNum();//车牌
            String zone = String.valueOf(map.get("plateNum"));//车牌

//            String mile= String.valueOf(carInfoDetailsDO.getMileage());//里程
            String mile= String.valueOf(map.get("mileage"));//里程
//            String regDate= String.valueOf(carInfoDetailsDO.getFirstRegistDate());//首次登记时间
            String regDate= String.valueOf(map.get("firstRegistDate"));//首次登记时间
            String allLevel="1";//(是否返回多⻋况,1是，0:否) 默认1

            String url1 = "http://testapi.che300.com/service/getUsedCarPrice";
            String coefficients="0.2";


            fairValue = UctpCarInfoSearchUtils.CarFairValue(modelId, zone, mile, regDate, allLevel, token, url1, coefficients);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fairValue;
    }



//    static Map<String,Long> map=new HashMap<>();
//    static Map<String,String> map1=new HashMap<>();

//    @GetMapping("/qiyuesuo")
//    @Operation(summary = "发起合同")
//    public CommonResult<String> qiyuesuo() throws Exception {
//        System.out.println("1111");
//        String url = "https://openapi.qiyuesuo.cn";
//        String accessKey ="q4xKsNcFI8";
//        String accessSecret = "qKPK101VGyLsnSqFoLzSCu3JGiMAVO";
//        QiYueSuoUtil qiYueSuoUtil = new QiYueSuoUtil(url, accessKey, accessSecret);
//        try {
//            SdkResponse<Contract> response = qiYueSuoUtil.signSealDraft("二手车","成都新致云服测试公司","罗一秀","13708206115","罗聪","17396202169");
//            Contract result = response.getResult();
//            Long id = result.getId();
//            File file = new File("/Users/qinjingjing/app/用户协议.docx");
//            qiYueSuoUtil.addDocumentByFile(id, file, "用户协议","docx");
//            qiYueSuoUtil.send(id);
//            System.out.println(id);
//            map.put("id",id);
//            SdkResponse<ContractPageResult> pageResponse = qiYueSuoUtil.gerenateSignUrl(id, "17396202169");
//            ContractPageResult pageResult = pageResponse.getResult();
//            String pageUrl = pageResult.getPageUrl();
//            System.out.println(pageUrl);
//            map1.put("url",pageUrl);
//            return success(pageUrl);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//    @PutMapping("/cancelContract")
//    @Operation(summary = "作废合同")
//    public CommonResult<String> cancelContract(@Valid @RequestBody UserProfileUpdateReqVO reqVO) throws Exception {
//        System.out.println("1111");
//        String url = "https://openapi.qiyuesuo.cn";
//        String accessKey ="q4xKsNcFI8";
//        String accessSecret = "qKPK101VGyLsnSqFoLzSCu3JGiMAVO";
//        QiYueSuoUtil qiYueSuoUtil = new QiYueSuoUtil(url, accessKey, accessSecret);
//        Long id = map.get("id");
//        String pageUrl1 = map1.get("url");
//        qiYueSuoUtil.withdrawOrVoidContract(id,"请求作废合同");
////        String pageUrl1 ="https://cloud.qiyuesuo.cn/contract/sign/3078989971495846817?hide_menu=true&ignoreClientRedirect=true&isInline=true";
//        return success(pageUrl1);
//    }


}
