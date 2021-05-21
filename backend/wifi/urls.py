from django.conf.urls import include, url
from django.urls import path

app_name = "wifi"
urlpatterns = [
    path('', include('rest_framework.urls', namespace='rest_framework_category')),
]
