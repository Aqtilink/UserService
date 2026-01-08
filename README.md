# User Service

A microservice that manages user profiles, authentication, and social connections for the Aqtilink platform. Built with Spring Boot 3.4, it handles user registration, profile management, and friend relationship management.

## Overview

The User Service is responsible for:
- User profile management
- User authentication and authorization via OAuth2/JWT
- Friend relationship management
- Friend request handling (send, accept, reject)
- User search and discovery
- Integration with Clerk for identity management

## Architecture

### Technology Stack

- **Framework**: Spring Boot 3.4.12
- **Java Version**: 17
- **Database**: PostgreSQL
- **Security**: OAuth2 with JWT (Clerk authentication)
- **ORM**: Hibernate/JPA
- **HTTP Client**: OkHttp 4.12.0

### Project Structure

```
user-service/
├── src/main/java/com/aqtilink/user_service/
│   ├── UserServiceApplication.java        # Main application class
│   ├── config/                            # Configuration classes
│   ├── controller/                        # REST API endpoints
│   ├── dto/                               # Data Transfer Objects
│   ├── messaging/                         # Message publishing
│   ├── model/                             # JPA entities
│   ├── repository/                        # Data access layer
│   ├── security/                          # Security configuration
│   └── service/                           # Business logic
└── src/main/resources/
    └── application.yml                    # Application configuration
```

## API Endpoints

### User Management

#### Get User
```
GET /api/v1/users/{clerkId}

Response: User
Status Code: 200 OK
```

#### Update User
```
PUT /api/v1/users/{clerkId}
Content-Type: application/json

Request Body:
{
  "firstName": "Jane",
  "age": 29,
  "city": "Maribor"
}

Response: User
Status Code: 200 OK
```

#### Delete User
```
DELETE /api/v1/users/{clerkId}

Status Code: 204 No Content
```

#### Search Users
```
GET /api/v1/users/search?q={query}

Response: List<FriendDTO>
Status Code: 200 OK
```

### Friend Management

#### Get User's Friends
```
GET /api/v1/users/{clerkId}/friends

Response: List<FriendDTO>
Status Code: 200 OK
```

#### Get Current User's Friends
```
GET /api/v1/users/me/friends

Response: List<FriendDTO>
Status Code: 200 OK
```

#### Get Users Batch
```
POST /api/v1/users/batch
Content-Type: application/json

Request Body: ["clerk-id-1", "clerk-id-2", ...]

Response: List<UserSummaryDTO>
Status Code: 200 OK
```

### Friend Requests

#### Send Friend Request
```
POST /api/v1/friend-requests/send?receiverClerkId={clerkId}

Response: FriendRequest
Status Code: 201 Created
```

#### Get Pending Requests
```
GET /api/v1/friend-requests/pending

Response: List<FriendRequestDTO>
Status Code: 200 OK
```

#### Accept Friend Request
```
POST /api/v1/friend-requests/{requestId}/accept

Status Code: 204 No Content
```

#### Reject Friend Request
```
POST /api/v1/friend-requests/{requestId}/reject

Status Code: 204 No Content
```

#### Delete Friend Request
```
DELETE /api/v1/friend-requests/{requestId}

Status Code: 204 No Content
```

## Data Models

### User Entity

```java
{
  "id": "UUID",
  "clerkId": "string (unique)",
  "firstName": "string",
  "lastName": "string",
  "email": "string (unique)",
  "age": "integer",
  "city": "string",
  "friends": [
    {
      "id": "UUID",
      "clerkId": "string",
      "firstName": "string",
      "lastName": "string",
      "email": "string"
    }
  ]
}
```

### FriendDTO

```java
{
  "id": "string",
  "name": "string",
  "email": "string"
}
```

### UserSummaryDTO

```java
{
  "id": "string",
  "name": "string",
  "email": "string"
}
```

### FriendRequest Entity

```java
{
  "id": "UUID",
  "senderId": "UUID",
  "receiverId": "UUID",
  "status": "PENDING | ACCEPTED | REJECTED",
  "createdAt": "LocalDateTime"
}
```

### FriendRequestDTO

```java
{
  "id": "UUID",
  "senderName": "string",
  "senderEmail": "string",
  "status": "PENDING | ACCEPTED | REJECTED"
}
```

## Key Features

### User Profiles
- Complete user information storage (name, email, age, city)
- Clerk integration for external authentication
- User search and discovery
- Profile update capabilities

### Friend System
- Many-to-many friend relationships
- Friend request management with status tracking
- Pending request retrieval
- Send, accept, reject, and delete friend requests

### Security
- OAuth2 resource server with JWT validation
- Clerk identity provider integration
- Role-based access control for profile updates
- Automatic user authentication via Clerk

### Clerk Integration
- User data syncing with Clerk
- JWT token validation
- User profile management
- Secret key-based API communication

