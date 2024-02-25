import logging
import json
import base64
from enum import Enum
from typing import (
    Optional,
    Dict,
    Union,
)
from pydantic import (
    validator, field_validator,
)
from pydantic_settings import BaseSettings


class FormatType(str, Enum):
    """Types of log format: human-readable or machine-readable"""

    GELF = "gelf"
    HUMAN = "human"


class Settings(BaseSettings):

    debug: bool = True

    route: str = "/api/v1"
    description: str = "Find Me RestAPI Service"

    server_type: Optional[str] = "local"

    server_url_qa: str = ""
    server_url_prod: str = ""

    # Database parameters
    postgres_username: Optional[str] = "find_me_admin"
    postgres_password: Optional[str] = "123"
    postgres_host: Optional[str] = "localhost"
    postgres_port: Optional[str] = "5432"
    postgres_database: Optional[str] = "find_me"

    # Sql  queries logs
    enable_query_logs: Optional[bool] = False

    # @field_validator("log_level", pre=True, allow_reuse=True)
    # def to_int(cls, v):  # pylint: disable=E0213
    #     return v if isinstance(v, int) else logging.getLevelName(v)
    #
    # @validator("firebase_service_account", pre=True)
    # def from_base64(cls, v):  # pylint: disable=E0213
    #     return (
    #         v
    #         if isinstance(v, dict)
    #         else json.loads(base64.b64decode(v).decode("utf-8"))
    #     )


settings = Settings()
