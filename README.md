# RedisLearning

# 一、Redis 概述

## 1. 简介

> 数据库排名：[DB-Engines Ranking - popularity ranking of database management systems](https://db-engines.com/en/ranking)
>
> ![image.png](assets/image0.png?t=1726039464112)

Redis (Remote Dictionary Server)；一个开源的 key-value 存储系统；它支持的主要数据类型包括：

- String（字符串）
- list（链表）
- set（集合）
- zset（或称为sorted set，有序集合）
- hash（哈希类型）

> 1. 支持 push/pop 、add/remove，获取交集、并集、差集等一些相关操作，操作是原子性的。
> 2. redis 支持各种不同方式的排序；
> 3. redis （与 memcatched 相同）数据存在内存中；
> 4. redis 会周期性的把更新的数据写入磁盘，或者把修改的操作追加到记录文件；rdb / aof (持久化)
> 5. redis 支持集群，实现 master-slave(主从)同步操作；

## 2. 应用场景 ❤️

- 缓存: 配合关系型数据库做高速缓存；
- 计数器: 进行自增自减运算；
- 时效性数据: 利用 expire 过期，例如手机验证码功能；
- 海量数据统计: 利用位图，存储用户是否是会员、日活统计、文章已读统计、是否参加过某次活动；
- 会话缓存: 使用 redis 统一存储多台服务器用到的 session 信息；
- 分布式队列/阻塞队列:通过 list 双向链表实现读取和阻塞队列；
- 分布式锁: 使用 redis 自带 setnx 命令实现分布式锁；
- 热点数据存储: 最新文章、最新评论，可以使用 redis 的 list 存储，ltrim 取出热点数据，删除旧数据；
- 社交系统: 通过 Set 功能实现，交集、并集实现获取共同好友，差集实现好友推荐，文章推荐；
- 排行榜: 利用 sorted-set 的有序性，实现排行榜功能，取 top n；
- 延迟队列: 利用消费者和生产者模式实现延迟队列；
- 去重复数据: 利用Set集合，去除大量重复数据；
- 发布/订阅消息: pub/sub模式；

## 3. Redis 安装

(1) 下载地址：https://download.redis.io/releases/redis-6.2.6.tar.gz

> 在 linux 环境下安装，需要安装 gcc，因为 redis 是 C 语言编写的
>
>> 通过 `gcc -v` 命令，确定是否安装，`apt install gcc` 安装
>>

- 下载命令：`wget https://download.redis.io/releases/redis-6.2.6.tar.gz`
- 解压命令：`tar -zxvf redis-6.2.6.tar.gz`
- 编译 redis：`cd redis-6.2.6 && make`
- 安装 redis：`make install`

  > 默认安装位置：/usr/local/bin
  >

![image.png](assets/image1.png)

(2) 安装后文件概述

- redis-benchmark：性能测试工具
- redis-check-aof：修复 aof 持久化文件
- redis-check-rdb：修复 rdb 持久化文件
- redis-cli：redis 命令行工具
- redis-sentinel：redis 集群哨兵使用
- redis-server：启动 redis

## 4. Redis 启动和退出

> Redis 默认占用端口号 6379

#### 4.1 前台启动（不推荐）

使用命令 `redis-server` 调用 redis-server，启动后窗口不能操作

![image.png](assets/image2.png)

> 查看 redis 进程：`ps -ef | grep redis`

![image.png](assets/image4.png)

#### 4.2 后台启动（推荐）

(1) 进入 /redis-6.2.6 下，找到 redis.conf 文件，通过 `cp redis.conf redis_1.conf` 进行复制

(2) 修改配置文件：`vim redis_1.conf`

![image.png](assets/image6.png)

（3）后台启动：`redis-server redis_1.conf`

![image.png](assets/image7.png)

#### 4.3 启动命令行

> 先启动 redis-server 再启动命令行工具 redis-cli

- 使用命令：`redis-cli`

![image.png](assets/image8.png)

- 输入命令 `ping` 测试

![image.png](assets/image9.png)

#### 4.4 退出 redis

- 输入命令：`shutdown`，回车
- 再输入命令：`exit`

![image.png](assets/image109.png)

## 5. Redis 常用命令

> 进入到 redis-cli 命令行使用

- select：默认16个数据库，类似数组下标从 0 开始，初始默认使用 0 号库，使用 select 命令进行切换，语法：

  ```sql
  select <dbid>
  ```

  ![image.png](assets/image12.png)
- 统一密码管理，所有库使用同样的密码；
- dbsize：查看当前数据库的 key 的数量；

  ![image.png](assets/image14.png)
- set < key> < value>：存入键值对；

  ![image.png](assets/image15.png)
- get < key>：获取 key 对应的值；

  ![image.png](assets/image28.png)
- flushdb：清空当前库；

  ![image.png](assets/image16.png)
- flushall：清空全部库；
- keys *：查看当前库的所有 key；

  ![image.png](assets/image17.png)
- exists < key>：判断某个 key 是否存在；

  ![image.png](assets/image189.png)
- type < key>：查看 key 的类型；

  ![image.png](assets/image21.png)
- object encoding < key>：查看底层的数据类型；

  ![image.png](assets/image22.png)
- del < key>：删除掉指定 key 数据；

  ![image.png](assets/image23.png)
- unlink < key>：根据选择非阻塞删除，仅将 key 从 keyspace 元数据中删除，真正的删除会在后续中做异步操作；（效率）
- expire < key> seconds：给 key 设置过期时间，以秒为单位；

  ![image.png](assets/image24.png)
- ttl < key>：查看 key 还有多少秒过期；

  ![image.png](assets/image26.png)

# 二、Redis 常用的五种数据类型

## 1. String 字符串

### （1）简介

> - String 类型在 redis 中最常见的一种类型;
> - String 类型是二进制安全的，可以存放字符串、数值、json、图像数据；“二进制安全”指的是以下几点：
>   - 任意数据存储：String 类型可以存储任何形式的二进制数据，由于Redis内部将字符串视为一个字节数组，因此它可以处理任何字节序列；
>   - 不解释数据：Redis 不会尝试去解析存储在 String 中的数据
>   - 传输安全：在Redis服务器和客户端之间传输数据时，数据不会被修改或解释，从而保证了数据的完整性
> - value 存储最大数据量是 512M

### （2）常用命令

- set < key> < value>：添加键值对，其中：
  - nx 参数：是当数据库中 key 不存在时，可以将 key-value 添加到数据库；
    - 在实现分布式锁时，可以使用 nx 参数来确保锁只能由一个客户端获取
  - xx 参数: 当数据库 key 存在时，可以将 key-value 添加到数据；
    - nx 与 xx 互斥；
  - ex 参数：设置 key-value 添加到数据库，并设置 key 的超时时间(以秒钟为单位)；
  - px 参数：设置 key-value 添加到数据库，并设置 key 的超时时间(以毫秒为单位)；
    - ex 与 px 互斥；

![image.png](assets/image29.png)

- get < key>：查询对应的键值；
- append < key> < value>：将给定的值追加到 key 的末尾；

![image.png](assets/image30.png)

- strlen < key>：获取值的长度；
- setnx < key> < value>：添加键值对，当数据库中 key 不存在时，可以将 key-value 添加到数据库；
- setex < key> < timeout>< value>：添加键值对，同时设置过期时间(以秒为单位)；
- incr < key>：将 key 中存储的数字加 1 处理，只能对数字值操作；
- decr < key>：将 key 中存储的数字减 1 处理，只能对数字值操作；
- incrby < key> < increment>：将key中存储的数字值增加指定步长的数值, 如果是空，值为步长。(具有原子性);
- decrby < key> < increment>：将key中存储的数字值减少指定步长的数值, 如果是空，值为步长。(具有原子性);
- mset < key1>< value1> [< key2>< value2>...]：同时设置1个或多个 key-value 值；
- mget < key1>< value1> [< key2>< value2>...]：同时获取1个或多个 value；
- msetnx < key1>< value1> [< key2>< value2>...]：当所有给定的 key 都不存在时，同时设置1个或多个 key-value 值(具有原子性)；
- getrange/substr < key>< start>< end>：将给定 key，获取从 start (包含)到 end (包含)的值；
- setrange < key>< offset>< value>：从偏移量 offset 开始，用 value 去覆盖 key 中存储的字符串值；
- getset < key>< value>：对给定的 key 设置新值，同时返回旧值，如果 key 不存在，则添加一个 key-value 值；

### （3）应用场景 ⭐️

1. 单值缓存处理

   > set key value / get key
   >
2. 对象缓存

   > - set stu:001 value(json)
   > - mset stu:001:name zhangsan stu:001:age 18 stu:001:gender 男
   >
3. 分布式锁

   > - setnx key:001 true // 返回 1 代表加锁成功
   > - setnx key:001 true // 返回 0 代表加锁失败
   > - //..业务操作
   > - del key:001 // 执行完业务释放锁
   > - set key:001 true ex 20 nx // 防止程序意外终止导致死锁
   >
4. 计数器

   > - incr article:read:1001 // 统计文章阅读数量
   >
5. 分布式系统全局序列号

   > - incrby orderid 100 //批是生成序列号
   >

## 2. List 列表

### （1）简介

- Redis 列表是简单的字符串列表，单键多值；
- 按照插入顺序排序。可以添加一元素到列表的头部(左边)或者尾部(右边)；
- 一个列表最多可以包含 $2^{32}-1$ 个元素；
- 底层是一个**双向链表**：❤️
  - 对两端的操作性能很高；
  - 通过索引下标的操作中间的节点性能会较差；

### （2）常用命令

- lpush < key> < value1> [< value2> < value3>...]：从左侧插入一个或多个值；
- lpushx < key> < value1> [< value2> < value3>...]：将一个或多个值插入到已存在的列表头部;
- lrange < key> < start> < stop>：获取列表指走范围内的元素，0左边第1位，-1右边第1位，0~-1取出所有；
- rpush < key> < value1> [< value2> < value3>...]：从右侧插入一个或多个值；
- rpushx < key> < value1> [< value2> < value3>...]：将一个或多个值插入到已存在的列表尾部;
- lpop < key> [count]：移除并获取列表中左边第1个元素，count 表明获取的总数量，返回的为移除的元素；
- rpop < key> [count]：移除并获取列表中右边第1个元素，count 表明获取的总数量，返回的为移除的元素；
- rpoplpush < source>< destination>：移除源列表的尾部的元素(右边第一个)，将该元素添加到目标列表的头部(左边第一个)，并返回；
- lindex < key> < index>：通过索引获取列表中的元素；
- llen < key>：获取列表长度；
- linsert < key> before|after < pivot>< element>: 在 < pivot>基准元素前或者后面插入<element>
  - 如果 < key> 不存在，返回 0；
  - 如果 < pivot> 不存在，返回-1；
  - 如果操作成功，返回执行后的列表长度；
- lrem < key> < count> < element>：根据< count>的值，移除列表中与参数< element>相等的元素
  - count=0 移除表中所有与参数相等的值；
  - count>0 从表头开始向表尾搜索，移除与参数相等的元素，数量为 count；
  - count<0 从表尾开始向表头搜索，移除与参数相等的元素，数量为 count 的绝对值；
- lset < key> < index> < element>：设置给定素引位置的值；
- ltrim < key> < start> < stop>：对列表进行修剪，只保留给定区间的元素，不在指定区间的被删除；（包含端点）
- brpop < key> < timeout>：阻塞式移除指定 key 的元素，如果 key 中没有元素，就等待，直到有元素或超时，执行结束；

### （3）应用场景 ❤️

1. 数据队列

   > - 堆栈：stack = lpush + lpop
   > - 队列：queue = lpush + rpop
   > - 阻塞式消息队列：block_mq = lpush + brpop
   >
2. 订阅号时间线

   > lrange key start stop
   >

## 3. Hash 哈希

### （1）简介

是一个 string 类型的键和 value(对象)，特别适合于存储对象，类似于 java 里面学习的 Map <String, Object>；

> 假设场景: 需要在 redis 中存储学生对象，对象属性包括(id,name,gender,age)，有以下几种处理方式：
>
> - 方式一: 用 key 存储学生id，用 value 存储序列化之后用户对象 (如果用户属性数据需要修改，操作较复杂，开销较大)；
> - 方式二: 用 key 存储学生 id:属性名，用 value 存储属性值 (用户 id 数据冗余)；stu001:id 001 / stu001:name zhangsan；
> - 方式三: 用 key 存储学生 id，用 value 存储 field + value 的 hash；:star: 通过 key(学生id) + field(属性) 可以操作对应数据；

![image.png](assets/image31.png)

### （2）常用命令

- hset < key> < field> < value>[< field> < value>...]：用于为哈希表中的字段赋值，如果字段在 hash 表中存在，则会被覆盖；

![image.png](assets/image32.png)

- hget < key> < field>：返回哈希表中指定的字段的值；
- hmget < key> < field> [< field> < field>...]：获取哈希表中所有给定的字段值；

![image.png](assets/image33.png)

- hgetall < key>: 获取在哈希表中指定 key 的所有字段和值；

![image.png](assets/image34.png)

- hsetnx < key> < field> < value>：只有在字段不存在时，才设置哈希表字段中的值；
- hmset: 用法同 hset，在 redis4.0 中被弃用；
- hexists < key> < field>：判断哈希表中指定的字段是否存在，存在返回1，否则返回0；
- hkeys < key>：获取哈希表中所有的字段；
- hvals < key>：获取哈希表中所有的值；
- hlen < key>: 获取哈希表中的 field 数量；
- hdel < key> < field> [< field>...]：删除一个或多个哈希表字段；
- hincrby < key> < fied>< increment>：为哈希表 key 中指定的 field 字段的整数值加上增加 increment（整数值,可以是负数）；
- hincrbyfloat < key>< field> < increment>：为哈希表 key 中指定的 field 字段的**浮点数**值加上增加 increment（浮点数,可以是负数）；

### （3）应用场景

1. 对象缓存：hset stu:001 name zhangsan age 20 gender man；
2. 电商购物车操作：

   > 以用户 id 作为 key，以商品 id 作为 field，以商品数量作为 value
   >
   > - 添加商品:
   >   - hset user:001 s:001 1
   >   - hset user:001 s:002 2
   > - 增减商品数量: hincrby user:001 s:001 3
   > - 查看购物车商品总数: hlen user:001
   > - 删除商品: hdel user:001 s:001
   > - 获取所有商品: hgetall user:001
   >

## 4. Set 集合

set 是 string 类型元素无序集合，对外提供的功能和 list 类似，是一个列表功能；集合成员是唯一的；

### （1）常用命令

- sadd < key> < member> [< member>..]：将一个或多个成员元素加入到集合中，如果集合中已经包含成员元素，则被忽略；
- smembers < key>：返回集合中的所有成员；

![image.png](assets/image35.png)

- sismember < key> < member>：判断给定的成员元素是否在集合中存在，如果存在返回 1，否则返回 0；
- scard < key>: 返回集合中元素个数；
- srem < key>< member>[< member>...]：移除集合中一个或多个元素；
- spop < key> [< count>]：移除并返回集合中的一个或 count 个随机元素；
- srandmember < key> [< count>]：与 spop 相似，返回随机元素，不做移除；
- smove < source> < destination> < member>：将 member 元素从 source 源移动到 destinatlon 目标；
- sinter < key> [< key>...]：返回给定集合的交集(共同包含)元素；

![image.png](assets/image25.png)

- sinterstore < destination> < key1> [< key2>...]：返回给定所有集合的交集，并存储到 destination 目标中；

![image.png](assets/image36.png)

- sunion < key> [< key>...]：返回给定集合的并集(所有)元素；
- sunionstore < destination> < key1> [< key2>...]：返回给定所有集合的并集，并存储到 destination 目标中；
- sdiff < key> [< key>...]：返回给定集合的差集(key1中不包含在key2中的元素)；
- sdiffstore < destination><key1>[< key2>...]：返回给定所有集合的差集，并存储到 destination 目标中；

### （2）应用场景

1. 抽奖

   > - 参与抽奖: sadd cj001 user:13000000000 user:13455556666 user:13566667777
   > - 查看所有参与用户: smembers cj001
   > - 实现抽奖：spop cj0013 / srandmember cj001 3
   >
2. 朋友圈点赞

   > - 点赞
   >   - sadd like:friend001 user:001
   >   - sadd like:friend001 user:002
   > - 取消点赞 srem like:friend001 user:001
   > - 判断用户是否已点赞 sismember like:friend001 user:001
   > - 显示点赞用户 smembers like:friend001
   > - 获取点赞次数 scard like:friend001
   >
3. 关注模型：sinter 交集 sunion 并集 sdiff 差集

   > - 微博 sadd g:list:u001 1001 sadd g:list:u002 1001 你们共同关注的 sinter交集
   > - QQ 你们有共同好友 sinter交集
   > - 快手 可能认识的人 sdiff差集
   >

## 5. ZSet 有序集合

### （1）简介

- 有序集合是 string 类型元素的集合，不允许重复出现成员；
- 每个元素会关联一个 double 类型的分数，redis 是通过分数为集合中的成员进行从小到大的排序；
- 有序集合的成员是唯一的，但是分数可以重复；
- 成员因为有序，可以根据分数或者次序来快速获取一个范围内的元素；

### （2）常用命令

- zadd < key> < score> < member>[< score>< member>...]：将一个或多个元素及其分数加入到有序集合中；
- zrange < key>< min>< max> [byscore | bylex] [rev] [ limit offset count] [withscores]：返回有序集合指定区间的成员，(byscore 按分数区间，bylex 按字典区间，rev 反向排序(分数大的写前边，小的写后边)，limit分页(offset 偏移量，count返回的总数)，withscores 返回时带有对应的分数)；
- zrevrange < key> < start> < stop> [ limit offset count]：返回集合反转后的成员
- zrangebyscore < key> < min> < max> [withscores] [ limit offset count]：参考 zrange 用法；
- zrevrangebyscore < key>< max>< min> [withscores] [limit offset count]：参考zrange用法
- zrangebylex < key> < min> < max> [limit offset count：通过字典区间返回有序集合的成员；
  - zrangebylex k2 - + ：减号最小值，加号最大值；
  - zrangebylex k2 [aa (ac：[ 中括号表示包含给定值，( 小括号表示不包含给定值；

![image.png](assets/image37.png)

- zcard < key>：获取集合中的成员数量；
- zincrby < key> < increment> < member>：为集合中指定成员分数加上增量 icrement；
- zrem < key> < member> [< member>...]：移除集合的一个或多个成员；
- zcount < key> < min> < max>：统计集合中指定区间分数(都包含)的成员数量；
- zrank < key> < member>：获取集合中成员的索引位置；
- zscore < key> < member>：获取集合中成员的分数值；

### （3）应用场景

1. 按时间先后顺序排序: 朋友圈点赞 zadd 1656667779666
2. 热搜 微博 今日头条 快手
3. 获取 topN zrevrange k1 300 10 limit 0 10

# 三、Redis 持久化

- 目前，redis 的持久化主要应用 AOF(Append Only File) 和 RDB (Redis Database Backup) 两大机制；
- AOF 以日志的形式来记录每个写操作 (增量保存)，将 redis 执行过的所有（正确的）写指令全部记录下来 (读操作不记录)；
- 只许追加文件，但不可以改写文件；
- redis 启动之初会读取该文件，进行重新构建数据；

## 1. AOF

### （1）开启 aof

- AOF 默认不开启，在 conf 配置文件中进行配置；

![image.png](assets/image39.png)

![image.png](assets/image40.png)

- 重新启动

![image.png](assets/image41.png)

> - 默认文件名是 appendonly.aof；
> - 默认是启动后的相对路径，redis 在哪里启动，appendonly.ao f文件就在哪生成；

### （2）AOF 日志是如何实现的

#### a. 为什么使用 AOF 日志？

1️⃣ **WAL**：数据库写前日志 (Write Ahead Log)，在实际写数据库前，先把修改的数据记录到日志文件中，以便发生故障时，及时恢复；

2️⃣ **AOF**：数据库写后日志，redis 先去执行命令，把数据写入内存中，然后才去记录日志；

![image.png](assets/image42.png)

> ⭐️ .aof 文件中的保存内容
>
> ![image.png](assets/image44.png)

> ❤️ redis 为什么使用写后日志AOF？
>
> 1. 避免检查开销：向AOF中记录日志，是不做检查的,如果写前执行，很有可能将错误指令记录到日志中，在使用 redis 恢复日志时，就可能会出现错误;
>    > 先执行后记录，只有执行成功的指令才会被记录，以免在恢复数据的时候引入错误指令；（错误指令不会被记录，读取操作不被记录）
>    >
> 2. 不会阻塞当前的写操作

#### b. AOF 日志有什么风险？:heart:

![image.png](assets/image46.png)

1. .aof 文件可能由于异常原因被损坏，可以使用 redis 自带的命令 `redis-check-aof --fix appendonly.aof` 文件，修复成功则可以正确启动；
2. 由于刚刚执行一个指令，还没有写入日志，就宕机了。就会导致数据永久丢失 (redis 做为数据库存储的情况下)；
3. AOF 避免了对当前指令的阻塞，但可能会由于磁盘写入压力较大，对下一个操作带来阻塞风险；

### （3）AOF 日志三种写回策略

配置 conf 文件：

![image.png](assets/image49.png)

1. aways：同步写回，每个写指令执行完，立即同步将指令写入磁盘日志文件中；
2. everysec：每秒写回，默认配置方式。每个写指令执行完，先把日志写到AOF文件的内存缓冲区，每隔一秒把缓冲区的内容写入磁盘；
3. no：操作系统控制写回，每个写指令执行完，先把日志写到 AOF 文件的内存缓冲区，由操作系统决定何时把缓区的内容写入磁盘；

![image.png](assets/image50.png?t=1726217060715)

### （4）AOF 日志重写机制

> - 由于服务器资源有限，appendonly.aof 的文件大小随着使用不断增加，需要对其进行重写，降低资源占用；
> - 根据 appendonly.aof，将数据重新写入内存，出现类似于进栈/出栈的操作，需要重写 .aof 文件，只要写入最后结果的命令

Redis 根据数据库现有数据，创建一个新的 AOF 文件，读取数据库中所有键值对，重新对应一条命令写入。可以使用命令 `bgreWriteaof`，（ 在 redis-cli 环境执行）

#### a. AOF 重写相关配置

配置 conf 文件：

![image.png](assets/image51.png)

> 例如 文件 80m，开如重写，重写后降到 50m，下一次，达到 100m 再开始重写；

#### b. AOF 重写流程

- bgrewirteaof 触发重写，判断是否当前有重写在运行，如果有，则等待重写结束后再执行；
- 主进程 fork 出一个子进程，执行重写操作，保证主进程不阻塞，可以继续执行命令；
- 子进程循环遍历 reids 内存中的所有数据到临时文件，客户端的写请求同时写入 aof 缓冲区和 aof 重写缓冲区保证原 AOF 文件完整以及新的 AOF 文件生成期间的新的数据修改操作不会丢失；
- 子进程写完新 AOF 文件以后，向主进程发送信号，主进程更新统计信息；
- 主进程把 aof 重写缓冲区中的数据写入到新的 AOF 文件用新 AOF 文件覆盖掉旧的 AOF 文件，完成 AOF 重写；

![image.png](assets/image52.png)

## 2. RDB

由于 Redis 是单线程的，所以要避免使得主线程阻塞的操作；

> - RDB：（Redis DataBase）内存快照，记录内存中某一时刻数据的状态；
> - RDB 和 AOF 相比，RDB记录的是数据，不是操作指令；
> - redis 提供了两个命令生成 RDB 文件：
>   - save: 在主线程中执行，会导致阻塞；
>   - bgsave: 创建一个子进程，专门用来写 RDB，避免主线程阻塞，默认配置；

- 使用 RDB

![image.png](assets/image54.png)

### （1）写时复制技术

> 例: 6GB内存数据量，磁盘的写入 0.3GB/S，需要 20S 时间，来完成 RDB 文件的写入?
>
> 处理技术：写时复制技术 (copy-on-write cow)，在执行快照处理时，仍然正确执行写入操作；

![image.png](assets/image53.png?t=1726222268804)

### （2）快照频率

配置 conf 文件，修改快照频率；

![image.png](assets/image55.png)

- 全量快照 & 增量快照
- 混合使用 AOF & RDB

### （3）混合应用 AOF & RDB

1. 通过 redis.conf 配置文件；
2. 打开 aof；
3. 打开混合配置；（默认是打开的）

![image.png](assets/image57.png)

🔴 混合的过程

![image.png](assets/image58.png)

![image.png](assets/image.png?t=1726229888123)

> - 在 aof 文件中，前半部分，就是 rdb 文件的内容，从 bgrewirteaof 之后，是 aof 文件内容；
> - 重写就是把 RDB 的内容放到 AOF 中；

## 3. 关于对 redis 执久化处理的建议

- 【    /    】 如果数据在服务器运行的时候，使用 redis 做缓冲，可以不使用任何持久化方式；
- 【rdb & aof】 数据不能丢失，rdb 和 aof 混合使用是一个好的选择；
- 【   rdb   】 如果数据不要求非常严格，可以允许分钟级别丢失，可以使用 rdb；
- 【   aof   】 如果只使用 AOF，建议配置策略是 everysec，在可靠性和性能之间做了一个折中；
- 如果磁盘允许，尽量避免 AOF 重写的频率；

对比：:heart:

> AOF:
>
> - 数据不易丢失：AOF 可以记录所有的写操作，即使 Redis 崩溃，也可以通过重放 AOF 文件来恢复数据，理论上数据丢失的可能性很小；
> - 恢复速度慢: AOF 文件记录了所有的写操作，因此文件体积通常较大，这可能导致读取速度变慢和占用更多的磁盘空间 I/O；
>
> RDB:
>
> - 数据安全性低：如果 Redis 在两次快照之间崩溃，那么这段时间内的数据将会丢失；
> - 恢复速度快：DB 文件通常较小，当 Redis 重启时，加载 RDB 文件恢复数据的速度较快；相较于 AOF 文件，通常体积更小；不太占用磁盘 I/O；

# 四、Redis 集群方案

> - AOF & RDB 持久化方案解决了数据安全问题，保证数据尽可能地在服务器意外宕机时少丢失，最大程度恢复数据；
> - 但是在服务器宕机到恢复的这段时间，新来的请求如何处理？也就是如何保证服务器少服务少中断，提高服务的可用性；
> - redis 提高可用性的方式，增加副本（集群方案）；

![image.png](assets/image73.png?t=1726468291100)

## 1. 主从复制

### （1）读写分离

1️⃣ 主从库之间采用读写分离的方式：

> - 读操作: 主库、从库都可以处理；
> - 写操作: 首先写到主库执行，然后再将主库同步给从库；

🔴 读写分离的作用：

- 实现读写分离，性能扩展；
- 容灾快速恢复；

![image.png](assets/image60.png)

### （2）主从复制使用

> 配置一主二从，如下所示：

![image.png](assets/image56.png?t=1726303872440)

🔴 步骤: 分别创建三个 redis 实例；

1. 以默认配置文件 redis.conf 作为基本通用配置（需要打开后台运行），配置三个 不同的 conf 文件；

![image.png](assets/image61.png)

```ini
# include 命令用于在当前文件中包含其它文件内容
include redis-base.conf
# pidfile 命令用于指定 Redis 进程 ID (PID) 文件的路径
pidfile /var/run/redis_6379.pid
# port 命令用于设置Redis 服务器监听的端口号
port 6379
# dbfilename 命令 指定了数据持久化文件的名字
dbfilename dump6379.rdb
```

> 上述补充指令在 redis-base.conf 配置文件中都存在，新增的覆盖掉原来的配置；

3. 打开三个窗口，以三个配置文件，分别启动 redis-server；
4. redis-cli 命令行工具连接到不同端口；

> info replication：查看主从状态

![image.png](assets/image62.png)

4. 从节点连接到主节点

> replicaof host port

![image.png](assets/image63.png)

### （3）宕机演示

> 主节点可以读写，从节点只能读；

![image.png](assets/image64.png?t=1726312194328)

1. 从服务器宕机

- 6381 上调用 shutdown；
- 在主服务器上写入数据；
- 6381 重新连上时，仍然可以接收到主服务器的数据；

![image.png](assets/image66.png)

2. 主服务器宕机

- 6379 服务器调用 shutdown；
- 在从服务器上仍然可以读取数据；
- 从服务器显示主服务器的状态为 down；
- 当主服务器重新启动，从服务器显示主服务器的状态是 up；

![image.png](assets/image67.png)

### （4）主从同步原理 ❤️

![image.png](assets/image68.png)

1️⃣ 第一阶段，主从建立连接，协商同步。

- 从库和主库建立连接，告诉主库即将进行同步操作。主库需要确认并回复主从就可以开始进行同步处理了。
- 从库向主库发送一个 psync 指令，包含两个参数：
  - 一个是主库的 runID：runID 是每个 redis 实例启动时生成的一个随机ID，唯一标识；
  - 另一个是复制进度 offset：
- 第一次复制时，从库不知道主库的 runid，所以设为 ？，offset 设置为 -1 表示第一次复制；
- 主库收到指令后，会发送给从库 fullresync 指令去响应，带着主库的 runid，还有目前复制进度 offset；
  - fullresync 表示全量复制，主库把所有内容都复制给从库；
- 从库会记录下这两个参数。

2️⃣ 第二阶段，主库将所有数据发送给从库进行同步。

- 从库收到 rdb 文件后，在本地把原有的数据清除，同步从主库接收到的 rdb 文件；
- 如果在主库把数据跟从库同步的过程中，主库还有数据写入，为了保证主从数据的一致性，主库会在内存中给一块空间 replication buffer，专门记录 rdb 文件生成后收到的所有写操作。

3️⃣ 第三阶段，主库把第二阶段执行过程中新收到的操作 replication buffer，再发送给从库，从库再加载执行这些操作，就实现同步处理了。

### （5）主-从-从模式

> 如果有很多从节点。只有一个主节点时，主节点向从节点同步数据（rdb文件）的压力很大，所以需要采用 主-从-从 模式来分散主节点的压力；

采用 主-从-从 模式，将主库生成和传输 rdb 文件的压力，以级联方式分散到从库上。

![image.png](assets/image69.png)

![image.png](assets/image70.png)

> 主库同步给从库，从库再同步给从库的从库；

### （6）网络连接异常情况

> - 在 redis 2.8 之前，如果网络异常，再次连接后，需要做全量复制；
> - 从 redis 2.8 之后，采用增量复制方式
>   - 需要用到 repl_backlog_buffer 缓冲区；
>   - 主库把收到写操作，写入 repllcation buffer，同时，也写入到 repl_backlog_buffer 缓冲区；
>   - repl_backlog_buffer 缓冲区，主库会记录自己写到的位置，从库会记录自己读到的位置

![image.png](assets/image71.png)

![image.png](assets/image72.png)

> 增量同步：master_repl_offset 和 slave_repl_offset，各自记录  写/读 的位置，当从节点宕机后再上线，slave_repl_offset 只需要读取差值即可；
>
> 问题：当从节点宕机时间过久，master_repl_offset 写了一圈越过 slave_repl_offset，这时会丢失数据，所以要设置合适的 repl-backlog-size 缓冲区大小

⭐️ repl_backlog_buffer 缓冲区大小可以配置，默认 1M 大小；

> 缓冲空间大小 = 主库写入速度 * 操作大小 - 主从库网络传输速度 * 操作大小
>
> repl-backlog-size = 缓冲空间大小 * 2

## 2. 哨兵模式

> 主从复制模式，解决了从节点宕机后读数据的问题；但是并没有解决主节点宕机无法写数据的问题；（主节点读写，从节点只读）

哨兵模式：当主库宕机，在从库中选择一个，切换为主库。

问题：

> 1. 主库是否真正宕机?
> 2. 哪一个从库可以作为主库使用?
> 3. 如何实现将新的主库的信息通知给从库和客户端?

### （1）基本流程

> 哨兵主要任务：
>
> - 监控：周期性 ping 主/从节点
>   - 从库：当前 sentinel 节点指定时间内未收到从库应答，则认为主客观都下线；
>   - 主库：当前 sentinel 节点指定时间内未收到主库应答，则认为主库主观下线，当大部分 sentinel 节点都主观认为主库下线，则主库变为客观下线；此时需要重新选择主库；
> - 选择主库
>   - 筛选：筛掉一些宕机和网络差的节点；
>   - 打分：三轮打分选择分数最高，优先级、与主库同步程度、id值；
> - 通知

### （2）哨兵模式配置

1. 新建配置文件：`vim sentinel.conf`，编辑并保存如下内容：

```ini
# 配置哨兵端口
port 26379
# 配置监控的猪节点地址 1 表示只要有一个哨兵主观认为主节点下线，则主节点就客观下线
# sentinel monitor <自定义的reids主节点名称> <IP> <port> <数量>
sentinel monitor mymaster 127.0.0.1 6379 1
# 配置主观认为主节点下线的等待时间，单位 毫秒
sentinel down-after-milliseconds mymaster 30000
```

2. 启动三个 redis 实例，配置成一主二从模式；

   ![image.png](assets/image77.png)
3. 启动哨兵：`redis-sentinel sentinel.conf`

![image.png](assets/image78.png)

4. 将主节点宕机，观察哨兵监控信息变化；

> 将一个从库 6380，切换成主库，将 6381，切换成 6379 的从库；

![image.png](assets/image79.png)

5. 将主节点再次上线，观察哨兵监控信息变化；

> 原来主库 6379 再次启动，6379 切换成 6380 的从库；

![image.png](assets/image80.png)

#### ❤️ 主节点切换的实现方式：自动重写 redis 实例配置文件

![image.png](assets/image81.png)

### （3）选主流程 ❤️

> 筛选 + 打分机制，来实现新主库的选定

1. 筛选：筛选掉一些宕机的和网络不好的节点，剩下的节点作为备选进行打分；
2. 打分：三轮打分

- 第一轮：优先级
  - 通过 replica-priority 配置项，给不同的从库设置优先级，可以将内存大，网络好，配置高的从库优先级设置更高；
- 第二轮：和原主库同步程度；
  - 选择和原主库 repl_backlog_buffer （环形缓冲区）中的位置最接近的，做为分数最高；
- 第三轮：ID 号小的从库得分高，每一个 redis 实例都有一个id；

![image.png](assets/image82.png)

### （4）哨兵集群

> - 采用多个哨兵，组成一个集群，以少数服从多数的原则，来判断主库是否已客观下线：（防止单个哨兵产生较大误判概率）；
> - 多数一般指 n/2 + 1；（向下取整）
> - 如果集群中，有哨兵实例掉线，其他的哨兵还可以继续协作，来完成主从库监控和切换的工作；

#### a. 部署

模式：redis 实例一主二从，sentinel 实例三个节点集群；

1. 创建了一个目录 mysentinel；
2. 分别创建三个哨兵配置文件

> - sentinel26379.conf
> - sentinel26380.conf
> - sentinel26381.conf

配置如下：

> `sentinel down-after-milliseconds mymaster 30000`，每个哨兵等待时间要配置一致，否则达不成一致选库切换；:star:

```ini
port 26381
sentinel monitor mymaster 127.0.0.1 6379 2
```

```ini
port 26380
sentinel monitor mymaster 127.0.0.1 6379 2
```

```ini
port 26379
sentinel monitor mymaster 127.0.0.1 6379 2
```

3. 再次配置一主二从 reedis 实例；
4. 启动三个 redis 实例，配置成一主二从，6379是主库；
5. 依次启动三个哨兵实例，主库宕机，发现主库下线后，选举新的从库做为主库；

![image.png](assets/image83.png)

#### b. 运行机制

> - 基于 pub/sub，即 发布/订阅 机制实现哨兵集群组成；
> - 基于 info 命令实现哨兵监控从库；
> - 基于自身的 pub/sub，实现哨兵与客户端之间的通知（主库切换）；

##### 发布 / 订阅 ❤️

- 订阅：`subscrie channel [channel1...]`
- 发布：`publis channel <message>`

> 订阅一个或多个频道，一旦频道上有消息发布，订阅了这个频道的用户都可以收到信息；
>
> ![image.png](assets/image84.png)

![image.png](assets/image85.png)

结构图：

![image.png](assets/image.88png)

## 3. 分片集群

> 业务场景，需要存储 50G 的数据，但是内存和硬盘配置不足时，选用两种方式解决：
>
> - 纵向扩展：加内存，加硬盘，提高CPU；
>   - 简单、直接；RDB存储效率要考虑，成本要考虑
> - 横向扩展：加实例；:star:

### （1）分片集群配置

配置成一主一从的三个分片：

![image.png](assets/image90.png)

1. 创建文件夹`mkdir mycluster`，在 /mycluster 目录下配置 6 个配置文件；
2. 从 /redis-6.2.6 目录下，将默认配置文件 redis.conf 复制到 /mycluster 目录下，并打开后台启动 `daemonize yes`；
3. 配置 6 个配置文件：

> redis6379.conf / redis6380.conf / redis6479.conf / redis6480.conf / redis6579.conf / redis6580.conf

![image.png](assets/image92.png)

> vim编辑器下，替换内容命令：`:%s/原内容/替换内容`
>
> ```ini
> :%s/6379/6380
> ```

```ini
include redis-base.conf
pidfile "/var/run/redis_6379.pid"
port 6379
dbfilename "dump6379.rdb"
# 打开集群配置 
cluster-enabled yes
# 设定节点配置文件
cluster-config-file nodes-6379.conf
# 设置节点失联时间
cluster-node-timeout 15000
```

4. 修改 redis-base.conf 配置文件，将 bind ip 地址加入（加入本机IP地址），172.20.255.51

> 这里不适用本地环回地址 127.0.0.1 是因为，本地环回地址的数据包不会离开本机，因此不能在网络中的其他节点间传递。本地环回地址的设计目的是为了本地测试和调试，而不是为了支持分布式系统或集群环境下的节点间通信。因此，在配置集群时，应该使用实际的网络接口地址，确保各个节点能够互相识别并建立有效的通信链路。:star:

![image.png](assets/image89.png)

5. 启动这 6 个 redis 实例，并保证启动成功；

![image.png](assets/image91.png)

6. 将 6 个 redis 实例合成一个集群；

```bash
redis-cli --cluster create <node1-ip>:<node1-port> <node2-ip>:<node2-port> ... <nodeN-ip>:<nodeN-port> --cluster-replicas <replicas-count>
```

```bash
   redis-cli --cluster create \
     172.20.255.51:6379 \
     172.20.255.51:6380 \
     172.20.255.51:6479 \
     172.20.255.51:6480 \
     172.20.255.51:6579 \
     172.20.255.51:6580 \
     --cluster-replicas 1
```

> 其中 `--cluster-replicas 1` 表示每个主节点拥有一个从节点

![image.png](assets/image93.png)

> 注意：下面分配结果与上面不是同一次启动的结果，后续以下面为准；

![image.png](assets/image96.png)

> 6379->6580
>
> 6380->6480
>
> 6479->6579

### （2）Hash Slot 哈希槽

在使用 redis cluster 方案中，一个分片集群有 16384 个哈希槽，哈希槽表示数据分区；

```bash
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
```

> - 划分方法：16384 / n；其中 n 是有 n 个分片；
> - 数据存在哪个分片上是通过键值对的 key 按照 CRC16 算法计算一个 16bit 的值；
> - 再用这个值对 16384 取模运算，得到的数代表对应编号的 hash slot；
> - hash16 % 16384 [0-16383]，落在上述哪个区间，就在哪个分片；

❤️【hash slot 分配方案】：

- cluster create 命令创建集群时，redis 会自动把这些 hash slot 平均分布在集群实例上。如果集群中有 N 个实例(主库)，每个实例上分配到的 hash slot 就是 16384/N；
- 使用 cluster addslos 手工分配哈希槽；

### （3）数据操作

1. 集群方式启动命令行工具：`redis-cli -c -p 6379`，加 -c 参数；
2. 向 redis 中设置一个键值对，key 会经过运算后，得到相应的 hash slot 进行存储；

![image.png](assets/image.07png)

3. 一次设置多个键值对，向集群中加入多个 key-value，由于在不同的 hash slot，此时会报错；要通过`{}`来定义组的概念，使用 key 中`{}`内相同内容的键值对放在一个 slot 中；

![image.png](assets/image98.png)

4. 取值：`get k1` / `get name{user:001}`

![image.png](assets/image99.png)

### （4）常用命令

> redis-cli 命令行环境运行

- cluster nodes：显示集群节点的配置信息；

![image.png](assets/image100.png)

> 6379->6580
>
> 6380->6480
>
> 6479->6579

- cluster keyslot < key>：获取 key 的哈希槽；

![image.png](assets/image101.png)

- cluster countkeysinslot < slot>：返回当前哈希槽中 key 的数量（仅查询当前 redis 实例所连接的 hash slot 范围）；

![image.png](assets/image102.png)

- cluster getkeysinslot < slot> < count>：返回当前槽中指定的 count 数量的 key；

![image.png](assets/image103.png)

### （5）故障演示

【挂掉一个 master 并重新启动】

1. 将 6379 宕机，以集群方式登录 6580；
2. 使用 cluster nodes 查看节点状态，6379 的 slave 6580 变成了 master；
3. 把 6379 再次启动，启动后，6379 变成了 6580 的 salve；

![image.png](assets/image104.png)

![image.png](assets/image105.png)

【挂掉一个分片】

> 如果有一段 hash slot 的主从节点都宕机（挂掉一个分片），redis 分片集群是否继续工作？

默认情况下是不工作的，可以通过配置 redis.conf 文件，设置当一个分片挂掉后，即其分管的 hash slot 失效后，其余分片继续工作；

> 将 cluster-require-full-coverage 配置项设置为 no；通过下面配置，默认是yes，如果主从都挂掉，整个集群就都挂掉如果是no，就表示该 hash slo t数据全部都不能使用，也无法存储；

![image.png](assets/image106.png)

## 4. 亿级数据访问处理 ❤️

### （1）场景描述

> - 手机 APP 用户登录信息，一天用户登录 ID 或设备 ID；
> - 电商或者美团平台，一个商品对应的评论；
>   - 商品评论的排序
> - 文章对应的评论；
> - APP 上有打卡信息；
>   - 月活统计
> - 网站上访问量统计；
>   - 统计独立访客(Unique Vistitor UV)
> - 统计新增用户第二天还留存的；
>
> 上述都是大数据量访问（亿级、千/百万级）

### （2）集合的统计模式

> 四种统计模式：聚合统计、排序统计、二值状态统计、基数统计；

#### a. 聚合统计

Set：统计多个集合的交集、差集、并集：

> - set集合，来存储所有登录系统的用户 `user:id`；
> - set集合，来存储当日新增用户信息 `user:id:20240918`；

1. 假设系统是2024年09月18日上线，统计当天用户：`sadd user:id:202409181001 1002 1003 1004 1005`；
2. 统计总用户量：`sunionstore user:id user:id user:id:20240918`
3. 第2天09月19日上线用户：`sadd user:id:20240919 1001 1003 1006 1007`；
4. 统计当日新增用户：`sdiffstore user:new user:id:20240919 user:id`；
5. 统计第一天登录，第二天还在的用户：`sinterstore user:save user:id:20240918 user:id:20240919`；
6. 统计第一天登录，第二天流失的用户：`sdiffstore user:rem user:id:20240918 user:id:20240919`

#### b. 排序统计

List、Set、Hash、ZSet 四种集合中，List 和 Zset 是属于有序的集合；

- 一种使用 List，通过 lpush 加入；
- 一种使用 Zset，按分数权重处理；

> 具体使用见第二章

#### c. 二值状态统计 - bitmap ❤️

> - Redis 的 Bitmap 可以非常大，理论上可以达到 512 MB，但实际上根据应用的需求，通常会控制在一个合理的范围内，以避免不必要的内存消耗和性能问题；
> - Redis 的 Bitmap优势： ⭐️
>   - 采用二进制位级存储，极大节省内存；
>   - 且位操作高效；
>   - 位操作通常是原子性的，适用于高并发场景；
> - 应用场景如：用户状态跟踪、统计分析、权限管理等

- 统计统计疫苗接种人数、打卡、签到等只有两种状态的场景；
- redis 提供了一种扩展的数据类型 bitmap （位图）；
- 可以把 bitmap 看为是一个 bit 数组，默认值是0；
- 常用命令：
  - setbit < key> < offset> < value>：在下标 offset 处设置值，offset 相当于 bit 数组的下标，从 0 开始；（每一个下标都是一个bit）
  - getbit < key> < offset>：获取下标 offset 处的值；
  - bitcount < key> [start end]：统计下标 [start end] 区间（以字节为单位的，1byte=8bit）的值；
  - bitop < operaion> < key> < key1> [< key2>...]：对 key1,key2...的操作 operaion，并将结果保存在 key；
    - 其中 operaion 可以是 and等，可以按位与/或；
- 例如：统计一下，2024年9月份的一个上班打卡情况；

![image.png](assets/image1055.png)

![image.png](assets/image107.png)

#### d. 基数统计 - HyperLogLog (hll)

统计一个集合中不重复的元素个数，例如统计网页的 UV（独立访客）；

- 第一种：使用 set 或者 hash 来完成统计；
  - sadd page1:uv u1001 u1002 u1003
    scard page1:uv
  - 存在的问题：如果数据量非常大，且页面多，访问人数非常多，造成内存紧张；
- 第二种：HyperLogLog 是用于统计基数的一种数据集合类型；
  - HyperlogLog 是用于统计基数的一种数据集合类型。
    - 优点在于当集合元素非常多，使用 hll 所需要的空间是固定目很小，使用12kb内存，可以存储2^64个元素的基数；
    - 缺点在于统计规则是基于概率完成的，会有 0.81% 左右的误差；如果统计1000万次，实际上可以是1100万或900万人；
  - 常用命令:
    - pfadd page1:uv u1001 u1002 u1003
      pfcount page1:uv
      pfadd page2:uv u1001 u1004
      pfmerge page:uv page1:uv page2:uv
      pfcount page:uv

#### 总结：

![image.png](assets/image110.png)

# 五、Geospatial 地理空间

#### 简介

> 在 Redis 中，Geospatial 数据结构是一种非常强大的功能，用于存储和查询地理位置信息。它允许你高效地存储和检索地理坐标，并执行各种地理空间查询，如距离范围查询、最近邻查询等

- 基于位置信息服务(Location-Based Service,LBS)的应用，Redis3.2 版本后增加了对 GEO 类型的支持；
- 主要来维护元素的经纬度；
- redis 基于这种类型，提供了经纬度设置、查询、范围查询、距离查询、经纬度 hash 等一些相关操作；

## 1. GEO 的底层结构

> 场景：
>
> 1. 约车系统，针对每一辆车，有一个唯一编号，车辆有行驶的经纬度；
> 2. 呼叫车辆，会暴露用户的经纬度，根据经纬度进行范围查找，进行匹配；
> 3. 把附近车辆找到后，车辆信息获取，将信息反馈给用户；

1️⃣ GEO 的底层结构是使用 zset 实现的，需要将经纬度放在一起，生成一个权重的分数（GEOHash 编码），按这个分数进行排序；\

2️⃣ GEOHash 编码；:star:

![image.png](assets/image111.png)

![image.png](assets/image112.png)

> - 经度: 11010
> - 纬度: 10111
>
> 最后合成的编码: 1110011101 (是经纬度每次交替顺序取一位的结果，或者说是交叉合并的结果)；
>
> 就可以将最后合成的编码作为 zset 存储的权重；

![image.png](assets/image113.png)

## 2. GEO 操作命令

> 经纬度坐标拾取：https://api.map.baidu.com/lbsapi/getpoint/index.html

- geoadd < key> < longitude> < latitude> < member> [longitude latitude member..]：添加地理位置(经度 纬度 名称)；
- geopos < key> < member> [member...]：获取指定的位置坐标值；
- geodist < key> < member1> < member2> [m|km|fm|mi]：获取两个位置之间的直线距离，单位：m 米 km 千米 ft英尺 m英尺；
- georadius < key> < longitude> < latitue> radius [m|km|fm|mi]：以指定的经纬度做为中心，找出给定半径内的位置；

![image.png](assets/image116.png)

![image.png](assets/image117.png)

# 六、Redis 事务操作

> 事务：ACID
>
> - 原子性(Atomicity)
> - 一致性(Consistency)
> - 隔离性(Isolation)
> - 持久性(Durabiliby)

redis 事务提供了multi、exec命令来完成：

- 第一步，客户端使用 multi 命令显式地开启事务；
- 第二步，客户端把事务中要执行的指令发送给服务器端，例如 set、get、lpush，这些指令不会立即执行，进入一个队列中；
- 第三步，客户端向服务器发送一个命令 exec，来完成事务提交，当服务器端收到这个指令后，实际去执行上一步中的命令队列；
- 或者，调用 discard 命令，停止事务；

![image.png](assets/image114.png)

![image.png](assets/image115.png)

## 1. 原子性

- 第一种情况，在执行 exec 指令前，客户端发送操作命令有误；
  - redis 会报错并记录这个错误；
  - 此时，还可以继续发送命令操作；
  - 在执行 exec 命令之后，redis 拒绝执行所有提交的指令，返回事务失败的结果；
  - 保证了原子性； 🔴
- 第二种情况，向服务器发送指令，其中有指令和操作的数据类型不匹配；
  - redis 检测不出错误，发送数据不匹配的命令不会报错；
  - 提交 exec 执行，数据不匹配的指令报错，其它指令都正常执行；
  - 出现错误不会发生回滚；
  - 不能保证原子性； 🔴
- 第三种情况，在执行事务的 exec 指令时，redis 实例发生了故障，导致事务执行失败；
  - 如果 redis 开启了 aof 日志，可能会有一部分指令被记录到 AOF 日志中，需要使用 redis-check-aof 去检查 aof 文件；
  - 将未完成事务操作从 aof 清除；
  - 保证了原子性； 🔴

![image.png](assets/image1178.png)

![image.png](assets/image118.png)

## 2. 一致性

- 第一种情况，指令进入队列时就报错，整个事务全部被放弃执行，可以保证数据的一致性；
- 第二种情况，进入队列时没有报错，实际执行时报错，有错误的指令不去执行，正确的指令可以正常执行，可以保证数据的一致性；
- 第三种情况，exec 指令时 redis 实例发生故障，根据 RDB 和 AOF 情况来做判断；
  - 如果没有开启 rdb 和 aof，不做持久化，重启后没有数据，保证了一致性；:star:
  - 如果使用了 rdb 方式，rdb 不会在事务执行的时候去保存数据，保证了一致性；⭐️
  - 使用 aof 日志，如果事务队列操作记录没有进入 aof，可以保证一致性；:red_circle:
  - 如果已加入了一部分，使用 redis-check-aof 清除事务中已完成的操作，保证了一致性；🔴

## 3.隔离性

客户端提交 exec 来执行事务：

- 提交 exec 之前；
  - 并发操作在 exec 指令前，要实现隔离性的保证，需要使用 watch 机制，否则不能保证隔离性；
  - 在事务执行前，相当于有一个监控器在监控 key 是否已经被修改过了；
  - 如果已修改，则放弃事务执行，避免了事务的隔离性被破坏；
  - 如果客户再次执行，此时，没有其他客户端去修改数据，则执行成功；
  - 类似于乐观锁，atomic 原子操作；:red_circle:
    - 每次操作数据都检查版本号，看是否已经被修改；
    - 悲观锁：synchronized，每次操作数据都加锁；
  - unwatch 指令取消所有监控的 key；
- 提交 exec 之后；
  - 并发操作在 exec 指令后，对事务没有影响，可以保证事务的隔离性；

![image.png](assets/image119.png)

![image.png](assets/image120.png)

## 4. 持久性

redis内存数据库，取决于持久化配置模式：

- 不开启 rdb 和 aof，只当作缓存使用，是不能保证持久性；
- 使用 rdb，如果在一个事务执行后，下一次的 rdb 快照还未执行前，redis 实例发生故障了，不能保证持久性；
- 使用 aof，配置选项 everysec、always、no，也不能保证持久性；

无论 redis 采用什么配置模式，都不能保证事务的持久 性

# 七、Redis 缓存

## 1. 缓存简介

### （1）缓存的分类（穿透型&旁路型（redis））

> 穿透型缓存：
>
> - 缓存与后端数据交互在一起，对服务端的调用隐藏细节；
> - 如果从缓存中可以读到教据，就直接返回，如果读不到就到数据库中去读取；
> - 从数据库中读到教据，也是先更新缓存，再返回给服务端；
> - 向数据库中写入数据，也是先写入缓存中，再同步给数据库；
>
> ![image.png](assets/image121.png?t=1726728316589)
>
> 旁路型缓存：
>
> 1. 服务先到缓存中读取数据，如果数据存在，就直接返回；
> 2. 如果缓存中没有数据，就到数据库中去读取；
> 3. 服务再将从数据库中读到的数据同步给缓存；
>
> ![image.png](assets/image122.png)

🔴 redis 是旁路型缓存

### （2）缓存的特征

> 缓存的特点：
>
> - 效率高
> - 容量小

## 2. 缓存处理

### （1）缓存处理的两种情况

- 缓存命中：redis 中有相应的数据，直接从 redis 中读取，性能很高；
- 缓存缺失：redis 中没有相应的数据，从后端关系型数据库中读取数据，性能很低；
- 缓存更新：如果发生缓存缺失，为了后续程序请求中可以从缓存中读取数据，要将缺失的数据写入 redis；

### （2）缓存的类型

#### a. 只读缓存

> - 只用读取数据的缓存;
> - 如果有写入数据的请求，直接发到后端的 mysql 或 oracle 数据库，在数据库中完成增删除改；
> - 对于删除和修改的数据来说，redis 中可能会有旧的数据，需要将旧的数据删除；
> - 下一次读取时，redis 缓存缺失，那么就从数据库中读数据，并更新到 redis 缓存中；

![image.png](assets/image123.png)

使用场景：缓存图片、视频、手机的通讯记录、银行的以往帐单，等等改动不频繁的数据；

#### b. 读写缓存

> - 读写缓存，不只完成对数据读取任务，数据的增加、删除、修改操作，也是在 redis 缓存中完成；
> - 由于 redis 内存数据库效率很高，所以可以快速响应给服务端调用；
> - redis 内存数据，在 redis 实例出现问题时，导致数据丢失；

##### i. 同步直写

优先保证数据可靠；

##### ii. 异步写回

执行效率高；

建议：

- 对写请求操作进行高效处理，选择读写缓存；
- 如果写操作很少，需要提升读取效率，选择只读缓存；

## 3. Redis 缓存数据的删除和替换

### （1）过期删除策略

- 可以使用 ttl 查看 key 的状态；
- 已过期的数据，redis 并未马上删除；
- 优先去执行读写数据操作，删除操作延后执行；

> redis 中每一个 value 对应一个内存地址，在过期表 expires 中，一个内存地址，对应一个时间截，如果达到指定时间，就完成删除处理
>
> ![image.png](assets/image124.png)

三种过期删除策略：

- 定时删除：创建一个定时器，当 key 设置过期时间已到达，删除 key，同时 expires 中也删除；
  - 优点：节约内存；
  - 缺点：对于 cpu 实时处理压力影响，对 redis 执行的效率有影响；
- 惰性删除：数据到达过期时间，先不做删除，直到下次访问该数据时，再做删除；
  - 执行流程：在 get 数据时，先执行 redis 中一个内部函数 expireIfNeeded()
    - 如果没有过期，就返回；
    - 如果已过期就删除，返回 -2；
  - 优点：节约 CPU 资源；
  - 缺点：内存占用过大；（以空间换时间）
- 定期删除：
  - redis 启动服务时，读取 server.hz 的值，默认为 10，可以通过 info server 指令查看；
  - 每秒钟执行 server.hz 次定时轮询，调用 serverCron() 函数，函数中又执行 databasesCron()，对 16 个数据库进行轮，执行了 activeExpireCycle()，检测其中元素的过期情况；
  - 每次轮询都执行 250ms/server.hz 时长；
  - 随机从对应的库中抽取 w 个(w 默认20) key 进行检测，如果 key 已过期，则删除 key；
    - 如果一轮中删除的 key 数量大于 w*25%，则再次循环刚才的过程；
    - 如果一轮中删除的 ke y数量 <=w*25%，则开始检查下一个库;

![image.png](assets/image125.png)

> redis 使用惰性删除和定期删除的策略

### （2）缓存淘汰策略

1. redis 设置缓存最大容量：

- 配置 redis.conf 配置文件，更改 maxmemory 配置项；
- 一般情况，建议设置为总数据的 15% 到 30% 左右，在实际生产环境下，可以设置50%；
- 如果不设置，默认全部使用；

![image.png](assets/image128.png)

2. redis 设置缓存淘汰策略：

> 配置 redis.conf 配置文件，更改 maxmemory-policy 配置项；

![image.png](assets/image129.png)

> 8 种淘汰策略：

```mermaid
graph LR
A(淘汰策略)-->B(不淘汰)
B-->M(noevction)
A-->C(进行数据淘汰)
C-->D(过期数据淘汰)
C-->E(所有数据淘汰)
D-->F(volatile-lru)
D-->G(volatile-lfu)
D-->H(volatile-ttl)
D-->I(volatile-random)
E-->J(allkeys-lru)
E-->K(allkeys-lfu)
E-->L(allkeys-random)
```

- 不淘汰：

  - noevction：在 redis 默认情况下，不进行数据淘汰，一旦缓存被写满了，再有写请求，redis 直接返回错误；
- 设置了过期的数据淘汰：

  - volatile-ttl：在进行筛选时，根据过期时间先后顺序进行一个删除，越早过期的越先被删除；
  - volatile-random：在设置了过期时间的键值对中，进行随机删除；
  - valotile-lru：会使用 LRU 算法筛选设置了过期的键值对；
  - valotile-lfu：会使用 LFU 算法筛选设置了过期的键值对；
- 所有数据淘汰：

  - allkeys-random：从所有键值对中随机筛选并删除；
  - allkeys-lru：从所有键值对中采用 LRU 算法进行筛选删除；
  - alkeys-lfu：从所有键值对中采用 LFU 算法进行筛选删除；

> - LRU 算法：
>   - 算法 Least Recently Used，最近最少使用原则，最不常用的数据会被筛选出来，最近频繁使用的数据会保留；
>   - LRU 算法，需要使用链表来管理所有缓存数据，带来内存开销；
>   - 有数据被访问时，需要执行链表数据的移动，会降低 redis 性能；
>   - 记录数据最后一次访问的时间截，第一次会随机选出 N 个数据，作为一个候选集合，作一个排序，再把 lru 最小的数据进行淘汰；
>     - N 的配置项：maxmemory-samples 5
> - LFU 算法：
>   - 算法 Least Frequenty Used，最不常用原则；
>   - 根据历史访问频率来淘汰数据，每个数据块都有一个引用计数，按引用计数来排序，如果引用计数相同，按照时间排序；
>     - 新加入的数据放在队尾，引用计为 1；
>     - 当数据被访问，引用计数增加，队列重排；
>     - 当需要淘汰数据时，将队列尾部的数据块删除；

## 4. Redis 缓存异常问题

> 四个问题：缓存数据一致性、缓存雪崩、缓存击穿、缓存穿透

### （1）缓存数据一致性

一致性包括两种情况：

- 缓存中有数据，需要和数据库值相同；
- 缓存中没有数据，数据库中的数据是最新值；

如果不符合以上两种情况，则出现数据不一致的问题；

- 读写缓存
  - 同步直写
  - 异步写回
- 只读缓存
  - 新增数据：数据直接写到数据库中，缓存不做操作，满足一致性两种情况的第二种；
  - 删改数据：
    - 先删缓存后更新数据库：会出现缓存删除成功，数据库更新失败；
      - 数据不一致：不满足一致性两种情况的第二种
    - 先更新数据库再删缓存：可能会导致，数据库更新成功，缓存删除失败；
      - 数据不一致：不满足一致性两种情况的第一种
  - 解决数据不一致的方案
    - 重试机制：把删除的缓存值或要更新数据库值先存储到消息队列中( kafka 消息队列)，出现失败情况就重试，成功就从消息队列移除；（单线程）
    - 延迟双删：:heart: （多线程）先删缓存后更新数据库，隔一段时间后，再次删除缓存；
      - 先删缓存后更新数据库，假设T1线程先别除缓存，再执行更新数据库。还未更新成功时，T2线程进行读取，发现缓存中没有数据，到数据库中读取，会读取到旧的数据。如果T2还将旧数据更新到缓存中，那T1线程再进行读取，也读到的旧值。
    - 先更新数据库再删除缓存，假设 T1 线程先删除数据库中的值，还没来得及别除缓存时，T2 线程就开始读取数据。T2 会先从缓存中读取，缓存命中，T2拿到的就是旧的数据，直到T1将缓存中数据删除，其他线程再次读取，可以拿到新值；

```java
redis.delCache()
db.update()
Thread.sleep(2000)
redis.delCache()
```

![image.png](assets/image130.png)

![image.png](assets/image131.png)

### （2）缓存雪崩

简介：

> - 大星的应用请求无法在 redis 中完成处理，缓存中读取不到数据，直接进入到数据库服务器；
> - 数据库压力激增，数据库崩溃，请求堆积在 redis，导致 redis 服务器崩溃，导致 redis 集群崩溃，应用服务器崩溃，称为雪崩；

原因1：缓存中有大星数据同时过期；

> 解决方案：
>
> - 页面静态化处理数据：对于不经常更换的数据，生成静态页；
> - 避免大量数据同时过期：为商品过期时间追加一个随机数，在一个较小的范围内(1~3分钟)；:heart:
> - 构建多级缓存架构：redis 缓存 + nginx 缓存 + ehcache 缓存；
> - 延长或取消热度超高的数据过期时间；
> - 服务降级：不同的数据采取不同的处理方式；（非核心数据的数据请求不处理或简单处理，核心数据的请求正常处理）

原因2：redis 实例故障；

> 解决方案：
>
> - 服务熔断或限流处理；
> - 提前预防
>   - 灾难预警：监控 redis 服务器性能指标，包括数据库服务器性能指标，CPU、内存、平均响应时间、线程数等；
>   - 集群：一有节点出故障，主从切换；

### （3）缓存击穿

简介：

> 对某个访问频繁热点数据的请求，主要发生在热点数据失效

解决方案：

> - 预先设定：电商双11，商铺设定几款是主打商品，延长过期时间；
> - 实时监控：监控访问量，避免访问量激增；
> - 定时任务：启动任务调度器，后台刷新数据有效期；
> - 分布式锁：可防止缓存击穿，但会有性能问题；

### （4）缓存穿透

简介：

> 要访问的数据在 redis 中不存在，在数据库中也不存在；

原因：

> 1. 业务层误操作；
> 2. 恶意攻击；

解决方案：

> - 缓存空值或缺省值；
> - 使用布隆过滤器，快速判断数据是否存在；
> - 在请求入口前端进行请求检测；
> - 实时监控；
> - key 加密；

# 八、jedis 操作

## 1. 准备工作

1. 创建 Maven 工程，引入依赖 lombok、logback、jedis

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.slz.redis.demo</groupId>
    <artifactId>redis-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.22</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.7</version>
        </dependency>
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>4.0.1</version>
        </dependency>
    </dependencies>
</project>
```

2. 配置 logback 用到的 xml 文件 logback.xml；

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration
        xmlns="http://ch.qos.logback/xml/ns/logback"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://ch.qos.logback/xml/ns/logback logback.xsd">
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%date{HH:mm:ss.SSS} %c [%t] - %m%n</pattern>
        </encoder>
    </appender>
    <logger name="c" level="debug" additivity="false">
        <appender-ref ref="STDOUT"/>
    </logger>
    <root level="ERROR">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

3. 修改 redis.conf 配置文件，加入服务器内网 IP 地址；(注意是绑定内网 IP，调用时通过公网 IP 访问)

```ini
bind 172.20.255.51 127.0.0.1 -::1
```

4. 修改 redis.conf 配置文件，将文件保护模式修改，原有默认值是 yes，改成 no；

```ini
# By default protected mode is enabled. You should disable it only if
# you are sure you want clients from other hosts to connect to Redis
# even if no authentication is configured, nor a specific set of interfaces
# are explicitly listed using the "bind" directive.
protected-mode no
```

5. 查看一下 linux 防火墙状态，开放相应端口 6379；
6. 编写 Java 程序测试连接；

```java
@Slf4j(topic = "c.TestConnection")
public class TestConnection {
    public static void main(String[] args) {
        String ping;
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            ping = jedis.ping();
        }
        log.debug(ping);
    }
}
```

```log
12:44:47.498 c.TestConnection [main] - PONG
```

## 2. 操作各种数据类型

### （1）String

```java
@Slf4j(topic = "c.TestString")
public class TestString {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            String set = jedis.set("k2", "v2");
            log.debug(set);
            String k2 = jedis.get("k2");
            log.debug(k2);
            String mset = jedis.mset("k3", "v3", "k4", "v4");
            log.debug(mset);
            List<String> mget = jedis.mget("k1", "k2", "k3");
            log.debug(mget.toString());
            String setex = jedis.setex("kk", 30, "vv");
            log.debug(setex);
            TimeUnit.SECONDS.sleep(5);
            long ttl = jedis.ttl("kk");
            log.debug(String.valueOf(ttl));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### （2）List

```java
@Slf4j(topic = "c.TestList")
public class TestList {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            long lpush = jedis.lpush("list1", "a", "b", "c", "d", "e");
            log.debug(String.valueOf(lpush));
            List<String> list1 = jedis.lpop("list1", 2);
            log.debug(list1.toString());
            List<String> list11 = jedis.lrange("list1", 0, 1);
            log.debug(list11.toString());
        }
    }
}
```

### （3）Hsah

```java
@Slf4j(topic = "c.TestHash")
public class TestHash {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            long hset1 = jedis.hset("user:001", "name", "zhangsan");
            long hset2 = jedis.hset("user:001", "age", "22");
            long hset3 = jedis.hset("user:001", "gender", "female");
            log.debug("" + hset1 + " " + hset2 + " " + hset3);
            String name = jedis.hget("user:001", "name");
            Map<String, String> map = jedis.hgetAll("user:001");
            log.debug(map.toString());
            Map<String, String> m = new HashMap<>();
            m.put("name", "xiaoming");
            m.put("age", "25");
            long hset = jedis.hset("stu:001", m);
            log.debug(String.valueOf(hset));
        }
    }
}
```

### （4）Set

```java
@Slf4j(topic = "c.TestSet")
public class TestSet {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            long sadd = jedis.sadd("myset", "a", "b", "a", "c", "d");
            log.debug(String.valueOf(sadd));
            Set<String> myset = jedis.smembers("myset");
            log.debug(myset.toString());
            long myset1 = jedis.scard("myset");
            log.debug(String.valueOf(myset1));
            long srem = jedis.srem("myset", "a");
            log.debug(String.valueOf(srem));
            String myset2 = jedis.spop("myset");
            log.debug(myset2);

            long sadd1 = jedis.sadd("myset2", "c", "c", "f", "g");
            log.debug(String.valueOf(sadd1));
            long sunionstore = jedis.sunionstore("union_set", "myset", "myset2");
            log.debug(String.valueOf(sunionstore));
        }
    }
}

```

### （5）ZSet

```java
@Slf4j(topic = "c.TestZSet")
public class TestZSet {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.zadd("z1", 1, "a");
        jedis.zadd("z1", 2, "b");
        jedis.zadd("z1", 3, "c");

        Map<String, Double> m = new HashMap<>();
        m.put("c", 4.0);
        m.put("a",5.0);
        m.put("d",6.0);
        m.put("f", 7.0);
        jedis.zadd("z2",m);

        List<String> z2 = jedis.zrange("z2", 0, -1);
        log.debug(z2.toString());
        List<String> z21 = jedis.zrevrange("z2", 0, -1);
        log.debug(z21.toString());

        Set<String> zdiff1 = jedis.zdiff("z1", "z2");
        log.debug(zdiff1.toString());
        Set<String> zdiff2 = jedis.zdiff("z2", "z1");
        log.debug(zdiff2.toString());

        zdiff1.addAll(zdiff2);
        log.debug(zdiff1.toString());

        Long z22 = jedis.zrank("z2", "f"); // 返回索引位置
        log.debug(z22.toString());
    }
}
```

### （6）Geo

```java
@Slf4j(topic = "c.TestGeo")
public class TestGeo {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            long loc1 = jedis.geoadd("geo:beijing", 116.417492, 39.911836, "gugong");
            long loc2 = jedis.geoadd("geo:beijing", 116.466935, 39.960963, "bridge");
            long loc3 = jedis.geoadd("geo:beijing", 116.216846, 39.91405, "mountain");

            Double geodist = jedis.geodist("geo:beijing", "gugong", "mountain", GeoUnit.KM);
            log.debug(geodist.toString());

            List<GeoRadiusResponse> georadius = jedis.georadius("geo:beijing", 116.417492, 39.911836, 2, GeoUnit.KM);
            georadius.forEach(g->{
                System.out.println(g.getMemberByString());
            });

            List<GeoCoordinate> geopos = jedis.geopos("geo:beijing", "gugong", "bridge", "mountain");
            geopos.forEach(g->{
                System.out.println(g.toString());
            });
        }
    }
}
```

## 3. 应用案例

> 需求：用户使用银行 APP 登录，要求动态发送手机验证码，验证码设定 5 分钟有效，1 天内最多发送 3 次；

```java
@Slf4j(topic = "c.TestApp")
public class TestApp {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        send(jedis, "10010");
    }

    public static void send(Jedis jedis, String phone){
        String count_key = "v:" + phone + ":count"; // 当前手机号已发送次数
        String code_key = "v:" + phone + ":code"; // 当前手机号已收到的验证码
        String s = jedis.get(count_key); // 获取当前手机号已发送次数
        if(s==null){
            // 如果没有获取，即s==null，就表时该key不存在，第一次设置，有效时间为1天
            jedis.setex(count_key, 60*60*24, "1");
        } else if (Integer.parseInt(s)<3) { // 如果不到3次，可以为用户发送，每发送一次，记数增加1
            jedis.incr(count_key);

        } else {
            log.debug("今日已经请求3次，请24小时后再试");
            jedis.close();
            return;
        }
        String code = getCode();
        jedis.setex(code_key, 60*5, code); // 发送验证码，进行保存，设置过期时间
        log.debug("向" + phone + "发送成功", code);
        jedis.close();
    }

    public static String getCode(){
        String pattern = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(pattern.charAt((int)(Math.random()*pattern.length())));
        }
        return sb.toString();
//        return new DecimalFormat("000000").format(new Random().nextInt(100000));
    }
}
```
