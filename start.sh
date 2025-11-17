#!/bin/bash
set -e

concurrently \
  --names "BACKEND,FRONTEND" \
  --prefix "[HELIOS {name}]" \
  --prefix-colors "yellow,cyan" \
  "
    cd helios-backend && \
    if [ -f .env ]; then \
      set -o allexport &&
      source .env &&
      set +o allexport; \
    else \
      echo '.env file not found' && exit 1; \
    fi && \
    ./gradlew bootRun
  " \
  "
    cd helios-frontend && \
    npm run dev
  "
