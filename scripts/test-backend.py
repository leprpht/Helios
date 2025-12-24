#!/usr/bin/env python3

import os
import subprocess
import sys
from pathlib import Path

BACKEND_DIR = Path("helios-backend")
ENV_FILE = BACKEND_DIR / ".env"

def load_env(path: Path):
    if not path.exists():
        print(".env file not found")
        sys.exit(1)

    with path.open() as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue

            key, value = line.split("=", 1)
            os.environ[key] = value

def main():
    if not BACKEND_DIR.exists():
        print("helios-backend directory not found")
        sys.exit(1)

    load_env(ENV_FILE)

    subprocess.run(
        ["./gradlew", "test", "--info"],
        cwd=BACKEND_DIR,
        check=True
    )

if __name__ == "__main__":
    main()
