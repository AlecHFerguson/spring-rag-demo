# AI Demo
Creating several AI-integrated applications. Work in progress at the moment.

## PostgreSQL Setup
```shell
podman run -d \
  --name pgvector-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=vector_db \
  -p 5432:5432 \
  docker.io/pgvector/pgvector:pg17
```

```shell
curl -H "Content-type: text/plain" \
  --data "What is the best vegan pizza in San Francisco?" \
  http://localhost:8080/chat

```
