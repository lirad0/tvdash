# TV Dashboard Backend

This module provides a Spring Boot backend for the Angular tableau UI.

## What is included
- A Spring Boot 3 application with MongoDB support
- CRUD endpoints for tableau cards (name, image URL, and target URL)
- CRUD endpoints for URL-only items for special weather/widget links
- Seed data based on the items that appear in the Angular tableau view
- Docker support with a MongoDB container and an Amazon Corretto-based backend image

## Run locally with Docker Compose

```bash
docker compose up --build
```

The backend will be available at http://localhost:8080 and MongoDB will be available at localhost:27017.

## API examples

### Health
```bash
curl http://localhost:8001/api/health
```

### List cards
```bash
curl http://localhost:8001/api/tableau/cards
```

### Create a new card
```bash
curl -X POST http://localhost:8001/api/tableau/cards \
  -H "Content-Type: application/json" \
  -d '{"name":"My Item","imageUrl":"img/my-item.png","url":"https://example.com"}'
```

### Create a URL-only item
```bash
curl -X POST http://localhost:8001/api/tableau/url-only-items \
  -H "Content-Type: application/json" \
  -d '{"url":"https://example.com/widget"}'
```
