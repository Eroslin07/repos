package com.newtouch.uctp.module.business.controller.app.carinfodetails;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name =  "App管理 - 车辆明细")
@RestController
@RequestMapping("/uctp/car-info-details")
@Validated
public class CarInfoDetailsController {
}
