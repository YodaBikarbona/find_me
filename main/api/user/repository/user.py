from typing import List

from sqlalchemy.sql import or_

from main.api.base.base import Session
from main.api.user.model.user import User


def get_all_users(session: Session) -> List[User]:
    return session.query(User).all()


def get_existing_user(session: Session, username: str, email: str, phone_number: str) -> User:
    return (session.query(User)
            .filter(or_(User.username == username, User.email == email, User.phone_number == phone_number)).first())


def new_user(session: Session, username: str, email: str, phone_number: str) -> User:
    user = User(username=username, email=email, phone_number=phone_number)
    session.add(user)
    session.flush()
    return user


def get_user_by_email(session: Session, email: str) -> User:
    return session.query(User).filter(User.email == email).first()