## Configuration

### Environment Variables

```yaml
# Database Configuration
SPRING_DATASOURCE_URL: jdbc:postgresql://host:5432/user_service_db
SPRING_DATASOURCE_USERNAME: postgres
SPRING_DATASOURCE_PASSWORD: postgres

# Server Port
SERVER_PORT: 8080

# JWT/OAuth2
JWK_SET_URI: https://clerk.aqtilink.live/.well-known/jwks.json

# Clerk Configuration
CLERK_SECRET_KEY: <clerk-secret-key>
CLERK_API_URL: https://api.clerk.com/v1

# Service Communication
ACTIVITY_SERVICE_URL: http://localhost:8081
SERVICE_API_KEY: <api-key>

# Actuator
ACTUATOR_ENDPOINTS: health,info

# Database Migration
JPA_DDL_AUTO: update
JPA_SHOW_SQL: false

# RabbitMQ
SPRING_RABBITMQ_HOST: localhost
SPRING_RABBITMQ_PORT: 5672
SPRING_RABBITMQ_USERNAME: guest
SPRING_RABBITMQ_PASSWORD: guest
```

### Application Properties

- **Port**: 8080
- **Database Migration**: Automatic (Hibernate DDL-auto: update)
- **Actuator Endpoints**: /health, /info
- **JWT Validation**: Validates tokens from Clerk OAuth2 provider

## Dependencies

### Core Framework
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-data-jpa` - Database ORM
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-oauth2-resource-server` - JWT validation

### Data & Validation
- `postgresql` - Database driver

### Communication
- `okhttp` 4.12.0 - HTTP client for Clerk API calls

### Development
- `spring-boot-devtools` - Live reload support
- `spring-boot-starter-test` - Testing framework
- `spring-boot-starter-actuator` - Monitoring and metrics

## Security

### Authentication
- OAuth2 resource server configuration
- JWT token validation against Clerk's JWKS endpoint
- Bearer token required for protected endpoints

### Authorization
- Users can only update their own profiles
- Friend request operations require authentication
- Service-to-service communication via API keys

## Running the Service

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Docker and Docker Compose (for containerized deployment)

### Local Development

1. **Install dependencies**:
   ```bash
   ./mvnw clean install
   ```

2. **Configure environment**:
   ```bash
   export JWK_SET_URI=https://clerk.aqtilink.live/.well-known/jwks.json
   export CLERK_SECRET_KEY=<your-clerk-secret>
   ```

3. **Run the service**:
   ```bash
   ./mvnw spring-boot:run
   ```

   Service will start at `http://localhost:8080`

### Docker Deployment

```bash
docker build -t user-service:latest .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/user_service_db \
  -e JWK_SET_URI=https://clerk.aqtilink.live/.well-known/jwks.json \
  -e CLERK_SECRET_KEY=<secret> \
  user-service:latest
```

## Database Schema

### Tables

**users**
- id (UUID, Primary Key)
- clerkId (String, Unique, Not Null)
- firstName (String, Not Null)
- lastName (String, Not Null)
- email (String, Unique, Not Null)
- age (Integer, Not Null)
- city (String, Not Null)

**user_friends** (Many-to-Many)
- user_id (UUID, Foreign Key)
- friend_id (UUID, Foreign Key)

**friend_requests**
- id (UUID, Primary Key)
- senderId (UUID, Foreign Key)
- receiverId (UUID, Foreign Key)
- status (String, Default: 'PENDING')
- createdAt (Timestamp)

## Integration Points

### Activity Service
- **Endpoint**: ACTIVITY_SERVICE_URL
- **Purpose**: Share user information with Activity Service
- **Communication**: REST API

### Clerk Identity Provider
- **Endpoint**: CLERK_API_URL
- **Purpose**: User authentication and profile sync
- **Authentication**: Secret key-based

## Error Handling

### Standard HTTP Status Codes
- `201 Created` - User/Request successfully created
- `200 OK` - Successful GET, PUT, POST operations
- `204 No Content` - Successful DELETE operation
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing/invalid JWT token
- `403 Forbidden` - Authorization failure (e.g., trying to update another user's profile)
- `404 Not Found` - User or request not found
- `500 Internal Server Error` - Server-side error

## Development Guidelines

### Adding New Endpoints

1. Create controller method in `UserController` or `FriendRequestController`
2. Add business logic to `UserService` or `FriendRequestService`
3. Create/update DTOs in `dto/` package
4. Update repository if new queries needed

### Database Changes

1. Modify JPA entities in `model/`
2. Let Hibernate auto-generate schema changes (DDL-auto: update)

### Testing

Run tests with:
```bash
./mvnw test
```

## References

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/3.4.x/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [Clerk Documentation](https://clerk.com/docs)
