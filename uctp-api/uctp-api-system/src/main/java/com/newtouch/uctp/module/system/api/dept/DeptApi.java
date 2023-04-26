package com.newtouch.uctp.module.system.api.dept;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.util.collection.CollectionUtils;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 部门")
public interface DeptApi {

    String PREFIX = ApiConstants.PREFIX + "/dept";

    @GetMapping(PREFIX + "/get")
    @Operation(summary = "获得部门信息")
    @Parameter(name = "id", description = "部门编号", example = "1024", required = true)
    CommonResult<DeptRespDTO> getDept(@RequestParam("id") Long id);

    @GetMapping(PREFIX + "/list")
    @Operation(summary = "获得部门信息数组")
    @Parameter(name = "ids", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<DeptRespDTO>> getDeptList(@RequestParam("ids") Collection<Long> ids);

    @GetMapping(PREFIX + "/valid")
    @Operation(summary = "校验部门是否合法")
    @Parameter(name = "ids", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<Boolean> validateDeptList(@RequestParam("ids") Collection<Long> ids);

    @GetMapping("/getByName")
    @Operation(summary = "根据部门名称获得部门信息")
    @Parameter(name = "name", description = "部门名称", required = true, example = "万国二手车")
    public CommonResult<DeptRespDTO> getDeptByName(@RequestParam("name") String name);

    /**
     * 获得指定编号的部门 Map
     *
     * @param ids 部门编号数组
     * @return 部门 Map
     */
    default Map<Long, DeptRespDTO> getDeptMap(Set<Long> ids) {
        return CollectionUtils.convertMap(getDeptList(ids).getCheckedData(), DeptRespDTO::getId);
    }

    @GetMapping("/get/userId")
    @Operation(summary = "根据部门名称获得部门信息")
    @Parameter(name = "userId", description = "用户Id", required = true, example = "1024")
    public CommonResult<DeptRespDTO> getDeptByUserId(@RequestParam("name") Long userId);
}
