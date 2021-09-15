/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.controller.mall;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import ltd.newbee.mall.annotion.RepeatSubmit;
import ltd.newbee.mall.common.*;
import ltd.newbee.mall.config.AlipayConfig;
import ltd.newbee.mall.config.ProjectConfig;
import ltd.newbee.mall.controller.vo.NewBeeMallOrderDetailVO;
import ltd.newbee.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.controller.vo.NewBeeMallUserVO;
import ltd.newbee.mall.dao.MallUserMapper;
import ltd.newbee.mall.entity.NewBeeMallOrder;
import ltd.newbee.mall.service.NewBeeMallOrderService;
import ltd.newbee.mall.service.NewBeeMallShoppingCartService;
import ltd.newbee.mall.util.MD5Util;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class OrderController {

    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private NewBeeMallShoppingCartService newBeeMallShoppingCartService;
    @Autowired
    private NewBeeMallOrderService newBeeMallOrderService;
    @Autowired
    private MallUserMapper mallUserMapper;
    @Autowired
    private AlipayConfig alipayConfig;

    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NewBeeMallOrderDetailVO orderDetailVO = newBeeMallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }

    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty((CharSequence) params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", newBeeMallOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }

    @RepeatSubmit
    @GetMapping("/saveOrder")
    public String saveOrder(Long couponUserId, HttpSession httpSession) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = newBeeMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //无收货地址
            NewBeeMallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据则跳转至错误页
            NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        //保存订单并返回订单号
        String saveOrderResult = newBeeMallOrderService.saveOrder(user, couponUserId, myShoppingCartItems);
        //跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }

    @RepeatSubmit
    @GetMapping("/saveSeckillOrder/{seckillSuccessId}/{userId}/{seckillSecretKey}")
    public String saveOrder(@PathVariable Long seckillSuccessId,
                            @PathVariable Long userId,
                            @PathVariable String seckillSecretKey) {
        if (seckillSecretKey == null || !seckillSecretKey.equals(MD5Util.MD5Encode(seckillSuccessId + Constants.SECKILL_ORDER_SALT, Constants.UTF_ENCODING))) {
            throw new NewBeeMallException("秒杀商品下单不合法");
        }
        // 保存订单并返回订单号
        String saveOrderResult = newBeeMallOrderService.seckillSaveOrder(seckillSuccessId, userId);
        // 跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }

    @RepeatSubmit
    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NewBeeMallOrder newBeeMallOrder = judgeOrderUserId(orderNo, user.getUserId());
        //判断订单状态
        if (newBeeMallOrder.getOrderStatus().intValue() != NewBeeMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
            NewBeeMallException.fail(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", newBeeMallOrder.getTotalPrice());
        return "mall/pay-select";
    }

    @RepeatSubmit
    @GetMapping("/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) throws UnsupportedEncodingException {
        NewBeeMallUserVO mallUserVO = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Long userId = mallUserVO.getUserId();
        NewBeeMallOrder newBeeMallOrder = judgeOrderUserId(orderNo, userId);
        // 判断订单userId
        if (!userId.equals(newBeeMallOrder.getUserId())) {
            NewBeeMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
        }
        // 判断订单状态
        if (newBeeMallOrder.getOrderStatus() != NewBeeMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()
                || newBeeMallOrder.getPayStatus() != PayStatusEnum.PAY_ING.getPayStatus()) {
            throw new NewBeeMallException("订单结算异常");
        }
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", newBeeMallOrder.getTotalPrice());
        if (payType == 1) {
            request.setCharacterEncoding(Constants.UTF_ENCODING);
            // 初始化
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGateway(), alipayConfig.getAppId(),
                    alipayConfig.getRsaPrivateKey(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getSigntype());
            // 创建API对应的request
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            // 在公共参数中设置回跳和通知地址,通知地址需要公网可访问
            String url = ProjectConfig.getServerUrl() + request.getContextPath();
            alipayRequest.setReturnUrl(url + "/returnOrders/" + newBeeMallOrder.getOrderNo() + "/" + userId);
            alipayRequest.setNotifyUrl(url + "/paySuccess?payType=1&orderNo=" + newBeeMallOrder.getOrderNo());

            // 填充业务参数

            // 必填
            // 商户订单号，需保证在商户端不重复
            String out_trade_no = newBeeMallOrder.getOrderNo() + new Random().nextInt(9999);
            // 销售产品码，与支付宝签约的产品码名称。目前仅支持FAST_INSTANT_TRADE_PAY
            String product_code = "FAST_INSTANT_TRADE_PAY";
            // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。
            String total_amount = newBeeMallOrder.getTotalPrice() + "";
            // 订单标题
            String subject = "支付宝测试";

            // 选填
            // 商品描述，可空
            String body = "商品描述";

            alipayRequest.setBizContent("{" + "\"out_trade_no\":\"" + out_trade_no + "\"," + "\"product_code\":\""
                    + product_code + "\"," + "\"total_amount\":\"" + total_amount + "\"," + "\"subject\":\"" + subject
                    + "\"," + "\"body\":\"" + body + "\"}");
            // 请求
            String form;
            try {
                // 需要自行申请支付宝的沙箱账号、申请appID，并在配置文件中依次配置AppID、密钥、公钥，否则这里会报错。
                form = alipayClient.pageExecute(alipayRequest).getBody();//调用SDK生成表单
                request.setAttribute("form", form);
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            return "mall/alipay";
        } else {
            return "mall/wxpay";
        }
    }

    @GetMapping("/returnOrders/{orderNo}/{userId}")
    public String returnOrderDetailPage(HttpServletRequest request, @PathVariable String orderNo, @PathVariable Long userId) {
        log.info("支付宝return通知数据记录：orderNo: {}, 当前登陆用户：{}", orderNo, userId);
        // NewBeeMallOrder newBeeMallOrder = judgeOrderUserId(orderNo, userId);
        // 将notifyUrl中逻辑放到此处：未支付订单更新订单状态
        // if (newBeeMallOrder.getOrderStatus() != NewBeeMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()
        //         || newBeeMallOrder.getPayStatus() != PayStatusEnum.PAY_ING.getPayStatus()) {
        //     throw new NewBeeMallException("订单关闭异常");
        // }
        // newBeeMallOrder.setOrderStatus((byte) NewBeeMallOrderStatusEnum.ORDER_PAID.getOrderStatus());
        // newBeeMallOrder.setPayType((byte) 1);
        // newBeeMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
        // newBeeMallOrder.setPayTime(new Date());
        // newBeeMallOrder.setUpdateTime(new Date());
        // if (!newBeeMallOrderService.updateByPrimaryKeySelective(newBeeMallOrder)) {
        //     return "error/error_5xx";
        // }
        NewBeeMallOrderDetailVO orderDetailVO = newBeeMallOrderService.getOrderDetailByOrderNo(orderNo, userId);
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }

    @PostMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(Integer payType, String orderNo) {
        log.info("支付宝paySuccess通知数据记录：orderNo: {}, payType：{}", orderNo, payType);
        String payResult = newBeeMallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

    @RepeatSubmit
    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = newBeeMallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @RepeatSubmit
    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = newBeeMallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    /**
     * 判断订单关联用户id和当前登陆用户是否一致
     *
     * @param orderNo 订单编号
     * @param userId  用户ID
     * @return 验证成功后返回订单对象
     */
    private NewBeeMallOrder judgeOrderUserId(String orderNo, Long userId) {
        NewBeeMallOrder newBeeMallOrder = newBeeMallOrderService.getNewBeeMallOrderByOrderNo(orderNo);
        // 判断订单userId
        if (newBeeMallOrder == null || !newBeeMallOrder.getUserId().equals(userId)) {
            throw new NewBeeMallException("当前订单用户异常");
        }
        return newBeeMallOrder;
    }
}
