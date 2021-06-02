package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.NewBeeMallSeckillSuccess;

public interface NewBeeMallSeckillSuccessMapper {
    int deleteByPrimaryKey(Integer secId);

    int insert(NewBeeMallSeckillSuccess record);

    int insertSelective(NewBeeMallSeckillSuccess record);

    NewBeeMallSeckillSuccess selectByPrimaryKey(Long secId);

    int updateByPrimaryKeySelective(NewBeeMallSeckillSuccess record);

    int updateByPrimaryKey(NewBeeMallSeckillSuccess record);

    NewBeeMallSeckillSuccess getSeckillSuccessByUserIdAndSeckillId(Long userId, Long seckillId);
}
