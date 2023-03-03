from sqlalchemy import (
    Column,
    Integer,
    String,
    Boolean,
    Text,
    ForeignKey,
    Table
)
from sqlalchemy.orm import relationship

from ..database import Base


class Pub(Base):
    """Модель пабов."""

    __tablename__ = 'pubs'

    id = Column(Integer, primary_key=True, index=True)
    address = Column(Text, nullable=False)
    email = Column(String, nullable=False)
    phone_number = Column(String, unique=True, nullable=False)
    # company = ForeignKey
