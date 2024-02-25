from fastapi import APIRouter

from main.api.base.messages.error_messages import *
from main.api.config import settings
from main.api.logger.logger import logger
from main.api.user.serializer.request.login import Login
from main.api.user.service.login import login_service
from main.api.utils.response import error_response

router = APIRouter(
    prefix=f"{settings.route}/login",
    tags=["users"],
)


@router.post("")
async def login(data: Login):
    try:
        return login_service(data=data)
    except Exception as err:
        logger.error(f"Something went wrong, the user cannot login! {err}")
        return error_response(message=BASE_ERROR, status_code=500)
