from sqlalchemy import (
    Sequence,
    Column,
    Unicode,
    Integer,
    ForeignKey,
)
from sqlalchemy.orm import relationship

from main.api.base.base import (
    Base,
    engine,
)
from main.api.base.model.base_model import BaseModel
from main.api.user.model.user import User
from main.api.utils.helper import generate_random_string


class UserSecurity(BaseModel):
    __tablename__ = "user_securities"

    access_token_secret = Column(Unicode(255), nullable=False, default=generate_random_string(255))
    refresh_token_secret = Column(Unicode(255), nullable=False, default=generate_random_string(255))
    password = Column(Unicode(255), nullable=False)
    salt = Column(Unicode(255), nullable=False)
    # Foreign keys
    user_id = Column(Integer, ForeignKey('users.id', ondelete='CASCADE'), nullable=False)
    # Relationships
    user = relationship(User)


UserSecurity.__table__.columns['id'].sequence = Sequence('user_security_id_seq')
Base.metadata.create_all(engine)
