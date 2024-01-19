package io.jenkins.plugins.ci

import io.jenkins.plugins.ci.model.Configuration
import io.jenkins.plugins.ci.parser.ConfigParser

class Pipeline {

    String yamlPath
    Configuration config

    Pipeline(String yamlPath) {
        this.yamlPath = yamlPath ?: '.jenkins/config.yaml'
    }

    def execute(def ctx) {
        this.buildPipeline(ctx)
    }

    def buildPipeline(def ctx) {
        def script = {
            node {
                this.buildSetupStage(ctx)
                this.buildStages(ctx)
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildSetupStage(def ctx) {
        def script = {
            stage('Setup') {
                checkout ctx.scm
                def yaml = readYaml file: this.yamlPath
                this.config = ConfigParser.parse(ctx, yaml)
                if (this.config.environment) {
                    List<String> environment = this.buildEnvironment(def ctx)
                    environment.each {
                        def (key, value) = it.split('=', 2)
                        ctx.env.setProperty(k, v)
                    }
                }
            }
        }
        script.delegate = ctx
        script.call()
    }

    def buildStages(def ctx) {
        def script = {
            stage('Schrödinger Cat') {
                this.config.steps.each { step ->
                    step.execute()
                }
            }
        }
        script.delegate = ctx
        script.call()
    }

    def getEnvironment(def ctx) {
        def script = {
            String output = sh(
                label: "Preparing environment variables",
                script: environment.collect { k, v -> "$k=$v && echo $k=\$$k"}.join('\n'),
                returnStdout: true
            ).trim()
            return output.split('\n').collect { it -> it }
        }
        script.delegate = ctx
        return script.call()
    }

}
