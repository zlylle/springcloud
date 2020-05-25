# SpringCloud:



# 1,环境搭建:



## 1,创建父工程,pom依赖

```java
....
```

## 2,创建子模块,pay模块

![](.\图片\sc的3.png)

### 1,子模块名字:

​		cloud_pay_8001

### 2,pom依赖

### 3,创建application.yml

```yml
server:
	port: 8001   
spring:
	application:
		name: cloud-payment-service
	datasource:
    # 当前数据源操作类型
    type: com.alibaba.druid.pool.DruidDataSource
    # mysql驱动类
    driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/cloud20?useUnicode=true&characterEncoding=
            UTF-8&useSSL=false&serverTimezone=GMT%2B8								
    username: root
    password: 123456
mybatis:			
    mapper-locations: classpath*:mapper/*.xml
   	type-aliases-package: com.eiletxie.springcloud.entities
   			它一般对应我们的实体类所在的包，这个时候会自动取对应包中不包括包名的简单类名作为包括包名的别名。多个package之间可以用逗号或者分号等来进行分隔（value的值一定要是包的全）
```

### 4,主启动类    

​		....

### 5,业务类

#### 1,sql

![](.\图片\sc的4.png)

#### 	2,实体类

![](.\图片\sc的5.png)

#### 3,.entity类

![](.\图片\sc的6.png)

#### 4,dao层:

![](.\图片\sc的7.png)

#### 5,mapper配置文件类

​				**在resource下,创建mapper/PayMapper.xml**

![1589770363755](C:\Users\89860\AppData\Roaming\Typora\typora-user-images\1589770363755.png)

#### 6,写service和serviceImpl

![](.\图片\sc的9.png)

![sc的9](.\图片\sc的10.png)

#### 7,controller

![](.\图片\sc的11.png)

![](.\图片\sc的12.png)







## 3,热部署:

![](.\图片\sc的13.png)

![](.\图片\sc的14.png)

.....

.....

....





## 4,order模块

![](.\图片\sc的3.png)

### **1,pom**		

### **2,yml配置文件**

![](.\图片\order模块1.png)

### **3,主启动类**

### **4.复制pay模块的实体类,entity类**

### **5,写controller类**

​		因为这里是消费者类,主要是消费,那么就没有service和dao,需要调用pay模块的方法

​		并且这里还没有微服务的远程调用,那么如果要调用另外一个模块,则需要使用基本的api调用

使用RestTemplate调用pay模块,

​	![](.\图片\order模块2.png)

![](.\图片\order模块3.png)



​	将restTemplate注入到容器

![](.\图片\order模块4.png)

编写controller:

![](.\图片\order模块5.png)



## 5,重构,

新建一个模块,将重复代码抽取到一个公共模块中

### 1,创建commons模块

### 2,抽取公共pom

![](.\图片\commons模块.png)

### 3,entity和实体类放入commons中

![](D:\计算机\springcloud\typora图片\Snipaste_2020-05-24_19-26-34.png)

### 4,使用mavne,将commone模块打包(install),

​		其他模块引入commons







# 2,服务注册与发现



## 6,Eureka:

前面我们没有服务注册中心,也可以服务间调用,为什么还要服务注册?

当服务很多时,单靠代码手动管理是很麻烦的,需要一个公共组件,统一管理多服务,包括服务是否正常运行,等

Eureka用于**==服务注册==**,目前官网**已经停止更新**

​	![](.\图片\Eureka的1.png)



![](.\图片\Eureka的2.png)

![](.\图片\Eureka的3.png)



 ![](.\图片\Eureka的4.png)



### **单机版eureka:**

#### **1,创建项目cloud_eureka_server_7001**

#### **2,引入pom依赖**

​		eurka最新的依赖变了

![](.\图片\Eureka的5.png)

#### 3,配置文件:

![](.\图片\Eureka的6.png)

#### 4,主启动类	

![](.\图片\Eureka的7.png)

#### **5,此时就可以启动当前项目了**

