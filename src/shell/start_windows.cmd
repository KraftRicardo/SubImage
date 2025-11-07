@echo off
@REM java -jar SubImage-0.0.0.1.jar --module-path "C:\Users\kraft\.jdks\javafx-sdk-22.0.1\lib" --add-modules=javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
java\openjdk-20.0.2\bin\java -jar SubImage-0.0.0.1.jar --module-path "java\javafx-sdk-22.0.1\lib" --add-modules=javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
pause