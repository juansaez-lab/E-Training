pipeline {
    agent {
        kubernetes {
            agentContainer('okd')
            agentInjection(true)
            yaml(
                    elcaBuildYamlPod(
                            disableJnlpAgent: true,
                            disableCacheVolume: true,
                            containers: [
                                    elcaContainers.oc()
                            ]
                    )
            )
        }
    }

    environment {
        CPU_LIMIT = getResources(params.Environment, params.Location, 'CPU_LIMIT')
        CPU_REQUESTS = getResources(params.Environment, params.Location, 'CPU_REQUESTS')
        MEMORY_LIMIT = getResources(params.Environment, params.Location, 'MEMORY_LIMIT')
        MEMORY_REQUESTS = getResources(params.Environment, params.Location, 'MEMORY_REQUESTS')
    }

    options {
        skipDefaultCheckout()
        timeout(time: 10, unit: 'MINUTES')
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

        stage('Deploy Backend') {
            steps {
                container('oc') {
                    script {
                        openshift.withCluster() {
                            openshift.withProject('prj-elca-timecontrol') {
                                def sanitizedTag = params.BranchOrTag.replaceAll(/[\/\\]/, '-')
                                sh "oc process -f deploy/e-time-backend/deploy/etime-backend-deployment.yml " +
                                        "IMAGE_TAG=${sanitizedTag} " +
                                        "ENVIRONMENT=${params.Environment} " +
                                        "LOCATION=${params.Location} " +
                                        "CPU_LIMIT=${CPU_LIMIT} " +
                                        "MEMORY_LIMIT=${MEMORY_LIMIT} " +
                                        "CPU_REQUESTS=${CPU_REQUESTS} " +
                                        "MEMORY_REQUESTS=${MEMORY_REQUESTS} | oc apply -f -"
                            }
                        }
                    }
                }
            }
        }
    }
}

def getResources(environment, location, type) {
    def resources = [
            staging: [
                    CPU_LIMIT: '500m', CPU_REQUESTS: '100m', MEMORY_LIMIT: '512Mi', MEMORY_REQUESTS: '256Mi'
            ],
            production: [
                    es: [CPU_LIMIT: '2', CPU_REQUESTS: '100m', MEMORY_LIMIT: '8Gi', MEMORY_REQUESTS: '5Gi'],
                    mu: [CPU_LIMIT: '2', CPU_REQUESTS: '100m', MEMORY_LIMIT: '2Gi', MEMORY_REQUESTS: '1Gi']
            ]
    ]

    return resources[environment]?.get(location)?.get(type)
            ?: resources[environment]?.get(type)
}

