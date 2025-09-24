pipeline {
    agent {
        kubernetes {
            yaml(
                elcaBuildYamlPod(
                    disableCacheVolume: true,
                    containers: [
                        elcaContainers.oc(),
                        elcaContainers.maven(
                            tag: '3.9.9-eclipse-temurin-21',
                            requestMemory: '3Gi',
                            limitMemory: '3Gi',
                            limitCpu: '2000m'
                        )
                    ]
                )
            )
        }
    }

    environment {
        MAVEN_LOCAL_REPO = '/home/jenkins/.m2/repository'
        MAVEN_OPTIONS = '-B -V'
    }

    options {
        timeout(time: 60, unit: 'MINUTES')
        cache(defaultBranch: 'main', caches: [elcaMavenCache()])
    }

    stages {
        stage('Clone Deploy') {
            steps {
                script {
                    dir('deploy') {
                        git branch: 'master',
                            url: 'ssh://git@bitbucket.svc.elca.ch:7999/elca-timecontrol/e-time-deploy.git',
                            credentialsId: 'git-ssh'
                    }
                }
            }
        }
        stage('Clone Backend') {
            steps {
                script {
                    dir('backend') {
                        git branch: 'master',
                            url: 'ssh://git@bitbucket.svc.elca.ch:7999/elca-timecontrol/e-time-backend.git',
                            credentialsId: 'git-ssh'

                        def isTag = sh(script: "git show-ref --tags | grep -q 'refs/tags/${params.BranchOrTag}'", returnStatus: true) == 0

                        if (isTag) {
                            sh "git checkout tags/${params.BranchOrTag}"
                        } else {
                            sh "git checkout ${params.BranchOrTag}"
                        }
                    }
                }
            }
        }
        stage('Build Backend') {
            steps {
                script {
                    dir('backend') {
                        elcaMavenCommand(goals: 'clean install', properties: '-P deployment -DskipTests')
                    }

                    sh 'cp -r backend/target deploy/e-time-backend/build/docker-container/'
                }
            }
        }
        stage('Create BuildConfigs in OpenShift') {
            steps {
                container('oc') {
                    script {
                        openshift.withCluster() {
                            openshift.withProject('prj-elca-timecontrol') {
                                def sanitizedTag = params.BranchOrTag.replaceAll(/[\/\\]/, '-')
                                sh "oc process -f deploy/e-time-backend/build/build.yml -p IMAGE_TAG=${sanitizedTag} | oc apply -f -"
                            }
                        }
                    }
                }
            }
        }
        stage('Trigger OpenShift Build for Backend') {
            steps {
                script {
                    container('oc') {
                        openshift.withCluster() {
                            openshift.withProject('prj-elca-timecontrol') {
                                def buildSuccess = false
                                def attempts = 0
                                def maxAttempts = 3

                                while (!buildSuccess && (attempts < maxAttempts)) {
                                    attempts++
                                    def status = sh(script: "oc start-build etime-backend-build-config --from-dir=deploy/e-time-backend/build/docker-container --wait", returnStatus: true)

                                    if (status == 0) {
                                        echo "Build successful on attempt ${attempts}"
                                        buildSuccess = true
                                    } else {
                                        echo "Build attempt ${attempts} failed"
                                        if (attempts < maxAttempts) {
                                            echo "Retrying..."
                                        }
                                    }
                                }

                                if (!buildSuccess) {
                                    error "Backend build failed after ${maxAttempts} attempts"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
