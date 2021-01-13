package com.LibArcgisMap;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;



import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class MapViewCustom extends SimpleViewManager<MapActivity> {
    private String soTo = "0";
    private String soThua = "0";
    private  MapActivity map ;

    @Override
    public String getName() {
        return "MapArcgisViewAndroid";
    }


    @Override
    public MapActivity createViewInstance( ThemedReactContext reactContext) {
        map=new MapActivity(reactContext,soTo,soThua);
        return map;
    }
    @ReactProp(name = "SoTo")
    public void setSoTo(MapActivity view, @Nullable String message) {
        Log.i("Set SoTo", "ANDROID_SAMPLE_UI");
        view.setSoTo(message);
    }
    @ReactProp(name = "SoThua")
    public void setSoThua(MapActivity view, @Nullable String message) {
        Log.i("Set SoThua", "ANDROID_SAMPLE_UI");
        view.setSoThua(message);
    }
    @ReactProp(name = "IDVungQH")
    public void setIDVungQH(MapActivity view, @Nullable String message) {
        Log.i("Set IDVungQH", "ANDROID_SAMPLE_UI");
        view.setIDVungQH(message);
    }

    @ReactProp(name = "MaXa")
    public void setSoThua(MapActivity view, @Nullable int message) {
        Log.i("Set SoThua", "ANDROID_SAMPLE_UI");
        view.setMaXa(message);
    }

    @ReactProp(name = "UserNameDT")
    public void setUserNameDT(MapActivity view, @Nullable String message) {
        Log.i("Set UserNameDT", "ANDROID_SAMPLE_UI");
        view.setUserNameDT(message);
    }
    @ReactProp(name = "PasswordDT")
    public void setPasswordDT(MapActivity view, @Nullable String message) {
        Log.i("Set PasswordDT", "ANDROID_SAMPLE_UI");
        view.setPasswordDT(message);
    }

    @ReactProp(name = "layers")
    public void setLayers(MapActivity view, @Nullable ReadableArray layers) {
        Log.e("Nghia", "123"+layers);
        view.AddLayer(layers);
//        if (layers == null || layers.size() < 1) {
//            Log.v(REACT_CLASS, "set layers: adding default layer");
//            map.getOperationalLayers().add(new ArcGISTiledLayer(DEFAULT_LAYER));
//        } else {
//            //mapView.removeAllViews();
//            for (int i = 0; i < layers.size(); i++) {
//                ReadableMap layer = layers.getMap(i);
//                String type = layer.getString("type");
//                String url = layer.getString("url");
//
//                if (!url.equals("")) {
//                    if (type.equals("ArcGISTiledMapServiceLayer")) {
//                        Log.v(REACT_CLASS, "set layers: adding ArcGISTiledMapServiceLayer:" + url);
//                        ArcGISTiledLayer tiledLayerBaseMap = new ArcGISTiledLayer(url);
//
//                        map.getOperationalLayers().add(tiledLayerBaseMap);
//
//
//                    } else if (type.equals("ArcGISFeatureLayer")) {
//                        Log.v(REACT_CLASS, "set layers: adding ArcGISFeatureLayer:" + url);
//                        ServiceFeatureTable mServiceFeatureTable=new ServiceFeatureTable(url);
//
//                        map.getOperationalLayers().add(new FeatureLayer(mServiceFeatureTable));
//
//                    } else {
//                        Log.v(REACT_CLASS, "set layers: unrecognized layer: " + type);
//                    }
//                } else {
//                    Log.v(REACT_CLASS, "set layers: invalid url:" + url);
//                }
//            }
//            mapView.setMap(map);
//        }
    }


    @Override
    public Map<String,Integer> getCommandsMap() {
        Log.d("React"," View manager getCommandsMap:");
        return MapBuilder.of(
                "TimKiemThuaDat",
                1,"UpdateMaXa",2,"ZoomToGPS",3,"TimKiemVungQuyHoach",4,"UpdateMapVeTinh",5,"UpdateMapDuong",6
        );
    }

    @Override
    public void receiveCommand(
            MapActivity view,
            int commandType,
            @Nullable ReadableArray args) {
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case 1: {
                view.TimKiem();
                return;
            }
            case 2: {
                view.UpdateMaXa();
                return;
            }
            case 3: {
                view.ZoomToGPS();
                return;
            }
            case 4: {
                view.TimKiemVungQuyHoach();
                return;
            }
            case 5: {
                view.UpdateMapVeTinh();
                return;
            }
            case 6: {
                view.UpdateMapDuong();
                return;
            }

            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }


}
