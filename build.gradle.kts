import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
  // Apply the java-library plugin for API and implementation separation.
  `java-library`

  id("io.github.mschout.all-conventions") version "0.6.0"
  id("io.github.mschout.maven-publish-conventions") version "0.6.0"

  // delombok sources
  id("io.freefair.lombok") version "9.5.0"
}

lombok { version = "1.18.46" }

group = "io.github.mschout"

description = "netty-codec-netstring"

val gitVersion = extra["gitVersion"] as groovy.lang.Closure<*>

version = gitVersion.call().toString()

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  testImplementation("com.google.guava:guava:31.1-jre")

  compileOnly("io.netty:netty-codec:4.1.80.Final")
  testImplementation("io.netty:netty-codec:4.1.80.Final")
}

tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

mavenPublishing {
  configure(JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = true))

  publishToMavenCentral()

  signAllPublications()

  coordinates(group.toString(), "netty-codec-netstring", version.toString())

  pom {
    name.set("netty-codec-netstring")
    description.set("Netstring encoder/decoder for Netty")
    url.set("https://github.com/mschout/netty-codec-netstring")

    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }

    developers {
      developer {
        id.set("mschout")
        name.set("Michael Schout")
        url.set("https://github.com/mschout")
      }
    }

    scm {
      url.set("https://github.com/mschout/netty-codec-netstring")
      connection.set("scm:git:git://github.com/mschout/netty-codec-netstring.git")
      developerConnection.set("scm:git:ssh://git@github.com/mschout/netty-codec-netstring.git")
    }
  }
}
