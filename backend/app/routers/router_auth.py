from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.crud import crud_players, crud_companies, crud_staff
from app.schemas import schemas_auth, schemas_players, schemas_companies, \
    schemas_staff
from app.routers.utils import (
    get_current_user,
    sign_in_user, oauth2_scheme
)
from app.database import get_db
from app.schemas.schemas_auth import UserTypes

router = APIRouter()


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


@router.post(
    '/auth/staff/signup',
    response_model=schemas_staff.Staff
)
def create_staff(
    staff: schemas_staff.StaffCreate,
    db: Session = Depends(get_db),
):
    if crud_staff.get_staff(db, staff.username):
        raise HTTPException(
            status_code=400,
            detail='Пользователь с таким логином уже существует.'
        )
    return crud_staff.create_staff(db, staff)


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


@router.post('/auth/staff/signin', response_model=schemas_auth.Token)
def sign_in_staff(
    staff_data: schemas_staff.StaffLogin,
    db: Session = Depends(get_db),
):
    return sign_in_user(db, staff_data)
