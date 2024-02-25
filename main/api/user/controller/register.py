from fastapi import APIRouter

from main.api.base.messages.error_messages import BASE_ERROR
from main.api.config import settings
from main.api.logger.logger import logger
from main.api.user.serializer.request.user import NewUser
from main.api.user.service.user import create_new_user
from main.api.utils.response import error_response

router = APIRouter(
    prefix=f"{settings.route}/register",
    tags=["users"],
)


@router.post("")
async def new_user(data: NewUser):
    try:
        return create_new_user(data=data)
    except Exception as err:
        logger.error(f"Something went wrong, a new record cannot be added! {err}")
        return error_response(message=BASE_ERROR, status_code=500)
