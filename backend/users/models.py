from django.contrib.auth.models import AbstractUser
from django.core.validators import RegexValidator
from django.db import models

from users.validators import validate_username

ROLE_CHOICES = (
    ('user', 'Аутентифицированный пользователь'),
    ('company', 'Компания'),
    ('admin', 'Администратор'),
)


class CustomUser(AbstractUser):
    username = models.CharField(
        max_length=150,
        unique=True,
        validators=[validate_username],
        verbose_name='Логин',
    )
    email = models.EmailField(
        unique=True,
        max_length=254,
        verbose_name='Почта',
    )
    role = models.CharField(
        max_length=30,
        choices=ROLE_CHOICES,
        blank=True,
        default='user',
        verbose_name='Роль',
    )
    bio = models.TextField(
        blank=True,
        verbose_name='Доп. информация',
    )
    registered_office = models.CharField(
        max_length=256,
        blank=True,
        null=True,
        verbose_name='Юр. адрес'
    )
    phone_number = models.CharField(
        max_length=12,
        validators=[RegexValidator(regex=r'^\+7[0-9]{10}$')],
        unique=True,
        blank=True,
        null=True,
        verbose_name='Телефонный номер'
    )
    photo = models.ImageField(
        verbose_name='Фото'
    )

    class Meta:
        verbose_name = 'Пользователь'
        verbose_name_plural = 'Пользователи'

    @property
    def is_admin(self):
        return self.is_superuser or self.role == 'admin'

    @property
    def is_company(self):
        return self.role == 'company'
