from fastapi import APIRouter

from main.api.base.messages.error_messages import *
from main.api.logger.logger import logger
from main.api.config import settings
from main.api.user.service.user import fetch_all_users
from main.api.utils.response import error_response

router = APIRouter(
    prefix=f"{settings.route}/users",
    tags=["users"],
)


@router.get("")
async def get_users():
    try:
        return fetch_all_users()
    except Exception as err:
        logger.error(f"Something went wrong getting the user! {err}")
        return error_response(message=BASE_ERROR, status_code=500)