#### **6,其他服务注册到eureka:**

比如此时pay模块加入eureka:

##### 1.主启动类上,加注解,表示当前是eureka客户端

![](.\图片\Eureka的10.png)

##### 2,修改pom,引入

![](.\图片\Eureka的8.png)

##### 3,修改配置文件:

![](.\图片\Eureka的9.png)

##### 4,pay模块重启,就可以注册到eureka中了





**order模块的注册是一样的**





### 集群版eureka:

#### 集群原理:

![](.\图片\Eureka的11.png)

 ```java
1,就是pay模块启动时,注册自己,并且自身信息也放入eureka
2.order模块,首先也注册自己,放入信息,当要调用pay时,先从eureka拿到pay的调用地址
3.通过HttpClient调用
 	并且还会缓存一份到本地,每30秒更新一次
 ```

![](.\图片\Eureka的12.png)

**集群构建原理:**

​		互相注册

![](.\图片\Eureka的13.png)



#### **构建新erueka项目**

名字:cloud_eureka_server_7002

##### 1,pom文件:

​		粘贴7001的即可

##### 2,配置文件:

​		在写配置文件前,修改一下主机的hosts文件

![](.\图片\Eureka的14.png)

首先修改之前的7001的eureka项目,因为多个eureka需要互相注册

![](.\图片\Eureka的15.png)

然后修改7002

​			**7002也是一样的,只不过端口和地址改一下**

##### 3,主启动类:

​		复制7001的即可

##### 4,然后启动7001,7002即可

![](D:\计算机\springcloud\typora图片\1.png)

![](D:\计算机\springcloud\typora图片\2.png)

![3](D:\计算机\springcloud\typora图片\3.png)

效果：两个服务互相注册，服务正常访问

#### 将pay,order模块注册到eureka集群中:

##### 1,只需要修改配置文件即可:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8417.png)

##### 2,两个模块都修改上面的都一样即可

​			然后启动两个模块

​			要先启动7001,7002,然后是pay模块8001,然后是order(80)



### 3,将pay模块也配置为集群模式:

#### 0,创建新模块,8002

​	名称: cloud_pay_8002

#### 1,pom文件,复制8001的

#### 2,配置文件复制8001的

​		端口修改一下,改为8002

​		服务名称不用改,用一样的

#### 3.主启动类,复制8001的

#### 4,mapper,service,controller都复制一份

​		然后就启动服务即可

​		此时访问order模块,发现并没有负载均衡到两个pay,模块中,而是只访问8001

​		虽然是使用RestTemplate访问的微服务,但是也可以实现负载均衡(eureka整合了ribbon)

​		![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8418.png)

**注意这样还不可以,需要让RestTemplate开启负载均衡注解,还可以指定负载均衡算法,默认轮询**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8419.png)







### 4,修改服务主机名和ip在eureka的web上显示

比如修改pay模块

#### 1,修改配置文件:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8420.png)





### 5,eureka服务发现:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8421.png)

以pay模块为例

#### 1,首先添加一个注解,在controller中

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8422.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8423.png)



#### 2,在主启动类上添加一个注解

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8424.png)

**然后重启8001.访问/payment/discover**y



### 6,Eureka自我保护:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8426.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8427.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8425.png)



![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8428.png)



**eureka服务端配置:**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8429.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8430.png)

​			**设置接受心跳时间间隔**



**客户端(比如pay模块):**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Eureka%E7%9A%8431.png)



**此时启动erueka和pay.此时如果直接关闭了pay,那么erueka会直接删除其注册信息**

# 3,服务调用



## 10,Ribbon负载均衡:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon.png)

**Ribbon目前也进入维护,基本上不准备更新了**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%842.png)

**进程内LB(本地负载均衡)**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%845.png)





**集中式LB(服务端负载均衡)**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%844.png)







**区别**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%843.png)



**Ribbon就是负载均衡+RestTemplate**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%846.png)



![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%847.png)



![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%848.png)







### 使用Ribbon:

