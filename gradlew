#!/bin/sh
# Minimal Gradle bootstrap for AtelierTrack (clean-room, MIT).
# Downloads Gradle 8.7 once and forwards all args. Requires JDK 17+.
set -e
GRADLE_VERSION="8.7"
DIST_DIR="$HOME/.gradle-bootstrap/gradle-$GRADLE_VERSION"
if [ ! -x "$DIST_DIR/bin/gradle" ]; then
  echo "Downloading Gradle $GRADLE_VERSION..."
  mkdir -p "$HOME/.gradle-bootstrap"
  curl -sL "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o /tmp/gradle-dl.zip
  unzip -q -o /tmp/gradle-dl.zip -d "$HOME/.gradle-bootstrap"
fi
exec "$DIST_DIR/bin/gradle" "$@"
