![newbee-mall-plus-logo](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/newbee-mall-plus-logo.png?x-oss-process=image/resize,h_240,w_480)

![Build Status](https://img.shields.io/badge/build-passing-green.svg)
![Version 2.0.0](https://img.shields.io/badge/version-2.0.0-yellow.svg)
[![License](https://img.shields.io/badge/license-GPL3.0-blue.svg)](https://github.com/newbee-ltd/newbee-mall/blob/master/LICENSE)

newbee-mall-plus 项目是 [newbee-mall](https://github.com/newbee-ltd/newbee-mall)
的升级版本，新增了优惠券模块、商品秒杀模块、支付宝支付，后续如果有技术栈和功能的升级也会放在这个仓库里，当前项目中的代码主要由 [@十三](https://github.com/newbee-mall)
和 [@wayn111](https://github.com/wayn111) 共同开发。newbee-mall 项目是一套电商系统，包括 newbee-mall 商城系统及 newbee-mall-admin 商城后台管理系统，基于
Spring Boot 2.X 及相关技术栈开发。

新蜂商城PLUS版本线上预览地址：[http://mall-plus.newbee.ltd](http://mall-plus.newbee.ltd?from=github)，账号可自行注册。

## 升级特点

1. 支持优惠卷使用，包含三种类型的优惠卷：注册赠卷、指定分类用卷、指定商品用卷。 用户可以在首页领取优惠卷后在下订单页面会看到满足可用条件的优惠卷，选择使用后，计算订单金额时，会扣减。
2. 添加秒杀专区，前台页面已经添加秒杀商品倒计时展示，在秒杀开启时间内， 用户对秒杀商品进行下单时，系统内秒杀接口采用redis缓存、令牌桶限流、存储过程等多种手段优化秒杀操作，使得秒杀操作最高支持万人秒杀
   附jmeter100000请求压测截图，配置：家用电脑6核12线程运行内存2g，100000请求时长持续2分5秒、最大响应时长900毫秒、异常率0%、吞吐量798每秒
   ![](./images/linux下jmeter-1s-100000请求.png)
4. 支付宝网页支付集成，new-bee-mall-plus采用的是支付宝沙箱支付环境（真实支付环境需要企业信息），演示站点已经把沙箱买家账号/密码展示出来了
   ![](./images/支付页面.jpg)
5. rabbitMQ集成解耦下单支付模块、elasticsearch集成优化商城搜索展示，暂时还没有实现，如果大家喜欢的话可以提issue😎，作者会加急更新的哦

**坚持不易，如果觉得项目还不错的话可以给项目一个 Star 吧，也是对我自 2019 年开始一直更新这个项目的一种鼓励啦，谢谢各位的支持。**

- newbee-mall 对新手开发者十分友好，无需复杂的操作步骤，**仅需 2 秒就可以启动这个完整的商城项目；**
- newbee-mall **也是一个企业级别的 Spring Boot 大型项目，对于各个阶段的 Java 开发者都是极佳的选择；**
- 你可以把它作为 Spring Boot 技术栈的综合实践项目，**newbee-mall 足够符合要求，且代码开源、功能完备、流程完整、页面交互美观；**
- 技术栈新颖且知识点丰富，学习后可以提升大家对于知识的理解和掌握，**可以进一步提升你的市场竞争力；**
- 对于部分求职中的 Java 开发者，**你也可以将该项目放入求职简历中以丰富你的工作履历；**
- **newbee-mall 还有一些不完善的地方，鄙人才疏学浅，望见谅；**
- **有任何问题都可以反馈给我，我会尽量完善该项目。**

![](https://raw.githubusercontent.com/newbee-ltd/newbee-mall-vue-app/master/static-files/newbee-mall.png)

## 开发部署

```
# 1. 克隆项目

# 2. 导入项目依赖
将newbee-mall-plus目录用idea打开，导入maven依赖

# 3. 安装Mysql8.0+、Redis3.0+、Jdk8+、Maven3.5+

# 4. 导入sql文件
在项目根目录下sql文件夹下，找到`newbee_mall_plus_schema.sql`、`秒杀存储过程.sql`文件，新建mysql数据库newbee_mall_plus_db，导入其中

# 5. 解压项目图片
将项目根目录下upload.zip文件加压缩到D盘upload文件夹中，eg:D:\\upload

# 6. 修改Mysql、Redis连接配置
修改`application-dev.yml`文件中数据连接配置相关信息

# 7. 启动项目
找到`NewBeeMallPlusApplication`文件，右键`run AdminApplication`，启动项目

# 8. 访问
打开浏览器输入：http://localhost:28079/index.html
```

------

**新蜂商城有两个仓库：**

- [新蜂商城 newbee-mall](https://github.com/newbee-ltd/newbee-mall)
- [新蜂商城升级版 newbee-mall-plus](https://github.com/newbee-ltd/newbee-mall-plus)

**新蜂商城前后端分离版本也已经开源，包括四个仓库：**

- [新蜂商城后端接口 newbee-mall-api](https://github.com/newbee-ltd/newbee-mall-api)
- [新蜂商城 Vue2 版本 newbee-mall-vue-app](https://github.com/newbee-ltd/newbee-mall-vue-app)
- [新蜂商城 Vue3 版本 newbee-mall-vue3-app](https://github.com/newbee-ltd/newbee-mall-vue3-app)
- [新蜂商城后台管理系统 Vue3 版本 vue3-admin](https://github.com/newbee-ltd/vue3-admin)

> 更多 Spring Boot 实战项目可以关注十三的另一个代码仓库 [spring-boot-projects](https://github.com/ZHENFENG13/spring-boot-projects)，该仓库中主要是 Spring Boot 的入门学习教程以及一些常用的 Spring Boot 实战项目教程，包括 Spring Boot 使用的各种示例代码，同时也包括一些实战项目的项目源码和效果展示，实战项目包括基本的 web 开发以及目前大家普遍使用的前后端分离实践项目等，后续会根据大家的反馈继续增加一些实战项目源码，摆脱各种 hello world 入门案例的束缚，真正的掌握 Spring Boot 开发。

关注公众号：**程序员十三**，回复"勾搭"进群交流。

![wx-gzh](https://newbee-mall.oss-cn-beijing.aliyuncs.com/wx-gzh/%E7%A8%8B%E5%BA%8F%E5%91%98%E5%8D%81%E4%B8%89-%E5%85%AC%E4%BC%97%E5%8F%B7.png)

## newbee-mall 开发及部署文档

- [**Spring Boot 大型线上商城项目实战教程**](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [技术选型之 Spring Boot](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [前期准备工作及基础环境搭建](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 项目初体验--项目搭建及启动](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 核心详解及源码分析](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 之 DispatchServlet 自动配置源码解读](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 之 Web 开发及 MVC 自动配置分析](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Thymeleaf 模板引擎技术介绍及整合](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Thymeleaf 语法详解及编码实践](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 实践之数据源自动配置及数据库操作](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 实践之整合 Mybatis 操作数据库](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [项目初体验：启动和使用新蜂商城](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城功能模块和流程设计详解](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [前端页面设计及技术选型](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [页面布局制作及跳转逻辑实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 整合 kaptcha 实现验证码功能](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城后台管理系统登录功能实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [登陆拦截器设置并完善身份验证](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [通用分页功能设计与开发实践](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [JqGrid 插件整合制作分页效果](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot 实践之文件上传处理及路径回显](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城轮播图管理模块开发](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城分类管理模块开发-1](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城分类管理模块开发-2](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [富文本编辑器 KindEditor 介绍及整合详解](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城商品类目三级联动功能实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城商品编辑功能实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城商品管理模块功能实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城首页制作-1](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城首页制作-2](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城首页模块配置及功能完善](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城会员的注册/登录功能实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城搜索商品功能实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城购物车功能实现](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [Spring Boot中的事务处理](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城订单确认页和订单生成功能实践](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城个人订单列表和订单详情页制作](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城订单流程功能完善](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [新蜂商城错误页面制作](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)
- [小册总结](https://juejin.im/book/5da2f9d4f265da5b81794d48?referrer=59199e22a22b9d0058279886)

## 联系作者

> 大家有任何问题或者建议都可以在 [issues](https://github.com/newbee-ltd/newbee-mall-plus/issues) 中反馈给我，我会慢慢完善这个项目。

- 我的邮箱：2449207463@qq.com
- QQ技术交流群：791509631 719099151

> newbee-mall-plus 在 GitHub 和国内的码云都创建了代码仓库，如果有人访问 GitHub 比较慢的话，建议在 Gitee 上查看该项目，两个仓库会保持同步更新。

- [newbee-mall-plus in GitHub](https://github.com/newbee-ltd/newbee-mall-plus)
- [newbee-mall-plus in Gitee](https://gitee.com/newbee-ltd/newbee-mall-plus)

## 软件著作权

> 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！

## 页面展示

以下为商城项目的部分页面，由于篇幅所限，无法一一列举，重要节点及重要功能的页面都已整理在下方。

### 商城页面预览

- 秒杀商品列表页面

  ![seckill-page](https://13-doc.oss-cn-beijing.aliyuncs.com/images/book01/springboot-example/seckill-page.png)

- 优惠券列表页面

  ![coupon-list](https://13-doc.oss-cn-beijing.aliyuncs.com/images/book01/springboot-example/coupon-list.gif)

- 我的优惠券页面

  ![my-coupons](https://13-doc.oss-cn-beijing.aliyuncs.com/images/book01/springboot-example/my-coupons.gif)

- 支付宝接入(截的支付宝网站的预览图)

  ![sandbox-pay01](https://13-doc.oss-cn-beijing.aliyuncs.com/images/book01/springboot-example/sandbox-pay01.png)

  ![sandbox-pay02](https://13-doc.oss-cn-beijing.aliyuncs.com/images/book01/springboot-example/sandbox-pay02.png)

- 商城首页 1

  ![index](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/index-01-2020.gif)

- 商城首页 2

  ![index](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/index-02.png)

- 商品搜索

  ![search](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/search.png)

- 购物车

  ![cart](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/cart.png)

- 订单结算

  ![settle](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/settle.png)

- 订单列表

  ![orders](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/orders.png)

- 支付页面

  ![settle](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/wx-pay.png)

### 后台管理页面

- 登录页

  ![login](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/manage-login.png)

- 轮播图管理

  ![carousel](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/manage-carousel.png)

- 新品上线

  ![config](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/manage-index-config.png)

- 分类管理

  ![category](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/manage-category.png)

- 商品管理

  ![goods](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/manage-goods.png)

- 商品编辑

  ![edit](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/manage-goods-edit-new.png)

- 订单管理

  ![order](https://newbee-mall.oss-cn-beijing.aliyuncs.com/poster/product/manage-order.png)

## 感谢

- [spring-projects](https://github.com/spring-projects/spring-boot)
- [thymeleaf](https://github.com/thymeleaf/thymeleaf)
- [mybatis](https://github.com/mybatis/mybatis-3)
- [ColorlibHQ](https://github.com/ColorlibHQ/AdminLTE)
- [tonytomov](https://github.com/tonytomov/jqGrid)
- [t4t5](https://github.com/t4t5/sweetalert)
- [skytotwo](https://github.com/skytotwo/Alipay-WeChat-HTML)
- [EasyCaptcha](https://github.com/whvcse/EasyCaptcha)
- [wangeditor-team](https://github.com/wangeditor-team/wangEditor)
- [Vue](https://github.com/vuejs/vue)
- [Vant](https://github.com/youzan/vant)
