from django.urls import path, include
from rest_framework.routers import DefaultRouter

from api.views import (
    CustomUserViewSet,
    FriendsListViewSet,
    FriendsCreateDestroyViewSet,
    PubViewSet,
    MenuViewSet,
)

router_v1 = DefaultRouter()

router_v1.register('users/friends', FriendsListViewSet, basename='friends')
router_v1.register(
    r'users/(?P<user_id>\d+)/friend',
    FriendsCreateDestroyViewSet,
    basename='add_delete_friend'
)
router_v1.register('users', CustomUserViewSet, basename='users')
router_v1.register('pubs', PubViewSet, basename='pubs')
router_v1.register(
    r'pubs/(?P<pub_id>\d+)/menu', MenuViewSet, basename='menu'
)

urlpatterns = [
    path('v1/auth/', include('djoser.urls.authtoken')),
    path('v1/', include(router_v1.urls)),
    path('v1/', include('djoser.urls.base'))
]
