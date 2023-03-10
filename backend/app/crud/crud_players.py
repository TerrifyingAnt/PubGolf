from pydantic import EmailStr
from sqlalchemy.orm import Session

from app.schemas import schemas_players
from app.models import models_players


def get_player(db: Session, email: EmailStr):
    return db.query(models_players.Player).filter(models_players.Player.email == email).first()


def create_player(
    db: Session,
    player: schemas_players.PlayerCreate,
    hashed_password: str
):
    db_player = models_players.Player(
        email=player.email,
        hashed_password=hashed_password
    )
    db.add(db_player)
    db.commit()
    db.refresh(db_player)
    return db_player
