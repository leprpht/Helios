#!/bin/bash
set -e

./scripts/test-backend.sh

./scripts/test-frontend.sh
