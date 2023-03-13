from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from app.database import get_db
from app.routers.utils import oauth2_scheme, get_current_user
from app.schemas import schemas_staff
from app.schemas.schemas_auth import UserTypes

router = APIRouter()


@router.get('/staff/me', response_model=schemas_staff.Staff)
def get_current_staff(
    db: Session = Depends(get_db),
    token: str = Depends(oauth2_scheme)
):
    return get_current_user(db, token, UserTypes.staff)
