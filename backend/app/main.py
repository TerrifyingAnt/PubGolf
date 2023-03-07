from fastapi import FastAPI

from app import users
from app.database import engine
from app.users.router import router as users_router

users.models.Base.metadata.create_all(bind=engine)

app = FastAPI()

app.include_router(users_router)
