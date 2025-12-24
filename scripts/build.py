#!/usr/bin/env python3

import subprocess
import sys
import threading

RESET = "\033[0m"
YELLOW = "\033[33m"
CYAN = "\033[36m"

def stream_output(prefix, color, stream):
    for line in iter(stream.readline, ""):
        print(f"{color}[HELIOS {prefix}]{RESET} {line}", end="")

def run_process(name, color, cmd):
    proc = subprocess.Popen(
        cmd,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True
    )

    threading.Thread(
        target=stream_output,
        args=(name, color, proc.stdout),
        daemon=True
    ).start()

    return proc

def main():
    backend = run_process(
        "BACKEND",
        YELLOW,
        ["./scripts/build-backend.sh"]
    )

    frontend = run_process(
        "FRONTEND",
        CYAN,
        ["./scripts/build-frontend.sh"]
    )

    exit_codes = []

    try:
        exit_codes.append(backend.wait())
        exit_codes.append(frontend.wait())
    except KeyboardInterrupt:
        backend.terminate()
        frontend.terminate()
        sys.exit(1)

    if any(code != 0 for code in exit_codes):
        sys.exit(1)

if __name__ == "__main__":
    main()
