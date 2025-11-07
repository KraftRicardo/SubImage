#!/bin/bash

# Assuming the JavaFX SDK is located in /usr/lib/jvm/javafx-sdk-22.0.1/
# and OpenJDK is installed at /usr/lib/jvm/openjdk-20.0.2/
# Update these paths according to your installation directories

JAVA_HOME="/usr/lib/jvm/openjdk-20.0.2"
JAVAFX_PATH="/usr/lib/jvm/javafx-sdk-22.0.1/lib"

# Running the Java application
"$JAVA_HOME/bin/java" -jar SubImage-0.0.0.1.jar --module-path "$JAVAFX_PATH" --add-modules=javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
