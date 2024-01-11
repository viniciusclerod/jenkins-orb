// src/com/jenkins/ci/reference/Command.groovy
package com.jenkins.ci.reference

import com.jenkins.ci.helpers.MapHelper
import com.jenkins.ci.reference.Step

class Command {
    def context
    String name
    List<Step> steps = []
    Map parameters = [:]
    String description

    def call(arguments) {
        if (this.steps.isEmpty()) {
            this.context.invokeMethod(this.name, this.getArgs(arguments))
        } else {
            this.steps.each { step -> 
                Map args = step.arguments.collectEntries {
                    ["${it.key}": this.parseAttribute(it.value)]
                }
                step.call(args)
            }
        }
    }

    def getArgs(arguments) {
        Map defaultParams = this.parameters.collectEntries { key, val ->
            ["${key}": val.default]
        }.findAll { it.value != null }
        Map stepParams = arguments.collectEntries { key, val ->
            String type = value.getClass().getSimpleName().toLowerCase()
            return ["${key}": (type == this.parameters[key].type) ? val : null]
        }.findAll { it.value != null }
        return MapHelper.merge(defaultParams, stepParams)
    }

    def parseAttribute(String text) {
        return text.replaceAll(/<<\s*([\S]+)\s*>>/) { match ->
            def keys = match[1].split("\\.")
            def value = this
            keys.each { key -> value = value[key] }
            return value
        }
    }
}
