from djoser.serializers import (
    UserSerializer,
    UserCreatePasswordRetypeSerializer,
    PasswordRetypeSerializer,
    CurrentPasswordSerializer
)
from rest_framework import serializers

from users.models import CustomUser, Friendship
from pubs.models import Pub, Menu


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
        if friend.is_company:
            raise serializers.ValidationError(
                'Нельзя добавить в друзья аккаунт комапнии!'
            )
        if Friendship.objects.filter(user=user, friend=friend).exists():
            raise serializers.ValidationError(
                'Пользователь уже есть у Вас в друзьях!')
        return data

    def get_is_friend(self, obj):
        return Friendship.objects.filter(
            user=self.context['request'].user,
            friend=obj.friend
        ).exists()


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

    def validate_name(self, alcohol_name):
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
        return alcohol_percent
