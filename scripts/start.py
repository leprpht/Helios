#!/usr/bin/env python3

import subprocess
import sys
import threading
import signal

RESET = "\033[0m"
YELLOW = "\033[33m"
CYAN = "\033[36m"

def stream_output(name, color, stream):
    for line in iter(stream.readline, ""):
        print(f"{color}[HELIOS {name}]{RESET} {line}", end="")

def start_process(name, color, cmd):
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
    backend = start_process(
        "BACKEND",
        YELLOW,
        ["./scripts/start-backend.sh"]
    )

    frontend = start_process(
        "FRONTEND",
        CYAN,
        ["./scripts/start-frontend.sh"]
    )

    def shutdown(signum, frame):
        backend.terminate()
        frontend.terminate()
        sys.exit(1)

    signal.signal(signal.SIGINT, shutdown)
    signal.signal(signal.SIGTERM, shutdown)

    exit_codes = [
        backend.wait(),
        frontend.wait()
    ]

    if any(code != 0 for code in exit_codes):
        sys.exit(1)

if __name__ == "__main__":
    main()
