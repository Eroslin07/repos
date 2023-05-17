package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.pos.vo.AddPosReqVO;
import com.newtouch.uctp.module.business.controller.app.pos.vo.PosRespVO;
import com.newtouch.uctp.module.business.convert.app.POSDevicesConvert;
import com.newtouch.uctp.module.business.dal.dataobject.PosDO;
import com.newtouch.uctp.module.system.api.user.dto.AddAccountDTO;

import java.util.List;
import java.util.Map;

/**
 *
 * @author qjj
 */
public interface PosService {


    void addPos(AddPosReqVO reqVO);


    int deletePos(Long id);

    List<PosDO> getPosList(Long deptId);

    List<PosDO> getPosListDrop(Long deptId);
}
