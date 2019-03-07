import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler

// 插件
plugins {
    `java-library`
    signing
    `maven-publish`
    id("org.springframework.boot") version "2.1.3.RELEASE" apply false
}
apply(plugin = "org.gradle.java-library")
apply(plugin = "org.gradle.signing")
apply(plugin = "org.gradle.maven-publish")
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

// GPG Sign
extra["signing.keyId"] = System.getProperty("gpg.keyId")
extra["signing.password"] = System.getProperty("gpg.password")
extra["signing.secretKeyRingFile"] = System.getProperty("gpg.secretKeyFile")

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

// 源码 jar 包(用于发布到 Maven 库)
val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allJava)
}

// JavaDoc jar 包配置
tasks.withType<Javadoc> {
    options.encoding = "UTF-8"

    (options as? StandardJavadocDocletOptions)?.also {
        it.charSet = "UTF-8"
        it.isAuthor = true
        it.isVersion = true
        it.links = listOf("https://docs.oracle.com/javase/8/docs/api")
        if (JavaVersion.current().isJava9Compatible) {
            it.addBooleanOption("html5", true)
        }
    }
}

// JavaDoc 包(用于发布到 Maven 库)
val javadocJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
    archiveClassifier.set("javadoc")
    from(tasks["javadoc"])
}

// 发布到 Maven 库
publishing {
    repositories {
        maven {
            // 目标仓库
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            setUrl(if (version.toString().endsWith("RELEASE")) releasesRepoUrl else snapshotsRepoUrl)

            // 登录凭据，从启动参数中读取，避免直接把密码暴漏在源代码中
            credentials {
                username = System.getProperty("nexus.username")
                password = System.getProperty("nexus.password")
            }
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            pom {
                name.set("mybatis-pager-spring-boot-starter")
                description.set("为 SpringBoot(基于 Servlet 的 Web 项目) + Mybatis 提供简单易用的分页查询支持")
                inceptionYear.set("2019")
                url.set("https://github.com/Cat7373/mybatis-pager-spring-boot-starter")

                artifactId = project.name
                groupId = "${project.group}"
                version = "${project.version}"
                packaging = "jar"

                scm {
                    connection.set("scm:git@github.com:Cat7373/mybatis-pager-spring-boot-starter.git")
                    developerConnection.set("scm:git@github.com:Cat7373/mybatis-pager-spring-boot-starter.git")
                    url.set("https://github.com/Cat7373/mybatis-pager-spring-boot-starter")
                }

                issueManagement {
                    url.set("https://github.com/Cat7373/mybatis-pager-spring-boot-starter/issues")
                }

                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://raw.githubusercontent.com/Cat7373/mybatis-pager-spring-boot-starter/master/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("Cat73")
                        name.set("Cat73")
                        email.set("root@cat73.org")
                        url.set("https://github.com/Cat7373")
                        timezone.set("Asia/Shanghai")
                    }
                }
            }

            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
        }
    }

    signing {
        sign(publishing.publications.getByName("mavenJava"))
    }
}
