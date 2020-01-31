# IBO Blog System

> IBO (imprint Blog) imprint blog. Presumably everyone wants their blog to record what they think, keep some thoughts, and leave a mark of victory.

Official site: https://www.ijson.net

> The development cycle of this blog is calculated on a monthly basis.After all, I usually go to work. I don't write code every day. I can only write this code on Saturday and Sunday and release it monthly.



[中文](README_ZH.md) | [English](README.md) | [Code upgrade record](https://github.com/ijson/in-blog-boot/wiki/upgrade)


# Installation documentation

1. [Install MongoDB on Mac OSX](https://www.ijson.net/article/cuiyongxu/details/1578799878.html)
2. [IBO 1.0.* Blog installation documentation](https://www.ijson.net/article/cuiyongxu/details/1578800710.html)
3. [IBO 1.0.* Upgrade 1.1. * Upgrade documentation](https://www.ijson.net/article/cuiyongxu/details/1580268690.html)
4. [IBO 1.1.* Installation and use of version services](https://www.ijson.net/article/cuiyongxu/details/1580392186.html)


# Use of technology

1. SpringBoot 2.1.6
2. Mongodb 3.8.2
3. Ibeetl 2.7.22
4. CKEditor 4.12.1
5. Layui(X-admin)


# Feature

* Support for soft article releases such as Markdown
* Simple design and beautiful interface
* Support separate storage of article attachments
* Simple deployment and no container dependencies
* Non-relational storage
* Support article sharing
* Support draft save
* Support custom header information, easy to add Google, Baidu and other large analysis platform header code
* Whether the user can register, whether the user leaves a comment, there is a system switch
* Support dynamic menu
* Support to display blogger information, field display custom
* Support adding friendship link
* Support role management, can customize different roles, including registered user roles, etc.
* Support permission management, convenient management permissions
* Support personal information modification and password modification
* Can preview registered users, and support users to disable delete and other operations
* Support article review and release. Registered users can publish articles after review by the administrator (can be modified in the role)
* Support to view the list of article tags, and support the reverse query of blog posts


# Home Preview

> 1.1.0 Add Friendly Link

![Add a link to the homepage](https://oscimg.oschina.net/oscnet/up-895f05ba2f5b7a46852b5c9e947a950e844.png)
![Home](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/1.jpeg)
![Tag Wall](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/2.jpeg)
![Tag Article List](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/3.jpeg)
![Article Search](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/4.jpeg)
![Article Detail Page](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/5.jpeg)
![Article Sharing](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/6.jpeg)
![Article Likes](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/7.jpeg)
![Hot tags_not shown on the details page](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/8.jpeg)
![Recently published](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/9.jpeg)
![Show me the details page comment after login](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/10.jpeg)
![Comment display](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/21.jpeg)





# 1.1.* Preview of background interface

![Management background](https://oscimg.oschina.net/oscnet/up-50a4a2f597f74377f651e65cb28442d62ec.png)


### Website settings

> Support site name customization and role setting for registered users

![](https://oscimg.oschina.net/oscnet/up-edf5a5895581beaea6771d985d4d8b57a86.png)

### Switch management

> Whether the site allows registration, whether comments are allowed, whether to display blogger information, and whether to launch dynamic menus,
> Currently WeChat login, WeChat settings, Weibo login, Weibo settings, QQ login, Tencent settings will be developed in 1.3. *


![](https://oscimg.oschina.net/oscnet/up-4364eebb00d75e20dd772d4a63317b0b7a2.png)


### Blogger Information Show

> If the display of blogger information is enabled in the switch, which fields are displayed, you need to configure it.


![](https://oscimg.oschina.net/oscnet/up-90a00b6737fa2db3d8ba60a115f9f336845.png)


### Links

> Support adding friendly links for display on the homepage

![](https://oscimg.oschina.net/oscnet/up-91f692837cc840084aa26b3b88c0d185c9b.png)

### Authority management


> The system will pre-fabricate some permissions by default. In the later development of secondary students, you need to add the corresponding link address here for normal access.

![](https://oscimg.oschina.net/oscnet/up-3d469d1e5c65f773208998ba6b92b562821.png)

`Add permission page`

![](https://oscimg.oschina.net/oscnet/up-3601c5f5f821d31a3dcabb5bb3f91671e58.png)

`Set the directory under which permissions are hung`

![](https://oscimg.oschina.net/oscnet/up-7eef92e772b1d696bb10a54ae494f524a49.png)

### Role management

> The following are pre-made system roles.The default system roles are not editable, and new permissions are automatically granted to system roles.

![](https://oscimg.oschina.net/oscnet/up-824a221d89e6800d71efa0a5da3d477712e.png)

`New character animation`

![](https://oscimg.oschina.net/oscnet/up-f2d9b907a95ef3ee33a2c7bb5101270bad2.gif)

> The following is the initial registration role.By default, articles published by the registration role need to be reviewed by the administrator. At present, comments need to be reviewed. It is still under development.
![](https://oscimg.oschina.net/oscnet/up-7162c0cd282d3d309f35e15ffe4482a43ff.png)


### Header management

> For example, Baidu analysis, google analysis, etc., you need to add meta in the header. At this time, you can add it directly here, without manual addition.

![](https://oscimg.oschina.net/oscnet/up-0b709adc7ad92f1cd00a6de03cca2afca87.png)

### Personal information settings

> Personal information settings support the user's personal information settings, if the current person is a blogger, the information will be displayed on the homepage of the website

![](https://oscimg.oschina.net/oscnet/up-722622b8058c94cad736c257d5d9909962e.png)

`change Password`

![](https://oscimg.oschina.net/oscnet/up-55bce95aea6aa15a4d84786b073a51e9af4.png)


### Member list


> Used to show the number of registered user users, and can be disabled or deleted

![](https://oscimg.oschina.net/oscnet/up-0a8a507cc0ed6e5899b12f51c69f9e18735.png)

> The user deletes the list, deletes it as tombstone in the member list, and it is really deleted here. In order for everyone to see intuitively, I have created a test member myself. The member can be restored after the deletion. At the same time, all permissions will be combined restore

![](https://oscimg.oschina.net/oscnet/up-746599f938677a49e0c485fee972d65d5b9.png)

### New / Edit Article

> The new article still saves the original 1.0 style, but this time added the save draft function, the article can't finish writing, the save draft will be written next time

![](https://oscimg.oschina.net/oscnet/up-522c011e11b82e49431a69a3cd14ad2311e.gif)

### Full article list

> A list of all blog data on the current website, only administrators

![](https://oscimg.oschina.net/oscnet/up-fc48885ee15b1e8a9ed1652b0d704ae3043.png)

### Full draft list

> Only management can see all draft information of current website

![](https://oscimg.oschina.net/oscnet/up-5361b1f2194198aff4822ab88d18be234ec.png)

### User draft list

> Draft list information of the current user, this permission exists for all personnel, depending on whether the webmaster has revoked this permission in the role

![](https://oscimg.oschina.net/oscnet/up-6424caf4852b554b07ef8a0f000a8c9c27d.png)

### User Article List

> Article list information of the current user, this permission exists for all personnel, depending on whether the webmaster repossess this permission in the role

![](https://oscimg.oschina.net/oscnet/up-c1c0bb39f49cf09d6c54abe44dc302dc017.png)


### Pending list

> After an ordinary user or a registered user publishes an article, if the administrator is set to publish after approval, the administrator needs to approve here. The following is the article created by `test0001` just created. The administrator view needs to check the content of the article.
> Then perform the consent or rejection operation.After consent, the article will be displayed on the homepage. If it is rejected, it needs to be modified by the publisher of the article and then submitted to the administrator for approval again.The main purpose of this function is to prevent the problem of bad content or malicious advertisements.


![](https://oscimg.oschina.net/oscnet/up-c8ab8b417551bce2a6c63bb2ea755443ebe.png)

`test0001 view list`

![](https://oscimg.oschina.net/oscnet/up-5be6f4962f8f6ca912dc259a4e7e57c3c3a.png)


### List of tags

> The tag list is all the tag tags used in the current article. The management has the right to delete them, modify the tag name operation, and perform reverse checks to see what blog posts currently use the current tag.

![](https://oscimg.oschina.net/oscnet/up-ed3c4975236a8c5ca1fc49ae9b485975848.png)

### Article checklist display

> Find out which blogs are currently used by tags

![](https://oscimg.oschina.net/oscnet/up-a74a443ac8328abb179fe5f76794dfefb3a.png)





# 1.0. * Background interface preview

![Background home](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/11.jpeg)
![Article list](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/12.jpeg)
![List of articles when the menu is collapsed](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/13.jpeg)
![Article List_Background User Details](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/14.jpeg)
![New / Edit Article](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/15.jpeg)
![My Settings-My Information](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/16.jpeg)
![My Settings-Change Password](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/17.jpeg)
![My Settings-Site Settings](https://github.com/ijson/resource/blob/master/in-blog-boot/readme/18.jpeg)