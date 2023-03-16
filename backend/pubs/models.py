from django.db import models

from pubs.validators import validate_phone


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

    # company = models.ForeignKey()

    class Meta:
        verbose_name = "Паб"
        verbose_name_plural = "Пабы"

    def __str__(self):
        return self.address


class Alcohol(models.Model):
    pass


class Menu(models.Model):
    pass
