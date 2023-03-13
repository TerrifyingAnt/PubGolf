from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session

from app.crud import crud_players, crud_companies
from app.schemas import schemas_auth, schemas_players, schemas_companies
from app.routers.utils import (
    get_current_user,
    sign_in_user
)
from app.database import get_db
from app.schemas.schemas_auth import UserTypes

router = APIRouter()

oauth2_scheme = OAuth2PasswordBearer(tokenUrl='token')


@router.post('/auth/players/signup', response_model=schemas_players.Player)
def create_player(
    player: schemas_players.PlayerCreate,
    db: Session = Depends(get_db),
):
    if crud_players.get_player(db, player.email):
        raise HTTPException(
            status_code=400,
            detail='Пользователь с такой почтой уже существует.'
        )
    return crud_players.create_player(db, player)


@router.post(
    '/auth/companies/signup',
    response_model=schemas_companies.Company
)
def create_company(
    company: schemas_companies.CompanyCreate,
    db: Session = Depends(get_db),
):
    if crud_companies.get_company(db, company.username):
        raise HTTPException(
            status_code=400,
            detail='Пользователь с таким логином уже существует.'
        )
    return crud_companies.create_company(db, company)


@router.post('/auth/players/signin', response_model=schemas_auth.Token)
def sign_in_player(
    player_data: schemas_players.PlayerLogin,
    db: Session = Depends(get_db),
):
    return sign_in_user(db, player_data)


@router.post('/auth/companies/signin', response_model=schemas_auth.Token)
def sign_in_company(
    company_data: schemas_companies.CompanyLogin,
    db: Session = Depends(get_db),
):
    return sign_in_user(db, company_data)


@router.get('/players/me', response_model=schemas_players.Player)
def get_current_player(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    return get_current_user(db, token, UserTypes.player)


@router.get('/companies/me', response_model=schemas_companies.Company)
def get_current_company(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    return get_current_user(db, token, UserTypes.company)
