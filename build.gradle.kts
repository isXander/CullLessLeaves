plugins {
    `java-library`

    id("me.modmuss50.mod-publish-plugin") version "0.6.1+"
    `maven-publish`

    id("fabric-loom")
}

group = "dev.isxander"
val versionWithoutMC = property("modVersion")!!.toString()
version = "$versionWithoutMC+${stonecutter.current.project}"
val isAlpha = "alpha" in version.toString()
val isBeta = "beta" in version.toString()
base.archivesName.set(property("modName").toString())

repositories {
    mavenCentral()
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com")
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") }
        filter { includeGroup("maven.modrinth") }
    }
}

val mcVersion = property("mcVersion")!!.toString()
val mcSemverVersion = stonecutter.current.version

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabricLoader")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fapi")}")
    modImplementation("maven.modrinth:sodium:${property("deps.sodium")}")
    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
    modImplementation("com.terraformersmc:modmenu:${property("deps.modMenu")}")
}

tasks {
    processResources {
        val modId: String by project
        val modName: String by project
        val modDescription: String by project
        val githubProject: String by project

        val props = buildMap {
            put("id", modId)
            put("group", project.group)
            put("name", modName)
            put("description", modDescription)
            put("version", project.version)
            put("github", githubProject)

            put("mc", findProperty("fmj.mcDep"))
            put("fapi", findProperty("fmj.fapiDep") ?: "*")
        }
        props.forEach(inputs::property)

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.release = project.property("java.version").toString().toInt()
}

publishMods {
    val modChangelog =
        rootProject.file("changelog.md")
            .takeIf { it.exists() }
            ?.readText()
            ?.replace("{version}", versionWithoutMC)
            ?.replace("{targets}", stonecutter.versions
                .map { it.project }
                .joinToString(separator = "\n") { "- $it" })
            ?: "No changelog provided."
    changelog.set(modChangelog)

    file.set(tasks.remapJar.get().archiveFile)

    type.set(
        when {
            isAlpha -> ALPHA
            isBeta -> BETA
            else -> STABLE
        }
    )

    modLoaders.add("fabric")

    fun versionList(prop: String) = findProperty(prop)?.toString()
        ?.split(',')
        ?.map { it.trim() }
        ?: emptyList()

    // modrinth and curseforge use different formats for snapshots. this can be expressed globally
    val stableMCVersions = versionList("pub.stableMC")

    val modrinthId: String by project
    if (modrinthId.isNotBlank() && hasProperty("modrinth.token")) {
        modrinth {
            projectId.set(modrinthId)
            accessToken.set(findProperty("modrinth.token")?.toString())
            minecraftVersions.addAll(stableMCVersions)
            minecraftVersions.addAll(versionList("pub.modrinthMC"))

            announcementTitle = "Download $mcVersion for Fabric from Modrinth"

            requires { slug.set("yacl") }
            requires { slug.set("sodium") }

            requires { slug.set("fabric-api") }
            optional { slug.set("modmenu") }
        }
    }

    val curseforgeId: String by project
    if (curseforgeId.isNotBlank() && hasProperty("curseforge.token")) {
        curseforge {
            projectId = curseforgeId
            projectSlug = findProperty("curseforgeSlug")!!.toString()
            accessToken = findProperty("curseforge.token")?.toString()
            minecraftVersions.addAll(stableMCVersions)
            minecraftVersions.addAll(versionList("pub.curseMC"))

            announcementTitle = "Download $mcVersion for Fabric from CurseForge"

            requires { slug.set("yacl") }
            requires { slug.set("sodium") }

            requires { slug.set("fabric-api") }
            optional { slug.set("modmenu") }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mod") {
            groupId = "dev.isxander"
            artifactId = "cull-less-leaves"

            from(components["java"])
        }
    }

    repositories {
        val username = "XANDER_MAVEN_USER".let { System.getenv(it) ?: findProperty(it) }?.toString()
        val password = "XANDER_MAVEN_PASS".let { System.getenv(it) ?: findProperty(it) }?.toString()
        if (username != null && password != null) {
            maven(url = "https://maven.isxander.dev/releases") {
                name = "XanderReleases"
                credentials {
                    this.username = username
                    this.password = password
                }
            }
        } else {
            println("Xander Maven credentials not satisfied.")
        }
    }
}

tasks.create("releaseModVersion") {
    dependsOn("publishMods")
    dependsOn("publish")
}

