plugins {
  // Apply the java-library plugin for API and implementation separation.
  `java-library`
  `maven-publish`
  signing

  id("io.github.mschout.all-conventions") version "0.6.0"

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

java {
  withSourcesJar()
  withJavadocJar()
}

dependencies {
  testImplementation("com.google.guava:guava:31.1-jre")

  compileOnly("io.netty:netty-codec:4.1.80.Final")
  testImplementation("io.netty:netty-codec:4.1.80.Final")
}

tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

signing {
  useGpgCmd()
  sign(publishing.publications)
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "io.github.mschout"
      artifactId = "netty-codec-netstring"

      from(components["java"])

      pom {
        name = "${groupId}:${artifactId}"
        description = "Interface to Email Sender Rewriting Scheme for Java"
        url = "https://github.com/mschout/netty-codec-netstring"
        licenses {
          license {
            name = "The Apache License, Version 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
          }
        }
        developers {
          developer {
            name = "Michael Schout"
            email = "schoutm@gmail.com"
            organizationUrl = "https://github.com/mscnout"
          }
        }
        scm {
          connection = "scm:git:git://github.com/mschout/netty-codec-netstring.git"
          developerConnection = "scm:git:ssh://github.com:mschout/netty-codec-netstring.git"
          url = "https://github.com/mschout/netty-codec-netstring/tree/master"
        }
      }
    }
  }
}

// nexusPublishing {
//   repositories {
//     sonatype {
//       nexusUrl = uri("https://s01.oss.sonatype.org/service/local/")
//       snapshotRepositoryUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//       username = System.getenv("OSSRH_USERNAME") ?: "credentials"
//       password = System.getenv("OSSRH_PASSWORD") ?: "credentials"
//     }
//   }
// }
