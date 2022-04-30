plugins {
    java

    id("fabric-loom") version "0.12.+"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.+"
    id("io.github.juuxel.loom-quiltflower") version "1.7.+"

    id("com.modrinth.minotaur") version "2.+"
    id("com.matthewprenger.cursegradle") version "1.+"
    id("com.github.breadmoirai.github-release") version "2.+"
    `maven-publish`
}

group = "dev.isxander"
version = "1.0.0"

repositories {
    mavenCentral()
}

val minecraftVersion: String by project

dependencies {
    val fabricLoaderVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        officialMojangMappings()
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:$minecraftVersion+build.+:v2"))
    })

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
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
        gameVersions.set(listOf(minecraftVersion))
        loaders.set(listOf("fabric", "quilt"))
        changelog.set(changelogText)
        syncBodyFrom.set(file("README.md").readText())
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
            addGameVersion(minecraftVersion)
            addGameVersion("Fabric")
            addGameVersion("Java 17")

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
    targetCommitish("1.18")
    body(changelogText)
    releaseAssets(tasks["remapJar"].outputs.files)
}

publishing {
    publications {
        create<MavenPublication>("mod") {
            groupId = group.toString()
            artifactId = base.archivesName.get()

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("woverflow.username") && hasProperty("woverflow.token")) {
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = property("woverflow.username")?.toString()
                    password = property("woverflow.token")?.toString()
                }
            }
        }
    }
}
