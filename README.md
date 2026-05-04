# AgencyBoard

AgencyBoard is a SaaS platform built for digital marketing agencies. It centralizes client management, campaign tracking, and AI-generated executive reports in a single REST API — eliminating the manual work of consolidating data and writing reports for each client.

The platform is multi-tenant by design: each authenticated user only sees their own clients and data.

---

## What it does

**Client management** — Create and manage the agency's clients, storing key information like company, industry, and contact details.

**Campaign tracking** — Each client can have multiple campaigns across different channels (Google Ads, Meta, etc.), with budget, spend, and status tracking.

**AI-powered reports** — Generate professional executive reports for any client with a single API call. The system feeds real campaign data into OpenAI's GPT-4o-mini and returns a structured 3-paragraph summary covering performance, insights, and recommendations. Reports that used to take an hour to write are ready in seconds.

**Dashboard stats** — A single endpoint that aggregates all key metrics for the authenticated user: total clients, campaigns, active campaigns, total budget, total spend, and reports generated.

---

## Backend

The backend is a REST API built with **Spring Boot 3** and **Java 21**.

**Tech stack:**
- Spring Boot 3.5 + Java 21
- Spring Security with stateless JWT authentication
- Spring Data JPA + Hibernate (auto-migrations)
- PostgreSQL (hosted on Railway)
- OpenAI API (gpt-4o-mini) via REST

**Module structure:**

```
src/main/java/com/agencyboard/agencyboard/
├── auth/        # Register and login endpoints, JWT token generation
├── user/        # User entity, roles (ADMIN / CLIENT)
├── client/      # Client CRUD, scoped to authenticated user
├── campaign/    # Campaign management per client, status updates
├── report/      # AI report generation and storage
├── dashboard/   # Aggregated stats for the current user
└── security/    # JWT filter, security config, password encoder
```

**API endpoints:**

```
POST   /api/auth/register
POST   /api/auth/login

POST   /api/clients
GET    /api/clients
GET    /api/clients/{id}

POST   /api/clients/{clientId}/campaigns
GET    /api/clients/{clientId}/campaigns
PATCH  /api/campaigns/{id}/status

POST   /api/clients/{clientId}/reports/generate
GET    /api/clients/{clientId}/reports

GET    /api/dashboard
```

---

## Running with Docker

### Prerequisites

- Docker installed on your machine
- A PostgreSQL database (Railway recommended — [railway.app](https://railway.app))
- An OpenAI API key ([platform.openai.com](https://platform.openai.com))

### 1. Clone the repository

```bash
git clone https://github.com/your-username/agencyboard.git
cd agencyboard/backend/agencyboard
```

### 2. Build the Docker image

```bash
docker build -t agencyboard-backend .
```

### 3. Run the container

```bash
docker run -p 8080:8080 \
  -e DB_HOST=your-railway-host.proxy.rlwy.net \
  -e DB_PORT=your-railway-port \
  -e DB_NAME=railway \
  -e DB_USER=postgres \
  -e DB_PASSWORD=your-database-password \
  -e OPENAI_API_KEY=sk-your-openai-api-key \
  agencyboard-backend
```

The API will be available at `http://localhost:8080`.

> For the Railway database, use the **public URL** found under Settings → Networking in the Railway dashboard. The public port is usually not 5432.

### Environment variables reference

| Variable | Description |
|----------|-------------|
| `DB_HOST` | PostgreSQL public host |
| `DB_PORT` | PostgreSQL public port |
| `DB_NAME` | Database name (default: `railway`) |
| `DB_USER` | Database user (default: `postgres`) |
| `DB_PASSWORD` | Database password |
| `OPENAI_API_KEY` | OpenAI API key for report generation |

---

## Running locally (without Docker)

If you prefer to run it directly with IntelliJ or Gradle, create a `.env` file in `backend/agencyboard/`:

```env
DB_HOST=your-railway-host.proxy.rlwy.net
DB_PORT=your-railway-port
DB_NAME=railway
DB_USER=postgres
DB_PASSWORD=your-database-password
OPENAI_API_KEY=sk-your-openai-api-key
```

Then run `AgencyboardApplication.java` from IntelliJ, or via terminal:

```bash
./gradlew bootRun
```

---

## Frontend

The frontend is currently under development using **Next.js 15** (App Router), **Tailwind CSS**, and **ShadCN UI**.

---

## License

MIT