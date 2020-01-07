# [v1.0.4-RELEASE] 20??-??-??

# [v1.0.3-RELEASE] 2020-01-07
* 例行升级依赖

# [v1.0.2-RELEASE] 2019-06-12
* 现在使用导出模式时，可以再少写一点代码了
* 一些性能优化
* Fix [GitHub#1](https://github.com/Cat7373/mybatis-pager/issues/1)、[GitHub#2](https://github.com/Cat7373/mybatis-pager/issues/2)
* 修复多线程使用时`PagerResults`可能会出现的异常

# [v1.0.1-RELEASE] 2019-03-13
* 简化返回值处理器的实现，多数情况下可以少覆盖一个方法了
* 不再向外传递`WEB`容器的依赖，避免与使用者自行引入的重复和出现冲突
* 移除不必要的依赖，减小打包体积
* 增加空安全的注解
* 已测试可用于`SpringBoot 1.x`，测试的版本包括：
    * 由本包自动引入的依赖直接支持
        * 1.5.19.RELEASE
        * 1.4.7.RELEASE
        * 1.4.4.RELEASE
        * 1.4.0.RELEASE
    * 手动配置可用的`Mybatis`环境后支持
        * 1.3.8.RELEASE
        * 1.3.0.RELEASE
        * 1.2.1.RELEASE
        * 1.2.0.RELEASE
    * 需手动配置可用的`Mybatis`环境，且只支持分页，不支持导出模式
        * 1.1.12.RELEASE
        * 1.1.0.RELEASE
    * 测试环境配置太麻烦，未测试成功
        * 1.0.2.RELEASE
        * 1.0.0.RELEASE

# [v1.0.0-RELEASE] 2019-03-07
* 首个可用版本
