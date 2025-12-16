#!/bin/bash
set -e

cd helios-backend

if [ -f .env ]; then
  set -o allexport
  source .env
  set +o allexport
else
  echo ".env file not found"
  exit 1
fi

./gradlew build
