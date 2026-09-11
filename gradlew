#!/bin/sh
# Codemagic fallback Gradle launcher.
# The hosted Android build image provides the Gradle CLI.
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
echo "ERROR: Gradle CLI was not found on the build machine." >&2
exit 1
