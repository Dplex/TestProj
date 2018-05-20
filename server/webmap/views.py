from django.shortcuts import render

from django.http import HttpResponse
from rest_framework import viewsets
from webmap.serializers import GeoSerializer
from webmap.models import Geo

# Create your views here.

class GeoViewSet(viewsets.ModelViewSet):
    queryset = Geo.objects.all()
    serializer_class = GeoSerializer

def index(request):
    return HttpResponse("Heelo, world. You're at the polls index.")