#### 1,默认我们使用eureka的新版本时,它默认集成了ribbon:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%849.png)

**==这个starter中集成了reibbon了==**



#### 2,我们也可以手动引入ribbon

**放到order模块中,因为只有order访问pay时需要负载均衡**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8410.png)



#### 3,RestTemplate类:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8411.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8412.png)

```java
RestTemplate的:
		xxxForObject()方法,返回的是响应体中的数据
    xxxForEntity()方法.返回的是entity对象,这个对象不仅仅包含响应体数据,还包含响应体信息(状态码等)
```





#### Ribbon常用负载均衡算法:

**IRule接口,Riboon使用该接口,根据特定算法从所有服务中,选择一个服务,**

**Rule接口有7个实现类,每个实现类代表一个负载均衡算法**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8414.png)







#### 使用Ribbon:

**==这里使用eureka的那一套服务==**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8415.png)

**==也就是不能放在主启动类所在的包及子包下==**

##### 1,修改order模块

##### 2,额外创建一个包

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8416.png)

##### 3,创建配置类,指定负载均衡算法

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8417.png)

##### 4,在主启动类上加一个注解

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8418.png)

**表示,访问CLOUD_pAYMENT_SERVICE的服务时,使用我们自定义的负载均衡算法**

#### 自定义负载均衡算法:

##### 1,ribbon的轮询算法原理

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8419.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8421.png)



##### 2,自定义负载均衡算法:

**1,给**pay模块(8001,8002),的controller方法添加一个方法,返回当前节点端口

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8423.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8422.png)

**2,修改order模块**

去掉@LoadBalanced

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8424.png)



##### 3,自定义接口

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8429.png)

​					==具体的算法在实现类中实现==

##### 4,接口实现类

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8425.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8426.png)



##### 5,修改controller:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8427.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Ribbon%E7%9A%8428.png)



##### 6,启动服务,测试即可	

![](D:\计算机\springcloud\typora图片\Snipaste_2020-05-25_09-19-49.png)

## 11,OpenFeign

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%841.png)

**是一个声明式的web客户端,只需要创建一个接口,添加注解即可完成微服务之间的调用**



![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%842.png)

==就是A要调用B,Feign就是在A中创建一个一模一样的B对外提供服务的的接口,我们调用这个接口,就可以服务到B==



### **Feign与OpenFeign区别**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%843.png)





### 使用OpenFeign

```java
之前的服务间调用,我们使用的是ribbon+RestTemplate
		现在改为使用Feign
```

#### 1,新建一个order项目,用于feign测试

名字cloud_order_feign-80

#### 2,pom文件

#### 3,配置文件

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%844.png)

#### 4,主启动类

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%845.png)

#### 5,fegin需要调用的其他的服务的接口

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%846.png)

#### 6,controller

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%847.png)

#### 7测试:

启动两个erueka(7001,7002)

启动两个pay(8001,8002)

启动当前的order模块



**Feign默认使用ribbon实现负载均衡**





### OpenFeign超时机制:

==OpenFeign默认等待时间是1秒,超过1秒,直接报错==

#### 1,设置超时时间,修改配置文件:

**因为OpenFeign的底层是ribbon进行负载均衡,所以它的超时时间是由ribbon控制**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%848.png)

### OpenFeign日志:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%849.png)



**OpenFeign的日志级别有:**
![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%8410.png)





#### 1,使用OpenFeign的日志:

**实现在配置类中添加OpenFeign的日志类**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%8411.png)

#### 2,为指定类设置日志级别:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%8413.png)

**配置文件中:**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Feign%E7%9A%8412.png)



#### 3,启动服务即可



# 4,服务降级:



## 12,Hystrix服务降级

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%842.png)



![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%843.png)

作用:服务降级，服务熔断，接近实时的监控等



### hystrix中的重要概念:

#### 1,服务降级

**比如当某个服务繁忙,不能让客户端的请求一直等待,应该立刻返回给客户端一个备选方案**



#### 2,服务熔断

