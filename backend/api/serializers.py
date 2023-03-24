from djoser.serializers import (
    UserSerializer,
    UserCreatePasswordRetypeSerializer,
    PasswordRetypeSerializer,
    CurrentPasswordSerializer
)
from rest_framework import serializers

from users.models import CustomUser, Friendship


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
        return (self.context['request'].user.is_authenticated
                and Friendship.objects.filter(
                    user=self.context['request'].user,
                    friend=obj
        ).exists())


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


class FriendsSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(
        source='friend.id',
        read_only=True,
    )
    username = serializers.CharField(
        source='friend.username',
        read_only=True
    )
    is_friend = serializers.SerializerMethodField()

    class Meta:
        model = Friendship
        fields = (
            'id',
            'username',
            'is_friend',
        )

    def validate(self, data):
        user = CustomUser.objects.get(id=self.context['request'].user.id)
        friend = CustomUser.objects.get(id=self.context['friend_id'])
        if user == friend:
            raise serializers.ValidationError(
                'Нельзя добавить в друзья самого себя!')
        if Friendship.objects.filter(user=user, friend=friend).exists():
            raise serializers.ValidationError(
                'Пользователь уже есть у Вас в друзьях!')
        return data

    def get_is_friend(self, obj):
        return Friendship.objects.filter(
            user=self.context['request'].user,
            friend=obj.friend
        ).exists()
