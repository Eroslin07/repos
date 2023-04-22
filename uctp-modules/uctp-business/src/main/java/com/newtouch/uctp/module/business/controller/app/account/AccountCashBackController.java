package com.newtouch.uctp.module.business.controller.app.account;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商户待回填保证金")
@RestController
@RequestMapping("/uctp/account/cashback")
@Validated
@Slf4j
public class AccountCashBackController {

}
