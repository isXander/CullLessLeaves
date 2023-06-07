plugins {
    java

    id("fabric-loom") version "1.2.+"
    id("io.github.juuxel.loom-quiltflower") version "1.10.+"

    id("com.modrinth.minotaur") version "2.+"
    id("com.matthewprenger.cursegradle") version "1.+"
    id("com.github.breadmoirai.github-release") version "2.4.+"
    `maven-publish`

    id("io.github.p03w.machete") version "2.+"
}

group = "dev.isxander"
version = "1.2.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.llamalad7.mixinextras")
            includeGroup("com.github.CaffeineMC")
        }
    }
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.isxander.dev/snapshots")
    maven("https://maven.shedaniel.me")
    maven("https://maven.terraformersmc.com")
}

//val sodium by sourceSets.registering {
//    compileClasspath += sourceSets.main.get().compileClasspath
//    runtimeClasspath += sourceSets.main.get().runtimeClasspath
//}

loom {
    runs {
//        register("sodium") {
//            client()
//            ideConfigGenerated(true)
//            name("Sodium Test")
//            source(sodium.get())
//        }

        getByName("client") {
            name("Vanilla Test")
        }

        getByName("server") {
            ideConfigGenerated(false)
        }
    }

//    createRemapConfigurations(sodium.get())
}

val minecraftVersion: String by project

dependencies {
    val fabricLoaderVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.+:v2")

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:3.0.0+1.20")
    modImplementation("com.terraformersmc:modmenu:7.0.0")

    "com.github.llamalad7.mixinextras:mixinextras-fabric:0.2.0-beta.8".let {
        implementation(it)
        annotationProcessor(it)
        include(it)
    }

    // sodium compat
    modImplementation("com.github.CaffeineMC:sodium-fabric:mc1.20-0.4.10")

    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:0.83.0+1.20")
}

java {
    withSourcesJar()
}

tasks {
    processResources {
        val modId: String by project
        val modName: String by project
        val modDescription: String by project
        val githubProject: String by project

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("github", githubProject)

        filesMatching(listOf("fabric.mod.json", "quilt.mod.json")) {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubProject,
            )
        }
    }

    register("releaseMod") {
        group = "mod"

        dependsOn("modrinth")
        dependsOn("modrinthSyncBody")
        dependsOn("curseforge")
        dependsOn("publish")
        dependsOn("githubRelease")
    }
}

val changelogText = file("changelogs/${project.version}.md").takeIf { it.exists() }?.readText() ?: "No changelog provided"

val modrinthId: String by project
if (modrinthId.isNotEmpty()) {
    modrinth {
        token.set(findProperty("modrinth.token")?.toString())
        projectId.set(modrinthId)
        versionNumber.set("${project.version}")
        versionType.set("release")
        uploadFile.set(tasks["remapJar"])
        gameVersions.set(listOf("1.19.4", "1.20"))
        loaders.set(listOf("fabric", "quilt"))
        changelog.set(changelogText)
        syncBodyFrom.set(file("README.md").readText())
        dependencies {
            required.project("yacl")
            optional.project("modmenu")
        }
    }
}

val curseforgeId: String by project
if (hasProperty("curseforge.token") && curseforgeId.isNotEmpty()) {
    curseforge {
        apiKey = findProperty("curseforge.token")
        project(closureOf<com.matthewprenger.cursegradle.CurseProject> {
            mainArtifact(tasks["remapJar"], closureOf<com.matthewprenger.cursegradle.CurseArtifact> {
                displayName = "${project.version}"
            })

            id = curseforgeId
            releaseType = "release"
            addGameVersion("1.19.4")
            addGameVersion("1.20")
            addGameVersion("Fabric")
            addGameVersion("Quilt")
            addGameVersion("Java 17")

            relations(closureOf<com.matthewprenger.cursegradle.CurseRelation> {
                requiredDependency("yacl")
                optionalDependency("modmenu")
            })

            changelog = changelogText
            changelogType = "markdown"
        })

        options(closureOf<com.matthewprenger.cursegradle.Options> {
            forgeGradleIntegration = false
        })
    }
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val githubProject: String by project
    val split = githubProject.split("/")
    owner(split[0])
    repo(split[1])
    tagName("${project.version}")
    targetCommitish("1.20")
    body(changelogText)
    releaseAssets(tasks["remapJar"].outputs.files)
}

tasks["githubRelease"].dependsOn("optimizeOutputsOfRemapJar")
tasks["modrinth"].dependsOn("optimizeOutputsOfRemapJar")

publishing {
    publications {
        create<MavenPublication>("mod") {
            groupId = group.toString()
            artifactId = property("modId") as String

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("XANDER_MAVEN_USER") && hasProperty("XANDER_MAVEN_PASS")) {
            maven(url = "https://maven.isxander.dev/releases") {
                credentials {
                    username = property("XANDER_MAVEN_USER")?.toString()
                    password = property("XANDER_MAVEN_PASS")?.toString()
                }
            }
        }
    }
}
