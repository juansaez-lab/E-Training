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
        CPU_LIMIT = getResources(params.Environment, 'CPU_LIMIT')
        CPU_REQUESTS = getResources(params.Environment, 'CPU_REQUESTS')
        MEMORY_LIMIT = getResources(params.Environment, 'MEMORY_LIMIT')
        MEMORY_REQUESTS = getResources(params.Environment, 'MEMORY_REQUESTS')
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

        stage('Deploy Frontend') {
            steps {
                container('oc') {
                    script {
                        openshift.withCluster() {
                            openshift.withProject('prj-elca-timecontrol') {
                                def sanitizedTag = params.BranchOrTag.replaceAll(/[\/\\]/, '-')
                                def status = sh (script: "oc process -f deploy/e-time-frontend/deploy/etime-frontend-deployment.yml " +
                                        "IMAGE_TAG=${sanitizedTag} " +
                                        "ENVIRONMENT=${params.Environment} " +
                                        "LOCATION=${params.Location} " +
                                        "CPU_LIMIT=${CPU_LIMIT} " +
                                        "MEMORY_LIMIT=${MEMORY_LIMIT} " +
                                        "CPU_REQUESTS=${CPU_REQUESTS} " +
                                        "MEMORY_REQUESTS=${MEMORY_REQUESTS} " +
                                        "| oc apply -f - 2>&1 | tee output.log",
                                        returnStatus: true
                                )

                                def output = readFile('output.log')
                                if (status != 0 && output.contains("field is immutable")) {
                                    echo "Ignoring immutable route error..."
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

def getResources(environment, type) {
    def resources = [
            staging: [
                    CPU_LIMIT: '500m', CPU_REQUESTS: '100m', MEMORY_LIMIT: '512Mi', MEMORY_REQUESTS: '256Mi'
            ],
            production: [
                    CPU_LIMIT: '1', CPU_REQUESTS: '100m', MEMORY_LIMIT: '1Gi', MEMORY_REQUESTS: '512Mi'
            ]
    ]

    return resources[environment]?.get(type)
}
