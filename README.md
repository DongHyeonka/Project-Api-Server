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

## Dev CI/CD

이 저장소는 [`.github/workflows/dev-ci-cd.yml`](/home/donghyeon/dev/Project-Api-Server/.github/workflows/dev-ci-cd.yml) 기준으로 dev CI/CD를 구성합니다.

- Pull Request to `develop`
  - `./gradlew test`
- Push to `develop`
  - `./gradlew test`
  - `ghcr.io/<owner>/project-api-server:dev`
  - `ghcr.io/<owner>/project-api-server:<short-sha>`
    두 태그로 이미지 빌드/푸시
  - `k8s/dev/kustomization.yaml`의 `newTag`를 `<short-sha>`로 갱신
  - 같은 `develop` 브랜치에 manifest 변경 반영
  - Argo CD가 이를 감지해 sync

현재 dev 배포 선언은 아래 파일로 관리합니다.

- Kustomize: [k8s/dev/kustomization.yaml](/home/donghyeon/dev/Project-Api-Server/k8s/dev/kustomization.yaml)
- Argo CD AppProject: [argocd/api-dev-project.yaml](/home/donghyeon/dev/Project-Api-Server/argocd/api-dev-project.yaml)
- Argo CD Application: [argocd/dev-api-server-application.yaml](/home/donghyeon/dev/Project-Api-Server/argocd/dev-api-server-application.yaml)

현재 dev 구성은 민감값 없이 `ConfigMap`만으로 실행되도록 잡았습니다. 따라서 auth-server와 달리 별도 app secret은 아직 필요하지 않습니다.

dev namespace에서 먼저 필요한 secret은 GHCR pull secret입니다.

```bash
kubectl create secret docker-registry ghcr-regcred \
  --namespace api-dev \
  --docker-server=ghcr.io \
  --docker-username=<github-username> \
  --docker-password=<github-pat-or-ghcr-token>
```

Argo CD는 클러스터에 별도 설치해야 합니다. 이 저장소는 Argo CD가 읽을 `AppProject`와 `Application` 선언을 함께 관리합니다.
적용 순서는 보통 `AppProject -> Application` 순서로 가져갑니다.

현재 dev namespace는 Pod Security Admission 기준으로 아래 라벨을 사용합니다.

- `enforce=baseline`
- `warn=restricted`
- `audit=restricted`

Deployment는 이에 맞춰 `runAsNonRoot`, `seccompProfile: RuntimeDefault`, `allowPrivilegeEscalation: false`, `capabilities.drop: [ALL]`, `startupProbe`, `livenessProbe`, `readinessProbe`를 포함합니다.
