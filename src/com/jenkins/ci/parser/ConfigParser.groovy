// src/com/jenkins/ci/parser/ConfigParser.groovy
package com.jenkins.ci.parser

import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.steps.Step

class ConfigParser {

    static Configuration parse(def yaml, def env) {
        Configuration config = new Configuration();
        
        config.jobs = parseJobs(yaml.jobs);

        return config;
    }

    static List<Job> parseJobs(def yamlJobs) {
        List<Job> jobs = yamlJobs.collect { key, value ->
            Job job = new Job(name: key)
            value.each {
                job.steps.add(it);
            }
            // job.steps.add(parseSteps(value.steps))
            return job
        }
        return jobs
    }

    // static List<Step> parseSteps(def yamlSteps) {
        // List<Step> steps = yamlSteps.collect { key, value ->
        //     Step step = new Step(name: key, command: value)
        //     return step
        // }
        // return steps
    //     return [[name: "run", command:""]]
    // }

}
