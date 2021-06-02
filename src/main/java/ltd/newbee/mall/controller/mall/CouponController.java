package ltd.newbee.mall.controller.mall;

import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.controller.vo.NewBeeMallCouponVO;
import ltd.newbee.mall.controller.vo.NewBeeMallUserVO;
import ltd.newbee.mall.service.NewBeeMallCouponService;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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
    public String myCoupons(HttpServletRequest request, HttpSession session) {
        NewBeeMallUserVO userVO = (NewBeeMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<NewBeeMallCouponVO> coupons = newBeeMallCouponService.selectMyCoupons(userVO.getUserId());
        request.setAttribute("myCoupons", coupons);
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
