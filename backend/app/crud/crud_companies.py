from pydantic import EmailStr
from sqlalchemy.orm import Session

from app.models import models_companies
from app.crud.utils import get_password_hash
from app.schemas.schemas_companies import CompanyCreate


def get_company(db: Session, username: str):
    return db.query(models_companies.Company).filter(
        models_companies.Company.username == username
    ).first()


def create_company(db: Session, company: CompanyCreate):
    db_company = models_companies.Company(
        username=company.username,
        address=company.address,
        photo=company.photo,
        about=company.about,
        email=company.email,
        phone_number=company.phone_number,
        hashed_password=get_password_hash(company.password)
    )
    db.add(db_company)
    db.commit()
    db.refresh(db_company)
    return db_company
