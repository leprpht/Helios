#!/bin/bash
set -e

cd helios-backend

if [ -f .env ]; then
  set -o allexport
  source .env
  set +o allexport
else
  echo ".env file not found in helios-backend!"
  exit 1
fi

echo -e "\033[33m[HELIOS BACKEND]\033[0m"

./gradlew test --info

echo -e
echo -e "\033[36m[HELIOS FRONTEND]\033[0m"

cd ../helios-frontend

npm run test
