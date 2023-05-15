package com.newtouch.uctp.module.bpm.service.payfailed;

import java.util.Map;

/**
 * @author helong
 * @date 2023/4/28 18:24
 */
public interface PayFailedService {
    /**
     * 发起支付失败流程
     * @param contractId  支付侧合同号ID（对应契约锁的合同ID）
     * @param procDefKey  流程定义标识（对应单据编号规则前四位）
     * @param variables   流程变量
     * @return
     */
    String createBpm(Long contractId, String procDefKey, Map<String, Object> variables);

    String createBpm(Long contractId, String failReason);
}
