# Breakable Toy II: Spotify API

## 🚀 Overview

A full-stack music discovery app using the Spotify API. Built with React (TypeScript, Vite, Material UI) and Spring Boot (Java).

---

## Features

- **Spotify OAuth login**
- **Search** for tracks, artists, and albums
- **View details** for albums, artists
- **User dashboard** with top artists and tracks
- **Responsive UI** with Material UI
- **Player controls** (using iframe)
- **Dockerized** for easy deployment

---

## 🌐 API Endpoints

| Service | Method | Endpoint | Description |
|---|---|---|---|
| Auth | GET    | /auth/spotify | Redirects to Spotify login |
| Auth | GET    | /auth/callback | Handles Spotify OAuth callback |
| Auth | DELETE | /auth/logout | Deletes stored token |
| Profile | GET    | /me/profile | Returns user profile data |
| Profile | GET    | /me/top/artists | Returns user's top artists |
| Profile | GET    | /me/top/tracks | Returns user's top tracks |
| Artist | GET    | /artists/{id} | Returns artist details |
| Artist | GET    | /artists/{id}/top-tracks | Returns top tracks for an artist |
| Artist | GET    | /artists/{id}/albums | Returns albums by an artist |
| Album | GET    | /albums/{id} | Returns album details |
| Album | GET    | /albums/{id}/tracks | Returns tracks in an album |
| Search | GET    | /search | Searches for tracks, artists, albums |
| Track | GET    | /tracks/{id} | Returns track details |

---

## Technologies Used

### Backend

*   Java 21 LTS
*   SpringBoot
*   Gradle
*   Runs on port **8080**

### Frontend

*   TypeScript
*   ReactJS
*   React Context
*   Vite
*   Material UI 
*   Runs on port **9090**

### Testing
*   Vitest
*   React Testing Library
*   JUnit
*   Mockito

---

## Running the application

### Prerequisites
- Node.js (v18+)
- Java 21+
- Spotify Developer credentials (Client ID/Secret)

### Setup

#### 1. Clone the repository
```bash
git clone https://github.com/your-username/spotify-app.git
cd spotify-app
```

#### 2. Configure Environment Variables
- Backend: Edit `backend/src/main/resources/application.properties` with your Spotify API credentials and frontend URL.

#### 3. Start the Backend
```bash
cd backend
./gradlew bootRun
```

#### 4. Start the Frontend
```bash
cd frontend
npm install
npm run dev
```

- The frontend runs on [http://localhost:9090](http://localhost:9090)
- The backend runs on [http://localhost:8080](http://localhost:8080)

### Docker (optional)
You can run both frontend and backend with Docker:

```bash
docker-compose up --build
```

## Project Structure

```
spotify-app/
├── backend/   # Spring Boot API server
├── frontend/  # React app
├── README.md
└── docker-compose.yml  # Docker configuration
```

## Testing
- **Frontend:**
  - `cd frontend && npm run test`
- **Backend:**
  - `cd backend && ./gradlew test`

---
**Note:** This project is not affiliated with Spotify. For educational/demo purposes only.