docker-compose.yml:
```
version: '3.0'

services:
  news:
    platform: linux/amd64
    image: openjdk:19-alpine
    volumes:
      - ./file:/app
    working_dir: /app
    command: [ "java", "-jar", "coinNews-1.0-SNAPSHOT-all.jar" ]
```
```
tree:
  root:
    docker-compose.yml
    file:
      coinNews-1.0-SNAPSHOT-all.jar
      .env
```
env struct:

```
WEB_HOOK_ID=???
```
