from sqlalchemy import (
    Column,
    Sequence,
    Boolean,
    Unicode, UniqueConstraint,
)
from sqlalchemy.orm import relationship

from main.api.base.base import (
    Base,
    engine,
)
from main.api.base.model.base_model import BaseModel


class User(BaseModel):
    __tablename__ = "users"

    username = Column(Unicode(32), nullable=False, unique=True)
    email = Column(Unicode(64), nullable=False, unique=True)
    phone_number = Column(Unicode(20), nullable=False, unique=True)
    activated = Column(Boolean, default=False)
    banned = Column(Boolean, default=False)

    # Constrains
    UniqueConstraint(username, name="unique_username")
    UniqueConstraint(email, name="unique_email")
    UniqueConstraint(phone_number, name="unique_phone_number")


User.__table__.columns['id'].sequence = Sequence('user_id_seq')
Base.metadata.create_all(engine)
