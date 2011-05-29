//http://wiki.gradle.org/display/GRADLE/Gradle+1.0-milestone-3+Release+Notes
//TODO DSL for grabbing all sourcesets ==> sourceSets.main.allSource.srcDirs.each { dir -> println "src dir: $dir" }
package com.jrocktech.gradle

import org.gradle.api.*

class MakeDirsPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Assign new convention object to project.
        // makedirs is a key for the plugins map. We can choose the name
        // ourselves here.
        project.convention.plugins.makedirs = new MakeDirsPluginConvention(project)

        // Closure to create a directory.
        def createDirs = {
            // Use basePackageDir property.
            def newDir = new File(it, project.convention.plugins.makedirs.basePackageDir)
            newDir.mkdirs()
        }

        project.task("mkdirs") << {
            if(project.plugins.hasPlugin('java')){
                project.sourceSets.all.java.srcDirs*.each createDirs
                project.sourceSets.all.resources.srcDirs*.each createDirs
            }
            if(project.plugins.hasPlugin('groovy')){
                project.sourceSets.all.groovy.srcDirs*.each createDirs
            }
            if(project.plugins.hasPlugin('scala')){
                project.sourceSets.all.scala.srcDirs*.each createDirs
            }
            if(project.plugins.hasPlugin('war')){
                createDirs project.webAppDir
            }
        }

        // Assign a description to the task.
        project.tasks.mkdirs.description = "Create source directories."
    }
}
