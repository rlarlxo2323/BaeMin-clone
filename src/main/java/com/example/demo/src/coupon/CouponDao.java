package com.example.demo.src.coupon;

import com.example.demo.src.coupon.model.Coupon;
import com.example.demo.src.coupon.model.GetCouponRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CouponDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Coupon> getCoupons(){
        String getCouponsQuery = "select * from coupon";
        return this.jdbcTemplate.query(getCouponsQuery,
                (rs,rowNum) -> new Coupon(
                        rs.getString("couponCode"),
                        rs.getString("couponName"),
                        rs.getString("discount"),
                        rs.getString("storeCode"),
                        rs.getString("priceMin"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("period"),
                        rs.getString("option"))
        );
    }

    public List<GetCouponRes> getCouponsById(String id){
        String getCouponsByIdQuery = "select c.discount, c.couponName, c.priceMin, c.`option` as options,\n" +
                "       concat(mc.startDate, ' ~ ', mc.endDate) as usePeriod\n" +
                "from member_has_coupon mc\n" +
                "inner join coupon c\n" +
                "on mc.couponCode = c.couponCode\n" +
                "where mc.userId=?";
        String getCouponsByIdParams = id;
        return this.jdbcTemplate.query(getCouponsByIdQuery,
                (rs,rowNum) -> new GetCouponRes(
                        rs.getString("discount"),
                        rs.getString("couponName"),
                        rs.getString("priceMin"),
                        rs.getString("options"),
                        rs.getString("usePeriod")),
                getCouponsByIdParams);
    }
}
