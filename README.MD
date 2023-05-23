# Microsserviços com JAVA e NODE (rabbitMQ)

Docker POSTGRESQL

docker run --name auth-db -p 5432:5432 -e POSTGRES_DB=auth-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=root -d postgres:11

docker run --name product-db -p 5433:5432 -e POSTGRES_DB=product-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=root -d postgres:11
