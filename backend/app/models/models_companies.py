from sqlalchemy import (
    Column,
    Integer,
    String,
    Boolean,
    Text,
)

from app.database import Base


class Company(Base):
    __tablename__ = 'companies'

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String, nullable=False)
    address = Column(Text, nullable=False)
    photo = Column(String)
    about = Column(Text)
    phone_number = Column(String, unique=True, nullable=False)
    is_active = Column(Boolean, default=True)
