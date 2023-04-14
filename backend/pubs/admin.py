from django.contrib import admin

from .models import Pub, Alcohol, Menu


@admin.register(Pub)
class PubAdmin(admin.ModelAdmin):
    list_display = ('pk', 'address', 'phone', 'company')
    search_fields = ('address', 'phone')


@admin.register(Alcohol)
class AlcoholAdmin(admin.ModelAdmin):
    list_display = ('name', 'cost', 'menu')


@admin.register(Menu)
class MenuAdmin(admin.ModelAdmin):
    list_display = ('pub',)
