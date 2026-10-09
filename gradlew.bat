@echo off
REM Minimal Gradle bootstrap for AtelierTrack (clean-room, MIT).
REM Downloads Gradle 8.7 once and forwards all args. Requires JDK 17+.
setlocal
set GRADLE_VERSION=8.7
set DIST_DIR=%USERPROFILE%\.gradle-bootstrap\gradle-%GRADLE_VERSION%
if not exist "%DIST_DIR%\bin\gradle.bat" (
  echo Downloading Gradle %GRADLE_VERSION%...
  powershell -NoProfile -Command "Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile \"$env:TEMP\gradle-dl.zip\""
  powershell -NoProfile -Command "Expand-Archive -Force \"$env:TEMP\gradle-dl.zip\" \"$env:USERPROFILE\.gradle-bootstrap\""
)
"%DIST_DIR%\bin\gradle.bat" %*
