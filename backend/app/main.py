from fastapi import FastAPI

from app.models import models_players, models_companies
from app.routers import router_auth
from app.database import engine

models_players.Base.metadata.create_all(bind=engine)
models_companies.Base.metadata.create_all(bind=engine)

app = FastAPI()

app.include_router(router_auth.router)
