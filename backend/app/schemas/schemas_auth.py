from enum import Enum

from pydantic import BaseModel, EmailStr


class UserTypes(Enum):
    player: str = 'player'
    company: str = 'company'


class Token(BaseModel):
    access_token: str
    token_type: str


class TokenData(BaseModel):
    email: EmailStr | None = None
    username: str | None = None
