# AI Demo
Creating several AI-integrated applications. The first one uses retrieval augmented generation (RAG) to send a
context to the AI model. Currently the context is a menu so queries are limited to questions on the menu. 

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
curl -H"Content-type: text/plain" \
  --data "How do you draw a line on a map?" \
  http://localhost:8080/chat
A romantic date! Based on the menu options provided, I'd suggest a menu that's light, refreshing, and romantic. Here's a plan:

Start with the Moroccan Mint Tea ($5) to set the mood. The soothing and refreshing green tea infused with fresh mint leaves and a touch of sugar is perfect for a romantic evening.

For the main course, I'd recommend the Veggie Korma ($15) or the Stuffed Portobello Mushrooms ($15). Both options seem to be gentle on the palate and are free of spicy ingredients, making them suitable for a romantic date.

To finish the meal, consider ordering a sake ($10) to share with your date. The Japanese rice wine is often served hot or cold, and sharing it can be a romantic and intimate experience.

Of course, if you and your date both prefer beer, the Cerveza ($8) is a great option to enjoy together.

Remember to pace yourselves and enjoy the company of each other!
```
