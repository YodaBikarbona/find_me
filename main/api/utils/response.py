from datetime import (
    datetime,
    timezone,
)

from fastapi.responses import JSONResponse


def now() -> datetime:
    return datetime.now(tz=timezone.utc)


def ok_response(message, status_code=200, cookies: dict = {}, **additional_data):
    """
    The function will create https ok response
    :param message:
    :param status_code:
    :param cookies:
    :param additional_data:
    :return: dict
    """
    data = {
        'status': 'OK',
        'code': status_code,
        'server_time': now().strftime("%Y-%m-%dT%H:%M:%S.%f%Z"),
        'message': message,
    }
    for k, v in additional_data.items():
        data['{0}'.format(k)] = v
    res = JSONResponse(data, status_code=status_code)
    for k, v in cookies.items():
        res.set_cookie(key=k, value=v)
    return res


def error_response(message, status_code):
    """
    The function will create https error response
    :param message:
    :param status_code:
    :return: dict
    """
    data = {
        'status': 'ERROR',
        'code': status_code,
        'server_time': now().strftime("%Y-%m-%dT%H:%M:%S.%f%Z"),
        'error_message': message
    }
    return JSONResponse(data, status_code=status_code)
