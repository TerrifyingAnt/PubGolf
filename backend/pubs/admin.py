from django.contrib import admin

from .models import Pub


@admin.register(Pub)
class PubAdmin(admin.ModelAdmin):
    list_display = ('pk', 'address')
