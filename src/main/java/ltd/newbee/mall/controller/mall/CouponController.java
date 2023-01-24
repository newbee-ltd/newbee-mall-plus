package ltd.newbee.mall.controller.mall;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.controller.vo.NewBeeMallCouponVO;
import ltd.newbee.mall.controller.vo.NewBeeMallUserVO;
import ltd.newbee.mall.service.NewBeeMallCouponService;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class CouponController {

    @Autowired
    private NewBeeMallCouponService newBeeMallCouponService;

    @GetMapping("/couponList")
    public String couponList(HttpServletRequest request, HttpSession session) {
        Long userId = null;
        if (session.getAttribute(Constants.MALL_USER_SESSION_KEY) != null) {
            userId = ((NewBeeMallUserVO) request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)).getUserId();
        }
        List<NewBeeMallCouponVO> coupons = newBeeMallCouponService.selectAvailableCoupon(userId);
        request.setAttribute("coupons", coupons);
        return "mall/coupon-list";
    }

    @GetMapping("/myCoupons")
    public String myCoupons(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession session) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty((CharSequence) params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.MY_COUPONS_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);

        PageResult<NewBeeMallCouponVO> pageResult = newBeeMallCouponService.selectMyCoupons(pageUtil);
        request.setAttribute("pageResult", pageResult);
        request.setAttribute("path", "myCoupons");
        return "mall/my-coupons";
    }

    @ResponseBody
    @PostMapping("coupon/{couponId}")
    public Result save(@PathVariable Long couponId, HttpSession session) {
        NewBeeMallUserVO userVO = (NewBeeMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        return ResultGenerator.genDmlResult(newBeeMallCouponService.saveCouponUser(couponId, userVO.getUserId()));
    }

    @ResponseBody
    @DeleteMapping("coupon/{couponUserId}")
    public Result delete(@PathVariable Long couponUserId) {
        return ResultGenerator.genDmlResult(newBeeMallCouponService.deleteCouponUser(couponUserId));
    }
}
