# ViewFUNCTION 智慧协作管理平台系统架构概述

## ❏ 什么是 ViewFUNCTION 智慧协作管理平台

随着企业的发展，会产生各种类型的工作流程，这些流程会交织在日常业务之中以确定业务运营的步调；而伴随着企业业务的增长，又会给企业带来海量的电子内容资产，这些电子内容资产互相交织在一起并与企业的流程息息相关。为了满足企业的多种业务需求，需要有一个统一的平台来管理内容和流程；通过自动化、简化和优化流程与内容之间的关系，来提高流程绩效、缩短循环周期时间，提升企业生产力。  

ViewFUNCTION 智慧协作管理平台是新一代的、集成式企业`工作流程处理`、`业务内容管理`以及`团队协作应用`系统。它结合了企业内容管理架构以及全面的业务流程管理框架，能够满足企业的各种复杂内容和流程管理需求。通过使用 ViewFUNCTION 智慧协作管理平台，可以方便快捷的构建功能强大的企业业务活动管理应用。同时通过独特地耦合流程、内容，以及与现有 IT 环境进行集成，ViewFUNCTION 智慧协作管理平台可以高效地为那些每天需要制订关键业务决策的人员提供内容支持。

## ❏ 系统架构概述

ViewFUNCTION 智慧协作管理平台主要使用 `Java EE` 技术开发服务层， `JavaScript` 技术开发各类 Web 端应用。前后端统一使用 `REST Web Service` 进行数据通讯。 
 
为实现系统中各种复杂的业务功能，平台内部按照不同的业务功能分为若干独立的业务服务层子功能组件，每个组件提供专门满足某一特定领域的子业务需求所需的全部功能特性。平台的整体业务功通过组合使用各个子功能组件的功能特性而实现。各个业务服务层子功能组件之间相互独立，所有子功能组件都可以独立运行，为各类外部调用应用系统提供相关领域的全部功能特性。

下图为 ViewFUNCTION 智慧协作管理平台架构框图：
![系统架构设计](pic/ViewfunctionArtechDesign.png)

#### ➜ 平台数据存储层结构

根据不同的业务需求以及功能扩展的需要，在 ViewFUNCTION 智慧协作管理平台中使用了多种不同的数据存贮技术和产品来保存业务数据和支撑业务功能需求。以下是这些技术和相应产品的简要说明：

◼︎ **RMDB - MySQL**（ [www.mysql.com](http://www.mysql.com) ）系统中使用关系数据库`MySQL`存储流程系统中的流程节点状态数据，消息系统中的持久化邮件数据和日程调度系统中的定时任务记录数据。

◼︎ **NoSQL - MongoDB**（ [www.mongodb.com](https://www.mongodb.com) ）系统中使用 NoSQL 数据库`MongoDB`存储流程系统中的结构化流程数据，附着在流程上的非结构化文件内容，以及其他的所有和内容关系系统相关的数据。

◼︎ **LDAP - ApacheDS**（ [directory.apache.org](http://directory.apache.org) ）系统中使用 LDAP 服务器 ApacheDS存储所有的活动空间中的人员基本信息和登录信息以及执行安全策略管理。

◼︎ **AMQP - Apache Qpid**（ [qpid.apache.org](http://qpid.apache.org) ）系统使用 AMQP 消息队列服务器 Apache Qpid 来存储中转存储所有执行异步通讯的临时数据和信息。

#### ➜ 平台业务服务层子功能组件

平台内部按照不同的业务功能分为多个业务服务层子功能组件，每个子功能组件都是以一个 `Jave EE` `Web Application`的形式统一的运行在同一个`Web Application Container`中。在 ViewFUNCTION 智慧协作管理平台的默认安装中使用 **Apache Tomcat**（[tomcat.apache.org](http://tomcat.apache.org) ）做为 Web Application Server（Container）。每一个子功能组件都以一个独立的  Application 的形式部署在 Tomcat 中。以下是各个业务服务层子功能组件的简要说明：

◼︎ **VFBAM Service Application** 通过直接在 Java 运行时环境中调用核心中央活动引擎 API 的方式提供了对`业务活动空间`进行业务操作的功能。VFBAM Service Application 提供了一套包含了所有针对`业务活动空间`进行操作功能的 `REST API` 供外部应用系统调用。通过调用这套 REST API， 可以实现例如 *启动业务流程实例*；*查询、分配、完成流程节点任务*，*为流程添加文件* 等各种业务功能。

◼︎ **VFBAM Admin Application** 通过直接在 Java 运行时环境中调用核心中央活动引擎 API 的方式提供了对`业务活动空间`进行管理的功能。VFBAM Admin Application 提供了一个 Web Application 应用，通过在 Web 浏览器中使用该应用。用户可以完成针对特定`业务活动空间`的所有管理工作（例如*部署、更新业务流程定义*；*管理参与者，角色，角色队列*；*实时处理运行中流程实例* 以及 *实时对流程运行进行内容管理* 等）。

◼︎ **VFBAM Client Application** ViewFUNCTION 系统的所有功能都以 `REST API` 的形式提供给外界应用系统使用。通过组合使用各类 REST Service，可以开发出使用 ViewFUNCTION 平台做为后端的各类应用。VFBAM Client Application 是 ViewFUNCTION 平台客户端应用的官方参考实现。它是一个使用 `Javascript` 技术编写的纯 WEB 客户端应用。它组合使用了平台提供的全部 REST Service，为平台的所有功能提供了操作界面。

◼︎ **Message Engine Service** 提供平台系统中所有和消息通讯相关的业务功能。Message Engine Service 使用 Apache Qpid 消息队列技术提供了一个泛用的异步数据交换机制，能够通过系统配置来动态添加消息主题供外部系统调用。针对 ViewFUNCTION 系统的信息和通知功能，Message Engine Service 提供了专门的实现机制，使用 MySQL 将消息队列中的异步临时存储和中转数据内容，持久化存储为ViewFUNCTION 系统的信息和通知，并提供 `REST API`供其他业务服务层功能组件使用。Message Engine Service 本身是一个可以脱离 ViewFUNCTION 体系独立运行的产品。其功能的实现不依赖于 ViewFUNCTION 系统的其他业务服务层功能组件。

◼︎ **Participant Management Service** 提供平台系统中所有和用户身份信息管理和登陆认证相关的业务功能。所有功能的操作都以`REST API` 的形式提供给外界应用系统使用。Participant Management Service 使用 `LDAP` 规范存储和管理用户身份信息。并通过使用 LDAP 服务器自身的用户身份验证机制和安全策略来执行ViewFUNCTION 平台的用户登录管理。默认安装下Participant Management Service 使用 `ApacheDS` 作为后端的 LDAP服务器。通过使用配置文件中的 LDAP 属性映射，Participant Management Service 也可以透明切换使用任何其他的 LDAP 服务器作为用户身份管理的后端数据源。Participant Management Service 本身是一个可以脱离 ViewFUNCTION 体系独立运行的产品。其功能的实现不依赖于 ViewFUNCTION 系统的其他业务服务层功能组件。

◼︎ **Scheduler Management Service** ViewFUNCTION 平台的很多功能依赖于定时任务调度和日程提示。Scheduler Management Service 使用 **Quartz** （[www.quartz-scheduler.org](http://www.quartz-scheduler.org) ）技术构建了定时任务调度系统供其他服务层功能组件调用，定时任务调度数据使用 MySQL持久化存储。在目前的 ViewFUNCTION 平台功能范围内，只有当业务活动定义中使用了任务截止日期提醒功能时才需使用 Scheduler Management Service。
