package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.pos.vo.AddPosReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.PosDO;

import java.util.List;

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
