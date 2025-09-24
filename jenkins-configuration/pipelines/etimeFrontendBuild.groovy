pipeline {
    agent {
        kubernetes {
            yaml(
                elcaBuildYamlPod(
                    disableCacheVolume: true,
                    containers: [
                        elcaContainers.oc(),
                        elcaContainers.node(
                            tag: '22.14.0',
                            requestMemory: '6Gi',
                            limitMemory: '6Gi',
                            limitCpu: '2000m'
                        )
                    ]
                )
            )
        }
    }

    environment {
        NPM_SETTINGS = 'global-npm'
    }

    options {
        skipDefaultCheckout()
        timeout(time: 60, unit: 'MINUTES')
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
        stage('Clone Frontend') {
            steps {
                script {
                    dir('frontend') {
                        git branch: 'master',
                                url: 'ssh://git@bitbucket.svc.elca.ch:7999/elca-timecontrol/e-time-frontend.git',
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
        stage('Build Frontend') {
            steps {
                script {
                    dir('frontend') {
                        elcaNodeCommand(
                                binary: 'yarn',
                                command: 'add -D @angular/cli @popperjs/core @zxcvbn-ts/core @zxcvbn-ts/language-en',
                                envs: ['PATH+EXTRA': '/home/jenkins/.local/bin:/usr/local/bin:/usr/local/lib/node_modules/.bin']
                        )

                        elcaNodeCommand(
                                binary: 'yarn',
                                command: 'ng build -c=deployment',
                                envs: [
                                        'PATH+EXTRA': '/home/jenkins/.local/bin:/usr/local/bin:/usr/local/lib/node_modules/.bin',
                                        'NODE_OPTIONS': '--max_old_space_size=4096'
                                ]
                        )
                    }

                    sh 'cp -r frontend/dist deploy/e-time-frontend/build/docker-container/'
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
                                sh "oc process -f deploy/e-time-frontend/build/build.yml -p IMAGE_TAG=${sanitizedTag} | oc apply -f -"
                            }
                        }
                    }
                }
            }
        }
        stage('Trigger OpenShift Build for Frontend') {
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
                                    def status = sh(script: "oc start-build etime-frontend-build-config --from-dir=deploy/e-time-frontend/build/docker-container --wait", returnStatus: true)

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
                                    error "Frontend build failed after ${maxAttempts} attempts"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
