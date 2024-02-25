from pydantic import (
    BaseModel,
    field_validator,
    EmailStr,
    Field,
)

from main.api.utils.helper import validate_phone_number


class NewUser(BaseModel):
    username: str = Field(min_length=6)
    email: EmailStr
    phone_number: str

    @field_validator("phone_number")
    def is_valid_phone_number(cls, phone_number):
        try:
            return validate_phone_number(phone_number)
        except ValueError as err:
            raise err
