from phonenumbers import (
    parse as parse_num,
    is_valid_number,
    PhoneNumberFormat,
    format_number,
)

from main.api.logger.logger import logger


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
