from djoser.serializers import (
    UserSerializer,
    UserCreatePasswordRetypeSerializer,
    PasswordRetypeSerializer,
    CurrentPasswordSerializer
)
from rest_framework import serializers

from users.models import CustomUser, FriendshipRequest
from pubs.models import Pub, Menu
from games.models import Game, Invitation, GameUser


class CustomUserCreateSerializer(UserCreatePasswordRetypeSerializer):

    class Meta:
        model = CustomUser
        fields = (
            'id',
            'username',
            'email',
            'password',
            'role',
            'bio',
            'photo',
            'registered_office',
            'phone_number'
        )
        required_fields = (
            'username',
            'email',
            'password'
        )


class CustomUserSerializer(UserSerializer):
    is_friend = serializers.SerializerMethodField()

    class Meta:
        model = CustomUser
        fields = (
            'id',
            'username',
            'role',
            'bio',
            'photo',
            'registered_office',
            'phone_number',
            'is_friend'
        )

    def get_is_friend(self, obj):
        request_user = self.context['request'].user

        return (request_user.is_authenticated
                and obj in request_user.friends.all())


class CustomUserMeSerializer(CustomUserSerializer):

    class Meta:
        model = CustomUser
        fields = (
            'id',
            'username',
            'email',
            'role',
            'bio',
            'photo',
            'registered_office',
            'phone_number',
            'is_friend'
        )


class CustomPasswordSerializer(PasswordRetypeSerializer):
    current_password = serializers.CharField(required=True)


class SetEmailSerializer(
    serializers.ModelSerializer,
    CurrentPasswordSerializer
):
    new_email = serializers.EmailField(required=True)

    class Meta:
        model = CustomUser
        fields = ('new_email', 'current_password')

    def validate(self, attrs):
        user = self.context['request'].user or self.user
        assert user is not None

        if attrs['new_email'] == user.email:
            raise serializers.ValidationError({
                'new_email': 'Введена та же почта, что и в аккаунте'
            })
        return super().validate(attrs)


class FriendshipRequestSerializer(serializers.ModelSerializer):
    from_user = serializers.CharField(
        source='from_user.username',
        read_only=True
    )
    to_user = serializers.CharField(
        source='to_user.username',
        read_only=True
    )

    class Meta:
        model = FriendshipRequest
        fields = '__all__'

    def validate(self, data):
        user = CustomUser.objects.get(id=self.context['request'].user.id)
        friend = CustomUser.objects.get(id=self.context['friend_id'])

        if user == friend:
            raise serializers.ValidationError(
                'Нельзя добавить в друзья самого себя!'
            )

        if friend.is_company:
            raise serializers.ValidationError(
                'Нельзя добавить в друзья аккаунт комапнии!'
            )

        if FriendshipRequest.objects.filter(
            from_user=user,
            to_user=friend
        ).exists():
            raise serializers.ValidationError('Заявка уже отправлена!')

        if friend in user.friends.all():
            raise serializers.ValidationError(
                'Пользователь уже есть у Вас в друзьях!'
            )

        return data


# class FriendsSerializer(serializers.ModelSerializer):
#     id = serializers.IntegerField(
#         source='friend.id',
#         read_only=True,
#     )
#     username = serializers.CharField(
#         source='friend.username',
#         read_only=True
#     )
#     is_friend = serializers.SerializerMethodField()
#
#     class Meta:
#         model = Friendship
#         fields = (
#             'id',
#             'username',
#             'is_friend',
#         )
#
#     def validate(self, data):
#         user = CustomUser.objects.get(id=self.context['request'].user.id)
#         friend = CustomUser.objects.get(id=self.context['friend_id'])
#         if user == friend:
#             raise serializers.ValidationError(
#                 'Нельзя добавить в друзья самого себя!')
#         if friend.is_company:
#             raise serializers.ValidationError(
#                 'Нельзя добавить в друзья аккаунт комапнии!'
#             )
#         if Friendship.objects.filter(user=user, friend=friend).exists():
#             raise serializers.ValidationError(
#                 'Пользователь уже есть у Вас в друзьях!')
#         return data
#
#     def get_is_friend(self, obj):
#         return Friendship.objects.filter(
#             user=self.context['request'].user,
#             friend=obj.friend
#         ).exists()


