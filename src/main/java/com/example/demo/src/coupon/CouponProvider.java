package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.src.coupon.model.Coupon;
import com.example.demo.src.coupon.model.GetCouponRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CouponProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CouponDao couponDao;
    private final JwtService jwtService;

    public CouponProvider(CouponDao couponDao, JwtService jwtService) {
        this.couponDao = couponDao;
        this.jwtService = jwtService;
    }

    public List<Coupon> getCoupons() throws BaseException {
        try{
            List<Coupon> getCouponRes = couponDao.getCoupons();
            return getCouponRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCouponRes> getCouponsById(String id) throws BaseException {
        try{
            List<GetCouponRes> getCouponByIdRes = couponDao.getCouponsById(id);
            return getCouponByIdRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
