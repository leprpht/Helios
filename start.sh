#!/bin/bash

concurrently \
  "cd helios-backend && ./gradlew bootRun" \
  "cd helios-frontend && npm run dev"
