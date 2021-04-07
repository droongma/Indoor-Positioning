from django.shortcuts import render
from rest_framework import viewsets
from wifi.models import Wifi
from wifi.serializers import WifiSerializer
# Create your views here.(APIs..)

class WifiViewSet(viewsets.ModelViewSet):
    queryset = Wifi.objects.all()
    serializer_class = WifiSerializer