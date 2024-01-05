@Library('jenkins-orb')
import com.jenkins.ci.reference.Configuration
import com.jenkins.ci.parser.ConfigParser

def call(String yamlPath) {
    env.PROJECT=$(git config --local remote.origin.url|sed -n 's#.*/\([^.]*\)\.git#\1#p')
    env.TAG=$(git rev-parse --short HEAD)

    def yaml = readYaml file: yamlPath
    Configuration config = ConfigParser.parse(yaml, env)
    def closure = buildPipeline(config)
    closure([:])
}
