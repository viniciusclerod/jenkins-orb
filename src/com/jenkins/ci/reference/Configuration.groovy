// src/com/jenkins/ci/reference/Configuration.groovy
package com.jenkins.ci.reference

import com.jenkins.ci.reference.Job
import com.jenkins.ci.reference.Stage

class Configuration {
    def context
    
    Map environment = [:]
    Map commands = [:]
    List<Job> jobs = []
    List<Stage> workflow = []

    Configuration(context) {
        this.context = context
    }
}
