package ltd.newbee.mall.controller.admin;

import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.entity.NewBeeMallSeckill;
import ltd.newbee.mall.redis.RedisCache;
import ltd.newbee.mall.service.NewBeeMallSeckillService;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class NewBeeMallSeckillController {

    @Autowired
    private NewBeeMallSeckillService newBeeMallSeckillService;
    @Autowired
    private RedisCache redisCache;

    @GetMapping("/seckill")
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "newbee_mall_seckill");
        return "admin/newbee_mall_seckill";
    }

    @ResponseBody
    @GetMapping("/seckill/list")
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty((CharSequence) params.get("page")) || StringUtils.isEmpty((CharSequence) params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallSeckillService.getSeckillPage(pageUtil));
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/seckill/save")
    public Result save(@RequestBody NewBeeMallSeckill newBeeMallSeckill) {
        if (newBeeMallSeckill == null || newBeeMallSeckill.getGoodsId() < 1 || newBeeMallSeckill.getSeckillNum() < 1 || newBeeMallSeckill.getSeckillPrice() < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
        boolean result = newBeeMallSeckillService.saveSeckill(newBeeMallSeckill);
        if (result) {
            // 虚拟库存预热
            redisCache.setCacheObject(Constants.SECKILL_GOODS_STOCK_KEY + newBeeMallSeckill.getSeckillId(), newBeeMallSeckill.getSeckillNum());
        }
        return ResultGenerator.genDmlResult(result);
    }

    /**
     * 更新
     */
    @PostMapping("/seckill/update")
    @ResponseBody
    public Result update(@RequestBody NewBeeMallSeckill newBeeMallSeckill) {
        if (newBeeMallSeckill == null || newBeeMallSeckill.getSeckillId() == null || newBeeMallSeckill.getGoodsId() < 1 || newBeeMallSeckill.getSeckillNum() < 1 || newBeeMallSeckill.getSeckillPrice() < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
        boolean result = newBeeMallSeckillService.updateSeckill(newBeeMallSeckill);
        if (result) {
            // 虚拟库存预热
            redisCache.setCacheObject(Constants.SECKILL_GOODS_STOCK_KEY + newBeeMallSeckill.getSeckillId(), newBeeMallSeckill.getSeckillNum());
            redisCache.deleteObject(Constants.SECKILL_GOODS_DETAIL + newBeeMallSeckill.getSeckillId());
            redisCache.deleteObject(Constants.SECKILL_GOODS_LIST);
        }
        return ResultGenerator.genDmlResult(result);
    }

    /**
     * 详情
     */
    @GetMapping("/seckill/{id}")
    @ResponseBody
    public Result Info(@PathVariable("id") Long id) {
        NewBeeMallSeckill newBeeMallSeckill = newBeeMallSeckillService.getSeckillById(id);
        return ResultGenerator.genSuccessResult(newBeeMallSeckill);
    }

    /**
     * 删除
     */
    @DeleteMapping("/seckill/{id}")
    @ResponseBody
    public Result delete(@PathVariable Long id) {
        redisCache.deleteObject(Constants.SECKILL_GOODS_DETAIL + id);
        redisCache.deleteObject(Constants.SECKILL_GOODS_LIST);
        return ResultGenerator.genDmlResult(newBeeMallSeckillService.deleteSeckillById(id));
    }
}