class PubSerializer(serializers.ModelSerializer):
    company = serializers.SlugRelatedField(
        slug_field='username',
        read_only=True
    )

    class Meta:
        model = Pub
        fields = (
            'id',
            'name',
            'pub_address',
            'company'
        )


class MenuSerializer(serializers.ModelSerializer):
    pub = serializers.IntegerField(
        source='pub.id',
        read_only=True
    )

    class Meta:
        model = Menu
        fields = (
            'id',
            'alcohol_name',
            'alcohol_percent',
            'cost',
            'pub'
        )

    def validate_alcohol_name(self, alcohol_name):
        user = CustomUser.objects.get(id=self.context['request'].user.id)
        pub = Pub.objects.get(company=user)
        if Menu.objects.filter(pub=pub, alcohol_name=alcohol_name).exists():
            raise serializers.ValidationError(
                'Такой алкоголь уже есть в меню.'
            )
        return alcohol_name

    def validate_cost(self, cost):
        if cost < 0:
            raise serializers.ValidationError(
                'Цена не может быть меньше 0.'
            )
        return cost

    def validate_alcohol_percent(self, alcohol_percent):
        if alcohol_percent > 100:
            raise serializers.ValidationError(
                'Процент содержания спирта не может быть больше 100%.'
            )
        elif alcohol_percent < 0:
            raise serializers.ValidationError(
                'Процент содержания спирта не может быть меньше 0%.'
            )
        return alcohol_percent


class GameSerializer(serializers.ModelSerializer):

    class Meta:
        model = Game
        fields = (
            'id',
            'name',
            'difficulty_level',
            'budget_level',
            'status',
            'start_time',
            'finish_time',
        )

    def validate_name(self, name):
        if Game.objects.filter(name=name).exists():
            raise serializers.ValidationError(
                'Комната с таким именем уже существует.'
            )


class InvitationSerializer(serializers.ModelSerializer):
    sender = serializers.ReadOnlyField(
        source='sender.username',
    )
    recipient = serializers.ReadOnlyField(
        source='recipient.username',
    )
    game = serializers.IntegerField(
        source='game.id',
        read_only=True
    )

    class Meta:
        model = Invitation
        fields = (
            'id',
            'sender',
            'recipient',
            'game'
        )

    def validate(self, data):
        sender = CustomUser.objects.get(id=self.context['request'].user.id)
        recipient_id = self.context['view'].kwargs['user_id']
        recipient = CustomUser.objects.get(id=recipient_id)

        if sender == recipient:
            raise serializers.ValidationError(
                'Нельзя пригласить самого себя!'
            )

        if recipient.is_company:
            raise serializers.ValidationError(
                'Нельзя пригласить аккаунт комапнии!'
            )

        if Invitation.objects.filter(
            sender=sender,
            recipient=recipient
        ).exists():
            raise serializers.ValidationError('Приглашение уже отправлено!')

        return data


class GameUserSerializer(serializers.ModelSerializer):
    user = serializers.SlugRelatedField(
        slug_field='username',
        read_only=True
    )
    game = serializers.IntegerField(
        source='game.id',
        read_only=True
    )

    class Meta:
        model = GameUser
        fields = '__all__'

    def validate(self, data):
        user = CustomUser.objects.get(id=self.context['request'].user.id)
        game_id = self.context['view'].kwargs['game_id']
        game = CustomUser.objects.get(id=game_id)

        if user.is_company:
            raise serializers.ValidationError(
                'Нельзя войти в игру будучи компанией.'
            )

        if GameUser.objects.filter(
            user=user,
            game=game
        ).exists():
            raise serializers.ValidationError('Вы уже присоединились к комнате.')

        return data
