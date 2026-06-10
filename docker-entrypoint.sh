#!/bin/bash
set -e

LIBS=/app/libs
CP="$LIBS/*"

echo "Starting Java chess server..."
java -cp "$CP" server.ServerMain &
JAVA_SERVER_PID=$!

sleep 2

echo "Starting Node bridge..."
CLIENT_JAR_CP="$CP" \
CHESS_SERVER_URL=http://localhost:8080 \
PORT=3000 \
node /app/server.js &
NODE_PID=$!

# Wait for either process to exit, then shut everything down
wait $JAVA_SERVER_PID $NODE_PID