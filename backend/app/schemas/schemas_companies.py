from pydantic import BaseModel, validator, constr
from pydantic.networks import EmailStr

from app.schemas.utils import (
    username_regex,
    password_regex,
    validate_re_password
)


class CompanyBase(BaseModel):
    username: constr(
        strip_whitespace=True,
        max_length=120,
        regex=username_regex
    )


class CompanyCreate(CompanyBase):
    password: constr(regex=password_regex)
    re_password: str
    address: str
    photo: str | None = None
    about: str | None = None
    email: EmailStr
    phone_number: constr(strip_whitespace=True)

    _validate_re_password = validator(
        're_password', allow_reuse=True)(validate_re_password)

    @validator('phone_number')
    def validate_phone_number(cls, value):
        if len(value) != 12 or value[0] != '+' or not value[1:].isdigit():
            raise ValueError('Неверный формат номера телефона.')
        return value


class CompanyLogin(CompanyBase):
    password: str


class Company(CompanyBase):
    id: int
    address: str
    photo: str | None = None
    about: str | None = None
    email: EmailStr
    phone_number: constr(strip_whitespace=True)
    is_active: bool
    # pubs: list[Pub] = []

    class Config:
        orm_mode = True
