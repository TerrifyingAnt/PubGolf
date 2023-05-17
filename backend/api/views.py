from django.shortcuts import get_object_or_404
from djoser.views import UserViewSet
from rest_framework import viewsets, mixins, status, permissions, serializers
from rest_framework.decorators import action
from rest_framework.response import Response

from api.pagination import GamesAndFriendsPagination
from api.permissions import (
    PlayerPermission,
    IsOwnerOrReadOnly,
    IsPubOwnerOrReadOnly
)
from api.serializers import (
    FriendshipRequestSerializer,
    CustomUserCreateSerializer,
    CustomPasswordSerializer,
    CustomUserSerializer,
    CustomUserMeSerializer,
    SetEmailSerializer,
    PubSerializer,
    MenuSerializer,
    GameSerializer,
    GameUserSerializer,
    InvitationSerializer
)
from users.models import CustomUser, FriendshipRequest
from pubs.models import Pub, Menu
from games.models import Game, Invitation, GameUser


class CustomUserViewSet(UserViewSet):

    def get_serializer_class(self):
        if self.action == 'create':
            return CustomUserCreateSerializer
        elif self.action == 'set_password':
            return CustomPasswordSerializer
        elif self.action == 'set_username':
            return serializers.SetUsernameSerializer
        elif self.action == 'set_email':
            return SetEmailSerializer
        elif self.action == 'me':
            return CustomUserMeSerializer
        return CustomUserSerializer

    def get_permissions(self):
        if self.action == 'retrieve':
            self.permission_classes = [permissions.IsAuthenticated]
        elif self.action == 'set_email':
            self.permission_classes = [permissions.CurrentUserOrAdmin]
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


class FriendshipRequestBaseViewSet(viewsets.GenericViewSet):
    serializer_class = FriendshipRequestSerializer
    permission_classes = (PlayerPermission,)


class FriendshipRequestViewSet(
    mixins.ListModelMixin,
    FriendshipRequestBaseViewSet
):
    def get_queryset(self):
        request = self.request

        if (
            'from-me' in request.query_params
            and request.query_params['from-me']
        ):
            return request.user.from_me_requests.all()

        return request.user.to_me_requests

    @action(methods=['post'], detail=True, url_path='accept')
    def accept_request(self, request, pk):
        friend_request = FriendshipRequest.objects.get(id=pk)

        if friend_request.to_user == request.user:
            friend_request.to_user.friends.add(friend_request.from_user)
            friend_request.from_user.friends.add(friend_request.to_user)
            friend_request.delete()
            return Response(
                {'status': 'Пользователь добавлен в друзья'},
                status=status.HTTP_201_CREATED
            )

        return Response(status=status.HTTP_404_NOT_FOUND)


class FriendshipRequestCreateDestroyViewSet(
    mixins.CreateModelMixin,
    mixins.DestroyModelMixin,
    FriendshipRequestBaseViewSet
):
    queryset = FriendshipRequest.objects.all()

    def get_serializer_context(self):
        context = super().get_serializer_context()
        context['friend_id'] = self.kwargs.get('user_id')

        return context

    def create(self, request, *args, **kwargs):
        user_id = self.kwargs.get('user_id')

        to_friend_request = FriendshipRequest.objects.filter(
            from_user_id=user_id,
            to_user=request.user
        )

        if to_friend_request.exists():
            to_user = get_object_or_404(CustomUser, id=user_id)
            request.user.friends.add(to_user)
            to_user.friends.add(request.user)
            to_friend_request.delete()
            return Response(
                {'status': 'Пользователь добавлен в друзья'},
                status=status.HTTP_201_CREATED
            )

        return super().create(request)

    def perform_create(self, serializer):
        request_user = self.request.user

        serializer.save(
            from_user=request_user,
            to_user=get_object_or_404(
                CustomUser,
                id=self.kwargs.get('user_id')
            )
        )

    @action(methods=['delete'], detail=True)
    def delete(self, request, user_id):
        get_object_or_404(
            FriendshipRequest,
            from_user=request.user,
            to_user=user_id
        ).delete()

        return Response(status=status.HTTP_204_NO_CONTENT)


class FriendViewSet(
    mixins.ListModelMixin,
    viewsets.GenericViewSet
):
    serializer_class = CustomUserSerializer

    def get_queryset(self):
        return self.request.user.friends.all()

    @action(methods=['delete'], detail=True)
    def delete(self, request, pk):
        friend_to_delete = get_object_or_404(CustomUser, id=pk)
        request.user.friends.remove(friend_to_delete)
        friend_to_delete.friends.remove(request.user)

        return Response(status=status.HTTP_204_NO_CONTENT)


class PubViewSet(viewsets.ModelViewSet):

    queryset = Pub.objects.all()
    serializer_class = PubSerializer
    permission_classes = (IsOwnerOrReadOnly,)

    def perform_create(self, serializer):
        serializer.save(company=self.request.user)


class MenuViewSet(
    mixins.ListModelMixin,
    mixins.CreateModelMixin,
    mixins.DestroyModelMixin,
    mixins.RetrieveModelMixin,
    mixins.UpdateModelMixin,
    viewsets.GenericViewSet
):

    serializer_class = MenuSerializer
    permission_classes = (IsPubOwnerOrReadOnly, )

    def get_queryset(self):
        pub_id = self.kwargs['pub_id']
        return Menu.objects.filter(pub=pub_id)

    def retrieve(self, request, pk=None, pub_id=None):
        menu = self.get_object()
        serializer = self.get_serializer(menu)
        return Response(serializer.data)

    def perform_create(self, serializer):
        user = self.request.user
        pub = Pub.objects.get(company=user)
        serializer.save(pub=pub)


class GameViewSet(viewsets.ModelViewSet):
    queryset = Game.objects.all()
    serializer_class = GameSerializer
    permission_classes = (PlayerPermission, )


class InvitationCreateViewSet(
    mixins.CreateModelMixin,
    viewsets.GenericViewSet
):
    queryset = Invitation.objects.all()
    serializer_class = InvitationSerializer
    permission_classes = (PlayerPermission, )

    def perform_create(self, serializer):
        game_id = self.kwargs['game_id']
        user_id = self.kwargs['user_id']
        game = Game.objects.get(id=game_id)
        recipient = CustomUser.objects.get(id=user_id)
        serializer.save(
            sender=self.request.user,
            recipient=recipient,
            game=game
        )


class InvitationReadViewSet(
    mixins.ListModelMixin,
    mixins.RetrieveModelMixin,
    viewsets.GenericViewSet
):
    serializer_class = InvitationSerializer
    permission_classes = (PlayerPermission, )

    def get_queryset(self):
        user = self.request.user
        return Invitation.objects.filter(recipient=user)


class GameUserViewSet(
    mixins.CreateModelMixin,
    mixins.DestroyModelMixin,
    viewsets.GenericViewSet
):
    queryset = GameUser.objects.all()
    serializer_class = GameUserSerializer
    permission_classes = (PlayerPermission, )

    def perform_create(self, serializer):
        game_id = self.kwargs['game_id']
        game = Game.objects.get(id=game_id)
        user = self.request.user
        serializer.save(user=user, game=game)

    @action(methods=['delete'], detail=True)
    def delete(self, request, game_id):
        get_object_or_404(
            GameUser,
            user=request.user,
            id=game_id
        ).delete()

        return Response(status=status.HTTP_204_NO_CONTENT)
