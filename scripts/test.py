#!/usr/bin/env python3

import subprocess
import sys

RESET = "\033[0m"
YELLOW = "\033[33m"
CYAN = "\033[36m"

def run_script(name, color, path):
    print(f"{color}[HELIOS {name}]{RESET} Starting {path}...")

    result = subprocess.run([path], check=False, text=True)
    
    if result.returncode != 0:
        print(f"{color}[HELIOS {name}]{RESET} Failed with exit code {result.returncode}")
        sys.exit(result.returncode)
    else:
        print(f"{color}[HELIOS {name}]{RESET} Completed successfully.")

def main():
    run_script("BACKEND", YELLOW, "./scripts/test-backend.sh")
    run_script("FRONTEND", CYAN, "./scripts/test-frontend.sh")

if __name__ == "__main__":
    main()
