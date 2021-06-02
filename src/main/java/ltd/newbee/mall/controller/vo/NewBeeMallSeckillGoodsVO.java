package ltd.newbee.mall.controller.vo;

import java.io.Serializable;
import java.util.Date;

public class NewBeeMallSeckillGoodsVO implements Serializable {
    private static final long serialVersionUID = -8719192110998138980L;

    private Long seckillId;

    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsDetailContent;

    private String goodsCoverImg;

    private Integer sellingPrice;

    private Integer seckillPrice;

    private Date seckillBegin;

    private Date seckillEnd;

    private String seckillBeginTime;

    private String seckillEndTime;

    private Long startDate;

    private Long endDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCoverImg() {
        return goodsCoverImg;
    }

    public void setGoodsCoverImg(String goodsCoverImg) {
        this.goodsCoverImg = goodsCoverImg;
    }

    public Integer getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Integer seckillPrice) {
        this.seckillPrice = seckillPrice;
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

    public Integer getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Integer sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getSeckillBeginTime() {
        return seckillBeginTime;
    }

    public void setSeckillBeginTime(String seckillBeginTime) {
        this.seckillBeginTime = seckillBeginTime;
    }

    public String getSeckillEndTime() {
        return seckillEndTime;
    }

    public void setSeckillEndTime(String seckillEndTime) {
        this.seckillEndTime = seckillEndTime;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getGoodsIntro() {
        return goodsIntro;
    }

    public void setGoodsIntro(String goodsIntro) {
        this.goodsIntro = goodsIntro;
    }

    public String getGoodsDetailContent() {
        return goodsDetailContent;
    }

    public void setGoodsDetailContent(String goodsDetailContent) {
        this.goodsDetailContent = goodsDetailContent;
    }
}
