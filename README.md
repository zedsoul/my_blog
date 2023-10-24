# my_blog
zlc_blog
问题记录：
2023.10.24
1.在配置好注册中心，想通过getway利用nacos的服务名称访问的时候结果一直报错没找到服务名称，原因是因为没有加上  
```
gateway:
      discovery:
        locator:
          enabled: true
```
