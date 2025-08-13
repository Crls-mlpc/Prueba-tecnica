# Prueba-tecnica de Carlos Malpica para CHAKRAY

## Tech stack
- Java 17
- Maven
- Javalin
- Gson
- SQLite
- AES-256 for password encryption

## Features
- 3 sample users preloaded from Database.java
- List users with optional sorting (sortedBy) and filtering (filter)
- Create new users with:
  - Unique tax_id in RFC format
  - Valid phone number (10 digits, optional country code, "AndresFormat")
  - Auto created_at in Madagascar timezone
  - AES-256 encrypted password

## Requirements
- Java 17 (or 11)
- Maven 3.6+

## Run
```bash
git clone https://github.com/<Crls-mlpc>/<Prueba-tecnica>.git
cd <Prueba-tecnica>
mvn clean package -DskipTests
java -jar target/app.jar
````
The server will start at:  
http://localhost:8081

## Available endpoints
- **GET /users** → List all users
- **POST /users** → Create a new user
## User format
```json
{
  "email": "user1@mail.com",
  "name": "user1",
  "phone": "+1 55 555 555 55",
  "password": "secret123",
  "tax_id": "AARR990101XXX",
  "addresses": [
    {"id":1,"name":"workaddress","street":"street No. 1","country_code":"UK"},
    {"id":2,"name":"homeaddress","street":"street No. 2","country_code":"AU"}
  ]
}
```
