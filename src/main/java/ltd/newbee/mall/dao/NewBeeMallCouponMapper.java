package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.NewBeeMallCoupon;
import ltd.newbee.mall.util.PageQueryUtil;

import java.util.List;

public interface NewBeeMallCouponMapper {
    int deleteByPrimaryKey(Long couponId);

    int deleteBatch(Integer[] couponIds);

    int insert(NewBeeMallCoupon record);

    int insertSelective(NewBeeMallCoupon record);

    NewBeeMallCoupon selectByPrimaryKey(Long couponId);

    int updateByPrimaryKeySelective(NewBeeMallCoupon record);

    int updateByPrimaryKey(NewBeeMallCoupon record);

    List<NewBeeMallCoupon> findCouponlList(PageQueryUtil pageUtil);

    int getTotalCoupons(PageQueryUtil pageUtil);

    List<NewBeeMallCoupon> selectAvailableCoupon();

    int reduceCouponTotal(Long couponId);

    List<NewBeeMallCoupon> selectByIds(List<Long> couponIds);

    List<NewBeeMallCoupon> selectAvailableGiveCoupon();

}