**当某个服务出现问题,卡死了,不能让用户一直等待,需要关闭所有对此服务的访问**

​			**然后调用服务降级**



#### 3,服务限流

**限流,比如秒杀场景,不能访问用户瞬间都访问服务器,限制一次只可以有多少请求**

### 使用hystrix,服务降级:

#### 1,创建带降级机制的pay模块 :

名字: cloud-hystrix-pay-8007

##### 2,pom文件

##### 3,配置文件

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%845.png)

##### 4,主启动类

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%848.png)

##### 5,service

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%846.png)

##### 6controller

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%847.png)

##### 7,先测试:

```java
此时使用压测工具,并发20000个请求,请求会延迟的那个方法,
		压测中,发现,另外一个方法并没有被压测,但是我们访问它时,却需要等待
		这就是因为被压测的方法它占用了服务器大部分资源,导致其他请求也变慢了
```



##### 8,先不加入hystrix,



#### 2,创建带降级的order模块:

##### 1,名字:  cloud-hystrix-order-80

##### 2,pom

##### 3,配置文件

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%849.png)

##### 4,主启动类

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8411.png)

##### 5,远程调用pay模块的接口:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8412.png)

##### 6,controller:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8413.png)

##### 7,测试

​			启动order模块,访问pay

​			再次压测2万并发,发现order访问也变慢了

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8414.png)



**解决:**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8415.png)

##### ![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8416.png)







#### 3,配置服务降级:

##### 1,修改pay模块

###### 1,为service的指定方法(会延迟的方法)添加@HystrixCommand注解

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8417.png)

###### 2,主启动类上,添加激活hystrix的注解

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8418.png)

###### 3,触发异常

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8419.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8420.png)**可以看到,也触发了降级**



##### 2,修改order模块,进行服务降级

一般服务降级,都是放在客户端(order模块),

###### 1,修改配置文件:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8422.png)

###### **2,主启动类添加直接,启用hystrix:**

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8423.png)

​	

###### 3,修改controller,添加降级方法什么的

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8424.png)



###### 4,测试

启动pay模块,order模块,

**注意:,这里pay模块和order模块都开启了服务降级**

​			但是order这里,设置了1.5秒就降级,所以访问时,一定会降级

 

##### 4,重构:

**上面出现的问题:**
		1,降级方法与业务方法写在了一块,耦合度高

​		2.每个业务方法都写了一个降级方法,重复代码多

##### **解决重复代码的问题**:

**配置一个全局的降级方法,所有方法都可以走这个降级方法,至于某些特殊创建,再单独创建方法**

###### 1,创建一个全局方法

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8426.png)

###### 2,使用注解指定其为全局降级方法(默认降级方法)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8427.png)

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8425.png)



###### 3,业务方法使用默认降级方法:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8428.png)



###### 4,测试:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8429.png)











##### 解决代码耦合度的问题:

修改order模块,这里开始,pay模块就不服务降级了,服务降级写在order模块即可

###### 1,Payservice接口是远程调用pay模块的,我们这里创建一个类实现service接口,在实现类中统一处理异常

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8430.png)

###### 2,修改配置文件:添加:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8431.png)

###### 3,让PayService的实现类生效:

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8432.png)

```java
它的运行逻辑是:
		当请求过来,首先还是通过Feign远程调用pay模块对应的方法
    但是如果pay模块报错,调用失败,那么就会调用PayMentFalbackService类的
    当前同名的方法,作为降级方法
```

###### 4,启动测试

启动order和pay正常访问--ok

==此时将pay服务关闭,order再次访问==

![](D:/%E8%AE%A1%E7%AE%97%E6%9C%BA/springcloud/%E5%9B%BE%E7%89%87/Hystrix%E7%9A%8433.png)

可以看到,并没有报500错误,而是降级访问==实现类==的同名方法

这样,即使服务器挂了,用户要不要一直等待,或者报错

问题:

​		**这样虽然解决了代码耦合度问题,但是又出现了过多重复代码的问题,每个方法都有一个降级方法**





