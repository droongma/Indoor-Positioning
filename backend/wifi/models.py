from django.db import models

# Create your models here.
class Wifi(models.Model):
    BSSID = models.CharField(max_length=50)
    RSSI = models.IntegerField()
