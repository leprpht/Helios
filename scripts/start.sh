#!/bin/bash
set -e

concurrently \
  --names "BACKEND,FRONTEND" \
  --prefix "[HELIOS {name}]" \
  --prefix-colors "yellow,cyan" \
  "
    ./scripts/start-backend.sh
  " \
  "
    ./scripts/start-frontend.sh
  "
