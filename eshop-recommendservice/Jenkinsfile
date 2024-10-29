pipeline {
  agent {
    kubernetes {
      yaml '''
kind: Pod
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
'''
    }
  }
  environment {
    IMAGE_REGISTRY = '566478607909.dkr.ecr.us-east-1.amazonaws.com'
  }
  stages {
    stage('Build with Kaniko') {
      steps {
        container(name: 'kaniko', shell: '/busybox/sh') {
          dir('eshop-recommendservice') {
            sh '''#!/busybox/sh
            /kaniko/executor \
            --git branch=feature-c1 \
            --context=. \
            --destination=${IMAGE_REGISTRY}/eshop-recommendservice:0.0.1
            '''
          }
        }
      }
    }
  }
}
