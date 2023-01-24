package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.NewBeeMallUserCouponRecord;
import ltd.newbee.mall.util.PageQueryUtil;

import java.util.List;

public interface NewBeeMallUserCouponRecordMapper {
    int deleteByPrimaryKey(Long couponUserId);

    int insert(NewBeeMallUserCouponRecord record);

    int insertSelective(NewBeeMallUserCouponRecord record);

    NewBeeMallUserCouponRecord selectByPrimaryKey(Long couponUserId);

    int updateByPrimaryKeySelective(NewBeeMallUserCouponRecord record);

    int updateByPrimaryKey(NewBeeMallUserCouponRecord record);

    int getUserCouponCount(Long userId, Long couponId);

    int getCouponCount(Long couponId);

    List<NewBeeMallUserCouponRecord> selectMyCoupons(PageQueryUtil pageQueryUtil);

    Integer countMyCoupons(PageQueryUtil pageQueryUtil);

    List<NewBeeMallUserCouponRecord> selectMyAvailableCoupons(Long userId);

    NewBeeMallUserCouponRecord getUserCouponByOrderId(Long orderId);
}
