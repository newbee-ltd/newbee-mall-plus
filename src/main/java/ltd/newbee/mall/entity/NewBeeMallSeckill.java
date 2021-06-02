package ltd.newbee.mall.entity;

import java.util.Date;

public class NewBeeMallSeckill {
    private Long seckillId;

    private Long goodsId;

    private Integer seckillPrice;

    private Integer seckillNum;

    private Boolean seckillStatus;

    private Date seckillBegin;

    private Date seckillEnd;

    private Integer seckillRank;

    private Date createTime;

    private Date updateTime;

    private Boolean isDeleted;

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Integer seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getSeckillNum() {
        return seckillNum;
    }

    public void setSeckillNum(Integer seckillNum) {
        this.seckillNum = seckillNum;
    }

    public Boolean getSeckillStatus() {
        return seckillStatus;
    }

    public void setSeckillStatus(Boolean seckillStatus) {
        this.seckillStatus = seckillStatus;
    }

    public Date getSeckillBegin() {
        return seckillBegin;
    }

    public void setSeckillBegin(Date seckillBegin) {
        this.seckillBegin = seckillBegin;
    }

    public Date getSeckillEnd() {
        return seckillEnd;
    }

    public void setSeckillEnd(Date seckillEnd) {
        this.seckillEnd = seckillEnd;
    }

    public Integer getSeckillRank() {
        return seckillRank;
    }

    public void setSeckillRank(Integer seckillRank) {
        this.seckillRank = seckillRank;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
