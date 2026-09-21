pipeline{
    agent any 

    stages{
        
        stage('Environment'){

            steps{
                sh '''
                java -version
                ./mvnw -version

                '''
            }
        }

        stage('Build and Test'){

            steps{

              sh './mvnw -B clean verify'

            }
        }


        stage('Artifact'){

            steps{

               sh 'ls -lah target/*.jar'

            }
        }

    }


    post{
        success{
            echo "building #${BUILD_NUMBER} successfully"
        }

        failure{
            echo "building #${BUILD_NUMBER} failed."
        }
    }
}