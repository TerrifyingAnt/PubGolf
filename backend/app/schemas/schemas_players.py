from pydantic import BaseModel, validator, constr
from pydantic.networks import EmailStr

from app.schemas.utils import password_regex, validate_re_password


class PlayerBase(BaseModel):
    email: EmailStr


class PlayerCreate(PlayerBase):
    password: constr(regex=password_regex)
    re_password: str

    _validate_re_password = validator(
        're_password', allow_reuse=True)(validate_re_password)


class PlayerLogin(PlayerBase):
    password: str


class Player(PlayerBase):
    id: int
    is_active: bool

    class Config:
        orm_mode = True
