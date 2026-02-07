# Inventory Health Monitor

Hackathon-ready, production-inspired restaurant intelligence platform for predictive inventory health.

## Architecture
- **Backend:** Spring Boot (Java 17), JPA, PostgreSQL
- **Frontend:** React + Vite
- **Style:** Modular monolith with a dedicated forecasting package

## Project Structure
```
backend/   Spring Boot API
frontend/  React dashboard
```

## Running the backend
1. Create a PostgreSQL database and user:
   ```sql
   CREATE DATABASE inventory;
   CREATE USER inventory WITH PASSWORD 'inventory';
   GRANT ALL PRIVILEGES ON DATABASE inventory TO inventory;
   ```
2. From the `backend` directory:
   ```bash
   mvn spring-boot:run
   ```

## Running the frontend
From the `frontend` directory:
```bash
npm install
npm run dev
```

The dashboard will call `GET http://localhost:8080/inventory/health`.

## API
`GET /inventory/health`

Response per ingredient:
- ingredientName
- location
- currentStock
- avgDailyUsage
- adjustedUsage
- daysRemaining
- riskLevel
- reorderRecommended
- upcomingEvent

## Forecasting logic
Reorder Point = (Adjusted Daily Usage × Lead Time Days) + Safety Stock

Risk levels:
- HEALTHY → >7 days remaining
- WARNING → 3–7 days
- CRITICAL → 1–3 days
- STOCKOUT_IMMINENT → <1 day
