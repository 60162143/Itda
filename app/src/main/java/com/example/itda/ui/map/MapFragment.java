package com.example.itda.ui.map;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.itda.R;
import com.example.itda.ui.home.mainStoreData;
import com.example.itda.ui.info.InfoActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {

    private ViewGroup mapViewContainer;
    public LocationManager lm;
    private View root;
    private MapView mapView;
    private ImageButton mapGPSBtn;
    private EditText mapSchText;
    private ImageButton mapRefreshBtn;
    public LinearLayoutManager llm;
    public InputMethodManager imm;
    public RecyclerView mapStoreRv;
    public MapRvAdapter mapStoreAdapter;
    public ArrayList<MapStoreData> map_store = new ArrayList<>();       // ?????? ??? ?????? ??????
    public ArrayList<mainStoreData> select_store = new ArrayList<>();     // ????????? ????????? Detail ??????
    public static RequestQueue requestQueue;
    final static private String MAPSTORE_URL = "http://no2955922.ivyro.net/store/getMapStore.php";
    final static private String MAINSTORE_URL = "http://no2955922.ivyro.net/store/getMainStore.php";
    final static private String MAIN_URL = "http://no2955922.ivyro.net";
    boolean isTrackingMode = false;     // ?????? ????????? ???????????? ???????????? Flag

    // =========== ?????????????????? addOnItemTouchListener ??? ?????? ?????? ================
    boolean firstDragFlag = true;      // ?????????????????? ????????? ???????????? ???????????? Flag
    boolean dragFlag = false;           // ?????? ????????? ??????????????? ???????????? Flag
    float startXPosition = 0;           // ?????? ???????????? ???????????? X(??????)??????
    // =========================================================================

    Intent intent;                      // ?????? ?????? ????????? ?????? ??????
    //?????? ?????? ?????? ??????
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(root.getContext(), "?????? ????????? ?????? ????????? ???????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
        }
    };

    //?????? ?????? ?????? ??????
    private void checkPermissions() {
        // ????????????(??????????????? 6.0) ?????? ?????? ??????
        TedPermission.with(root.getContext())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("?????? ???????????? ???????????? ?????? ????????? ???????????????")
                .setDeniedMessage("????????? ???????????? ??????????????? ???????????????...\n [??????] > [??????] ?????? ???????????? ?????????????????????.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                        //android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        //android.Manifest.permission.WRITE_EXTERNAL_STORAGE // ??????, ??????, ?????????, ?????? ????????? ??????
                )
                .check();
    }

    // ??? ??????
    private void initView() {
        mapView = new MapView(root.getContext());

        mapViewContainer = (ViewGroup) root.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setPOIItemEventListener(this);

        mapGPSBtn = (ImageButton) root.findViewById((R.id.gps_button));
        mapSchText = (EditText) root.findViewById((R.id.search_map_store));
        mapRefreshBtn = (ImageButton) root.findViewById((R.id.refresh_button));
        mapStoreRv = root.findViewById(R.id.map_store_rv);

        // ?????????
        imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);

        // ======== ?????????????????? MOVE, UP, DOWN ????????? ????????? ===================


        mapStoreRv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // ???????????? X??? ?????? ????????? ?????? ??????
                Display display  = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int display_width = size.x;

                switch(e.getAction()){
                    case MotionEvent.ACTION_MOVE:{
                        dragFlag = true;
                        if(firstDragFlag){
                            startXPosition = e.getX();
                            firstDragFlag = false;
                        }
                        break;

                    }
                    case MotionEvent.ACTION_UP : {
                        float endPosition = e.getX();
                        firstDragFlag = true;

                        double sel_lat;     // ?????? Position??? ??????
                        double sel_lon;     // ?????? Position??? ??????

                        if(dragFlag){
                            // ???????????? ?????? ??????, ?????? ???????????? ???????????? ??? ?????? ?????? ?????? Position ?????? ??????
                            if((startXPosition < endPosition) && (endPosition - startXPosition) > 10 && endPosition > (float)display_width / 2){        // ???????????? ???????????? ( ?????? Position ?????? )
                                mapStoreRv.smoothScrollToPosition(llm.findFirstVisibleItemPosition());
                                // ?????? 0??? Position??? ?????? ?????? ????????? ??????
                                sel_lat = map_store.get(llm.findFirstVisibleItemPosition()).getMapStoreLatitude();
                                sel_lon = map_store.get(llm.findFirstVisibleItemPosition()).getMapStoreLongitude();

                                MapPoint selectMapPoint = MapPoint.mapPointWithGeoCoord(sel_lat, sel_lon);

                                mapView.setMapCenterPoint(selectMapPoint, true);

                                mapView.selectPOIItem(mapView.getPOIItems()[llm.findFirstVisibleItemPosition()], true);
                            }else if((startXPosition > endPosition) && (startXPosition - endPosition) > 10 && endPosition < (float)display_width / 2){  // ??????????????? ???????????? ( ?????? Position ?????? )
                                mapStoreRv.smoothScrollToPosition(llm.findLastVisibleItemPosition());
                                // ?????? 0??? Position??? ?????? ?????? ????????? ??????
                                sel_lat = map_store.get(llm.findLastVisibleItemPosition()).getMapStoreLatitude();
                                sel_lon = map_store.get(llm.findLastVisibleItemPosition()).getMapStoreLongitude();

                                MapPoint selectMapPoint = MapPoint.mapPointWithGeoCoord(sel_lat, sel_lon);

                                mapView.setMapCenterPoint(selectMapPoint, true);

                                mapView.selectPOIItem(mapView.getPOIItems()[llm.findLastVisibleItemPosition()], true);
                            }else if((startXPosition < endPosition) && (endPosition - startXPosition) > 10){            // ???????????? ???????????? ( ?????? X )
                                mapStoreRv.smoothScrollToPosition(llm.findLastVisibleItemPosition());
                                // ?????? 0??? Position??? ?????? ?????? ????????? ??????
                                sel_lat = map_store.get(llm.findLastVisibleItemPosition()).getMapStoreLatitude();
                                sel_lon = map_store.get(llm.findLastVisibleItemPosition()).getMapStoreLongitude();

                                MapPoint selectMapPoint = MapPoint.mapPointWithGeoCoord(sel_lat, sel_lon);

                                mapView.setMapCenterPoint(selectMapPoint, true);

                                mapView.selectPOIItem(mapView.getPOIItems()[llm.findLastVisibleItemPosition()], true);
                            }else if((startXPosition > endPosition) && (startXPosition - endPosition) > 10){        // ??????????????? ???????????? ( ?????? X )
                                mapStoreRv.smoothScrollToPosition(llm.findFirstVisibleItemPosition());
                                // ?????? 0??? Position??? ?????? ?????? ????????? ??????
                                sel_lat = map_store.get(llm.findFirstVisibleItemPosition()).getMapStoreLatitude();
                                sel_lon = map_store.get(llm.findFirstVisibleItemPosition()).getMapStoreLongitude();

                                MapPoint selectMapPoint = MapPoint.mapPointWithGeoCoord(sel_lat, sel_lon);

                                mapView.setMapCenterPoint(selectMapPoint, true);

                                mapView.selectPOIItem(mapView.getPOIItems()[llm.findFirstVisibleItemPosition()], true);
                            }else{
                                // GET ?????? ???????????? ??????
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);
                                String selectStoreURL = String.format(MAINSTORE_URL + "?storeId=%s",map_store.get(position).getMapStoreId());

                                // ?????????????????? ?????? ??? ?????? ????????? ????????????
                                StringRequest selectStoreRequest = new StringRequest(Request.Method.GET, selectStoreURL, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray mainStoreArr = jsonObject.getJSONArray("store");

                                            JSONObject object = mainStoreArr.getJSONObject(0);

                                            mainStoreData selectStore = new mainStoreData(object.getInt("storeId")
                                                        , object.getString("storeName")
                                                        , object.getString("storeAddress")
                                                        , object.getString("storeParking")
                                                        , object.getDouble("storeLatitude")
                                                        , object.getDouble("storeLongitude")
                                                        , object.getString("storeNumber")
                                                        , object.getString("storeInfo")
                                                        , object.getInt("storeCategoryId")
                                                        , MAIN_URL + object.getString("storeThumbnailPath")
                                                        , object.getDouble("storeScore")
                                                        , object.getString("storeWorkingTime"));

                                            select_store = new ArrayList<>();
                                            select_store.add(selectStore);

                                            intent = new Intent(getActivity(), InfoActivity.class);
                                            intent.putExtra("Store", (Parcelable) select_store.get(0));
                                            startActivity(intent);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("TAG", "onErrorResponse : " + String.valueOf(error));
                                    }
                                });

                                selectStoreRequest.setShouldCache(false);
                                requestQueue.add(selectStoreRequest);
                            }
                        }
                        startXPosition = 0.0f;
                        dragFlag = false;

                        break;
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        // =================================================================

        // GPS ?????? ?????? ?????????
        mapGPSBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //????????? ????????? ?????? ?????? ???????????? ??????????????? ??????, ????????? ?????? ?????????????????? ???????????? ??????????????? ?????? ??????
                System.out.println("click");
                System.out.println(isTrackingMode);
                lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Location loc_Current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null ? lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) : lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                double cur_lat = loc_Current.getLatitude();
                double cur_lon = loc_Current.getLongitude();

                MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(cur_lat, cur_lon);
                //??? ????????? ?????? ?????? ??????
                mapView.setMapCenterPoint(currentMapPoint, true);

                if (!isTrackingMode) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                    isTrackingMode = true;
                }else{
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    isTrackingMode = false;
                }
            }
        });

        // ?????? EditText Key Press Listener
        mapSchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    mapSchText.setSelection(mapSchText.length());
                    // ?????? ?????? ?????? ??? ?????????????????? ????????? ????????????
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("schText", mapSchText.getText().toString());
                    getMapStoreData(param);
                    return true;
                }
                return false;
            }
        });

        // ?????? EditText ????????? ????????? ????????????
        mapSchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String schText = textView.getText().toString();

