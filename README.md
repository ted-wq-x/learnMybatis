MyBatis SQL Mapper Framework for Java
=====================================

[![Build Status](https://travis-ci.org/mybatis/mybatis-3.svg?branch=master)](https://travis-ci.org/mybatis/mybatis-3)
[![Coverage Status](https://coveralls.io/repos/mybatis/mybatis-3/badge.svg?branch=master&service=github)](https://coveralls.io/github/mybatis/mybatis-3?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56199c04a193340f320005d3/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56199c04a193340f320005d3)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Stack Overflow](http://img.shields.io/:stack%20overflow-mybatis-brightgreen.svg)](http://stackoverflow.com/questions/tagged/mybatis)
[![Project Stats](https://www.openhub.net/p/mybatis/widgets/project_thin_badge.gif)](https://www.openhub.net/p/mybatis)

![mybatis](http://mybatis.github.io/images/mybatis-logo.png)

The MyBatis SQL mapper framework makes it easier to use a relational database with object-oriented applications.
MyBatis couples objects with stored procedures or SQL statements using a XML descriptor or annotations.
Simplicity is the biggest advantage of the MyBatis data mapper over object relational mapping tools.

Essentials
----------

* [See the docs](http://mybatis.github.io/mybatis-3)
* [Download Latest](https://github.com/mybatis/mybatis-3/releases)
* [Download Snapshot](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/mybatis/)

# 学习源码

这个框架的本质是jdbc。

版本：3.4.6-SNAPSHOT

入口类：org.apache.ibatis.session.SqlSessionFactoryBuilder

整体的架构图：

![](http://img.blog.csdn.net/20141028140852531?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHVhbmxvdWlz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 进度

- [x] DataSource：org.apache.ibatis.datasource
- [x] Parsing：org.apache.ibatis.parsing
- [x] Transaction：org.apache.ibatis.transaction
- [x] cache：org.apache.ibatis.cache


## 设计模式

1. 工厂模式
2. 建造者
3. 装饰者：对于缓存的包装，所有包装器都是继承与Cache接口，并且构造器都含有Cache参数，当然这是装饰者模式的构造，在Mybatis中使用建造者模式构建cache（默认是lru）对象，并且有默认的装饰器
4. 单例
5. 代理
6. 组合
7. 模板方法
8. 适配器
9. 迭代器
10. 责任链

## 缓存机制

之前一直很好奇，为什么PerpetualCache中使用的是hashmap，这怎么保证线程安全性？看了代码之后才知道，在创建cache的时候，都使用了SynchronizedCache
进行了包装，所以是有的方法都是线程安全的。`CacheBuilder.setStandardDecorators()`

## 重要组件介绍



## 插件机制

对于mybatis的插件机制起作用的几个地方是：

* ParameterHandler
* ResultSetHandler
* StatementHandler
* Executor

在Configuration类当中都能够看到插件的调用。 

## 查询流程介绍

DefaultSqlSession-->Executor-->StatementHandler(ParameterHandler，ResultHandler，TypeHandler)

其中StatementHandler是对sql语句进行预先处理的，ParameterHandler对参数进行处理，ResultHandler对结果进行处理，TypeHandler对数据库类型和java类型转换。

StatementHandler的创建使用的是RoutingStatementHandler，该类是根据StatementType进行路由用的，简单工厂模式。

### Executor

![](pic/Executor.png)

对于Executor结构，主要使用的是装饰者模式，如CacheExecutor（二级缓存）；模板方法模式，如BaseExecutor。
首先对于BaseExecutor来说，一个重要的属性就是一级缓存（默认是session级别的，还有一个statement级别），该类负责一级缓存相关的操作，同时需要注意，一级缓存是在构造器中初始化的，
所以无法关闭，消除缓存影响的方式是，每当使用过一次session之后，清除缓存，也就是需要在sql语句上配置flushCache=true（该属性只针对query操作），
或者配置成statement，那样所有的一级缓存都会失效，那样每次查询都会清除缓存，update,delete,insert都会清除缓存。

// TODO Executor下面的几个实现类

### StatementHandler

![](pic/StatementHandler.png)

其中RoutingStatementHandler相当于是一个工厂，按照StatementType创建StatementHandler对象。
而BaseStatementHandler下的结构则是模板方法模式：

* SimpleStatementHandler：相当于是最基本的jdbc的方法调用，如Connection.createStatement().executeQuery()，感觉这个类基本没人使用，哈哈！
* PreparedStatementHandler：使用了预编译，如Connection.PrepareStatement()
* CallableStatementHandler：执行存储过程的

默认的是PreparedStatement(在MappedStatement的构造器中定义)，这个参数的指定时在sql语句中（如xml上）。

### ResultSetHandler

默认实现是DefaultResultSetHandler


### ParameterHandler(针对PreparedStatement)

默认实现是DefaultParameterHandler，使用TypeHandler进行处理。


### TypeHandler(针对PreparedStatement)

实现类有很多，用于对入参和返回参数进行类型处理，转换成java类型已经查询参数设置，也就是针对PreparedStatement进行参数设置和从resultSet获取值。

针对不同的类型有不同的实现类，所以子类很多，这里就不列举了。这里说下BaseTypeHandler类，该类的设计依然是模板模式。同时该类也继承了TypeReference抽象类
，这个类类似于工具类。






