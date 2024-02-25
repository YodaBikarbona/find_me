import secrets
from datetime import timedelta
from hashlib import sha512
from random import choice

from jose import jwt
from phonenumbers import (
    parse as parse_num,
    is_valid_number,
    PhoneNumberFormat,
    format_number,
)

from main.api.logger.logger import logger
from main.api.user.model.user import User
from main.api.utils.response import now


def validate_phone_number(phone_number: str) -> str:
    try:
        num_obj = parse_num(phone_number, region="HR")
        is_valid = is_valid_number(num_obj)
        if not is_valid:
            raise ValueError(f"{phone_number} is not a valid number!")
        return format_number(num_obj, PhoneNumberFormat.E164)
    except ValueError as err:
        logger.error(f"Invalid phone number! {phone_number}")
        raise err


def generate_random_string(size: int = 32, alphabet: str = tuple(chr(x) for x in range(33, 127))) -> str:
    return "".join(secrets.choice(alphabet) for _ in range(size))


def password_regex(password):
    if (len(password) < 8) or (len(password) > 25):
        return False
    is_upper = False
    is_lower = False
    is_spec = False
    is_digit = False
    spec = "@#$%^&+=.!/?*-"
    for char in password:
        try:
            char = int(char)
            is_digit = True
        except ValueError as ex:
            if char in spec:
                is_spec = True
            else:
                if char.isalpha():
                    if char == char.upper():
                        is_upper = True
                    if char == char.lower():
                        is_lower = True
    if is_upper and is_lower and is_spec and is_digit:
        return True
    else:
        return False


def new_salt():
    source = [chr(x) for x in range(32, 127)]
    salt = u''.join(choice(source) for x in range(0, 32))
    return salt


def new_psw(salt, password):
    password = str(sha512(u'{0}{1}'.format(password, salt).encode('utf-8', 'ignore')).hexdigest())
    return password


def check_passwords(password, row_password):
    if password != row_password:
        return False
    return True


def user_encode_security_token(user: User, secret: str, token_type: str = "jwt"):
    """
    The function will encode the security token that the client side will send
    in every request inside the header.
    :param user:
    :param secret:
    :param token_type:
    :return: False or hash (str)
    """
    if token_type == "refresh_jwt":
        expiration_date = now() + timedelta(days=180)
    else:
        expiration_date = now() + timedelta(minutes=5)
    return jwt.encode(
        {
            "user_id": user.id,
            "email": user.email,
            "exp": expiration_date,
         }, secret, algorithm='HS256')
