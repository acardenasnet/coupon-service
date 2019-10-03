package com.dou.javadevdaymx2019.couponservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
public class CouponController {

  @Value("${coupon.code}")
  private String couponCode;

  @GetMapping("/{coupon}")
  public Double getDisscount(@PathVariable String coupon) {
    log.info("Apply coupon : {}, coupon code is: {}",  coupon, couponCode);
    return coupon.equals(couponCode) ? 20D : 0D;
  }

}
