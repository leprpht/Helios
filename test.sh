#!/bin/bash
set -e

cd helios-backend || { echo "Backend folder not found!"; exit 1; }

if [ -f .env ]; then
  set -o allexport
  source .env
  set +o allexport
else
  echo ".env file not found in helios-backend!"
  exit 1
fi

./gradlew test --info
