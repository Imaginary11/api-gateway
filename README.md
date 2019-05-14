## api-gateway
基于SpringCloudGateway 实现的网关，包含IP黑名单，接口白名单，JWT权限认证等功能，拓展简单，易于上手。


## 组件介绍
![](http://10.246.84.77:4999/server/../Public/Uploads/2019-05-14/5cda827049ebb.png)

### 1.IP黑名单检查
该组件永远会被执行! 用户请求时第一步先经过黑名单检查,会获取用户的IP地址,如果用户的IP地址在全局黑名单中,结束请求并响应状态码:403;反则继续执行处理...
存储形式 redis string
- key : blacklist_ip:{ip}  
- ttl : one day

### 2.接口白名单检查
支持如下格式
 * /nc/*
 * /nc/sms/*
 * /nc/sms/deliver
 * /nc/sms/{id}
 * /nc/sms/{id}/info

存储形式 redis hash
- whitelist_api
- whitelist_api_pattern
- whitelist_service

### 3.权限认证
当创建API时开启了安全认证,该组件会被执行! 组件会将流程交给权限认证插件,权限认证插件负责做相关处理后决定将流程交给下一个组件处理或结束请求
默认全局开启 jwt 认证 ，接口白名单除外

### 4.动态路由
支持http/lb 协议路由。



## todo
-  参数检查
-  访问限制
-  前置处理器
-  中心处理器(主处理器)
-  后置处理器
-  异常处理器
-  HTTP/HTTPS
-  自定义服务

