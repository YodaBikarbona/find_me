from sqlalchemy import (
    Column,
    Sequence,
    Unicode,
    Text,
    Integer,
    ForeignKey,
    Date,
)
from sqlalchemy.orm import relationship

from main.api.base.base import (
    Base,
    engine,
)
from main.api.base.repository.base_model import BaseModel
from main.api.user.model.user import User


class Profile(BaseModel):
    __tablename__ = "profiles"

    first_name = Column(Unicode(16), nullable=True)
    last_name = Column(Unicode(16), nullable=True)
    gender = Column(Unicode(6), nullable=True)
    bio = Column(Unicode(1024), nullable=True)
    birthday = Column(Date)
    # Foreign keys
    user_id = Column(Integer, ForeignKey('users.id', ondelete='CASCADE'), nullable=False)
    # Relationships
    user = relationship(User, back_populates='profile')


Profile.__table__.columns['id'].sequence = Sequence('profile_id_seq')
Base.metadata.create_all(engine)
