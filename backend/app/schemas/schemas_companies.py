from pydantic import BaseModel, validator, constr
from pydantic.networks import EmailStr

username_regex = r'[A-Za-z0-9@#$%^&+=]'
password_regex = r'[A-Za-z0-9@#$%^&+=]{8,}'


def validate_re_password(value: str, values: dict) -> str:
    if 'password' in values and value != values['password']:
        raise ValueError('Пароли не совпадают')
    return value


class CompanyBase(BaseModel):
    username: constr(
        strip_whitespace=True,
        max_length=120,
        regex=username_regex
    )
    password: constr(regex=password_regex)
    address: str
    photo: str | None = None
    about: str | None = None
    email: EmailStr
    phone_number: constr(strip_whitespace=True)

    @validator('phone_number')
    def validate_phone_number(cls, value):
        if len(value) != 12 or value[0] != '+':
            raise ValueError('Неверный формат номера телефона.')
        return value


class CompanyCreate(CompanyBase):
    re_password: str

    _validate_re_password = validator(
        're_password', allow_reuse=True)(validate_re_password)


class Company(CompanyBase):
    id: int
    is_active: bool
    # pubs: list[Pub] = []

    class Config:
        orm_mode = True
