pipeline{
    agent any

    stages{
        stage('Source Info'){
            steps{
                echo 'Repository successfully loaded by jenkins'
                sh '''
                  echo "Current User:"
                  whoami

                  echo "Workspace:"
                  pwd

                  echo "Repository file:"
                  ls -la


                '''
            }
        }


        stage('Git info'){
            steps{
                sh '''
                echo "Git branch:"
                git branch --show-current

                echo "Commit:"
                git rev-parse --short HEAD

                echo "Commit message:"
                git log -1 --pretty=%B

                '''
            }
        }
        
        stage('Verify Project'){
            steps{
                sh '''
                echo "Checking spring boot project..."

                test -f pom.xml
                
                echo "pom.xml exists"

                '''
            }
        }
        stage('Pipeline Version'){
            steps{
                echo 'Pipeline version 2'
            }
        }
    }


    post{
        success{
            echo "Build #${BUILD_NUMBER} succeeded."
        }

        failure{
            echo "Build #${BUILD_NUMBER} failed."
        }
    }
}