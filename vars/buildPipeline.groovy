@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.jobs.Job
import com.jenkins.ci.reference.workflow.Stage

def call(Configuration config) {
    // env.PROJECT=$(git config --local remote.origin.url|sed -n 's#.*/\([^.]*\)\.git#\1#p')
    // env.TAG=$(git rev-parse --short HEAD)

    return { variables ->
        List<Stage> stgs = config.workflow
        stgs.each { stg ->
            if (stg.branches == null || (env.BRANCH_NAME =~ stg.branches).matches()) {
                stage(stg.name) {
                    if (stg.type == 'approval') {
                        input(message: "Approval is required to proceed.")
                    } else {
                        Job job = config.jobs.find { j -> j.name == stg.key }
                        jobEnv = job.environment.collect { k, v -> "${k}=${v}"}
                        echo "variables ${variables}"
                        echo "jobEnv ${jobEnv}"
                        withEnv(jobEnv) {
                            job.steps.each { step ->
                                if (step.type == 'sh') {
                                    sh script: step.command, returnStdout: true, label: step.name
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
