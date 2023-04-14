package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarCostVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 车辆主表 Mapper
 *
 * @author lc
 */
@Mapper
public interface CarInfoMapper extends BaseMapperX<CarInfoDO> {

    default PageResult<CarInfoDO> selectPage(AppCarInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CarInfoDO>()
                .likeIfPresent(CarInfoDO::getVin, reqVO.getVin())
                .likeIfPresent(CarInfoDO::getBrand, reqVO.getBrand())
                .eqIfPresent(CarInfoDO::getSalesStatus, reqVO.getSalesStatus())
                .eqIfPresent(CarInfoDO::getCheckStatus, reqVO.getCheckStatus())
                .orderByDesc(CarInfoDO::getCreateTime));
    }

//    Page<AppHomeCarInfoRespVO> selectAppHomePage(@Param("pageVO") AppHomeCarInfoPageReqVO pageVO);
    Page<AppHomeCarInfoRespVO> selectAppHomePage(@Param("pg") Page<AppHomeCarInfoRespVO> page,
                                                 @Param("pageVO") AppHomeCarInfoPageReqVO pageVO);

    default List<Map<String, Object>> selectCarCountGroupByStatus(){
        return selectMaps(new QueryWrapper<CarInfoDO>()
                .select("SALES_STATUS statusOne","STATUS statusTwo","count(*) num")
                .groupBy("SALES_STATUS","STATUS"));
    }

    Page<AppSellCarInfoPageRespVO> selectAppCellCarPage(@Param("pg") Page<AppSellCarInfoPageRespVO> page,@Param("pageVO")AppSellCarInfoPageReqVO pageVO);

    AppCarInfoAndDetailVO getCarInfoAndDetails(String id);

    PicResp getCarDCDetails(String id);


    //AppCarCostVO getCarCosts(String id);


    //List<AppContractarVO> getContractInfo(String carID);

    //List<AppCarInvoiceVo> getInvoicesInfo(String carID);

    CarDCVo getCarDC(String carID);
    List<CarDCVo> getCarIds(String carID);
    List<CarDCVo> getDrivingLicenseIds(String carID);
    List<CarDCVo> getCertificateIds(String carID);

   // List<CarDCVo> getContractIds(String contractID);

    //int updateContractStatas(@Param("vo") CarDCVo carDCVo);

    PeopleVo getPeopelInfo(String carID);

   // List<CarDCVo> getInvoiceIds(String id);

    default CarInfoDO selectByVin(String vin,String status){
        return selectOne(CarInfoDO::getVin,vin,CarInfoDO::getStatus,status);
    };

    default  List<CarInfoDO> selectIsSell(String vin,Integer statusThree){
        return selectList(new LambdaQueryWrapperX<CarInfoDO>()
                .likeIfPresent(CarInfoDO::getVin,vin)
                .ne(CarInfoDO::getStatusThree, statusThree)
        );
    };

    default List<CarInfoDO> selectIsExist(String vin, Integer salesStatus,Integer status){//salesStatus一级状态  status二级状态
        return selectList(new LambdaQueryWrapperX<CarInfoDO>()
                .likeIfPresent(CarInfoDO::getVin,vin)
                .eqIfPresent(CarInfoDO::getSalesStatus, salesStatus)
                .eqIfPresent(CarInfoDO::getStatus, status)
        );
    }
}
