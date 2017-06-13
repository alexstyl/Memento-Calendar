# Build script taken and altered from: https://github.com/firebase/quickstart-android/blob/master/build.sh
#!/bin/bash

# Exit on error
set -e


# Work off travis
if [[ ! -z TRAVIS_PULL_REQUEST ]]; then
  echo "TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
else
  echo "TRAVIS_PULL_REQUEST: unset, setting to false"
  TRAVIS_PULL_REQUEST=false
fi

echo "Building ${SAMPLE}"

# Copy mock secret.gradle file if necessary
if [ ! -f secret.gradle ]; then
  echo "Secret.gradle not found. Using sample"
  cp secret.gradle.sample secret.gradle
fi

# Copy mock google-services file if necessary
if [ ! -f ./android_mobile/google-services.json ]; then
  echo "Using mock google-services.json"
  cp mock-google-services.json ./android_mobile/google-services.json
fi

# Build
if [ $TRAVIS_PULL_REQUEST = false ] ; then
  # For a merged commit, build all configurations.
  ./gradlew clean build
else
  # On a pull request, just build debug which is much faster and catches
  # obvious errors.
  ./gradlew check -PdisablePreDex --continue --stacktrace :android_mobile:assembleDebug
fi
