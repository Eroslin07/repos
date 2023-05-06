package com.newtouch.uctp.module.system.api.dept;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.convert.dept.DeptConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.service.dept.DeptService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class DeptApiImpl implements DeptApi {

    @Resource
    private DeptService deptService;
    @Override
    public CommonResult<DeptRespDTO> getDept(Long id) {
        DeptDO dept = deptService.getDept(id);
        return success(DeptConvert.INSTANCE.convert03(dept));
    }

    @Override
    public CommonResult<List<DeptRespDTO>> getDeptList(Collection<Long> ids) {
        List<DeptDO> depts = deptService.getDeptList(ids);
        return success(DeptConvert.INSTANCE.convertList03(depts));
    }

    @Override
    public CommonResult<Boolean> validateDeptList(Collection<Long> ids) {
        deptService.validateDeptList(ids);
        return success(true);
    }

    @Override
    public CommonResult<DeptRespDTO> getDeptByName(String name) {
        DeptDO dept = deptService.getDeptByName(name);
        return success(DeptConvert.INSTANCE.convert03(dept));
    }

    @Override
    public CommonResult<DeptRespDTO> getDeptByUserId(Long userId) {
        DeptDO dept = deptService.getDeptByUserId(userId);
        return success(DeptConvert.INSTANCE.convert03(dept));
    }

    @Override
    public CommonResult<DeptRespDTO> getPlatformDept() {
        DeptDO dept = deptService.getPlatformDept();
        return success(DeptConvert.INSTANCE.convert03(dept));
    }

}
