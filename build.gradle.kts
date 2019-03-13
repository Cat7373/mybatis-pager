// 插件
plugins {
    `java-library`
    signing
    `maven-publish`
}
apply(plugin = "org.gradle.java-library")
apply(plugin = "org.gradle.signing")
apply(plugin = "org.gradle.maven-publish")

group = "org.cat73"
version = "1.0.1-RELEASE"

// 依赖版本控制
val springBootVersion  = "2.1.3.RELEASE"
val pagehelperVersion  = "1.2.10"
val poiVersion         = "4.0.1"
val jsr305Version      = "3.0.2"
val dependencyNames = mapOf(
        "spring-boot-starter-dependencies"    to "org.springframework.boot:spring-boot-dependencies:$springBootVersion",
        "spring-boot-starter-aop"             to "org.springframework.boot:spring-boot-starter-aop",
        "spring-boot-starter-jdbc"            to "org.springframework.boot:spring-boot-starter-jdbc",
        "spring-boot-starter-web"             to "org.springframework.boot:spring-boot-starter-web",
        "spring-boot-starter-undertow"        to "org.springframework.boot:spring-boot-starter-undertow",
        "spring-boot-configuration-processor" to "org.springframework.boot:spring-boot-configuration-processor:$springBootVersion",
        "spring-boot-starter-test"            to "org.springframework.boot:spring-boot-starter-test",
        "jsr305"                              to "com.google.code.findbugs:jsr305:$jsr305Version",
        "pagehelper-spring-boot-starter"      to "com.github.pagehelper:pagehelper-spring-boot-starter:$pagehelperVersion",
        "mysql-connector-java"                to "mysql:mysql-connector-java",
        "poi"                                 to "org.apache.poi:poi:$poiVersion",
        "junit-jupiter-api"                   to "org.junit.jupiter:junit-jupiter-api",
        "junit-jupiter-engine"                to "org.junit.jupiter:junit-jupiter-engine"
)

// GPG Sign
extra["signing.keyId"] = System.getProperty("gpg.keyId")
extra["signing.password"] = System.getProperty("gpg.password")
extra["signing.secretKeyRingFile"] = System.getProperty("gpg.secretKeyRingFile")

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
    implementation          (enforcedPlatform("${dependencyNames["spring-boot-starter-dependencies"]}"))
    api                     ("${dependencyNames["spring-boot-starter-aop"]}")
    api                     ("${dependencyNames["spring-boot-starter-jdbc"]}")
    api                     ("${dependencyNames["spring-boot-starter-web"]}") {
                     exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    compileOnly             ("${dependencyNames["spring-boot-starter-undertow"]}")
    api                     ("${dependencyNames["pagehelper-spring-boot-starter"]}")
    api                     ("${dependencyNames["poi"]}")
    api                     ("${dependencyNames["jsr305"]}")
    annotationProcessor     ("${dependencyNames["spring-boot-configuration-processor"]}")
    compileOnly             ("${dependencyNames["spring-boot-configuration-processor"]}")

    testImplementation      ("${dependencyNames["spring-boot-starter-test"]}")
    testImplementation      ("${dependencyNames["spring-boot-starter-undertow"]}")
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
                url.set("https://github.com/Cat7373/mybatis-pager")

                artifactId = project.name
                groupId = "${project.group}"
                version = "${project.version}"
                packaging = "jar"

                scm {
                    connection.set("scm:git@github.com:Cat7373/mybatis-pager.git")
                    developerConnection.set("scm:git@github.com:Cat7373/mybatis-pager.git")
                    url.set("https://github.com/Cat7373/mybatis-pager")
                }

                issueManagement {
                    url.set("https://github.com/Cat7373/mybatis-pager/issues")
                }

                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://raw.githubusercontent.com/Cat7373/mybatis-pager/master/LICENSE")
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
