def baseRepoUrl = 'https://github.com/MeetingTeam'
def mainBranch = 'feature/cicd'
def testBranch = 'test'

def appRepoName = 'chat-service'
def appRepoUrl = "${baseRepoUrl}/${appRepoName}.git"

def k8SRepoName = 'k8s-repo'
def helmPath = "${k8SRepoName}/application/${appRepoName}"
def helmValueFile = "values.yaml"

def dockerhubAccount = 'dockerhub'
def githubAccount = 'github'

def dockerImageName = 'hungtran679/mt_chat-service'
def dockerfilePath = '.'

def kanikoCacheImage = 'hungtran679/mt_kaniko_cache'
def sonarCloudOrganization = 'meetingteam'


def version = "v2.${BUILD_NUMBER}"

pipeline{
         agent {
                    kubernetes {
                              inheritFrom 'springboot'
                    }
          }
          
          environment {
                    DOCKER_REGISTRY = 'registry-1.docker.io'           
          }
          
          stages{
                    stage('code analysis'){
                              steps{
                                        container('maven'){
                                                  withSonarQubeEnv('SonarCloud') {
                                                            sh "mvn sonar:sonar -Dsonar.organization=${sonarCloudOrganization}"
                                                  }
                                        }
                              }
                    }
                    stage('Quality Gate Check') {
                              steps {
                                        timeout(time: 5, unit: 'MINUTES') {
                                                  waitForQualityGate(abortPipeline: true)
                                        }
                              }
                    }
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
                                                                      echo "{ \\"auths\\": { \\"\${DOCKER_REGISTRY}\\": { \\"auth\\": \\"\$(echo -n \${DOCKER_USER}:\${DOCKER_PASS} | base64)\\" } } }" > /kaniko/.docker/config.json
                                                                      /kaniko/executor \
                                                                      --context=${dockerfilePath} \
                                                                      --dockerfile=${dockerfilePath}/Dockerfile \
                                                                      --destination=\${DOCKER_REGISTRY}/${dockerImageName}:${version} \
                                                            """
                                                  }
                                        }
                              }
                    }
                    stage('scan built image'){
                              when{ branch mainBranch }
                              steps{
                                        container('trivy'){
                                                  sh "trivy image --timeout 15m \${DOCKER_REGISTRY}/${dockerImageName}:${version}"
                                                  //sh "trivy image --timeout 15m --severity HIGH,CRITICAL --exit-code 1 \${DOCKER_REGISTRY}/${dockerImageName}:${version}"
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
                                                            git clone https://\${GIT_USER}:\${GIT_PASS}@github.com/MeetingTeam/${k8SRepoName}.git --branch ${testBranch}
                                                            cd ${helmPath}
                                                            sed -i 's|  tag: .*|  tag: "${version}"|' ${helmValueFile}

                                                            git config --global user.email "jenkins@gmail.com"
                                                            git config --global user.name "Jenkins"
                                                            git add .
                                                            git commit -m "feat: update to version ${version}"
                                                            git push origin ${testBranch}
                                                  """		
				}				
                              }
                    }
          }
}