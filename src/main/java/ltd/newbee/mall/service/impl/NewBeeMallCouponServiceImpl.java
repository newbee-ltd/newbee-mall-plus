package ltd.newbee.mall.service.impl;

import ltd.newbee.mall.common.NewBeeMallException;
import ltd.newbee.mall.controller.vo.NewBeeMallCouponVO;
import ltd.newbee.mall.controller.vo.NewBeeMallMyCouponVO;
import ltd.newbee.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.dao.NewBeeMallCouponMapper;
import ltd.newbee.mall.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.dao.NewBeeMallUserCouponRecordMapper;
import ltd.newbee.mall.entity.NewBeeMallCoupon;
import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.entity.NewBeeMallUserCouponRecord;
import ltd.newbee.mall.service.NewBeeMallCouponService;
import ltd.newbee.mall.util.BeanUtil;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewBeeMallCouponServiceImpl implements NewBeeMallCouponService {

    @Autowired
    private NewBeeMallCouponMapper newBeeMallCouponMapper;

    @Autowired
    private NewBeeMallUserCouponRecordMapper newBeeMallUserCouponRecordMapper;

    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Override
    public PageResult getCouponPage(PageQueryUtil pageUtil) {
        List<NewBeeMallCoupon> carousels = newBeeMallCouponMapper.findCouponlList(pageUtil);
        int total = newBeeMallCouponMapper.getTotalCoupons(pageUtil);
        PageResult pageResult = new PageResult(carousels, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean saveCoupon(NewBeeMallCoupon newBeeMallCoupon) {
        return newBeeMallCouponMapper.insertSelective(newBeeMallCoupon) > 0;
    }

    @Override
    public boolean updateCoupon(NewBeeMallCoupon newBeeMallCoupon) {
        return newBeeMallCouponMapper.updateByPrimaryKeySelective(newBeeMallCoupon) > 0;
    }

    @Override
    public NewBeeMallCoupon getCouponById(Long id) {
        return newBeeMallCouponMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteCouponById(Long id) {
        return newBeeMallCouponMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<NewBeeMallCouponVO> selectAvailableCoupon(Long userId) {
        List<NewBeeMallCoupon> coupons = newBeeMallCouponMapper.selectAvailableCoupon();
        List<NewBeeMallCouponVO> couponVOS = BeanUtil.copyList(coupons, NewBeeMallCouponVO.class);
        for (NewBeeMallCouponVO couponVO : couponVOS) {
            if (userId != null) {
                int num = newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, couponVO.getCouponId());
                if (num > 0) {
                    couponVO.setHasReceived(true);
                }
            }
            if (couponVO.getCouponTotal() != 0) {
                int count = newBeeMallUserCouponRecordMapper.getCouponCount(couponVO.getCouponId());
                if (count >= couponVO.getCouponTotal()) {
                    couponVO.setSaleOut(true);
                }
            }
        }
        return couponVOS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCouponUser(Long couponId, Long userId) {
        NewBeeMallCoupon newBeeMallCoupon = newBeeMallCouponMapper.selectByPrimaryKey(couponId);
        if (newBeeMallCoupon.getCouponLimit() != 0) {
            int num = newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, couponId);
            if (num != 0) {
                throw new NewBeeMallException("优惠卷已经领过了,无法再次领取！");
            }
        }
        if (newBeeMallCoupon.getCouponTotal() != 0) {
            int count = newBeeMallUserCouponRecordMapper.getCouponCount(couponId);
            if (count >= newBeeMallCoupon.getCouponTotal()) {
                throw new NewBeeMallException("优惠卷已经领完了！");
            }
            if (newBeeMallCouponMapper.reduceCouponTotal(couponId) <= 0) {
                throw new NewBeeMallException("优惠卷领取失败！");
            }
        }
        NewBeeMallUserCouponRecord couponUser = new NewBeeMallUserCouponRecord();
        couponUser.setUserId(userId);
        couponUser.setCouponId(couponId);
        return newBeeMallUserCouponRecordMapper.insertSelective(couponUser) > 0;
    }

    @Override
    public List<NewBeeMallCouponVO> selectMyCoupons(Long userId) {
        List<NewBeeMallUserCouponRecord> coupons = newBeeMallUserCouponRecordMapper.selectMyCoupons(userId);
        List<NewBeeMallCouponVO> couponVOS = new ArrayList<>();
        for (NewBeeMallUserCouponRecord couponUser : coupons) {
            NewBeeMallCoupon newBeeMallCoupon = newBeeMallCouponMapper.selectByPrimaryKey(couponUser.getCouponId());
            NewBeeMallCouponVO newBeeMallCouponVO = new NewBeeMallCouponVO();
            BeanUtil.copyProperties(newBeeMallCoupon, newBeeMallCouponVO);
            newBeeMallCouponVO.setCouponUserId(couponUser.getCouponUserId());
            newBeeMallCouponVO.setUsed(couponUser.getUsedTime() != null);
            couponVOS.add(newBeeMallCouponVO);
        }
        return couponVOS;
    }

    @Override
    public List<NewBeeMallMyCouponVO> selectOrderCanUseCoupons(List<NewBeeMallShoppingCartItemVO> myShoppingCartItems, int priceTotal, Long userId) {
        List<NewBeeMallUserCouponRecord> couponUsers = newBeeMallUserCouponRecordMapper.selectMyAvailableCoupons(userId);
        List<NewBeeMallMyCouponVO> myCouponVOS = BeanUtil.copyList(couponUsers, NewBeeMallMyCouponVO.class);
        List<Long> couponIds = couponUsers.stream().map(NewBeeMallUserCouponRecord::getCouponId).collect(Collectors.toList());
        if (!couponIds.isEmpty()) {
            List<NewBeeMallCoupon> coupons = newBeeMallCouponMapper.selectByIds(couponIds);
            for (NewBeeMallCoupon coupon : coupons) {
                for (NewBeeMallMyCouponVO myCouponVO : myCouponVOS) {
                    if (coupon.getCouponId().equals(myCouponVO.getCouponId())) {
                        myCouponVO.setName(coupon.getCouponName());
                        myCouponVO.setCouponDesc(coupon.getCouponDesc());
                        myCouponVO.setDiscount(coupon.getDiscount());
                        myCouponVO.setMin(coupon.getMin());
                        myCouponVO.setGoodsType(coupon.getGoodsType());
                        myCouponVO.setGoodsValue(coupon.getGoodsValue());
                        ZonedDateTime zonedDateTime = coupon.getCouponEndTime().atStartOfDay(ZoneId.systemDefault());
                        myCouponVO.setEndTime(Date.from(zonedDateTime.toInstant()));
                    }
                }
            }
        }
        return myCouponVOS.stream().filter(item -> {
            boolean b = false;
            if (item.getMin() <= priceTotal) {
                if (item.getGoodsType() == 1) { // 指定分类可用
                    String[] split = item.getGoodsValue().split(",");
                    List<Long> goodsValue = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
                    List<Long> goodsIds = myShoppingCartItems.stream().map(NewBeeMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
                    List<NewBeeMallGoods> goods = newBeeMallGoodsMapper.selectByPrimaryKeys(goodsIds);
                    List<Long> categoryIds = goods.stream().map(NewBeeMallGoods::getGoodsCategoryId).collect(Collectors.toList());
                    for (Long categoryId : categoryIds) {
                        if (goodsValue.contains(categoryId)) {
                            b = true;
                            break;
                        }
                    }
                } else if (item.getGoodsType() == 2) { // 指定商品可用
                    String[] split = item.getGoodsValue().split(",");
                    List<Long> goodsValue = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
                    List<Long> goodsIds = myShoppingCartItems.stream().map(NewBeeMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
                    for (Long goodsId : goodsIds) {
                        if (goodsValue.contains(goodsId)) {
                            b = true;
                            break;
                        }
                    }
                } else { // 全场通用
                    b = true;
                }
            }
            return b;
        }).sorted(Comparator.comparingInt(NewBeeMallMyCouponVO::getDiscount)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteCouponUser(Long couponUserId) {
        return newBeeMallUserCouponRecordMapper.deleteByPrimaryKey(couponUserId) > 0;
    }

    @Override
    public void releaseCoupon(Long orderId) {
        NewBeeMallUserCouponRecord newBeeMallUserCouponRecord = newBeeMallUserCouponRecordMapper.getUserCouponByOrderId(orderId);
        if (newBeeMallUserCouponRecord == null) {
            return;
        }
        newBeeMallUserCouponRecord.setUseStatus((byte) 0);
        newBeeMallUserCouponRecord.setUpdateTime(new Date());
        newBeeMallUserCouponRecordMapper.updateByPrimaryKey(newBeeMallUserCouponRecord);
    }
}
