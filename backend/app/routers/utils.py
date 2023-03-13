from datetime import timedelta, datetime

from fastapi import HTTPException
from jose import jwt, JWTError
from pydantic import EmailStr
from sqlalchemy.orm import Session
from starlette import status

from app.crud import crud_players, crud_companies
from app.crud.utils import pwd_context
from app.schemas.schemas_auth import UserTypes
from app.schemas import schemas_auth, schemas_players, schemas_companies

SECRET_KEY = '85d2725c3b40ceb35c5571e6ca947b900b0588032a9061fba4014c6f23619686'
ALGORITHM = 'HS256'

ACCESS_TOKEN_EXPIRE_MINUTES = 30


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)


def authenticate_user(
    db: Session,
    user: schemas_players.PlayerLogin | schemas_companies.CompanyLogin
):
    if isinstance(user, schemas_players.PlayerLogin):
        db_user = crud_players.get_player(db, user.email)
    elif isinstance(user, schemas_companies.CompanyLogin):
        db_user = crud_companies.get_company(db, user.username)
    if not db_user:
        return False
    if not verify_password(user.password, db_user.hashed_password):
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


def get_current_user(db: Session, token: str, user_type: UserTypes):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail='Токен недействителен.',
        headers={'WWW-Authenticate': 'Bearer'},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        if user_type == UserTypes.player:
            email: EmailStr = payload.get('sub')
            if email is None:
                raise credentials_exception
            token_data = schemas_auth.TokenData(email=email)
        elif user_type == UserTypes.company:
            username: str = payload.get('sub')
            if username is None:
                raise credentials_exception
            token_data = schemas_auth.TokenData(username=username)
    except JWTError:
        raise credentials_exception
    user = None
    if user_type == UserTypes.player:
        user = crud_players.get_player(db, email=token_data.email)
    elif user_type == UserTypes.company:
        user = crud_companies.get_company(db, username=token_data.username)
    if user is None:
        raise credentials_exception
    return user


def sign_in_user(
    db: Session,
    user_data: schemas_players.PlayerLogin | schemas_companies.CompanyLogin
):
    user = authenticate_user(db, user_data)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail='Неверная почта или пароль.',
            headers={'WWW-Authenticate': 'Bearer'},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    sub = ''
    if type(user_data) == schemas_players.PlayerLogin:
        sub = user.email
    elif type(user_data) == schemas_companies.CompanyLogin:
        sub = user.username
    access_token = create_access_token(
        data={'sub': sub},
        expires_delta=access_token_expires
    )
    return {'access_token': access_token, 'token_type': 'bearer'}
