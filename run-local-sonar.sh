#!/bin/bash

# Set the location where the sonar report will be stored
OUTPUT_DIR="app/build/sonar-report"
mkdir -p "$OUTPUT_DIR"

# First run JaCoCo test report
echo "Generating JaCoCo test coverage report..."
./gradlew jacocoTestReport

# Check if sonar-scanner is available
if ! command -v sonar-scanner &> /dev/null; then
    echo "Error: sonar-scanner command not found."
    echo "Please install SonarScanner CLI first:"
    echo "  1. Download from https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/"
    echo "  2. Extract and add bin directory to your PATH"
    exit 1
fi

# Set up a basic local server first (if you have Docker)
echo "Setting up a temporary local SonarQube server..."
if command -v docker &> /dev/null; then
    if ! docker ps | grep -q sonarqube; then
        docker run -d --name sonarqube -p 9000:9000 sonarqube:community
        echo "Waiting for SonarQube to start..."
        sleep 60  # Give SonarQube time to start
    fi
    SERVER_URL="http://localhost:9000"
else
    echo "Docker not found. You'll need a running SonarQube server."
    SERVER_URL="http://localhost:9000"
fi

# Run sonar-scanner
echo "Running SonarQube analysis..."
cd app
sonar-scanner -Dsonar.projectBaseDir=. \
              -Dsonar.working.directory="$OUTPUT_DIR" \
              -Dproject.settings=../sonar-project.properties \
              -Dsonar.host.url="$SERVER_URL" \
              -Dsonar.scm.disabled=true
cd ..

echo "Analysis completed. Report stored in $OUTPUT_DIR"