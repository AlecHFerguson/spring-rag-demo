# Retrieval Augmented Generation Demo
Demo app showing Retrieval Augmented Generation (RAG) within Spring AI. RAG is a common AI
technique where specific information (context) is passed to the AI model which is used to
answer the user's question. You may pass your company's product information or help documentation
to the model, for example; then the response is based on correct information and less
prone to hallucination.

### Context
This app allows you to load a set of markdown files to be used as context in AI-based responses.
Files are loaded into a vector database; in this case `pgvector` from PostgreSQL. When a chat
is sent, Spring AI finds the documentation which most closely matches the caller's question and
passes it to the model. The model will tend to 

## AI Backend
This service is setup to run the AI model locally via [Ollama](https://ollama.com). For easy
startup, install Ollama locally and run the `llama3` model.

```shell
ollama run llama3
```

Ollama is configured as the backend in `application.properties`. Other backends have not been
tested; however, Spring AI has support for most models. See [Chat Model API](https://docs.spring.io/spring-ai/reference/api/chatmodel.html)
for details.

## PostgreSQL Setup
This app uses PostGreSQL, including PGVector. Use either `podman` or `docker` to create a local
instance. The username and password of this test instance are both `postgres`. In production use
better username and password.

```shell
podman run -d \
  --name pgvector-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=vector_db \
  -p 5432:5432 \
  docker.io/pgvector/pgvector:pg17
```

### App Properties
In order to run the app you must set username and password. Also, set markdown location if you need to load
markdown. Set these in `application.properties`.

```properties
spring.datasource.username=
spring.datasource.password=
ai.smartalec.markdown-directory=
```

## Context Setup
In order to have context to send to the model you need to load Markdown files. Save files with extention `.md`
in any directory on your computer; then pass the full path to the application via `application.properties`.

```properties
ai.smartalec.markdown-directory=/Users/my-user/path/to/files
```

Run the app, then make a cURL request to this endpoint to initialize load. 
```shell
curl -X POST \
  'http://localhost:8080/admin/load-markdown'
```

The loader runs in the background; you will receive a SUCCESS message whether or not it worked. Check the logs
for results. Prior to loading the loader deletes all existing records; this is to ensure consistency in case
the files have changed since last load. This way minor file changes do not result in duplicate records.

## Making a Request
```shell
curl -H 'Content-type: application/json' \
  --data '{"message":"How do you report an inaccuracy on the map?"}' \
  http://localhost:8080/chat

{
  "message": "CalTopo does not go out into the field to measure and report inaccuracy on maps. Instead, they use a variety of well-established data sets ...",
  "conversationId": "e998f1b5-6f15-4847-899d-c5b5c9263a75"
}
```

In order to maintain context for further messages, pass `conversationId` on the next request.
```shell
curl -H 'Content-type: application/json'   --data '{"message":"What might be the source for, say, a lake shown on a steep slope?","conversationId":"e998f1b5-6f15-4847-899d-c5b5c9263a75"}'   http://localhost:8080/chat

{
  "message":"In this case, since the data is sourced from the USGS (United States Geological Survey), you could report the error to them.",
  "conversationId":"e998f1b5-6f15-4847-899d-c5b5c9263a75"
}
```