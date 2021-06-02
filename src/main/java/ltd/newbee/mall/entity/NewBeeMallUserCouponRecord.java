package ltd.newbee.mall.entity;

import java.util.Date;

public class NewBeeMallUserCouponRecord {
    private Long couponUserId;

    private Long userId;

    private Long couponId;

    private Byte useStatus;

    private Date usedTime;

    private Long orderId;

    private Date createTime;

    private Date updateTime;

    private Byte isDeleted;

    public Long getCouponUserId() {
        return couponUserId;
    }

    public void setCouponUserId(Long couponUserId) {
        this.couponUserId = couponUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Byte getUseStatus() {
        return useStatus;
    }

    public NewBeeMallUserCouponRecord setUseStatus(Byte useStatus) {
        this.useStatus = useStatus;
        return this;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }
}
