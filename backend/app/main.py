from fastapi import FastAPI

from app import players
from app.database import engine
from app.auth.router import router as users_router
from app.players.router import router as players_router

players.models.Base.metadata.create_all(bind=engine)
# companies.models.Base.metadata.create_all(bind=engine)

app = FastAPI()

app.include_router(users_router)
app.include_router(players_router)
