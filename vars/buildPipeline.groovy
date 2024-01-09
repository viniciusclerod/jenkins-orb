@Library('jenkins-orb')
import com.jenkins.ci.helpers.MapHelper
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.reference.Stage

def call(Configuration config) {
    // Built-in environment variables
    buildEnvironment(MapHelper.merge([
        'PROJECT_REPONAME': '$(git config --local remote.origin.url | sed -n \'s#.*/\\([^.]*\\)\\.git#\\1#p\')',
        'COMMIT_SHA1': '$(git rev-parse HEAD)',
        'COMMIT_SHA1_SHORT': '$(git rev-parse --short HEAD)'
    ], config.environment), true)
    // Build stages
    return { variables ->
        List<Stage> stgs = config.workflow
        stgs.each { stg -> buildStage(stg, config) }
    }
}
