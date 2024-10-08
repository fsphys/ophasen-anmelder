ARG JAVA_VERSION=21

FROM eclipse-temurin:${JAVA_VERSION}-jdk-jammy AS builder

RUN set -o errexit -o nounset \
 && apt-get update \
 && apt-get install git -y

COPY . /src
WORKDIR /src

RUN chmod +x ./gradlew \
    && ./gradlew :bootJar --stacktrace --no-daemon

FROM eclipse-temurin:${JAVA_VERSION}-jre-jammy AS extractor

WORKDIR /src

COPY --from=builder build/libs/${SERVICE_NAME}*.jar ophasen-anmelder.jar
RUN java -Djarmode=layertools -jar ophasen-anmelder.jar extract

FROM eclipse-temurin:${JAVA_VERSION}-jre-jammy

ENV HTTP_PORT=8080
ENV MANAGEMENT_PORT=8081

ARG USER_USER=ophasen-anmelder
ARG USER_GROUP=ophasen-anmelder
ARG DATA_DIR=/data

RUN set -o errexit -o nounset \
 && groupadd --system --gid 1000 ${USER_GROUP} \
 && useradd --system --gid ${USER_GROUP} --uid 1000 --shell /bin/bash --create-home ${USER_USER} \
 && mkdir -p ${DATA_DIR} \
 && chown --recursive ${USER_USER}:${USER_GROUP} ${DATA_DIR} \
 && chown --recursive ${USER_USER}:${USER_GROUP} /home/${USER_USER} \
 \
 && apt-get update \
 && apt-get install -y --no-install-recommends curl jq \
 && rm -rf /var/lib/apt/lists/*

WORKDIR ${DATA_DIR}

USER ${USER_USER}:${USER_GROUP}
VOLUME ${DATA_DIR}

EXPOSE ${HTTP_PORT}
EXPOSE ${MANAGEMENT_PORT}

COPY --from=extractor /src/dependencies/ /opt/ophasen-anmelder
COPY --from=extractor /src/spring-boot-loader/ /opt/ophasen-anmelder
COPY --from=extractor /src/snapshot-dependencies/ /opt/ophasen-anmelder
COPY --from=extractor /src/application/ /opt/ophasen-anmelder

ENTRYPOINT ["java", "-cp", "/opt/ophasen-anmelder", "org.springframework.boot.loader.launch.JarLauncher"]

HEALTHCHECK --start-period=10s --interval=30s --timeout=3s --retries=3 \
            CMD curl --silent --fail --request GET "http://localhost:${MANAGEMENT_PORT}/actuator/health" \
                | jq --exit-status '.status == "UP"' || exit 1
