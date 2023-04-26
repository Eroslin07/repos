package com.newtouch.uctp.module.bpm.service.openinvoice;

/**
 * @author helong
 * @date 2023/4/26 11:05
 */
public interface BpmOpenInvoiceService {
    String createOpenInvoiceBpm(Long contractId, String procDefKey);
}
