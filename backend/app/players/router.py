from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from jose import jwt, JWTError
from pydantic import EmailStr
from sqlalchemy.orm import Session
from starlette import status

from app import players, auth
from app.database import get_db
from app.auth.utils import SECRET_KEY, ALGORITHM
from app.players import crud

router = APIRouter()

oauth2_scheme = OAuth2PasswordBearer(tokenUrl='token')



