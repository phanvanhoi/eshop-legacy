pipeline {
  agent {
    kubernetes {
      yaml """
kind: Pod
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
"""
    }
  }
  environment {
    IMAGE_REGISTRY = "566478607909.dkr.ecr.us-east-1.amazonaws.com"
  }
  stages {
    
    stage('Approval') {
      when {
        branch 'main'
      }
      steps {
        script {
          def plan = 'frontend CI'
          input message: "Do you want to build and push?",
              parameters: [text(name: 'Plan', description: 'Please review the work', defaultValue: plan)]
        }
      } 
    }
        
    stage('Build with Kaniko') {
      when {
        branch 'main'
      }
      steps {
        container(name: 'kaniko', shell: '/busybox/sh') {
          
          sh '''#!/busybox/sh
          /kaniko/executor \
          --git branch=main \
          --context=. \
          --destination=${IMAGE_REGISTRY}/eshop-frontend:0.0.1
          '''

        }
      }
      post {
        success { 
          slackSend(channel: 'C07RNC9DDBN', color: 'good', message: 'frontend CI success')
        }
        failure {
          slackSend(channel: 'C07RNC9DDBN', color: 'danger', message: 'frontend CI fail')
        }
      }
    }
  }
}