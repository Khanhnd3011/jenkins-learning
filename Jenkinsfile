pipeline{

    agent any

    stages{
        stage('Environment'){
            steps{
                sh '''
                echo "====== USER ====="
                whoamin

                echo "====== WORKSPACE ====="
                pwd

                echo "====== JAVA ====="
                java -version

                echo "======= MAVEN ====="
                ./mvnw -version

                '''
            }
        }

        stage('Clean'){
            steps{
                echo 'Cleaning previous build output...'
                sh './mvnw -B clean'
            }
        }

        stage('Compile'){
            steps{
                echo 'Compiling application ...'
                sh './mvnw -B compile'
            }
        }

        stage('Test'){
            steps{
                echo 'Running tests ...'
                sh './mvnw -B test'
            }
        }

        stage('Package'){
            steps{
                echo 'Packagin Spring Boot applicaton ...'
                sh './mvnw -B package -DskipTests'
            }
        }

        stage('Verity Artifact'){
            steps{
                sh '''
                echo " ===== TARGET DIRECTORY ======"
                ls -lah target

                echo "====== JAR FILES ===="
                ls -lah target/*.jar

                '''
            }
        }
    }

    post{
        success{
            echo "Build #${BUILD_NUMBER} succeeded."
        }
        
        failure{
            echo "Build #${BUILD_NUMBER} failed. "
        }
    }
}