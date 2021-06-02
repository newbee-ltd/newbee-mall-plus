package ltd.newbee.mall.controller.vo;


import ltd.newbee.mall.common.SeckillStatusEnum;

import java.io.Serializable;

/**
 * 秒杀服务接口地址暴露类
 */
public class ExposerVO implements Serializable {

    private static final long serialVersionUID = -7615136662052646516L;
    // 秒杀状态enum
    private SeckillStatusEnum seckillStatusEnum;

    // 一种加密措施
    private String md5;

    // id
    private long seckillId;

    // 系统当前时间（毫秒）
    private long now;

    // 开启时间
    private long start;

    // 结束时间
    private long end;

    public ExposerVO(SeckillStatusEnum seckillStatusEnum, String md5, long seckillId) {
        this.seckillStatusEnum = seckillStatusEnum;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public ExposerVO(SeckillStatusEnum seckillStatusEnum, long seckillId, long now, long start, long end) {
        this.seckillStatusEnum = seckillStatusEnum;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public ExposerVO(SeckillStatusEnum seckillStatusEnum, long seckillId) {
        this.seckillStatusEnum = seckillStatusEnum;
        this.seckillId = seckillId;
    }


    public SeckillStatusEnum getSeckillStatusEnum() {
        return seckillStatusEnum;
    }

    public void setSeckillStatusEnum(SeckillStatusEnum seckillStatusEnum) {
        this.seckillStatusEnum = seckillStatusEnum;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
