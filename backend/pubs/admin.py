from django.contrib import admin

from .models import Pub, Alcohol


@admin.register(Pub)
class PubAdmin(admin.ModelAdmin):
    list_display = ('pk', 'address', 'phone')
    search_fields = ('address', 'phone')


@admin.register(Alcohol)
class AlcoholAdmin(admin.ModelAdmin):
    list_display = ('name', 'cost')
    search_fields = ('name')
