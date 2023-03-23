from django.shortcuts import get_object_or_404
from djoser.serializers import SetUsernameSerializer
from djoser.views import UserViewSet
from rest_framework import viewsets, mixins, status, permissions
from rest_framework.decorators import action
from rest_framework.response import Response

from api.pagination import GamesAndFriendsPagination
from api.serializers import (
    FriendsSerializer,
    CustomUserCreateSerializer,
    CustomPasswordSerializer,
    CustomUserSerializer,
    CustomUserMeSerializer,
    SetEmailSerializer
)
from users.models import CustomUser, Friendship


class CustomUserViewSet(UserViewSet):

    def get_serializer_class(self):
        if self.action == 'create':
            return CustomUserCreateSerializer
        elif self.action == 'set_password':
            return CustomPasswordSerializer
        elif self.action == 'set_username':
            return SetUsernameSerializer
        elif self.action == 'set_email':
            return SetEmailSerializer
        elif self.action == 'me':
            return CustomUserMeSerializer
        return CustomUserSerializer

    def get_permissions(self):
        if self.action == 'retrieve':
            self.permission_classes = [permissions.IsAuthenticated]
        return super().get_permissions()

    def get_queryset(self):
        return CustomUser.objects.all()

    @action(['post'], detail=False, url_path='set_email')
    def set_email(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = self.request.user
        new_email = serializer.data['new_email']

        setattr(user, 'email', new_email)
        user.save()

        return Response(status=status.HTTP_204_NO_CONTENT)


class FriendsBaseViewSet(viewsets.GenericViewSet):
    serializer_class = FriendsSerializer

    def get_queryset(self):
        return self.request.user.user_friends.all()


class FriendsListViewSet(mixins.ListModelMixin, FriendsBaseViewSet):
    pagination_class = GamesAndFriendsPagination


class FriendsCreateDestroyViewSet(
    mixins.CreateModelMixin,
    mixins.DestroyModelMixin,
    FriendsBaseViewSet
):

    def get_serializer_context(self):
        context = super().get_serializer_context()
        context['friend_id'] = self.kwargs.get('user_id')
        return context

    def perform_create(self, serializer):
        serializer.save(
            user=self.request.user,
            friend=get_object_or_404(
                CustomUser, id=self.kwargs.get('user_id')
            ))

    @action(methods=['delete'], detail=True)
    def delete(self, request, user_id):
        get_object_or_404(
            Friendship,
            user=request.user,
            friend_id=user_id
        ).delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
