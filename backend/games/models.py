from django.db import models

from users.models import CustomUser

DIFFICULTY_LEVELS = (
    ('underbeerman', 'Подпивасник'),
    ('fan', 'Любитель'),
    ('freelanholic', 'Фриланголик')
)
BUDGET_LEVELS = (
    ('homeless', 'Бомж'),
    ('fan', 'Любитель'),
    ('major', 'Мажор')
)
GAME_STATUSES = (
    ('created', 'Создана комната'),
    ('started', 'Игра стартовала'),
    ('finished', 'Игра завершилась')
)


class Game(models.Model):
    """Модель комнаты."""

    name = models.CharField(
        unique=True,
        max_length=150,
        verbose_name='Название комнаты'
    )
    difficulty_level = models.CharField(
        max_length=50,
        choices=DIFFICULTY_LEVELS,
        verbose_name='Уровень сложности'
    )
    budget_level = models.CharField(
        max_length=50,
        choices=BUDGET_LEVELS,
        verbose_name='Уровень бюджета'
    )
    status = models.CharField(
        max_length=50,
        choices=GAME_STATUSES,
        verbose_name='Статус игры'
    )
    start_time = models.DateTimeField(
        blank=True,
        null=True,
        verbose_name='Время старта игры'
    )
    finish_time = models.DateTimeField(
        blank=True,
        null=True,
        verbose_name='Время завершения игры'
    )

    class Meta:
        verbose_name = 'Игра'
        verbose_name_plural = 'Игры'

    def __str__(self):
        return self.name


class Invitation(models.Model):
    """Модель приглашения в комнату."""

    sender = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        related_name='invitations_sent',
        verbose_name='Отправитель'
    )
    recipient = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        related_name='invitations_received',
        verbose_name='Получатель'
    )
    game = models.ForeignKey(
        Game,
        on_delete=models.CASCADE,
        verbose_name='Комната',
        related_name='invitations'
    )

    class Meta:
        verbose_name = 'Приглашение в комнату'
        verbose_name_plural = 'Приглашения в комнату'

    def __str__(self):
        return f'{self.sender} --- {self.recipient}'


class GameUser(models.Model):
    user = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        verbose_name='Игрок'
    )
    game = models.ForeignKey(
        Game,
        on_delete=models.CASCADE,
        verbose_name='Комната'
    )

    def __str__(self):
        return f'{self.user} --- {self.game}'


class Stage(models.Model):
    game = models.ForeignKey(
        Game,
        on_delete=models.CASCADE
    )
    # pub = models.ForeignKey(
    #     Pub,
    #     on_delete=models.CASCADE
    # )
