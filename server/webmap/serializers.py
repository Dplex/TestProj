from webmap.models import Geo
from rest_framework import serializers

class GeoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Geo
        fields = ('lon', 'lat')