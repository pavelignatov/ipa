#!/usr/bin/env bash
./gradlew clean
./gradlew web:assemble && java -jar web/build/libs/ipa-web-0.1.0.jar