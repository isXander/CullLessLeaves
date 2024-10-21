plugins {
    id("fabric-loom") version "1.8.+" apply false
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21-fabric" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "mod"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledRelease", stonecutter.chiseled) {
    group = "mod"
    ofTask("releaseModVersion")
}