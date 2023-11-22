# my_blog


##问题记录：

hutool工具地址：https://apidoc.gitee.com/dromara/hutool/
###2023.10.24
1.在配置好注册中心，想通过getway利用nacos的服务名称访问的时候结果一直报错没找到服务名称，原因是因为没有加上  
```
gateway:
      discovery:
        locator:
          enabled: true
```
###2023.10.25  
经过我半天的尝试发现上溯问题并不一定是配置文件引起的，因为我同样的配置在另外一个模块就不生效，通过不断地尝试，发现要自己利用java代码去配置效果会更好  
以下是JWT过滤器使用java配置的代码
```aidl
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/service/**")
                        .filters(f -> f.filter(new JWTFilter()))
                        .uri("lb://blog-server")
                )
                .build();
    }
}
```
- 今日知识点:  
  ------ 今天在整合jwt到getway网关的时候发现getway里面自带了web的依赖包，但是里面的web依赖是基于响应式webflux的，而我们使用的springboot一般都是使用基于servlet容器的。我们要想把jwt整合到getway网关中就必须使用过滤器，而不能使用interceptor拦截器。
  
###2023/11/15  
- BUG解决  
-----解决了一个一直困扰我的bug,就是我把服务已经注册到服务中心上但是在使用lb:blog-server进行映射的时候始终说找不到服务名，我刚开始以为是要以"分组名+服务名的形式"经过尝试后还是会报错，500是报服务名未注册这样，503是指服务不可以经过我翻阅资料发现：  问题原因：
  1. 由于netflix组件进入维护模式，spring cloud 2020.0.x开始移除netflix相关组件，当使用lb://SERVICE时，spring cloud gateway中由于缺少原有的ribbon负载均衡组件，而导致服务不可用。
  2. 对于nacos使用，我想当然地对服务进行命名空间，服务组分类，导致不在同一个命名空间，或者在同一个命名空间不在同一个组的服务，无法被彼此发现，此时，网关也呈现出503 Service Unavailable。

解决方法：
1. 添加相关负载均衡依赖spring-cloud-starter-loadbalancer
```<dependency>

  <groupId>org.springframework.cloud</groupId>

  <artifactId>spring-cloud-starter-loadbalancer</artifactId>

  <version>x.x.x</version>  

</dependency>  
```
2. 使用nacos时将微服务统一划归相同命名空间，相同服务组！由此，我的理解，管理多个项目属于一个命名空间，管理同一个项目多个微服务模块属于一个服务组。

##2023/11/20
- 功能新增  
新增了日志打印，rabbitMq整合发送邮件,密码加密处理功能，redis做缓存验证码，springmvc增加拦截器

##2023/11/21
- 功能新增  
新增了登录，删除博客，新增博客或编辑博客功能 其中删除博客不能传入json形式只能传:  
  [1726897919090499586,1726891386990579714]  
  这种形式因为接收是用list来接收的  

##2023/11/21
- 功能新增  
新增了点赞，收藏，搜索功能，解决了数据库中文变？的bug(解决办法在yml中jdbc链接那设置编码),新增后台两个查询接口
  