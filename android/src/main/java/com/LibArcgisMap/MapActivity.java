package com.LibArcgisMap;


import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureCollection;
import com.esri.arcgisruntime.data.FeatureCollectionTable;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.security.UserCredential;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapActivity extends LinearLayout implements LifecycleEventListener {

    // Variable Arcgis
    private MapView mMapView;
    private ArcGISMap map;
    private ServiceFeatureTable mServiceFeatureTable;
    private FeatureLayer mFeatureLayer;
    private ServiceFeatureTable mServiceFeatureTableMauQH;
    private FeatureLayer mFeatureLayerMauQH;
    private LocationDisplay lDisplayManager;
    private LocationDisplay mLocationDisplay;
    Basemap.Type basemapType;
    String soTo = "0";
    String idVungQH="0";
    String  soThua = "0";
    String  dienTich = "0";
    String  loaiDat="";
    String PasswordDT="123";
    String UserNameDT="123";
    private static LayerList ListLayer_Map;

    //
    private Context context;
    private  int _MaXa=26377;

    public MapActivity(Context context,String SoTo,String SoThua) {
        super(context);//ADD THIS
        this.context = context;
        this.soThua=SoThua;
        this.soTo=SoTo;
        init();
    }
    public void init() {
        inflate(context, R.layout.activity_map, this);
        setupMap();
        showCallLayout();
    }
    public void setupMap() {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud6806025350,none,1JPJD4SZ8Y4DRJE15232");
        mMapView = findViewById(R.id.mapView);
        if (mMapView != null) {
            basemapType = Basemap.Type.OPEN_STREET_MAP;
            double latitude =  10.890587;
            double longitude = 106.922532;
            int levelOfDetail = 11;
            map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);


//            UserCredential user = new UserCredential("dothibienhoa", "dothibienhoa2020");
//            UserCredential userBH = new UserCredential("bienhoa", "Stnmt75731");
//            ArcGISTiledLayer tiledLayerBaseMap = new ArcGISTiledLayer("http://datdai.stnmt.dongnai.gov.vn/arcgis/rest/services/DOTHIBIENHOA/PhanKhu_26377/MapServer");
//            tiledLayerBaseMap.setCredential(user);
//
//            String urlRanhThua=GetURLMap();
//            mServiceFeatureTable=new ServiceFeatureTable(urlRanhThua);
//            mServiceFeatureTable.setCredential(user);
//            mFeatureLayer=new FeatureLayer(mServiceFeatureTable);
//
//
//            mServiceFeatureTableMauQH =new ServiceFeatureTable("http://datdai.stnmt.dongnai.gov.vn/arcgis/rest/services/DOTHIBIENHOA/PhanKhu_26377/MapServer/0");
//            mServiceFeatureTableMauQH.setCredential(user);
//            mFeatureLayerMauQH =new FeatureLayer(mServiceFeatureTableMauQH);
//
//
//            map.getOperationalLayers().add(tiledLayerBaseMap);
//            map.getOperationalLayers().add(mFeatureLayerMauQH);
//            map.getOperationalLayers().add(mFeatureLayer);
            map.setMinScale(100000);
            mMapView.setMap(map);
            lDisplayManager = mMapView.getLocationDisplay();
            ZoomToXa(mMapView,_MaXa);

        }
    }
    private  void showCallLayout(){
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this.context, mMapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                Log.d("Info", "onSingleTapConfirmed: " + motionEvent.toString());
                mFeatureLayerMauQH.clearSelection();
                mFeatureLayer.clearSelection();

                // get the point that was clicked and convert it to a point in map coordinates
                android.graphics.Point screenPoint = new android.graphics.Point(Math.round(motionEvent.getX()),
                        Math.round(motionEvent.getY()));
                // create a map point from screen point
                Point mapPoint = mMapView.screenToLocation(screenPoint);
                // convert to WGS84 for lat/lon format
                Point wgs84Point = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
                int tolerance = 10;

                final ListenableFuture<IdentifyLayerResult> identifyLayerResultListenableFuture = mMapView
                        .identifyLayerAsync(mFeatureLayer, screenPoint, tolerance, false, 1);


                // create a textview for the callout
                identifyLayerResultListenableFuture.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdentifyLayerResult identifyLayerResult = identifyLayerResultListenableFuture.get();
                            for (GeoElement element : identifyLayerResult.getElements()) {
                                Feature feature = (Feature) element;
                                mFeatureLayer.selectFeature(feature);
                                // create a map of all available attributes as name value pairs
                                Map<String, Object> attr = feature.getAttributes();
                                Set<String> keys = attr.keySet();

                                for (String key : keys) {
                                    Object value = attr.get(key);
                                    if (key.equals("SH_TO")) {
                                        soTo = attr.get(key).toString();
                                    }
                                    if (key.equals("SH_THUA")) {
                                        soThua = attr.get(key).toString();
                                    }

                                    if (key.equals("DIEN_TICH")) {
                                        dienTich = attr.get(key).toString();
                                    }
                                    if (key.equals("LOAIDAT")) {
                                        loaiDat = attr.get(key).toString();
                                    }
                                    if (key.equals("TENVUNGQUY")) {
                                        // loaiQH=attr.get(key).toString();
                                    }


                                    Log.i(getResources().getString(R.string.app_name), "Select feature failed  : " + key + " | " + value + "\n");

                                }
                                String strContent = String.format("Số tờ: %s, số thửa: %s, diện tích: %.1fm²", soTo, soThua, Double.parseDouble(dienTich));
                                Log.i(getResources().getString(R.string.app_name), "Select soThua soTo  : " + strContent);

                                // center the mapview on selected feature
                                if (!soThua.equals("0") && !soTo.equals("0")) {
                                    Envelope envelope = feature.getGeometry().getExtent();
                                    mMapView.setViewpointGeometryAsync(envelope, 10);
                                    WritableMap datamap = Arguments.createMap();
                                    datamap.putString("SoTo", soTo);
                                    datamap.putString("SoThua", soThua);
                                    datamap.putString("DienTich", dienTich);
                                    datamap.putString("LoaiDat", loaiDat);

                                    ReactContext reactContext = (ReactContext) mMapView.getContext();
                                    reactContext
                                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                            .emit("dataToThua", datamap);
                                }
                            }
                        } catch (Exception e1) {
                            Log.e(getResources().getString(R.string.app_name), "Select feature failed: " + e1.getMessage());
                        }
                    }
                });


                return true;
            }
        });

    }
    private void ZoomToXa(final MapView mMapView, int maXa)
    {
        try{
            ServiceFeatureTable  mServiceFeatureTableZoom=new ServiceFeatureTable("http://datdai.stnmt.dongnai.gov.vn:8080/atlasadaptordatdai/rest/services/Atlas2015/NEN_KTXH_150715/MapServer/61");

            QueryParameters q = new QueryParameters();
            q.setWhereClause(String.format("MASO =%s",maXa));
            q.setReturnGeometry(true);
            final ListenableFuture<FeatureQueryResult> future = mServiceFeatureTableZoom.queryFeaturesAsync(q);
            future.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        FeatureQueryResult result = future.get();
                        Iterator<Feature> resultIterator = result.iterator();
                        if (resultIterator.hasNext()) {
                            Feature feature = resultIterator.next();
                            Envelope en = feature.getGeometry().getExtent();

                            mMapView.setViewpointGeometryAsync(en, 9);
                            // Toast.makeText(getApplicationContext(),feature.getAttributes().get("LOAIDAT").toString(), Toast.LENGTH_LONG).show();

                        } else {
                        }
                    } catch (Exception e) {
                        Log.e(getResources().getString(R.string.app_name),
                                "Error=" + e.getMessage());
                    }
                }
            });

        }
        catch (Exception ex) {
            Log.e( "Error=" ,ex.getMessage());

        }
    }

    public void setSoTo(String st) {
        this.soTo = st;
    }
    public void setIDVungQH(String st) {
        this.idVungQH = st;
    }

    public void setPasswordDT(String st) {
        this.PasswordDT = st;
    }
    public void setUserNameDT(String st) {
        this.UserNameDT = st;
    }
    public void AddLayer(ReadableArray layers){

        if (layers != null || layers.size() > 1) {
            ReadableMap layer = layers.getMap(0);
            String type = layer.getString("type");
            String url = layer.getString("url");
            String username = layer.getString("username");
            String password = layer.getString("password");

            UserCredential user = new UserCredential(username, password);
            ArcGISTiledLayer tiledLayerBaseMap = new ArcGISTiledLayer("http://datdai.stnmt.dongnai.gov.vn/arcgis/rest/services/DOTHIBIENHOA/PhanKhu_26377/MapServer");
            tiledLayerBaseMap.setCredential(user);

            String urlRanhThua=GetURLMap();
            mServiceFeatureTable=new ServiceFeatureTable(urlRanhThua);
            mServiceFeatureTable.setCredential(user);
            mFeatureLayer=new FeatureLayer(mServiceFeatureTable);


            mServiceFeatureTableMauQH =new ServiceFeatureTable("http://datdai.stnmt.dongnai.gov.vn/arcgis/rest/services/DOTHIBIENHOA/PhanKhu_26377/MapServer/0");
            mServiceFeatureTableMauQH.setCredential(user);
            mFeatureLayerMauQH =new FeatureLayer(mServiceFeatureTableMauQH);
            ListLayer_Map = map.getOperationalLayers();
            ListLayer_Map.add(tiledLayerBaseMap);
            ListLayer_Map.add(mFeatureLayerMauQH);
            ListLayer_Map.add(mFeatureLayer);
            //ListLayer_Map.remove(0);


            map.setMinScale(100000);
            mMapView.setMap(map);
        }
    }

    public void TimKiem(){
        final String soToTim=this.soTo;
        final String soThuaTim=this.soThua;
        try{
            if (soToTim != null && soToTim.length() > 0) {
                QueryParameters queryParameters = new QueryParameters();
                queryParameters.setWhereClause("SH_TO =" + soToTim + " and SH_THUA=" + soThuaTim + " ");
                try {
                    List<FeatureQueryResult> outFields = new ArrayList<>();

                    final ListenableFuture<FeatureQueryResult> future = mServiceFeatureTable.queryFeaturesAsync(queryParameters);

                    future.addDoneListener(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                //create a feature collection table from the query results
                                FeatureCollectionTable featureCollectionTable = new FeatureCollectionTable(future.get());

                                //create a feature collection from the above feature collection table
                                FeatureCollection featureCollection = new FeatureCollection();
                                featureCollection.getTables().add(featureCollectionTable);

                                FeatureQueryResult result = future.get();
                                Iterator<Feature> resultIterator = result.iterator();
                                soThua=soThuaTim;
                                soTo=soToTim;
                                if (resultIterator.hasNext()) {
                                    Feature feature1 = resultIterator.next();
                                    Envelope envelope = feature1.getGeometry().getExtent();
                                    mMapView.setViewpointGeometryAsync(envelope, 10);
                                    mFeatureLayer.selectFeature(feature1);
                                    //handleViewPageThuaDat();
                                } else {
                                }
                            } catch (Exception e) {

                                Log.e(getResources().getString(R.string.app_name),
                                        "Error=" + e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {

                }
            }

        }
        catch (Exception ex) {

        }

        System.out.println("Clikc tim kiem thua dat");
        System.out.println(this.soThua);
        System.out.println(this.soTo);

    }
    public void TimKiemVungQuyHoach(){
        mFeatureLayerMauQH.clearSelection();

        String idVungQH =this.idVungQH;
        try{
            if (idVungQH != null && idVungQH.length() > 0) {
                QueryParameters queryParameters = new QueryParameters();
                queryParameters.setWhereClause("OBJECTID_1 =" + idVungQH);
                try {
                    List<FeatureQueryResult> outFields = new ArrayList<>();

                    final ListenableFuture<FeatureQueryResult> future = mServiceFeatureTableMauQH.queryFeaturesAsync(queryParameters);

                    future.addDoneListener(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                //create a feature collection table from the query results
                                FeatureCollectionTable featureCollectionTable = new FeatureCollectionTable(future.get());

                                //create a feature collection from the above feature collection table
                                FeatureCollection featureCollection = new FeatureCollection();
                                featureCollection.getTables().add(featureCollectionTable);

                                FeatureQueryResult result = future.get();
                                Iterator<Feature> resultIterator = result.iterator();

                                if (resultIterator.hasNext()) {
                                    Feature feature1 = resultIterator.next();
                                    Envelope envelope = feature1.getGeometry().getExtent();
                                    mMapView.setViewpointGeometryAsync(envelope, 10);
                                    mFeatureLayerMauQH.selectFeature(feature1);
                                    //handleViewPageThuaDat();
                                } else {
                                }
                            } catch (Exception e) {

                                Log.e(getResources().getString(R.string.app_name),
                                        "Error=" + e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {

                }
            }

        }
        catch (Exception ex) {

        }
    }


    public  void UpdateMaXa(){
        setupMap();
        ZoomToXa(mMapView,this._MaXa);

        System.out.println("Click MaXa"+this._MaXa);

    }
    public  void UpdateMapVeTinh(){
        System.out.println("Click IMAGERY_WITH_LABELS");

        basemapType = Basemap.Type.IMAGERY_WITH_LABELS;
        mMapView.getMap().setBasemap(Basemap.createImageryWithLabelsVector());

    }
    public  void UpdateMapDuong(){
        System.out.println("Click OPEN_STREET_MAP");

        basemapType = Basemap.Type.OPEN_STREET_MAP;
        mMapView.getMap().setBasemap(Basemap.createOpenStreetMap());

    }
    public String GetURLMap()
    {
        String URL="";
        switch(this._MaXa) {
            case 26377:
                URL="http://datdai.stnmt.dongnai.gov.vn/arcgis/rest/services/DOTHIBIENHOA/26377/MapServer/0";
                break;
            case 26011:
                URL="http://stnmt.dongnai.gov.vn:8080/arcgis/rest/services/75731/26011/MapServer/1";
                break;
            default:
                URL="http://stnmt.dongnai.gov.vn:8080/arcgis/rest/services/75731/26377/MapServer/1";
        }
        return URL;
    }

    public void setSoThua(String st) {
        this.soThua = st;
    }

    public void setMaXa(int st) {
        this._MaXa = st;
    }

    public void ZoomToGPS() {
        try {
            lDisplayManager.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
            if (!lDisplayManager.isStarted())
            {
                lDisplayManager.startAsync();
            }
            lDisplayManager.isShowLocation();



        } catch (Exception ex) {
            Log.i("error",ex.getMessage());
            //ex.printStackTrace();
        }
    }

    @Override
    public void onHostResume() {
        mMapView.resume();

    }

    @Override
    public void onHostPause() {
        mMapView.pause();

    }

    @Override
    public void onHostDestroy() {
        mMapView.dispose();
        if (getContext() instanceof ReactContext) {
            ((ReactContext) getContext()).removeLifecycleEventListener(this);
        }
    }
}