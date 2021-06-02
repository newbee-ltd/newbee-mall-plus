package ltd.newbee.mall.service;

import ltd.newbee.mall.controller.vo.ExposerVO;
import ltd.newbee.mall.controller.vo.SeckillSuccessVO;
import ltd.newbee.mall.entity.NewBeeMallSeckill;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;

import java.util.List;

public interface NewBeeMallSeckillService {

    PageResult getSeckillPage(PageQueryUtil pageUtil);

    boolean saveSeckill(NewBeeMallSeckill newBeeMallSeckill);

    boolean updateSeckill(NewBeeMallSeckill newBeeMallSeckill);

    NewBeeMallSeckill getSeckillById(Long id);

    boolean deleteSeckillById(Long id);

    List<NewBeeMallSeckill> getHomeSeckillPage();

    ExposerVO exposerUrl(Long seckillId);

    SeckillSuccessVO executeSeckill(Long seckillId, Long userId);
}
