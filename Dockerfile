# ─── Stage 1: Build the Java project ────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS java-build

WORKDIR /build
COPY . .
# Build everything in one pass so the local `shared` module is in the reactor
# and gets installed before server/client try to resolve it
RUN mvn -q package dependency:copy-dependencies -DskipTests \
      -DoutputDirectory=target/dependency

# ─── Stage 2: Runtime image ─────────────────────────────────────────────────
FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy jars and all dependencies into a flat /app/libs folder
COPY --from=java-build /build/server/target/server.jar         ./libs/server.jar
COPY --from=java-build /build/client/target/client.jar         ./libs/client.jar
COPY --from=java-build /build/shared/target/shared.jar         ./libs/shared.jar
COPY --from=java-build /build/server/target/dependency/        ./libs/
COPY --from=java-build /build/client/target/dependency/        ./libs/

# Copy the Node bridge
COPY bridge/package.json ./
RUN npm install --omit=dev
COPY bridge/server.js ./

# Copy the web terminal UI
COPY web/ ./web/

EXPOSE 3000

COPY docker-entrypoint.sh ./
RUN chmod +x docker-entrypoint.sh
CMD ["./docker-entrypoint.sh"]