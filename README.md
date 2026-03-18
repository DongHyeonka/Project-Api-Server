# Project-Api-Server
Health Check를 할 수 있는 단순한 API를 관리하는 서버입니다.

## 이미지 빌드

루트의 [Dockerfile](/home/donghyeon/dev/Project-Api-Server/Dockerfile)로 `api-server` 이미지를 빌드할 수 있습니다.

현재 Dockerfile은 다음 기준으로 구성했습니다.

1. Gradle `bootJar` 멀티스테이지 빌드
2. Spring Boot layered jar 추출
3. non-root 사용자 실행
4. `/actuator/health` 기반 Docker healthcheck

로컬 빌드:

```bash
docker build -t project-api-server:local .
```

로컬 실행:

```bash
docker run --rm -p 8082:8082 \
  -e APP_SERVER_PORT=8082 \
  -e APP_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://host.docker.internal:8080 \
  project-api-server:local
```

환경별 차이는 이미지를 나누지 않고 런타임 환경 변수로 분리하는 것을 기준으로 합니다.

Actuator health 경로:

- `/actuator/health`
- `/actuator/health/liveness`
- `/actuator/health/readiness`
- `/livez`
- `/readyz`
