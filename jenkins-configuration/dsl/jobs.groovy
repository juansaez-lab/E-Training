
dsl.bitbucketSourcesMultibranchPipeline(
        context: this,
        name: 'e-time-backend-pipeline',
        description: 'Pipeline Multibranch for e-time-backend repository',
        repositoryName: 'e-time-backend'
)

dsl.bitbucketSourcesMultibranchPipeline(
        context: this,
        name: 'e-time-frontend-pipeline',
        description: 'Pipeline Multibranch for e-time-frontend repository',
        repositoryName: 'e-time-frontend'
)

dsl.bitbucketPipeline(
        context: this,
        name: 'e-time-backend-build-pipeline',
        description: 'Pipeline to build etime backend image',
        repositoryName: 'jenkins-configuration',
        scriptPath: 'pipelines/etimeBackendBuild.groovy',
        branch: 'main',
        parameters: [
                string: [
                        [
                                name: 'BranchOrTag',
                                description: 'Branch or Tag to build',
                        ]
                ]
        ]
)

dsl.bitbucketPipeline(
        context: this,
        name: 'e-time-fronted-build-pipeline',
        description: 'Pipeline to build etime frontend image',
        repositoryName: 'jenkins-configuration',
        scriptPath: 'pipelines/etimeFrontendBuild.groovy',
        branch: 'main',
        parameters: [
                string: [
                        [
                                name: 'BranchOrTag',
                                description: 'Branch or Tag to build',
                        ]
                ]
        ]
)

dsl.bitbucketPipeline(
        context: this,
        name: 'e-time-backend-deploy-pipeline',
        description: 'Pipeline to deploy etime backend',
        repositoryName: 'jenkins-configuration',
        scriptPath: 'pipelines/etimeBackendDeploy.groovy',
        branch: 'main',
        parameters: [
                string: [
                        [
                                name: 'BranchOrTag',
                                description: 'Branch or Tag to deploy',
                        ],
                ],
                choice: [
                        [
                                name: 'Environment',
                                description: 'Environment to deploy',
                                choices: ['staging', 'production']
                        ],
                        [
                                name: 'Location',
                                description: 'Location to deploy',
                                choices: ['es', 'mu'],
                        ],
                ],
        ]
)

dsl.bitbucketPipeline(
        context: this,
        name: 'e-time-frontend-deploy-pipeline',
        description: 'Pipeline to deploy etime frontend',
        repositoryName: 'jenkins-configuration',
        scriptPath: 'pipelines/etimeFrontendDeploy.groovy',
        branch: 'main',
        parameters: [
                string: [
                        [
                                name: 'BranchOrTag',
                                description: 'Branch or Tag to deploy',
                        ],
                ],
                choice: [
                        [
                                name: 'Environment',
                                description: 'Environment to deploy',
                                choices: ['staging', 'production']
                        ],
                        [
                                name: 'Location',
                                description: 'Location to deploy',
                                choices: ['es', 'mu'],
                        ],
                ],
        ]
)
