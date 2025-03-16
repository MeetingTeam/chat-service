def baseRepoUrl = 'https://github.com/MeetingTeam/'
def mainBranch = 'feature/cicd'
def devBranch = 'dev'

def appRepoName = 'chat-service'
def appRepoUrl = "${baseRepoUrl}/${appRepoName}.git"

def k8SRepoName = 'k8s-repo'
def k8SRepoUrl = "${baseRepoUrl}/${k8SRepoName}.git"
def helmPath = "${k8SRepoName}/application/${appRepoName}"
def helmValueFile = "values.yaml"

def dockerhubAccount = 'dockerhub'
def githubAccount = 'github'

def dockerfilePath = '.'
def migrationPath = 'src/main/resources/migration'
def version = "v2.${BUILD_NUMBER}"

pipeline{
         agent {
                    kubernetes {
                              inheritFrom 'springboot'
                    }
          }
          
          environment {
                    DOCKER_REGISTRY = 'https://registry-1.docker.io'
                    DOCKER_IMAGE_NAME = 'hungtran679/mt_chat-service'
          }
          
          stages{
                    stage('unit test stage'){
                              steps{
                                        container('maven'){
                                                  withCredentials([
                                                            usernamePassword(
                                                                      credentialsId: githubAccount, 
                                                                      passwordVariable: 'GIT_PASS', 
                                                                      usernameVariable: 'GIT_USER'
                                                            )
                                                  ]) {
                                                           sh """
                                                                      echo "<settings>
                                                                                          <servers>
                                                                                                    <server>
                                                                                                              <id>github</id>
                                                                                                              <username>\${GIT_USER}</username>
                                                                                                              <password>\${GIT_PASS}</password>
                                                                                                    </server>
                                                                                          </servers>
                                                                                </settings>" > /root/.m2/settings.xml
                                                                      mvn clean test
                                                           """
                                                  }                                        
                                        }
                              }
                    }
                    stage('build jar file'){
                              when{ branch mainBranch }
                              steps{
                                        container('maven'){
                                                   withCredentials([
                                                            usernamePassword(
                                                                      credentialsId: githubAccount, 
                                                                      passwordVariable: 'GIT_PASS', 
                                                                      usernameVariable: 'GIT_USER'
                                                            )
                                                  ]) {
                                                            sh "mvn clean package -DskipTests=true"
                                                  }
                                        }
                              }
                    }
                    stage('build and push docker image'){
                              when{ branch mainBranch }
                              steps{
                                        container('kaniko'){
                                                   withCredentials([
                                                            usernamePassword(
                                                                      credentialsId: dockerhubAccount, 
                                                                      usernameVariable: 'DOCKER_USER', 
                                                                      passwordVariable: 'DOCKER_PASS'
                                                            )
                                                  ]) {
                                                            sh """
                                                                      echo "{ "auths": { "${DOCKER_REGISTRY}": { "auth": "$(echo -n ${DOCKER_USER}:${DOCKER_PASS} | base64)" } } }" > /kaniko/.docker/config.json
                                                                      /kaniko/executor \
                                                                      --context=${dockerfilePath} \
                                                                      --dockerfile=${dockerfilePath}/Dockerfile \
                                                                      --destination=${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${version} \
                                                                      --cache=true \
                                                                      --cache-dir=/cache
                                                            """
                                                  }
                                        }
                              }
                    }
                    stage('update k8s repo'){
                              when{ branch mainBranch }
                              steps {
				withCredentials([
                                                  usernamePassword(
                                                            credentialsId: githubAccount, 
                                                            passwordVariable: 'GIT_PASS', 
                                                            usernameVariable: 'GIT_USER'
                                                  )
                                        ]) {
                                                  sh """
                                                            git clone ${k8SRepo} --branch ${k8SBranch}
                                                            cd ${helmPath}
                                                            sed -i 's|  tag: .*|  tag: "${version}"|' ${helmValueFile}

                                                            git config --global user.email "kobiet@gmail.com"
                                                            git config --global user.name "TeoTran"
                                                            git add . 
                                                            git commit -m "feat: update to version ${version}"
                                                            git push https://${GIT_USER}:${GIT_PASS}@github.com/HungTran170904/${k8SRepoName}.git
                                                  """		
				}				
                              }
                    }
          }
}