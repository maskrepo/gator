val quarkusVersion: String = "1.8.0.Final"
val maskModelVersion = "1.0.1-SNAPSHOT"
val maskUtilVersion = "1.0.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.10"
    id("io.quarkus") version "1.8.0.Final"
}

val myMavenRepoUser = "myMavenRepo"
val myMavenRepoPassword ="mask"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repository.aspose.com/repo/")
    }
    maven {
        url = uri("https://mymavenrepo.com/repo/OYRB63ZK3HSrWJfc2RIB/")
        credentials {
            username = myMavenRepoUser
            password = myMavenRepoPassword
        }
    }
}

dependencies {
    implementation("io.quarkus:quarkus-config-yaml:$quarkusVersion")
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging:$quarkusVersion")
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka:$quarkusVersion")
    implementation("io.quarkus:quarkus-rest-client:$quarkusVersion")
    implementation("io.quarkus:quarkus-vertx:$quarkusVersion")
    implementation("io.quarkus:quarkus-resteasy:$quarkusVersion")
    implementation("org.jboss.resteasy:resteasy-multipart-provider:4.5.6.Final")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    implementation("com.aspose:aspose-pdf:17.10")

    implementation("fr.convergence.proddoc.lib:mask-util:$maskUtilVersion")
    implementation("fr.convergence.proddoc.lib:mask-model:$maskModelVersion")


    testImplementation("io.quarkus:quarkus-junit5:$quarkusVersion")
    testImplementation("io.rest-assured:kotlin-extensions:4.3.0")
}

group = "fr.convergence.proddoc"
version = "1.0.0-SNAPSHOT"

quarkus {
    setOutputDirectory("$projectDir/build/classes/kotlin/main")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    quarkusDev {
        setSourceDir("$projectDir/src/main/kotlin")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        kotlinOptions.javaParameters = true
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }
    test {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }
}