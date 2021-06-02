/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.controller.common;

import ltd.newbee.mall.common.NewBeeMallException;
import ltd.newbee.mall.util.HttpUtil;
import ltd.newbee.mall.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * newbee-mall全局异常处理
 */
@RestControllerAdvice
public class NewBeeMallExceptionHandler {

    Logger log = LoggerFactory.getLogger(NewBeeMallExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest req) {
        Result result = new Result();
        result.setResultCode(500);
        // 区分是否为自定义异常
        if (e instanceof NewBeeMallException) {
            result.setMessage(e.getMessage());
        } else {
            log.error(e.getMessage(), e);
            result.setMessage("未知异常");
        }
        if (HttpUtil.isAjaxRequest(req)) {
            return result;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.addObject("stackTrace", e.getStackTrace());
        modelAndView.addObject("author", "十三");
        modelAndView.addObject("ltd", "新蜂商城");
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}
