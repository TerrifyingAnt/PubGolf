username_regex = r'[A-Za-z0-9@#$%^&+=]'
password_regex = r'[A-Za-z0-9@#$%^&+=]{8,}'


def validate_re_password(value: str, values: dict) -> str:
    if 'password' in values and value != values['password']:
        raise ValueError('Пароли не совпадают')
    return value
