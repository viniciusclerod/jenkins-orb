@Library('jenkins-orb')
import com.jenkins.orb.*
import com.jenkins.orb.ProjectConfiguration
import com.jenkins.orb.parser.ConfigParser

def call(String yamlPath) {
  def yaml = readYaml file: yamlPath

  // load project's configuration
  ProjectConfiguration projectConfig = ConfigParser.parse(yaml, env)

  def imageName = projectConfig.dockerConfiguration.imageName().toLowerCase()

  // build the image specified in the configuration
  def customImage = docker.build(imageName, "--file ${projectConfig.dockerfile} .")

  println "customImage: ${customImage}"
}
