from fastapi import FastAPI

from app.models import models_players, models_companies, models_staff
from app.routers import (
    router_auth,
    router_players,
    router_companies,
    router_staff
)
from app.database import engine

models_players.Base.metadata.create_all(bind=engine)
models_companies.Base.metadata.create_all(bind=engine)
models_staff.Base.metadata.create_all(bind=engine)

app = FastAPI()

app.include_router(router_auth.router)
app.include_router(router_players.router)
app.include_router(router_companies.router)
app.include_router(router_staff.router)