/*                if(schText.isEmpty()){
                    Toast.makeText(getContext(), "????????? ??????????????????.",Toast.LENGTH_SHORT).show();
                    return true;
                }*/
                if(EditorInfo.IME_ACTION_SEARCH == actionId){
                    //mapSchText.clearFocus(); //?????? ????????? ???????????? ??????
                    imm.hideSoftInputFromWindow(mapSchText.getWindowToken(),0); //????????? ?????????.
                    return false;
                }
                return true;
            }
        });

        mapRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapSchText.setText("");
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        mapView.setZoomLevel(1, true);

        // ??? ???
        mapView.zoomIn(true);

        // ??? ??????
        mapView.zoomOut(true);
    }

    // ?????? ????????? ???????????????
    public void getMapStoreData(Map<String, String> param){
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mapStoreRv.setHasFixedSize(true);
        mapStoreRv.setLayoutManager(llm);
        mapView.removeAllPOIItems();
        map_store = new ArrayList<>();

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //?????? ?????? ??????, ??????
        Location loc_Current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null ? lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) : lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double cur_lat = loc_Current.getLatitude();
        double cur_lon = loc_Current.getLongitude();

        Location cur_loc = new Location("cur_loc");
        cur_loc.setLatitude(cur_lat);
        cur_loc.setLongitude(cur_lon);

        // GET ?????? ???????????? ??????
        String mapStoreURL = MAPSTORE_URL;
        if(!param.isEmpty()){
            mapStoreURL = String.format(MAPSTORE_URL + "?schText=%s",param.get("schText"));
        }

        StringRequest MapStoreRequest = new StringRequest(Request.Method.GET, mapStoreURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray mapStoreArr = jsonObject.getJSONArray("store");

                    if(mapStoreArr.length() > 0){
                        for(int i = 0; i < mapStoreArr.length(); i++){
                            JSONObject object = mapStoreArr.getJSONObject(i);

                            // ?????? ???????????? ?????? ????????? ?????? ????????? ?????? ??????
                            Location point = new Location(object.getString("storeName"));
                            point.setLatitude(object.getDouble("storeLatitude"));
                            point.setLongitude(object.getDouble("storeLongitude"));

                            MapStoreData mapStore = new MapStoreData(object.getInt("storeId")
                                    , object.getString("storeName")
                                    , MAIN_URL + object.getString("storeThumbnailPath")
                                    , Float.parseFloat(object.getString("storeScore"))
                                    , object.getDouble("storeLatitude")
                                    , object.getDouble("storeLongitude")
                                    , cur_loc.distanceTo(point) / 1000
                                    , object.getString("storeInfo")
                                    , object.getString("storeHashTag"));

                            map_store.add(mapStore);

                            // ????????? ?????? ?????? ?????? ??????
                            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(map_store.get(i).getMapStoreLatitude(), map_store.get(i).getMapStoreLongitude());
                            // ?????? ????????? ???????????? ??????
                            MapPOIItem marker = new MapPOIItem();

                            // ?????? ?????? ??? ????????? ?????? ???
                            marker.setItemName(map_store.get(i).getMapStoreName());
                            // ??? ????????? ??? ????????????
                            marker.setTag(i);
                            // ????????? ???????????? ??? ????????? ??????
                            marker.setMapPoint(MARKER_POINT);
                            //  (?????? ???)???????????? ???????????? BluePin ?????? ????????? ???.
                            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                            // (?????? ???) ????????? ???????????????, ???????????? ???????????? RedPin ?????? ??????.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                            // ???????????? ?????? ???????????? ???????????? ???????????? ?????? ??????(????????? ??????)

                            mapView.addPOIItem(marker);
                        }


                        // ?????? 0??? Position??? ?????? ?????? ????????? ??????
                        double first_lat = map_store.get(0).getMapStoreLatitude();
                        double first_lon = map_store.get(0).getMapStoreLongitude();

                        MapPoint firstMapPoint = MapPoint.mapPointWithGeoCoord(first_lat, first_lon);

                        mapView.setMapCenterPoint(firstMapPoint, true);

                        mapView.selectPOIItem(mapView.getPOIItems()[0], true);

                    }else{
                        Toast.makeText(getContext(), "?????? ????????? ????????????.",Toast.LENGTH_SHORT).show();
                    }

                    mapStoreAdapter = new MapRvAdapter(getActivity());
                    mapStoreAdapter.setStores(map_store);
                    mapStoreAdapter.setMapView(mapView);
                    mapStoreRv.setAdapter(mapStoreAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse : " + String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                if(param.isEmpty()){
                    map.put("schText", "");
                }else{
                    map.put("schText", param.get("schText"));
                }

                System.out.println("############");
                System.out.println(map.get("schText"));
                //php??? ???????????? ?????? ??? ??????
                return map;
            }
        };

        MapStoreRequest.setShouldCache(false);
        requestQueue.add(MapStoreRequest);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.example.itda.ui.map.MapViewModel mapViewModel = new ViewModelProvider(this).get(com.example.itda.ui.map.MapViewModel.class);
        root = inflater.inflate(R.layout.fragment_map, container, false);

        checkPermissions();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                initView();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(root.getContext(), "?????? ????????? ?????? ????????? ???????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
            }
        };

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

    @Override
    public void onCurrentLocationUpdate(MapView mmapView, MapPoint mmapPoint, float v) {

        MapPoint.GeoCoordinate mapPointGeo = mmapPoint.getMapPointGeoCoord();
        System.out.println(String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, v));
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //??? ????????? ?????? ?????? ??????
        mapView.setMapCenterPoint(currentMapPoint, true);
        //??????????????? ?????? ?????? ??????
        double mCurrentLat = mapPointGeo.latitude;
        double mCurrentLng = mapPointGeo.longitude;
        Log.d("curLocation", "???????????? => " + mCurrentLat + "  " + mCurrentLng);

        //????????? ????????? ?????? ?????? ???????????? ??????????????? ??????, ????????? ?????? ?????????????????? ???????????? ??????????????? ?????? ??????
        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }

        // ?????? ?????? ?????? ??? ?????????????????? ????????? ????????????
        getMapStoreData(new HashMap<String, String>());
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    // mapView POI Click Event
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        mapStoreRv.smoothScrollToPosition(mapPOIItem.getTag());
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
