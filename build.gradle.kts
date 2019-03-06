import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler

// 插件
plugins {
    `java-library`
    id("org.springframework.boot") version "2.1.3.RELEASE" apply false
}
apply(plugin = "org.gradle.java-library")
apply(plugin = "io.spring.dependency-management")

group = "org.cat73"
version = "1.0.0-SNAPSHOT"

// 依赖版本控制
val pagehelperVersion  = "1.2.10"
val poiVersion         = "4.0.1"
val dependencyNames = mapOf(
        "spring-boot-starter-aop"             to "org.springframework.boot:spring-boot-starter-aop",
        "spring-boot-starter-jdbc"            to "org.springframework.boot:spring-boot-starter-jdbc",
        "spring-boot-starter-web"             to "org.springframework.boot:spring-boot-starter-web",
        "spring-boot-configuration-processor" to "org.springframework.boot:spring-boot-configuration-processor",
        "spring-boot-starter-test"            to "org.springframework.boot:spring-boot-starter-test",
        "pagehelper-spring-boot-starter"      to "com.github.pagehelper:pagehelper-spring-boot-starter:$pagehelperVersion",
        "mysql-connector-java"                to "mysql:mysql-connector-java",
        "poi"                                 to "org.apache.poi:poi:$poiVersion",
        "poi-ooxml"                           to "org.apache.poi:poi-ooxml:$poiVersion",
        "junit-jupiter-api"                   to "org.junit.jupiter:junit-jupiter-api",
        "junit-jupiter-engine"                to "org.junit.jupiter:junit-jupiter-engine"
)

// Spring 依赖管理
configure<DependencyManagementExtension> {
    imports(delegateClosureOf<ImportsHandler> {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    })
}

// Java 版本
configure<JavaPluginConvention> {
    val javaVersion = JavaVersion.VERSION_1_8

    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks {
    // 编码
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    // 使用 JUnit 的单元测试平台
    withType<Test> {
        useJUnitPlatform()
    }
}

// 仓库配置
repositories {
    jcenter()
}

// 公共依赖
dependencies {
    api                     ("${dependencyNames["spring-boot-starter-aop"]}")
    api                     ("${dependencyNames["spring-boot-starter-jdbc"]}")
    api                     ("${dependencyNames["spring-boot-starter-web"]}")
    api                     ("${dependencyNames["pagehelper-spring-boot-starter"]}")
    api                     ("${dependencyNames["poi"]}")
    api                     ("${dependencyNames["poi-ooxml"]}")
    annotationProcessor     ("${dependencyNames["spring-boot-configuration-processor"]}")
    compileOnly             ("${dependencyNames["spring-boot-configuration-processor"]}")

    testImplementation      ("${dependencyNames["spring-boot-starter-test"]}")
    testImplementation      ("${dependencyNames["junit-jupiter-api"]}")
    testRuntimeOnly         ("${dependencyNames["junit-jupiter-engine"]}")
    testRuntimeOnly         ("${dependencyNames["mysql-connector-java"]}")
}
