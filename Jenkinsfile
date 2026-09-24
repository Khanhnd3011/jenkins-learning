pipeline{
    agent any 

    options{
        buildDiscarder(
            logRotator(
                numToKeepStr: '10', // Keep only the 10 most recent builds
                daysToKeepStr: '30', // Discard builds older than 30 days
                artifactNumToKeepStr: '5' ,// Keep artifacts for only the last 5 builds
                artifactDaysToKeepStr: '7' // Discard artifacts older than 7 days
            )
        )
    }

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


       stage('Credential Test') {

    steps {

        withCredentials([
            string(
                credentialsId: 'practice-api-token',
                variable: 'API_TOKEN'
            )
        ]) {

            sh '''
                test -n "$API_TOKEN"
                echo "Credential is available"
            '''

        }

    }
}


    post{
        always{
               junit  testResults: 'target/surefire-reports/*.xml',
                      allowEmptyResults: true
        }

        success{
            archiveArtifacts artifacts: 'target/*.jar',
                             fingerprint: true
            echo "building #${BUILD_NUMBER} successfully"
        }

        failure{
            echo "building #${BUILD_NUMBER} failed."
        }
    }
}