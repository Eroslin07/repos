package com.newtouch.uctp.module.business.dal.dataobject.account;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

@TableName("uctp_merchant_pos_device")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPOSDeviceDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * POS机名称
     */
    private String posName;

    /**
     * POS机授权码
     */
    private String authCode;

    /**
     * POS机设备号
     */
    private String deviceSn;

    /**
     * POS机绑定状态
     */
    private String state;

    /**
     * 商户虚拟账户号
     */
    private String accountNo;

    /**
     * 平台商户号
     */
    private Long merchantId;

    private Integer revision;//乐观锁

    public enum DeviceState {
        BINDING("1", "已绑定设备"),
        UNBOUND("0", "未绑定设备");

        private String code;
        private String value;

        DeviceState(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
