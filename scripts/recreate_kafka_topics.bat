@echo off
SETLOCAL EnableDelayedExpansion

REM Set Kafka paths - adjust these if your Kafka installation is in a different location
SET "KAFKA_HOME=C:\kafka"
SET "KAFKA_BIN=%KAFKA_HOME%\bin\windows"
SET "BOOTSTRAP_SERVER=localhost:9092"
SET "TOPICS_TO_RECREATE=order_created inventory_reserved payment_success notification_sent"

echo --- Starting Full Kafka Topic Reset and Re-creation Process ---
echo.

REM Check if a topic exists and delete it if it does
FOR %%T IN (%TOPICS_TO_RECREATE%) DO (
    CALL "%KAFKA_BIN%\kafka-topics.bat" --bootstrap-server %BOOTSTRAP_SERVER% --delete --topic "%%T"
)

REM Create topics
FOR %%T IN (%TOPICS_TO_RECREATE%) DO (
    CALL "%KAFKA_BIN%\kafka-topics.bat" --bootstrap-server %BOOTSTRAP_SERVER% --create --topic "%%T" --partitions 1 --replication-factor 1
)

echo --- Kafka Topic Reset and Re-creation Process Completed ---
ENDLOCAL
pause