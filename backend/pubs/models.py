from django.db import models

from pubs.validators import validate_phone
from users.models import CustomUser


class Pub(models.Model):
    """Модель паба."""

    address = models.CharField(
        max_length=255,
        verbose_name="Адрес"
    )
    phone = models.CharField(
        max_length=12,
        unique=True,
        validators=[validate_phone],
        verbose_name="Номер телефона"
    )
    email = models.EmailField(
        unique=True,
        verbose_name="Почта"
    )

    company = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        verbose_name="Компания",
        related_name="pubs"
    )

    class Meta:
        verbose_name = "Паб"
        verbose_name_plural = "Пабы"

    def __str__(self):
        return self.company


class Menu(models.Model):
    """Модель меню."""

    pub = models.ForeignKey(
        Pub,
        on_delete=models.CASCADE,
        verbose_name='Паб',
        related_name="menus"
    )

    class Meta:
        verbose_name = "Меню"
        verbose_name_plural = "Меню"

    def __str__(self) -> str:
        return self.pub.company


class Alcohol(models.Model):
    """Модель алкоголя."""

    name = models.CharField(
        max_length=50,
        verbose_name="Название",
    )
    cost = models.PositiveIntegerField(
        verbose_name="Цена",
    )
    menu = models.ForeignKey(
        Menu,
        on_delete=models.CASCADE,
        verbose_name='Меню',
        related_name="alcohols"
    )

    class Meta:
        verbose_name = "Алкоголь"
        verbose_name_plural = "Алкоголь"

    def __str__(self) -> str:
        return self.name
