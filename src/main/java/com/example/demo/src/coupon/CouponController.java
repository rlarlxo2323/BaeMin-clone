package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.coupon.model.Coupon;
import com.example.demo.src.coupon.model.GetCouponRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/coupons")
public class CouponController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CouponProvider couponProvider;
    @Autowired
    private final CouponService couponService;
    @Autowired
    private final JwtService jwtService;

    public CouponController(CouponProvider couponProvider, CouponService couponService, JwtService jwtService) {
        this.couponProvider = couponProvider;
        this.couponService = couponService;
        this.jwtService = jwtService;
    }

    /**
     * 10. 쿠폰 조회 API
     * [GET] /coupons
     * @return BaseResponse<List<Coupon>>
     */
    @ResponseBody
    @GetMapping("") // (GET) giri.shop/app/coupons
    public BaseResponse<List<Coupon>> getCoupons() {
        try{
            List<Coupon> getCouponsRes = couponProvider.getCoupons();
            return new BaseResponse<>(getCouponsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 11. 회원 보유쿠폰 조회 API
     * [GET] /coupons/:userId
     * @return BaseResponse<List<GetCouponRes>> 예외처리 - 없는 사용자
     */
    @ResponseBody
    @GetMapping("/{id}") // (GET) giri.shop/app/coupons/:userId
    public BaseResponse<List<GetCouponRes>> getCouponsById(@PathVariable("id") String id) {
        try{
            List<GetCouponRes> getCouponsByIdRes = couponProvider.getCouponsById(id);
            return new BaseResponse<>(getCouponsByIdRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
