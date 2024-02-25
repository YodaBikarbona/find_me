from sqlalchemy.sql.functions import now
from sqlalchemy import (
    Column,
    Integer,
    TIMESTAMP,
)

from main.api.base.base import Base


class BaseModel(Base):
    __abstract__ = True
    id = Column(Integer, primary_key=True, index=True)
    created_at = Column(TIMESTAMP(timezone=True), nullable=False, default=now())
    modified_at = Column(TIMESTAMP(timezone=True), nullable=False, default=now())
