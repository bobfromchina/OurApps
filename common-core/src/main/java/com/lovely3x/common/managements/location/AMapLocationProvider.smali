.class public Lcom/lovely3x/common/managements/location/AMapLocationProvider;
.super Lcom/lovely3x/common/managements/location/LocationManager2$LocationProvider;
.source "AMapLocationProvider.java"

# interfaces
.implements Lcom/amap/api/location/AMapLocationListener;


# static fields
.field private static final PROVIDER_NAME:Ljava/lang/String; = "AMap"

.field private static final TAG:Ljava/lang/String; = "AMapLocationProvider"


# instance fields
.field private mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

.field private mConfiguration:Lcom/lovely3x/common/managements/location/LocationManager2$Configuration;


# direct methods
.method public constructor <init>(Lcom/lovely3x/common/managements/location/LocationManager2;)V
    .registers 2
    .param p1, "manager2"    # Lcom/lovely3x/common/managements/location/LocationManager2;

    .prologue
    .line 25
    invoke-direct {p0, p1}, Lcom/lovely3x/common/managements/location/LocationManager2$LocationProvider;-><init>(Lcom/lovely3x/common/managements/location/LocationManager2;)V

    .line 26
    return-void
.end method

.method private wrapLocationWrapper(Lcom/amap/api/location/AMapLocation;)Lcom/lovely3x/common/beans/LocationWrapper;
    .registers 6
    .param p1, "aMapLocation"    # Lcom/amap/api/location/AMapLocation;
    .annotation build Landroid/support/annotation/NonNull;
    .end annotation

    .prologue
    .line 82
    const-wide v2, 0x403e228e38f604ecL    # 30.134982643187797

    invoke-virtual {p1, v2, v3}, Lcom/amap/api/location/AMapLocation;->setLatitude(D)V

    .line 83
    const-wide v2, 0x405e049577aa44ebL    # 120.07162276866772

    invoke-virtual {p1, v2, v3}, Lcom/amap/api/location/AMapLocation;->setLongitude(D)V

    .line 85
    new-instance v0, Lcom/lovely3x/common/beans/LocationWrapper;

    invoke-direct {v0}, Lcom/lovely3x/common/beans/LocationWrapper;-><init>()V

    .line 86
    .local v0, "current":Lcom/lovely3x/common/beans/LocationWrapper;
    invoke-virtual {v0, p1}, Lcom/lovely3x/common/beans/LocationWrapper;->setOriginalLocation(Landroid/location/Location;)V

    .line 88
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getLatitude()D

    move-result-wide v2

    invoke-virtual {v0, v2, v3}, Lcom/lovely3x/common/beans/LocationWrapper;->setLatitude(D)V

    .line 89
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getLongitude()D

    move-result-wide v2

    invoke-virtual {v0, v2, v3}, Lcom/lovely3x/common/beans/LocationWrapper;->setLongitude(D)V

    .line 91
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getAddress()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/lovely3x/common/beans/LocationWrapper;->setQualifiedName(Ljava/lang/String;)V

    .line 92
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getProvince()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/lovely3x/common/beans/LocationWrapper;->setProvince(Ljava/lang/String;)V

    .line 93
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getCity()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/lovely3x/common/beans/LocationWrapper;->setCity(Ljava/lang/String;)V

    .line 94
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getDistrict()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/lovely3x/common/beans/LocationWrapper;->setDistrict(Ljava/lang/String;)V

    .line 96
    sget-object v1, Lcom/lovely3x/common/beans/LocationWrapper$CoorType;->GCJ_02:Lcom/lovely3x/common/beans/LocationWrapper$CoorType;

    invoke-virtual {v0, v1}, Lcom/lovely3x/common/beans/LocationWrapper;->setCoorType(Lcom/lovely3x/common/beans/LocationWrapper$CoorType;)V

    .line 98
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getTime()J

    move-result-wide v2

    invoke-virtual {v0, v2, v3}, Lcom/lovely3x/common/beans/LocationWrapper;->setTime(J)V

    .line 99
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getAltitude()D

    move-result-wide v2

    invoke-virtual {v0, v2, v3}, Lcom/lovely3x/common/beans/LocationWrapper;->setAlt(D)V

    .line 100
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getAccuracy()F

    move-result v1

    invoke-virtual {v0, v1}, Lcom/lovely3x/common/beans/LocationWrapper;->setAccuracy(F)V

    .line 101
    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getProvider()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/lovely3x/common/beans/LocationWrapper;->setProvider(Ljava/lang/String;)V

    .line 102
    return-object v0
