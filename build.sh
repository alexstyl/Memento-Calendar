#!/bin/bash

# Exit on error
set -e

# Limit memory usage
OPTS='-Dorg.gradle.jvmargs="-Xmx2048m -XX:+HeapDumpOnOutOfMemoryError"'


    # Copy mock secret.gradle file if necessary
    if [ ! -f secret.gradle ]; then
        echo "Secret.gradle not found. Using sample"
        cp secret.gradle.sample secret.gradle
    fi


    # Work off travis
    if [[ -v TRAVIS_PULL_REQUEST ]]; then
        echo "TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
    else
        echo "TRAVIS_PULL_REQUEST: unset, setting to false"
        TRAVIS_PULL_REQUEST=false
    fi

    # Build
    if [ $TRAVIS_PULL_REQUEST = false ] ; then
        # For a merged commit, build all configurations.
        GRADLE_OPTS=$OPTS ./gradlew clean build
    else
        # On a pull request, just build debug which is much faster and catches
        # obvious errors.
        GRADLE_OPTS=$OPTS ./gradlew clean :app:assembleDebug
    fi

