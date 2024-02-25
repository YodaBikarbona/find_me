import tempfile

from sqlalchemy import (
    create_engine,
    Engine,
    event, URL,
)
from sqlalchemy.orm import (
    sessionmaker,
    declarative_base,
)

from main.api.config import settings
from main.api.logger.logger import logger

user, password, host, port, database = (
    settings.postgres_username,
    settings.postgres_password,
    settings.postgres_host,
    settings.postgres_port,
    settings.postgres_database,
)

postgres_is_set = all((user, password, host, port, database))
_server_type = settings.server_type

DB = {
    "drivername": "postgresql",
    "host": settings.postgres_host,
    "port": settings.postgres_port,
    "username": settings.postgres_username,
    "password": settings.postgres_password,
    "database": settings.postgres_database,
    "query": {
        "options": "-c timezone=utc",
    },
}

def create_database_engine(_db_url, _database_type):
    if _database_type == "postgresql":
        return create_engine(URL(**DB), client_encoding="utf8", pool_pre_ping=True)
    _engine = create_engine(_db_url, encoding="utf8", pool_pre_ping=True)

    @event.listens_for(Engine, "connect")
    def set_sqlite_pragma(dbapi_connection, connection_record):
        cursor = dbapi_connection.cursor()
        cursor.execute("PRAGMA foreign_keys=ON")
        cursor.close()
    return _engine


def initiate_postgres_database():
    """
    The DB for staging/prod
    :return:
    """
    _database_type = "postgresql"
    _db_url = f"{_database_type}://{user}:{password}@{host}:{port}/{database}"
    _engine = create_database_engine(_db_url=_db_url, _database_type=_database_type)
    return _db_url, _engine


def initiate_postgres_database_local():
    """
    The DB for testing locally
    :return:
    """
    _database_type = "postgresql"
    _db_url = f"{_database_type}://{user}:{password}@{host}:{port}/{database}"
    _engine = create_database_engine(
        _db_url=_db_url, _database_type=_database_type)
    return _db_url, _engine


def initiate_sqlite_database():
    """
    The DB is only for unit tests.
    :return:
    """
    _database_type = "sqlite"
    _db_url = f"{_database_type}:///{tempfile.gettempdir()}/{database}.db"
    _engine = create_database_engine(
        _db_url=_db_url, _database_type=_database_type)
    return _db_url, _engine


if "local" not in _server_type and postgres_is_set:
    db_url, engine = initiate_postgres_database()
elif _server_type == "local":
    db_url, engine = initiate_postgres_database_local()
elif settings.server_type == "test":
    db_url, engine = initiate_sqlite_database()
else:
    db_url, engine = None, None
    logger.error(f"Server Type: {settings.server_type}")
    logger.error(f"The engine cannot be created by invalid DB values!")

Session = sessionmaker(bind=engine)
Base = declarative_base()
