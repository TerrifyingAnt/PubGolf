from pydantic import EmailStr
from sqlalchemy.orm import Session

from app.players import schemas
from app.players import models


def get_player(db: Session, email: EmailStr):
    return db.query(models.Player).filter(models.Player.email == email).first()


def create_player(
    db: Session,
    player: schemas.PlayerCreate,
    hashed_password: str
):
    db_player = models.Player(
        email=player.email,
        hashed_password=hashed_password
    )
    db.add(db_player)
    db.commit()
    db.refresh(db_player)
    return db_player
