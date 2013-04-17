import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-spring-secure-project"
  val appVersion      = "1.0-SNAPSHOT"

  val mainSecurePlayDependencies = Seq(
    javaCore,

    // Spring Core
    "org.springframework" % "spring-context" % "3.2.2.RELEASE",

    // Support for AOP
    "org.springframework" % "spring-aop" % "3.2.2.RELEASE",

    // Support for Spring Expression
    "org.springframework" % "spring-expression" % "3.2.2.RELEASE",

    // Spring ORM, support for Hibernate or other ORMs
    "org.springframework" % "spring-orm" % "3.2.2.RELEASE",

    // Support for transactions
    "org.springframework" % "spring-tx" % "3.2.2.RELEASE",

    // Support for jdbc
    "org.springframework" % "spring-jdbc" % "3.2.2.RELEASE",

    // Support for Spring Data JPA
    "org.springframework.data" % "spring-data-jpa" % "1.3.0.RELEASE",

    // Hibernate Entity Manager
    "org.hibernate" % "hibernate-entitymanager" % "3.6.10.Final",

    // Java Persistence API implementation from hibernate
    "org.hibernate.java-persistence" % "jpa-api" % "2.0-cr-1",

    // Pool of conections
    "c3p0" % "c3p0" % "0.9.1.2",

    // jdbc connector
    "mysql" % "mysql-connector-java" % "5.1.24",

    // Spring Security Core
    "org.springframework.security" % "spring-security-core" % "3.1.3.RELEASE"
  )

  val mainSecurePlayProject = play.Project(appName, appVersion, mainSecurePlayDependencies).settings()

}
