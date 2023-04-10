package com.newtouch.uctp.module.business.controller.app.carInfo;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.convert.app.CarInfoConvert;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.business.util.DownLoadUtils;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.error;
import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "App管理 - 车辆管理")
@RestController
@RequestMapping("/uctp/car-info")
@Validated
public class AppCarInfoController {
    @Resource
    private CarInfoService carInfoService;

    @Resource
    private FileApi fileApi;

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

    @GetMapping("/page")
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
    public CommonResult<List<Map<String, Object>>> getCarCountGroupByStatus() {
        List<Map<String, Object>> list = carInfoService.getCarCountGroupByStatus();
        return success(list);
    }

    @GetMapping("/sell/page")
    @Operation(summary = "获得APP卖车分页")
    public CommonResult<PageResult<AppSellCarInfoPageRespVO>> getSellCarInfoPage(@Valid AppSellCarInfoPageReqVO pageVO) {
        PageResult<AppSellCarInfoPageRespVO> pageResult = carInfoService.getSellCarInfoPage(pageVO);
        return success(pageResult);
    }


    @PostMapping("/insertCarInfo")
    @Operation(summary = "新增车辆信息")
    public CommonResult<String> insertCarInfo(@Valid @RequestBody AppCarInfoCreateReqVO createReqVO) {
        return success(carInfoService.insertCarInfo(createReqVO));
    }

    @PostMapping("/insertSellerInfo")
    @Operation(summary = "新增卖家信息")
    public CommonResult<String> insertSellerInfo(@Valid @RequestBody AppSellerInfoReqVO reqVO) {
        return success(carInfoService.insertSellerInfo(reqVO));
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
    public CommonResult<Boolean> saveSellCarInfo(@Valid @RequestBody AppSellCarInfoReqVO reqVO) {
        carInfoService.saveSellCarInfo(reqVO);
        return success(true);
    }

    @GetMapping("/getDetailds")
    @Operation(summary = "获得车辆明细信息")
    public CommonResult<AppCarInfoAndDetailVO> getCarInfoAndDetails(@Valid CarDCVo carDCVo) {


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
            AppCarInfoAndDetailVO pageResult = carInfoService.getCarInfoAndDetails(carDCVo.getId().toString());
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

    @PostMapping("/updateContractStatas")
    @Operation(summary = "作废合同状态")
    public CommonResult<String> updateContractStatas(@RequestBody  CarDCVo carDCVo) {
        return success(carInfoService.updateContractStatas(carDCVo));
    }

    @GetMapping("/downLoadContract/{filePath}")
    @Operation(summary = "通过路径下载合同")
    //public void downLoadContract(@PathVariable String filePath,@PathVariable String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException {
    public void downLoadContract(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        List<Map<String ,String>> list=new ArrayList<>();
//        for (Map<String, String> map : list) {
//            File file =new File(map.get("filePath"));
        //File file = DownLoadUtils.getResourceFile(map.get("filePath"));
//            DownLoadUtils.outFileByFile(map.get("fileName")+".pdf",file,false,request,response);
//        }
        String fName="/Users/huangr/newtouch/测试.pdf";
        File tempFile =new File( fName.trim());
        //File file = DownLoadUtils.getResourceFile("/Users/huangr/newtouch/测试.pdf");
        DownLoadUtils.outFileByFile("测试.pdf",tempFile,false,request,response);

    }

    @PostMapping("/download-one")
    @Operation(summary = "通过路径下载单个文件")
    public void downLoadone(@RequestBody DownloadExample example, HttpServletRequest request, HttpServletResponse response) throws IOException {
       // String url="http://61.172.179.54:9000/uctp-cloud/9be70b12034965ccfeabfbd36965720787ceca730afaa2ae915ef7e6ffb1f850.jpg";
        File file = DownLoadUtils.getResourceFile(example.getUrl());
        DownLoadUtils.outFileByFile(example.getName()+"."+example.getType(),file,false,request,response);
        //DownLoadUtils.outFileByFile("测试",file,false,request,response);

    }
    @PostMapping ("/download-more")
    @Operation(summary = "多文件下载")
    public void downLoadmore(@RequestBody DownloadMoreExample example, HttpServletRequest request, HttpServletResponse response) throws IOException {
       // String url="http://61.172.179.54:9000/uctp-cloud/9be70b12034965ccfeabfbd36965720787ceca730afaa2ae915ef7e6ffb1f850.jpg";
        for (DownloadExample exampleExample : example.getExamples()) {
            File file = DownLoadUtils.getResourceFile(exampleExample.getUrl());
            DownLoadUtils.outFileByFile(exampleExample.getName()+"."+exampleExample.getType(),file,false,request,response);
        }

        //DownLoadUtils.outFileByFile("测试",file,false,request,response);

    }

    @GetMapping("/getPeopleInfo")
    @Operation(summary = "卖家/买家信息查询")
    public CommonResult<PeopleVo> getPeopleInfo(@RequestParam("carID")  String carID) {
        PeopleVo pageResult = carInfoService.getPeopelInfo(carID);
        return success(pageResult);
    }



    /**
     * 将合同的url放到实体中
     */
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




}
