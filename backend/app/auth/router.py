from datetime import timedelta
from enum import Enum

from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from jose import jwt, JWTError
from pydantic import EmailStr
from sqlalchemy.orm import Session
from starlette import status

from app import players
from app.auth import schemas, utils
from app.auth.utils import SECRET_KEY, ALGORITHM
from app.database import get_db

router = APIRouter()

oauth2_scheme = OAuth2PasswordBearer(tokenUrl='token')
ACCESS_TOKEN_EXPIRE_MINUTES = 30


class UserTypes(Enum):
    player: str = 'player'
    company: str = 'company'


@router.post('/auth/players/signup', response_model=players.schemas.Player)
def sign_up_players(
    player: players.schemas.PlayerCreate,
    db: Session = Depends(get_db),
):
    if players.crud.get_player(db, player.email):
        raise HTTPException(
            status_code=400,
            detail='Пользователь с такой почтой уже существует.'
        )

    return players.crud.create_player(
        db,
        player,
        utils.get_password_hash(player.password)
    )


@router.post('/auth/players/signin', response_model=schemas.Token)
def sign_in_players(
    player: players.schemas.PlayerLogin,
    db: Session = Depends(get_db),
):
    player = utils.authenticate_user(db, player.email, player.password)
    if not player:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail='Неверная почта или пароль.',
            headers={'WWW-Authenticate': 'Bearer'},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = utils.create_access_token(
        data={'sub': player.email}, expires_delta=access_token_expires
    )
    return {'access_token': access_token, 'token_type': 'bearer'}


def get_current_user(db: Session, token: str, user_type: UserTypes):
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
    if user_type == UserTypes.player:
        user = players.crud.get_player(db, email=token_data.email)
    # elif user_type == UserTypes.company:
    #     user = companies.crud.get_company(db, email=token_data.email)
    if user is None:
        raise credentials_exception
    return user


@router.get('/players/me', response_model=players.schemas.Player)
def get_current_player(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    return get_current_user(db, token, UserTypes.player)


# @router.get('/companies/me', response_model=companies.schemas.Company)
# def get_current_player(
#     db: Session = Depends(get_db),
#     token: str = Depends(oauth2_scheme)
# ):
#     return get_current_user(db, token, UserTypes.company)
