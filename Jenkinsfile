pipeline {
	agent any

	triggers{
		githubPush()
	}

	environment {
		NAMESPACE = "cisnux-skywalker-dev"
		BUILD_NAME = "devsecops-starter"
		DEPLOYMENT_NAME = "jenkins"
	}

	stages {
		stage('Trigger Build in OpenShift'){
			steps {
				sh "oc start-build ${BUILD_NAME} --from-dir=. --follow -n ${NAMESPACE}"
			}
		}

		stage("Deploy to OpenShift") {
			steps {
				sh "oc rollout latest dc/${DEPLOYMENT_NAME} -n ${NAMESPACE}"
			}
		}
	}

	post {
		success {
			echo "Build and deployment successful!"
		}
		failure {
			echo "Build or deployment failed."
		}
		always {
			echo "Pipeline execution completed."
		}
	}

	//properties([pipelineTriggers([githubPush()])])
	//
	//node {
	//	git credentialsId: 'b8d10285-xxxx-xxxx-xxxx-xxxxx',
    //    url: 'https://github.com/ConsorciAOC-PRJ/qa_payload-jenkins.git', branch: 'master'
	//}
}