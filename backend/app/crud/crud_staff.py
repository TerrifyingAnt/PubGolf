from sqlalchemy.orm import Session

from app.crud.utils import get_password_hash
from app.schemas import schemas_staff
from app.models import models_staff


def get_staff(db: Session, username: str):
    return db.query(models_staff.Staff).filter(
        models_staff.Staff.username == username
    ).first()


def create_staff(db: Session, staff: schemas_staff.StaffCreate):
    db_staff = models_staff.Staff(
        username=staff.username,
        hashed_password=get_password_hash(staff.password)
    )
    db.add(db_staff)
    db.commit()
    db.refresh(db_staff)
    return db_staff
