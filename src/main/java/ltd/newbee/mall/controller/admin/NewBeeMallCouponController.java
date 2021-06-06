package ltd.newbee.mall.controller.admin;

import ltd.newbee.mall.entity.NewBeeMallCoupon;
import ltd.newbee.mall.service.NewBeeMallCouponService;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class NewBeeMallCouponController {

    @Autowired
    private NewBeeMallCouponService newBeeMallCouponService;

    @GetMapping("/coupon")
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "newbee_mall_coupon");
        return "admin/newbee_mall_coupon";
    }

    @ResponseBody
    @GetMapping("/coupon/list")
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty((CharSequence) params.get("page")) || StringUtils.isEmpty((CharSequence) params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallCouponService.getCouponPage(pageUtil));
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/coupon/save")
    public Result save(@RequestBody NewBeeMallCoupon newBeeMallCoupon) {
        return ResultGenerator.genDmlResult(newBeeMallCouponService.saveCoupon(newBeeMallCoupon));
    }

    /**
     * 更新
     */
    @PostMapping("/coupon/update")
    @ResponseBody
    public Result update(@RequestBody NewBeeMallCoupon newBeeMallCoupon) {
        newBeeMallCoupon.setUpdateTime(new Date());
        return ResultGenerator.genDmlResult(newBeeMallCouponService.updateCoupon(newBeeMallCoupon));
    }

    /**
     * 详情
     */
    @GetMapping("/coupon/{id}")
    @ResponseBody
    public Result Info(@PathVariable("id") Long id) {
        NewBeeMallCoupon newBeeMallCoupon = newBeeMallCouponService.getCouponById(id);
        return ResultGenerator.genSuccessResult(newBeeMallCoupon);
    }

    /**
     * 删除
     */
    @DeleteMapping("/coupon/{id}")
    @ResponseBody
    public Result delete(@PathVariable Long id) {
        return ResultGenerator.genDmlResult(newBeeMallCouponService.deleteCouponById(id));
    }
}
