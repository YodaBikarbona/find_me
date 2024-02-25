from main.api.base.base import Session
from main.api.user.model.security import UserSecurity


def new_user_security(session: Session, user_id: int, salt: str, password: str) -> UserSecurity:
    security = UserSecurity(user_id=user_id, salt=salt, password=password)
    session.add(security)
    session.flush()
    return security


def get_user_security_by_user_id(session: Session, user_id: int) -> UserSecurity:
    return session.query(UserSecurity).filter(UserSecurity.user_id == user_id).first()


def update_security(session: Session, security: UserSecurity, **kwargs):
    """
    Function will update a record
    :param session:
    :param security:
    :param kwargs:
    :return: User (updated record) or Exception
    """
    try:
        session.query(UserSecurity).filter(UserSecurity.id == security.id).update(kwargs)
    except Exception as err:
        raise Exception("Something went wrong, a record cannot be updated!", err)
