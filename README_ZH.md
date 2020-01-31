# IBO 博客系统

> IBO (imprint Blog) 印记博客.想必大家都希望自己的博客记录自己所想,留有所念,留下胜过的印记

官方站点：https://www.ijson.net

> 本博客开发周期按月计算,毕竟平时都上班,不会天天来编写代码,只能周六日去编写此代码,按月发布



[中文](README_ZH.md)| [英文](README.md)|[代码升级记录](https://github.com/ijson/in-blog-boot/wiki/upgrade)


# 安装文档

1. [Mac OSX 平台安装 MongoDB](https://www.ijson.net/article/cuiyongxu/details/1578799878.html)
2. [IBO 1.0.* 博客安装文档](https://www.ijson.net/article/cuiyongxu/details/1578800710.html)
3. [IBO 1.0.* 升级1.1.*升级文档](https://www.ijson.net/article/cuiyongxu/details/1580268690.html)
4. [IBO 1.1.* 版本服务安装与使用](https://www.ijson.net/article/cuiyongxu/details/1580392186.html)


# 使用技术

1. SpringBoot 2.1.6
2. Mongodb 3.8.2
3. Ibeetl 2.7.22
4. CKEditor 4.12.1
5. Layui(X-admin)


# 特性

* 支持Markdown等软文发布
* 设计简洁，界面美观
* 支持文章附件单独存储
* 部署简单,不依赖容器
* 非关系型存储
* 支持文章分享
* 支持草稿保存
* 支持自定义Header信息,方便添加google,baidu等大型分析平台header头代码
* 用户是否可注册,用户是否发表评论 ,存在系统开关
* 支持动态菜单
* 支持显示博主信息,字段显示自定义
* 支持添加友情连接
* 支持角色管理,可自定制不同角色,包括注册用户角色等
* 支持权限管理,管理权限方便
* 支持个人信息修改及密码修改
* 可预览注册用户,且支持用户禁用删除等操作
* 支持文章审核发布,注册用户发布文章需要管理员审核后发布(可在角色中修改)
* 支持查看文章标签列表,且支持标签反查询博文列表


# 首页预览

> 1.1.0添加友情链接

![首页添加友情链接](https://oscimg.oschina.net/oscnet/up-895f05ba2f5b7a46852b5c9e947a950e844.png)
![首页](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/1.jpeg)
![标签墙](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/2.jpeg)
![标签文章列表](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/3.jpeg)
![文章搜索](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/4.jpeg)
![文章详情页](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/5.jpeg)
![文章分享](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/6.jpeg)
![文章点赞](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/7.jpeg)
![热门标签_未登陆详情页展示](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/8.jpeg)
![最近发表](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/9.jpeg)
![登陆后详情页评论展示](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/10.jpeg)
![评论展示](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/21.jpeg)





# 1.1.* 后台界面预览

![管理后台](https://oscimg.oschina.net/oscnet/up-50a4a2f597f74377f651e65cb28442d62ec.png)

### 网站设置

> 支持网站名称自定义,以及注册用户的角色设置

![网站管理](https://oscimg.oschina.net/oscnet/up-edf5a5895581beaea6771d985d4d8b57a86.png)

### 开关管理

> 网站是否允许注册,是否允许评论,是否展示博主信息,以及是否启动动态菜单,
> 目前微信登录,微信设置,微博登录,微博设置,QQ登录,腾讯设置会在1.3.*中开发


![](https://oscimg.oschina.net/oscnet/up-4364eebb00d75e20dd772d4a63317b0b7a2.png)


### 博主信息展示

> 如果在开关中开启了展示博主信息,具体展示哪些字段,需要配置一下

![](https://oscimg.oschina.net/oscnet/up-90a00b6737fa2db3d8ba60a115f9f336845.png)


### 友情链接

> 支持添加友情链接,用于在首页展示

![](https://oscimg.oschina.net/oscnet/up-91f692837cc840084aa26b3b88c0d185c9b.png)

### 权限管理

> 系统默认会预制部分权限,后期各位同学二次开发时,需要在此处添加对应链接地址,才可正常访问

![](https://oscimg.oschina.net/oscnet/up-3d469d1e5c65f773208998ba6b92b562821.png)

`权限新增页面`

![](https://oscimg.oschina.net/oscnet/up-3601c5f5f821d31a3dcabb5bb3f91671e58.png)

`设置权限挂在哪级目录下`

![](https://oscimg.oschina.net/oscnet/up-7eef92e772b1d696bb10a54ae494f524a49.png)

### 角色管理

> 以下为预制系统角色,默认系统角色不可编辑,且新增的权限会自动授予给系统角色

![](https://oscimg.oschina.net/oscnet/up-824a221d89e6800d71efa0a5da3d477712e.png)

`新增角色动图`

![](https://oscimg.oschina.net/oscnet/up-f2d9b907a95ef3ee33a2c7bb5101270bad2.gif)

> 以下为初始化的注册角色,默认情况下注册角色发表的文章需要管理员审核,目前发表评论需审核,还在开发过程中,预计1.1.4+支持

![](https://oscimg.oschina.net/oscnet/up-7162c0cd282d3d309f35e15ffe4482a43ff.png)


### Header管理

> 例如百度分析,google分析等,需要在header中添加meta,此时可以在此处直接添加,无需手动添加

![](https://oscimg.oschina.net/oscnet/up-0b709adc7ad92f1cd00a6de03cca2afca87.png)

### 个人信息设置

> 个人信息设置支持用户的个人信息设置,如果当前人是博主,信息会展示在网站首页上

![](https://oscimg.oschina.net/oscnet/up-722622b8058c94cad736c257d5d9909962e.png)

`密码修改`

![](https://oscimg.oschina.net/oscnet/up-55bce95aea6aa15a4d84786b073a51e9af4.png)


### 会员列表

> 用于展示目前注册的会员用户数,并可执行禁用或者删除

![](https://oscimg.oschina.net/oscnet/up-0a8a507cc0ed6e5899b12f51c69f9e18735.png)

> 用户删除列表,在会员列表中删除为逻辑删除,此处真删除,为了各位同学能直观看到,自己创建了一个测试会员,会员删除后可以恢复,恢复的同时,会将所有权限一并恢复

![](https://oscimg.oschina.net/oscnet/up-746599f938677a49e0c485fee972d65d5b9.png)

### 新建/编辑文章

> 新建文章还是保存原有1.0风格,不过本次添加了保存草稿功能,文章写不完,保存草稿下次在写

![](https://oscimg.oschina.net/oscnet/up-522c011e11b82e49431a69a3cd14ad2311e.gif)

### 全部文章列表

> 为当前网站所有的博文数据列表,只有管理员能够看到

![](https://oscimg.oschina.net/oscnet/up-fc48885ee15b1e8a9ed1652b0d704ae3043.png)

### 全部草稿列表

> 只有管理能看到,当前网站所有草稿信息

![](https://oscimg.oschina.net/oscnet/up-5361b1f2194198aff4822ab88d18be234ec.png)

### 用户草稿列表

> 当前用户的草稿列表信息,所有人员都存在该权限,视站长是否在角色中将此权限收回

![](https://oscimg.oschina.net/oscnet/up-6424caf4852b554b07ef8a0f000a8c9c27d.png)

### 用户文章列表

> 当前用户的文章列表信息,所有人员都存在该权限,视站长是否在角色中将此权限收回

![](https://oscimg.oschina.net/oscnet/up-c1c0bb39f49cf09d6c54abe44dc302dc017.png)


### 待审核列表

> 普通用户或者注册用户发布文章后,如果管理员设置为审批后发布,需要管理员在此处进行审批,以下为刚刚创建的test0001创建的 文章,管理员视图需要查看下文章内容
> 然后执行同意或者驳回操作,同意后,文章将展示到首页上,如果驳回,需要文章发布者修改后,再次提交给管理员审批,此功能主要目的是防止存在不良内容或恶意广告的问题

![](https://oscimg.oschina.net/oscnet/up-c8ab8b417551bce2a6c63bb2ea755443ebe.png)

`test0001视图列表`

![](https://oscimg.oschina.net/oscnet/up-5be6f4962f8f6ca912dc259a4e7e57c3c3a.png)


### 标签列表

> 标签列表为当前文章所有使用的tag标签,管理有权可以对其进行删除,修改tag名称操作,并可进行反查,看目前使用当前tag的博文有哪些

![](https://oscimg.oschina.net/oscnet/up-ed3c4975236a8c5ca1fc49ae9b485975848.png)

### 文章反查列表展示

> 通过tag查询出目前使用的博客有哪些

![](https://oscimg.oschina.net/oscnet/up-a74a443ac8328abb179fe5f76794dfefb3a.png)





# 1.0.* 后台界面预览


![后台首页](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/11.jpeg)
![文章列表](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/12.jpeg)
![菜单收起时文章列表](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/13.jpeg)
![文章列表_后台用户详情](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/14.jpeg)
![新建/编辑文章](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/15.jpeg)
![我的设置-我的信息](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/16.jpeg)
![我的设置-修改密码](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/17.jpeg)
![我的设置-网站设置](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/18.jpeg)
![我的设置-博主信息](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/19.jpeg)






