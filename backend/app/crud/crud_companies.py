from pydantic import EmailStr
from sqlalchemy.orm import Session

from app.companies import models


def get_company(db: Session, email: EmailStr):
    return db.query(models.Company).filter(
        models.Company.email == email
    ).first()
