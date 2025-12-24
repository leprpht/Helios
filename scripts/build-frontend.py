#!/usr/bin/env python3

import subprocess
import sys
from pathlib import Path

FRONTEND_DIR = Path("helios-frontend")

def main():
    if not FRONTEND_DIR.exists():
        print("helios-frontend directory not found")
        sys.exit(1)

    subprocess.run(
        ["npm", "run", "build"],
        cwd=FRONTEND_DIR,
        check=True
    )

if __name__ == "__main__":
    main()
