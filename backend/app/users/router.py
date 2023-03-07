from datetime import timedelta, datetime

from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from jose import jwt, JWTError
from passlib.context import CryptContext
from pydantic import EmailStr
from sqlalchemy.orm import Session
from starlette import status

from app.users import models
from app.users import schemas
from app.database import get_db

SECRET_KEY = '85d2725c3b40ceb35c5571e6ca947b900b0588032a9061fba4014c6f23619686'
ALGORITHM = 'HS256'
ACCESS_TOKEN_EXPIRE_MINUTES = 30

router = APIRouter()

pwd_context = CryptContext(schemes=['bcrypt'], deprecated='auto')

oauth2_scheme = OAuth2PasswordBearer(tokenUrl='token')


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)


def get_password_hash(password):
    return pwd_context.hash(password)


def get_player(db: Session, email: EmailStr):
    return db.query(models.Player).filter(models.Player.email == email).first()


def authenticate_user(db: Session, email: EmailStr, password: str):
    user = get_player(db, email)
    if not user:
        return False
    if not verify_password(password, user.hashed_password):
        return False
    return user


def create_access_token(data: dict, expires_delta: timedelta | None = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=15)
    to_encode.update({'exp': expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


def get_current_player(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail='Токен недействителен.',
        headers={'WWW-Authenticate': 'Bearer'},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        email: EmailStr = payload.get('sub')
        if email is None:
            raise credentials_exception
        token_data = schemas.TokenData(email=email)
    except JWTError:
        raise credentials_exception
    player = get_player(db, email=token_data.email)
    if player is None:
        raise credentials_exception
    return player


@router.post('/auth/signup', response_model=schemas.Player)
async def sign_up(
    player: schemas.PlayerCreate,
    db: Session = Depends(get_db),
):
    if get_player(db, player.email):
        raise HTTPException(
            status_code=400,
            detail='Пользователь с такой почтой уже существует.'
        )
    hashed_password = pwd_context.hash(player.password)
    db_player = models.Player(
        email=player.email,
        hashed_password=hashed_password
    )
    db.add(db_player)
    db.commit()
    db.refresh(db_player)
    return db_player


@router.post('/auth/signin', response_model=schemas.Token)
async def login_for_access_token(
    player: schemas.PlayerLogin,
    db: Session = Depends(get_db),
):
    player = authenticate_user(db, player.email, player.password)
    if not player:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail='Неверная почта или пароль.',
            headers={'WWW-Authenticate': 'Bearer'},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={'sub': player.email}, expires_delta=access_token_expires
    )
    return {'access_token': access_token, 'token_type': 'bearer'}



