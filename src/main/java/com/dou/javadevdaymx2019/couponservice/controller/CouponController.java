package com.dou.javadevdaymx2019.couponservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CouponController {

  @GetMapping("/{coupon}")
  public Double getDisscount(@PathVariable String coupon) {
    log.info("Apply coupon : {}",  coupon);
    return coupon.equals("20DISCCOUNT") ? 20D : 0D;
  }

}
