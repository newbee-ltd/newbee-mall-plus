package ltd.newbee.mall.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.common.NewBeeMallException;
import ltd.newbee.mall.common.SeckillStatusEnum;
import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.controller.vo.ExposerVO;
import ltd.newbee.mall.controller.vo.NewBeeMallSeckillGoodsVO;
import ltd.newbee.mall.controller.vo.SeckillSuccessVO;
import ltd.newbee.mall.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.dao.NewBeeMallSeckillMapper;
import ltd.newbee.mall.dao.NewBeeMallSeckillSuccessMapper;
import ltd.newbee.mall.entity.NewBeeMallSeckill;
import ltd.newbee.mall.entity.NewBeeMallSeckillSuccess;
import ltd.newbee.mall.redis.RedisCache;
import ltd.newbee.mall.service.NewBeeMallSeckillService;
import ltd.newbee.mall.util.MD5Util;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class NewBeeMallSeckillServiceImpl implements NewBeeMallSeckillService {

    // 使用令牌桶RateLimiter 限流
    private static final RateLimiter rateLimiter = RateLimiter.create(10);

    @Autowired
    private NewBeeMallSeckillMapper newBeeMallSeckillMapper;

    @Autowired
    private NewBeeMallSeckillSuccessMapper newBeeMallSeckillSuccessMapper;

    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public PageResult getSeckillPage(PageQueryUtil pageUtil) {
        List<NewBeeMallSeckill> carousels = newBeeMallSeckillMapper.findSeckillList(pageUtil);
        int total = newBeeMallSeckillMapper.getTotalSeckills(pageUtil);
        PageResult pageResult = new PageResult(carousels, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean saveSeckill(NewBeeMallSeckill newBeeMallSeckill) {
        if (newBeeMallGoodsMapper.selectByPrimaryKey(newBeeMallSeckill.getGoodsId()) == null) {
            NewBeeMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        return newBeeMallSeckillMapper.insertSelective(newBeeMallSeckill) > 0;
    }

    @Override
    public boolean updateSeckill(NewBeeMallSeckill newBeeMallSeckill) {
        if (newBeeMallGoodsMapper.selectByPrimaryKey(newBeeMallSeckill.getGoodsId()) == null) {
            NewBeeMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        NewBeeMallSeckill temp = newBeeMallSeckillMapper.selectByPrimaryKey(newBeeMallSeckill.getSeckillId());
        if (temp == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        newBeeMallSeckill.setUpdateTime(new Date());
        return newBeeMallSeckillMapper.updateByPrimaryKeySelective(newBeeMallSeckill) > 0;
    }

    @Override
    public NewBeeMallSeckill getSeckillById(Long id) {
        return newBeeMallSeckillMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteSeckillById(Long id) {
        return newBeeMallSeckillMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<NewBeeMallSeckill> getHomeSeckillPage() {
        return newBeeMallSeckillMapper.findHomeSeckillList();
    }

    @Override
    public ExposerVO exposerUrl(Long seckillId) {
        NewBeeMallSeckillGoodsVO newBeeMallSeckillGoodsVO = redisCache.getCacheObject(Constants.SECKILL_GOODS_DETAIL + seckillId);
        Date startTime = newBeeMallSeckillGoodsVO.getSeckillBegin();
        Date endTime = newBeeMallSeckillGoodsVO.getSeckillEnd();
        // 系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new ExposerVO(SeckillStatusEnum.NOT_START, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // 检查虚拟库存
        Integer stock = redisCache.getCacheObject(Constants.SECKILL_GOODS_STOCK_KEY + seckillId);
        if (stock == null || stock < 0) {
            return new ExposerVO(SeckillStatusEnum.STARTED_SHORTAGE_STOCK, seckillId);
        }
        // 加密
        String md5 = MD5Util.MD5Encode(seckillId.toString(), Constants.UTF_ENCODING);
        return new ExposerVO(SeckillStatusEnum.START, md5, seckillId);
    }

    @Override
    public SeckillSuccessVO executeSeckill(Long seckillId, Long userId) {
        // 判断能否在500毫秒内得到令牌，如果不能则立即返回false，不会阻塞程序
        if (!rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS)) {
            throw new NewBeeMallException("秒杀失败");
        }
        // 判断用户是否购买过秒杀商品
        if (redisCache.containsCacheSet(Constants.SECKILL_SUCCESS_USER_ID + seckillId, userId)) {
            throw new NewBeeMallException("您已经购买过秒杀商品，请勿重复购买");
        }
        // 更新秒杀商品虚拟库存
        Long stock = redisCache.luaDecrement(Constants.SECKILL_GOODS_STOCK_KEY + seckillId);
        if (stock < 0) {
            throw new NewBeeMallException("秒杀商品已售空");
        }
        NewBeeMallSeckill newBeeMallSeckill = redisCache.getCacheObject(Constants.SECKILL_KEY + seckillId);
        if (newBeeMallSeckill == null) {
            newBeeMallSeckill = newBeeMallSeckillMapper.selectByPrimaryKey(seckillId);
            redisCache.setCacheObject(Constants.SECKILL_KEY + seckillId, newBeeMallSeckill, 24, TimeUnit.HOURS);
        }
        // 判断秒杀商品是否再有效期内
        long beginTime = newBeeMallSeckill.getSeckillBegin().getTime();
        long endTime = newBeeMallSeckill.getSeckillEnd().getTime();
        Date now = new Date();
        long nowTime = now.getTime();
        if (nowTime < beginTime) {
            throw new NewBeeMallException("秒杀未开启");
        } else if (nowTime > endTime) {
            throw new NewBeeMallException("秒杀已结束");
        }

        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>(8);
        map.put("seckillId", seckillId);
        map.put("userId", userId);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行存储过程，result被赋值
        try {
            newBeeMallSeckillMapper.killByProcedure(map);
        } catch (Exception e) {
            throw new NewBeeMallException(e.getMessage());
        }
        // 获取result -2sql执行失败 -1未插入数据 0未更新数据 1sql执行成功
        map.get("result");
        int result = MapUtils.getInteger(map, "result", -2);
        if (result != 1) {
            throw new NewBeeMallException("很遗憾！未抢购到秒杀商品");
        }
        // 记录购买过的用户
        redisCache.setCacheSet(Constants.SECKILL_SUCCESS_USER_ID + seckillId, userId);
        long endExpireTime = endTime / 1000;
        long nowExpireTime = nowTime / 1000;
        redisCache.expire(Constants.SECKILL_SUCCESS_USER_ID + seckillId, endExpireTime - nowExpireTime, TimeUnit.SECONDS);
        NewBeeMallSeckillSuccess seckillSuccess = newBeeMallSeckillSuccessMapper.getSeckillSuccessByUserIdAndSeckillId(userId, seckillId);
        SeckillSuccessVO seckillSuccessVO = new SeckillSuccessVO();
        Long seckillSuccessId = seckillSuccess.getSecId();
        seckillSuccessVO.setSeckillSuccessId(seckillSuccessId);
        seckillSuccessVO.setMd5(MD5Util.MD5Encode(seckillSuccessId + Constants.SECKILL_ORDER_SALT, Constants.UTF_ENCODING));
        return seckillSuccessVO;
    }

}
