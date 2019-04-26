# spring-data-redis-test


https://docs.spring.io/spring-data/data-redis/docs/current/reference/html


1、键空间

http://redisdoc.com/topic/notification.html

配置中，notify-keyspace-events参数可以是如下任意字符的组合：

- K：键空间通知，以 `__keyspace@<db>__` 为前缀，格式是 `__keyspace@<db>__:<KeyPattern>`, db为第几个库，监听所有库用 *  ，KeyPattern为需要监控的key模式，可用通配符
- E：键事件通知，以 `__keysevent@<db>__`为前缀，格式是 `__keyevent@<db>__:<OpsType>`, db为第几个库，监听所有库用 *  ，OpsType表示操作的命令类型
- g：del , expipre , rename 等类型无关的通用命令的通知, ...
- $：String命令
- l：List命令
- s：Set命令
- h：Hash命令
- z：有序集合命令
- x：过期事件（每次key过期时生成）
- e：驱逐事件（当key在内存满了被清除时生成）
- A：g$lshzxe的别名，因此”AKE”意味着所有的事件

2、响应式 redis- reactive reids

见：`https://github.com/kute/pure-webflux-test`中的`com.kute.webflux.config.redis`


