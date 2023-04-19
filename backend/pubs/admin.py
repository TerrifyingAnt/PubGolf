from django.contrib import admin

from .models import Pub, Menu


@admin.register(Pub)
class PubAdmin(admin.ModelAdmin):
    list_display = ('pk', 'address', 'phone', 'company')
    search_fields = ('address', 'phone')


@admin.register(Menu)
class MenuAdmin(admin.ModelAdmin):
    list_display = ('pk', 'pub', 'name', 'cost')
