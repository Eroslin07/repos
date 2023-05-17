package com.newtouch.uctp.module.business.service.impl;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.pos.vo.AddPosReqVO;
import com.newtouch.uctp.module.business.controller.app.pos.vo.PosRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.PosDO;
import com.newtouch.uctp.module.business.dal.mysql.CarInfoMapper;
import com.newtouch.uctp.module.business.dal.mysql.PosMapper;
import com.newtouch.uctp.module.business.service.PosService;
import com.newtouch.uctp.module.system.api.user.dto.AddAccountDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.system.enums.ErrorCodeConstants.*;

/**
 * pos机管理 Service 实现类
 *
 * @author qjj
 */
@Service
@Validated
@Slf4j
public class PosServiceImpl implements PosService {
    @Autowired
    private PosMapper posMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;


    @Override
    public void addPos(AddPosReqVO reqVO) {
        PosDO posDO = new PosDO();
        HashMap<Object, Object> map = new HashMap<>();
        if(null==reqVO.getId()){
            List<PosDO> posDOS = posMapper.selectByPosId(reqVO.getPosId());
            if(posDOS.size()>0){
                throw exception(AUTH_POS_IS_EXIST);
            }else{
                try {
                    posDO.setPosName(reqVO.getPosName());
                    posDO.setPosId(reqVO.getPosId());
                    posDO.setRemark(reqVO.getRemark());
                    posDO.setStatus(reqVO.getStatus());
                    posDO.setBusinessId(reqVO.getDeptId());
                    posDO.setTenantId(reqVO.getTenantId());
                    posMapper.insert(posDO);
                }catch (Exception e){
                    throw exception(AUTH_ADDPOS_ERROR);
                }
            }
        }else{
            List<PosDO> posDOS = posMapper.selectByPosId(reqVO.getPosId());
            if(posDOS.size()>0){
                for(PosDO pos:posDOS){
                    if(!pos.getId().equals(pos.getId())){
                        throw exception(AUTH_POS_IS_EXIST);
                    }
                }
            }
//            List<CarInfoDO> carInfoDOS = carInfoMapper.selectByPosId(reqVO.getPosId());
//            if(carInfoDOS.size()>0){
//                throw exception(AUTH_POS_IS_USED);
//            }
            try {
                PosDO posDO1 = posMapper.selectOne("id", reqVO.getId());
                posDO1.setPosName(reqVO.getPosName());
                posDO1.setPosId(reqVO.getPosId());
                posDO1.setRemark(reqVO.getRemark());
                posDO1.setStatus(reqVO.getStatus());
                posMapper.updateById(posDO1);
            }catch (Exception e){
                throw exception(AUTH_UPDATEPOS_ERROR);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePos(Long id) {
           return posMapper.deleteById(id);
    }

    @Override
    public List<PosDO> getPosList(Long deptId) {
        return posMapper.selectByBusinessId(deptId);
    }

    @Override
    public List<PosDO> getPosListDrop(Long deptId) {
        return posMapper.selectByBusinessIdAndStatus(deptId);
    }

}


