package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.NewBeeMallSeckill;
import ltd.newbee.mall.util.PageQueryUtil;

import java.util.List;
import java.util.Map;

public interface NewBeeMallSeckillMapper {
    int deleteByPrimaryKey(Long seckillId);

    int insert(NewBeeMallSeckill record);

    int insertSelective(NewBeeMallSeckill record);

    NewBeeMallSeckill selectByPrimaryKey(Long seckillId);

    int updateByPrimaryKeySelective(NewBeeMallSeckill record);

    int updateByPrimaryKey(NewBeeMallSeckill record);

    List<NewBeeMallSeckill> findSeckillList(PageQueryUtil pageUtil);

    int getTotalSeckills(PageQueryUtil pageUtil);

    List<NewBeeMallSeckill> findHomeSeckillList();

    int getHomeTotalSeckills(PageQueryUtil pageUtil);

    void killByProcedure(Map<String, Object> map);

    boolean addStock(Long seckillId);
}
