from sqlalchemy import (
    Column,
    Integer,
    String,
    Boolean,
    ForeignKey,
    Table
)
from sqlalchemy.orm import relationship

from app.database import Base


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
        secondaryjoin=id == friends.c.friend_id
    )
