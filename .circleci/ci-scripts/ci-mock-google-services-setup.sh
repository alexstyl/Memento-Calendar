#!/usr/bin/env bash

export APP_MODULE_PATH="android_mobile"
export JSON_PATH="$APP_MODULE_PATH/google-services.json"

if [ ! -e ${JSON_PATH} ]; then
    echo "Add mock google-services.json file to $JSON_PATH..."
    cp -f mock-google-services.json  ${JSON_PATH}

    echo "Done."
else
    echo "The $JSON_PATH file already exists, skipping..."
fi
