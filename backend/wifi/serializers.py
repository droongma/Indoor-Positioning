from rest_framework import serializers
from wifi.models import Wifi

class WifiSerializer(serializers.ModelSerializer):
    class Meta:
        model = Wifi
        fields = "__all__"