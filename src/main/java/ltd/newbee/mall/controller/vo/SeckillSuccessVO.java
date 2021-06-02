package ltd.newbee.mall.controller.vo;

import java.io.Serializable;

/**
 * 用户秒杀成功VO
 */
public class SeckillSuccessVO implements Serializable {

    private static final long serialVersionUID = 1503814153626594835L;

    private Long seckillSuccessId;

    private String md5;

    public Long getSeckillSuccessId() {
        return seckillSuccessId;
    }

    public void setSeckillSuccessId(Long seckillSuccessId) {
        this.seckillSuccessId = seckillSuccessId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
