package ltd.newbee.mall.entity;

import java.util.Date;

public class NewBeeMallSeckillSuccess {
    
    private Long secId;

    private Long seckillId;

    private Long userId;

    private Byte state;

    private Date createTime;

    public Long getSecId() {
        return secId;
    }

    public void setSecId(Long secId) {
        this.secId = secId;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
