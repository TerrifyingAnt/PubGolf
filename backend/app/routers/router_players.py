from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from app.database import get_db
from app.routers.utils import get_current_user, oauth2_scheme
from app.schemas import schemas_players
from app.schemas.schemas_auth import UserTypes

router = APIRouter()


@router.get('/players/me', response_model=schemas_players.Player)
def get_current_player(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    return get_current_user(db, token, UserTypes.player)
