from main.api.base.base import Session
from main.api.base.messages.error_messages import *
from main.api.base.messages.messages import *
from main.api.profile.repository.profile import new_profile
from main.api.user.repository.security import new_user_security
from main.api.user.repository.user import (
    get_all_users,
    get_existing_user,
    new_user,
)
from main.api.user.serializer.request.user import NewUser
from main.api.user.serializer.response.user import dump
from main.api.utils.helper import new_salt, new_psw
from main.api.utils.response import (
    ok_response,
    error_response,
)


def fetch_all_users():
    with Session.begin() as session:
        try:
            users = get_all_users(session=session)
            if not users:
                return error_response(message=USERS_NOT_FOUND, status_code=404)
            res = [dump(user) for user in users]
            return ok_response(message="User", **{"data": res})
        except Exception as err:
            session.rollback()
            raise err


def create_new_user(data: NewUser) -> dict:
    with Session.begin() as session:
        try:
            if not data.is_valid_password():
                return error_response(message=BAD_REQUEST, status_code=400)
            user = get_existing_user(
                session=session, username=data.username, email=data.email, phone_number=data.phone_number)
            if user:
                return error_response(message=USER_ALREADY_EXISTS, status_code=409)
            user = new_user(session=session, username=data.username, email=data.email, phone_number=data.phone_number)
            salt = new_salt()
            password = new_psw(salt=salt, password=data.password)
            new_user_security(session=session, user_id=user.id, salt=salt, password=password)
            new_profile(session=session, user_id=user.id)
            return ok_response(message=SUCCESSFULLY_REGISTERED, status_code=201)
        except Exception as err:
            session.rollback()
            raise err