.end method


# virtual methods
.method public getProviderName()Ljava/lang/String;
    .registers 2

    .prologue
    .line 30
    const-string v0, "AMap"

    return-object v0
.end method

.method public onConfigurationChanged(Lcom/lovely3x/common/managements/location/LocationManager2$Configuration;)V
    .registers 2
    .param p1, "configuration"    # Lcom/lovely3x/common/managements/location/LocationManager2$Configuration;

    .prologue
    .line 35
    iput-object p1, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mConfiguration:Lcom/lovely3x/common/managements/location/LocationManager2$Configuration;

    .line 36
    return-void
.end method

.method public onLocationChanged(Lcom/amap/api/location/AMapLocation;)V
    .registers 6
    .param p1, "aMapLocation"    # Lcom/amap/api/location/AMapLocation;

    .prologue
    .line 68
    const-string v1, "AMapLocationProvider"

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "onLocationChanged => "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v1, v2}, Lcom/lovely3x/common/utils/ALog;->i(Ljava/lang/String;Ljava/lang/String;)V

    .line 70
    if-eqz p1, :cond_29

    invoke-virtual {p1}, Lcom/amap/api/location/AMapLocation;->getErrorCode()I

    move-result v1

    if-nez v1, :cond_29

    .line 71
    invoke-direct {p0, p1}, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->wrapLocationWrapper(Lcom/amap/api/location/AMapLocation;)Lcom/lovely3x/common/beans/LocationWrapper;

    move-result-object v0

    .line 72
    .local v0, "current":Lcom/lovely3x/common/beans/LocationWrapper;
    iget-object v1, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mManager:Lcom/lovely3x/common/managements/location/LocationManager2;

    invoke-virtual {v1, v0}, Lcom/lovely3x/common/managements/location/LocationManager2;->onLocationChanged(Lcom/lovely3x/common/beans/LocationWrapper;)V

    .line 74
    .end local v0    # "current":Lcom/lovely3x/common/beans/LocationWrapper;
    :cond_29
    return-void
.end method

.method public startLocation()V
    .registers 5

    .prologue
    .line 40
    new-instance v1, Lcom/amap/api/location/AMapLocationClient;

    iget-object v2, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mManager:Lcom/lovely3x/common/managements/location/LocationManager2;

    invoke-virtual {v2}, Lcom/lovely3x/common/managements/location/LocationManager2;->getContext()Landroid/content/Context;

    move-result-object v2

    invoke-direct {v1, v2}, Lcom/amap/api/location/AMapLocationClient;-><init>(Landroid/content/Context;)V

    iput-object v1, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

    .line 41
    new-instance v0, Lcom/amap/api/location/AMapLocationClientOption;

    invoke-direct {v0}, Lcom/amap/api/location/AMapLocationClientOption;-><init>()V

    .line 43
    .local v0, "mLocationOption":Lcom/amap/api/location/AMapLocationClientOption;
    iget-object v1, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

    invoke-virtual {v1, p0}, Lcom/amap/api/location/AMapLocationClient;->setLocationListener(Lcom/amap/api/location/AMapLocationListener;)V

    .line 47
    const-wide/16 v2, 0x3e8

    invoke-virtual {v0, v2, v3}, Lcom/amap/api/location/AMapLocationClientOption;->setInterval(J)Lcom/amap/api/location/AMapLocationClientOption;

    .line 48
    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lcom/amap/api/location/AMapLocationClientOption;->setGpsFirst(Z)Lcom/amap/api/location/AMapLocationClientOption;

    .line 50
    iget-object v1, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

    invoke-virtual {v1, v0}, Lcom/amap/api/location/AMapLocationClient;->setLocationOption(Lcom/amap/api/location/AMapLocationClientOption;)V

    .line 55
    iget-object v1, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

    invoke-virtual {v1}, Lcom/amap/api/location/AMapLocationClient;->startLocation()V

    .line 56
    return-void
.end method

.method public stopLocation()V
    .registers 2

    .prologue
    .line 60
    iget-object v0, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

    if-eqz v0, :cond_c

    .line 61
    iget-object v0, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

    invoke-virtual {v0}, Lcom/amap/api/location/AMapLocationClient;->stopLocation()V

    .line 62
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/lovely3x/common/managements/location/AMapLocationProvider;->mAMapLocationManager:Lcom/amap/api/location/AMapLocationClient;

    .line 64
    :cond_c
    return-void
.end method
