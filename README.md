# mybatis-pager-spring-boot-starter
为 SpringBoot(基于 Servlet 的 Web 项目) + Mybatis 提供简单易用的分页查询支持


![](https://img.shields.io/maven-central/v/org.cat73/mybatis-pager-spring-boot-starter.svg)
![](https://img.shields.io/github/license/Cat7373/mybatis-pager.svg)

## 功能说明
以前要做一个分页查询需要做的：

1. 每个接口都需要接收分页参数
2. 写一个查询特定页数据的`SQL`
3. 写一个查询总记录数的`SQL`
4. 写一个分页查询结果的实体类
5. 将结果拼到分页查询结果的实体类中，并响应给前端
6. 如果需要导出`Excel`，又需要写一堆`POI`操作

用了这个包之后你需要做的：

1. 在需要分页查询的接口上增加`@Pager`这个注解
2. 写一个常规的查询(无需带分页)
3. 如果需要导出`Excel`，在`@Pager`上增加一点配置，再写一个简单的导出类即可

## 环境要求
* `Java8`及以上
* `SpringBoot2.x` + 基于`Servlet`的`Web`支持
* `Mybatis 3.x`

## 使用文档
1. [快速入门](docs/1.QuickStart.md)

## 开发者
### 构建说明
执行下面的代码来构建

```sh
./gradlew clean jar
```

你会在`build/libs`里找到构建结果

### 上传到 Maven 库
```sh
./gradlew clean jar test publish \
    -Dnexus.username="<Nexus Username>" \
    -Dnexus.password="<Nexus Password>" \
    -Dgpg.keyId="<GPG Key Id>" \
    -Dgpg.password="<GPG Password>" \
    -Dgpg.secretKeyRingFile="<GPG Secret File Name>"

# 可预先将用户名和密码放到环境变量中，免得每次都要输入
./gradlew clean jar test publish \
    -Dnexus.username="$nexusUsername" \
    -Dnexus.password="$nexusPassword" \
    -Dgpg.keyId="$gpgKeyId" \
    -Dgpg.password="$gpgPassword" \
    -Dgpg.secretKeyRingFile="$gpgSecretFileName"
```

### 签名
上传到`Maven`库的包使用`GPG`进行签名，公钥可在[这里](https://blog.cat73.org/about/gpg.html)获得

验签方法：

```sh
# 首先请自行下载 GPG 公钥并保存到本地，本例中使用 public.gpg 来保存公钥

# 导入 GPG 公钥
gpg --import public.gpg

# 验证签名
gpg --verify 文件名.asc 文件名
```

## FAQ
* 为何没使用`Lombok`？
    * `Lombok`对非`Java`的语言如`Kotlin`不太友好，与其想支持时再去掉，不如一开始就不用

## 参考内容
> 仅按字母序进行排序，无先后顺序

* [Mybatis](https://github.com/mybatis/mybatis-3)
* [Mybatis-PageHelper](https://github.com/pagehelper/Mybatis-PageHelper)
* [Spring-Boot](https://github.com/spring-projects/spring-boot)
