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


class Company(Base):
    __tablename__ = 'companies'

    id = Column(Integer, primary_key=True, index=True)
    login = Column(String, unique=True, index=True, nullable=False)
    hashed_password = Column(String, nullable=False)
    address = Column(Text, nullable=False)
    photo = Column(String)
    about = Column(Text)
    email = Column(String, unique=True, nullable=False)
    phone_number = Column(String, unique=True, nullable=False)
    is_active = Column(Boolean, default=True)


class Staff(Base):
    __tablename__ = 'staff'

    id = Column(Integer, primary_key=True, index=True)
    login = Column(String, unique=True, index=True, nullable=False)
    hashed_password = Column(String, nullable=False)
    is_active = Column(Boolean, default=True)


friends = Table(
    'friends',
    Base.metadata,
    Column('player_id', Integer, ForeignKey('players.id')),
    Column('friend_id', Integer, ForeignKey('players.id'))
)


class Player(Base):
    __tablename__ = 'players'

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    is_active = Column(Boolean, default=True)

    friends = relationship(
        'Player', secondary=friends,
        primaryjoin=id == friends.c.player_id,
        secondaryjoin=id == friends.c.friend_id,
        backref='friends'
    )
