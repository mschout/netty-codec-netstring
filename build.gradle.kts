import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
  // Apply the java-library plugin for API and implementation separation.
  `java-library`

  alias(libs.plugins.mschout.all.conventions)
  alias(libs.plugins.mschout.maven.publish.conventions)

  // delombok sources
  alias(libs.plugins.freefair.lombok)
}

lombok { version = libs.versions.lombok.get() }

group = "io.github.mschout"

description = "netty-codec-netstring"

val gitVersion = extra["gitVersion"] as groovy.lang.Closure<*>

version = gitVersion.call().toString()

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  compileOnly(libs.netty.codec)
  testImplementation(libs.netty.codec)
}

tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

mavenPublishing {
  configure(JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = SourcesJar.Sources()))

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
