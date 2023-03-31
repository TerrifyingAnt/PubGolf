from django.contrib import admin
<<<<<<< HEAD

from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('api.urls'))
=======
from django.urls import path

urlpatterns = [
    path('admin/', admin.site.urls),
>>>>>>> origin/backend-pubs
]
