from main.api.base.base import Session
from main.api.base.messages.error_messages import *
from main.api.base.messages.messages import SUCCESSFULLY_LOGGED_IN
from main.api.user.repository.security import get_user_security_by_user_id, update_security
from main.api.user.repository.user import get_user_by_email
from main.api.user.serializer.request.login import Login
from main.api.utils.helper import new_psw, check_passwords, user_encode_security_token, generate_random_string
from main.api.utils.response import error_response, ok_response


def login_service(data: Login):
    with Session.begin() as session:
        try:
            user = get_user_by_email(session=session, email=data.email)
            if not user:
                return error_response(message=INVALID_USER_OR_PASSWORD, status_code=403)
            security = get_user_security_by_user_id(session=session, user_id=user.id)
            if not check_passwords(
                    password=security.password,
                    row_password=new_psw(salt=security.salt, password=data.password)
            ):
                return error_response(message=INVALID_USER_OR_PASSWORD, status_code=403)
            update_security(session=session, security=security, **{
                "access_token_secret": generate_random_string(255),
                "refresh_token_secret": generate_random_string(255),
            })
            return ok_response(
                message=SUCCESSFULLY_LOGGED_IN,
                cookies={
                    "Authorization": user_encode_security_token(user=user, secret=security.access_token_secret),
                    "refresh_token": user_encode_security_token(
                        user=user, secret=security.refresh_token_secret, token_type="refresh_jwt"),
                },
            )
        except Exception as err:
            session.rollback()
            raise err
