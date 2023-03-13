from pydantic import BaseModel, constr, validator

from app.schemas.utils import (
    username_regex,
    password_regex,
    validate_re_password
)


class StaffBase(BaseModel):
    username: constr(
        strip_whitespace=True,
        max_length=120,
        regex=username_regex
    )


class StaffCreate(StaffBase):
    password: constr(regex=password_regex)
    re_password: str

    _validate_re_password = validator(
        're_password', allow_reuse=True)(validate_re_password)


class StaffLogin(StaffBase):
    password: str


class Staff(StaffBase):
    id: int
    is_active: bool

    class Config:
        orm_mode = True
