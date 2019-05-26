export BUILD_NUMBER=$(cat ../version/number)
export ROOT_FOLDER=$( pwd )
export GRADLE_USER_HOME="${ROOT_FOLDER}/.gradle"

./gradlew build publish