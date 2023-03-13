from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from app.database import get_db
from app.routers.utils import oauth2_scheme, get_current_user
from app.schemas import schemas_companies
from app.schemas.schemas_auth import UserTypes

router = APIRouter()


@router.get('/companies/me', response_model=schemas_companies.Company)
def get_current_company(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    return get_current_user(db, token, UserTypes.company)
