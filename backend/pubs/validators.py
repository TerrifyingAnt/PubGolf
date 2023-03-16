import re

from django.core.exceptions import ValidationError


def validate_phone(value):
    phone_regex = r'^\+7\d{10}$'
    if not re.match(phone_regex, value):
        raise ValidationError(
            "Номер телефона должен быть в формате +70123456789"
        )
    return value
