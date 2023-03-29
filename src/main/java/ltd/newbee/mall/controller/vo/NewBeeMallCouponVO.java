package ltd.newbee.mall.controller.vo;

import java.time.LocalDate;
import java.util.StringJoiner;

public class NewBeeMallCouponVO {

    private Long couponId;

    private Long couponUserId;

    private String couponName;

    private String couponDesc;

    private Integer couponTotal;

    private boolean saleOut;

    private boolean useStatus;

    private Integer discount;

    private Integer min;

    private Byte couponLimit;

    private Byte couponType;

    private Byte status;

    private Byte goodsType;

    private String goodsValue;

    private String code;

    private LocalDate couponStartTime;

    private LocalDate couponEndTime;

    private boolean hasReceived;

    public Long getCouponUserId() {
        return couponUserId;
    }

    public void setCouponUserId(Long couponUserId) {
        this.couponUserId = couponUserId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public NewBeeMallCouponVO setCouponId(Long couponId) {
        this.couponId = couponId;
        return this;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public NewBeeMallCouponVO setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
        return this;
    }

    public Integer getCouponTotal() {
        return couponTotal;
    }

    public NewBeeMallCouponVO setCouponTotal(Integer couponTotal) {
        this.couponTotal = couponTotal;
        return this;
    }

    public boolean isSaleOut() {
        return saleOut;
    }

    public NewBeeMallCouponVO setSaleOut(boolean saleOut) {
        this.saleOut = saleOut;
        return this;
    }

    public Integer getDiscount() {
        return discount;
    }

    public NewBeeMallCouponVO setDiscount(Integer discount) {
        this.discount = discount;
        return this;
    }

    public Integer getMin() {
        return min;
    }

    public NewBeeMallCouponVO setMin(Integer min) {
        this.min = min;
        return this;
    }

    public Byte getCouponLimit() {
        return couponLimit;
    }

    public NewBeeMallCouponVO setCouponLimit(Byte couponLimit) {
        this.couponLimit = couponLimit;
        return this;
    }

    public Byte getCouponType() {
        return couponType;
    }

    public NewBeeMallCouponVO setCouponType(Byte couponType) {
        this.couponType = couponType;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public NewBeeMallCouponVO setStatus(Byte status) {
        this.status = status;
        return this;
    }

    public Byte getGoodsType() {
        return goodsType;
    }

    public NewBeeMallCouponVO setGoodsType(Byte goodsType) {
        this.goodsType = goodsType;
        return this;
    }

    public String getGoodsValue() {
        return goodsValue;
    }

    public NewBeeMallCouponVO setGoodsValue(String goodsValue) {
        this.goodsValue = goodsValue;
        return this;
    }

    public String getCode() {
        return code;
    }

    public NewBeeMallCouponVO setCode(String code) {
        this.code = code;
        return this;
    }

    public LocalDate getCouponStartTime() {
        return couponStartTime;
    }

    public NewBeeMallCouponVO setCouponStartTime(LocalDate couponStartTime) {
        this.couponStartTime = couponStartTime;
        return this;
    }

    public LocalDate getCouponEndTime() {
        return couponEndTime;
    }

    public NewBeeMallCouponVO setCouponEndTime(LocalDate couponEndTime) {
        this.couponEndTime = couponEndTime;
        return this;
    }

    public boolean isUseStatus() {
        return useStatus;
    }

    public void setUseStatus(boolean useStatus) {
        this.useStatus = useStatus;
    }

    public boolean isHasReceived() {
        return hasReceived;
    }

    public void setHasReceived(boolean hasReceived) {
        this.hasReceived = hasReceived;
    }

    @Override
    public String toString() {
        return "NewBeeMallCouponVO{" +
                "couponId=" + couponId +
                ", couponUserId=" + couponUserId +
                ", couponName='" + couponName + '\'' +
                ", couponDesc='" + couponDesc + '\'' +
                ", couponTotal=" + couponTotal +
                ", saleOut=" + saleOut +
                ", useStatus=" + useStatus +
                ", discount=" + discount +
                ", min=" + min +
                ", couponLimit=" + couponLimit +
                ", couponType=" + couponType +
                ", status=" + status +
                ", goodsType=" + goodsType +
                ", goodsValue='" + goodsValue + '\'' +
                ", code='" + code + '\'' +
                ", couponStartTime=" + couponStartTime +
                ", couponEndTime=" + couponEndTime +
                ", hasReceived=" + hasReceived +
                '}';
    }
}
