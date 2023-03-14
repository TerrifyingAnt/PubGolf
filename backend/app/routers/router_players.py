from fastapi import APIRouter, Depends, Body, HTTPException
from pydantic import EmailStr
from sqlalchemy.orm import Session
from starlette import status

from app.crud import crud_players
from app.database import get_db
from app.routers.utils import get_current_user, oauth2_scheme, get_token_data
from app.schemas import schemas_players
from app.schemas.schemas_auth import UserTypes

router = APIRouter()


@router.get('/players/me', response_model=schemas_players.Player)
def get_current_player(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    return get_current_user(db, token, UserTypes.player)


@router.patch('/players/reset_email', response_model=schemas_players.Player)
def reset_player_email(
    new_email: EmailStr = Body(embed=True),
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme),
):
    email = get_token_data(token, UserTypes.player).email
    player = crud_players.get_player(db, email)
    if player is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail='Пользователь не существует.'
        )
    return crud_players.update_player(
        db,
        player.email,
        new_email
    )
