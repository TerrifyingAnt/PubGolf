from pydantic import BaseModel, validator, constr
from pydantic.networks import EmailStr

username_regex = r'[A-Za-z0-9@#$%^&+=]'
password_regex = r'[A-Za-z0-9@#$%^&+=]{8,}'


def validate_re_password(value: str, values: dict) -> str:
    if 'password' in values and value != values['password']:
        raise ValueError('Пароли не совпадают')
    return value


class StaffBase(BaseModel):
    username: constr(
        strip_whitespace=True,
        max_length=120,
        regex=username_regex
    )
    password: constr(regex=password_regex)


class StaffCreate(StaffBase):
    re_password: str

    _validate_re_password = validator(
        're_password', allow_reuse=True)(validate_re_password)


class Staff(StaffBase):
    id: int
    is_active: bool

    class Config:
        orm_mode = True


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
