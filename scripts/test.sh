#!/bin/bash

export APP_ENV=LOCAL_TEST
echo "Running mvn $@ test"
mvn "$@" test
unset APP_ENV
