# 短视频微信小程序开发

## 0.目录结构

video-wx 微信小程序端

videos-admin 短视频管理后台

videos-dev 微信小程序接口

## 1.技术栈

小程序后台接口：

1、核心框架：Spring Framework 4.3.14.RELEASE

2、SpringBoot：1.5.10.RELEASE

3、持久层框架：MyBatis 3.4.5 + pagehelper 5.1.2

4、MariaDB 10.2.6

5、数据库连接池：阿里巴巴 Druid 1.1.0

6、zookeeper：3.4.11

7、spring-data-redis：1.8.7.RELEASE

8、swagger2：2.4.0

9、FFmpeg：2.0.1.1

短视频后台管理：

1、核心框架：Spring Framework 4.3.8.RELEASE

2、持久层框架：MyBatis 3.2.8 + pagehelper 4.1.3

3、MariaDB 10.2.6

4、数据库连接池：阿里巴巴 Druid 1.1.0

5、jackson：2.7.4

6、slf4j：1.7.21

7、zookeeper：3.4.11

8、前端框架：Bootstrap + Jquery

9、前端分页组件：jqGrid

## 2. 小程序功能模块

注册登陆，个人信息（个人视频，喜欢视频，关注的人）的管理

短视频上传

短视频展示（评论，点赞，分享，下载，举报，视频搜索）

## 3.视频管理后台模块

用户列表管理

视频管理

bgm管理

举报管理

## 4. 项目技术解决方法

-   在添加bgm的时候，后台管理系统在zookeeper上添加结点，小程序接口端监听到添加操作后自动进行下载到本地服务器，并删除zookeeper上的结点

![](D:\gitCommit\images\122.png)

-   登陆时，使用redis-session创建无状态session解决方案，便于集群扩展和权限认证
-   FFmpeg对视频与音频进行合成，并取得视频的封面图
-   使用开源[wsSearchView](https://github.com/mindawei/wsSearchView)进行视频搜索









