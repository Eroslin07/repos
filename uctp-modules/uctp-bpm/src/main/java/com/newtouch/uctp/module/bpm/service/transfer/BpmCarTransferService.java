package com.newtouch.uctp.module.bpm.service.transfer;

/**
 * @author helong
 * @date 2023/4/24 15:04
 */
public interface BpmCarTransferService {
    String createTransferBpm(Long carId, String procDefKey);
}
