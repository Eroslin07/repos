package com.newtouch.uctp.module.system.job.demo;

import com.newtouch.uctp.framework.tenant.core.job.TenantJob;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
@TenantJob
public class DemoJob {

    @XxlJob("demoJob")
    public void execute() {
        System.out.println("美滋滋");
    }

}
