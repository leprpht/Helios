#!/bin/bash
set -e

concurrently \
  --names "BACKEND,FRONTEND" \
  --prefix "[HELIOS {name}]" \
  --prefix-colors "yellow,cyan" \
  "
    ./scripts/build-backend.sh
  " \
  "
    ./scripts/build-frontend.sh
  "
