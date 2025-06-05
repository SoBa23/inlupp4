#!/usr/bin/env bash
# Simple script to download and set up a local Maven distribution.
# This is useful in environments where Maven is not installed system wide.
# The script downloads Maven into the 'tools/maven' directory and updates
# the PATH so that the 'mvn' command can be used by the makefile.

set -e

MAVEN_VERSION="3.9.5"
MAVEN_DIR="tools/maven"

# Allow the user to override the Maven version via environment variable
if [ -n "$MAVEN_VERSION_OVERRIDE" ]; then
  MAVEN_VERSION="$MAVEN_VERSION_OVERRIDE"
fi

if [ ! -d "$MAVEN_DIR" ]; then
  mkdir -p "$MAVEN_DIR"
  ARCHIVE="apache-maven-${MAVEN_VERSION}-bin.tar.gz"
  URL="https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/${ARCHIVE}"
  echo "Downloading Maven ${MAVEN_VERSION}..."
  curl -L "$URL" -o "$ARCHIVE"
  tar -xzf "$ARCHIVE" --strip-components=1 -C "$MAVEN_DIR"
  rm "$ARCHIVE"
fi

export PATH="$(pwd)/$MAVEN_DIR/bin:$PATH"

cat <<EOM
Maven has been installed locally in '$MAVEN_DIR'.
Add the following line to your shell before running Maven commands:

    export PATH="$(pwd)/$MAVEN_DIR/bin:$PATH"

EOM
