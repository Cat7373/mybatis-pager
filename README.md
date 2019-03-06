# mybatis-pager-spring-boot-starter
为 SpringBoot(基于 Servlet 的 Web 项目) + Mybatis 提供简单易用的分页查询支持

## 环境要求
* `Java8`及以上
* `SpringBoot2.x` + 基于`Servlet`的`Web`支持
* `Mybatis 3.x`

## 使用文档
1. [使用文档](docs/1.QuickStart.md)

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
