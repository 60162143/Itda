package com.example.itda.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    public RecyclerView CategoryRv;
    public RecyclerView MainStoreRv;

    public LinearLayoutManager llm;
    public GridLayoutManager glm;

    public ArrayList<mainCategoryData> category = new ArrayList<>();
    public ArrayList<mainStoreData> main_store = new ArrayList<>();

    public CategoryRvAdapter CategoryAdapter;
    public MainStoreRvAdapter MainStoreAdapter;

    public static RequestQueue requestQueue;
    final static private String CATEGORY_URL = "http://no2955922.ivyro.net/store/getCategory.php";
    final static private String MAINSTORE_URL = "http://no2955922.ivyro.net/store/getMainStore.php";
    final static private String MAIN_URL = "http://no2955922.ivyro.net";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CategoryRv = root.findViewById(R.id.main_category_rv);
        MainStoreRv = root.findViewById(R.id.main_store_rv);

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        //카테고리 만드는 메소드
        makeCategory();

        //메인화면 가게 리스트 만드는 메소드
        makeMainStore(new HashMap<String, String>());

        return root;
    }

    public void makeCategory(){
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        CategoryRv.setHasFixedSize(true);
        CategoryRv.setLayoutManager(llm);

        StringRequest CategoryRequest = new StringRequest(Request.Method.GET, CATEGORY_URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray categoryArr = jsonObject.getJSONArray("category");
                    System.out.println("size : " + categoryArr.length());
                    for(int i = 0; i < categoryArr.length(); i++){
                        JSONObject objectInArray = categoryArr.getJSONObject(i);
                        mainCategoryData mainCategory = new mainCategoryData(objectInArray.getInt("categoryId")
                                , objectInArray.getString("categoryNm")
                                , MAIN_URL + objectInArray.getString("imagePath")
                                , objectInArray.getInt("imageId"));
                        category.add(mainCategory);
                    }
                    CategoryAdapter = new CategoryRvAdapter();
                    CategoryAdapter.setCategories(category);
                    CategoryRv.setAdapter(CategoryAdapter);
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
        CategoryRequest.setShouldCache(false);
        requestQueue.add(CategoryRequest);
    }

    public void makeMainStore(Map<String, String> param){
        glm = new GridLayoutManager(getActivity(), 2);

        MainStoreRv.setHasFixedSize(true);
        MainStoreRv.setLayoutManager(glm);
        StringRequest MainStoreRequest = new StringRequest(Request.Method.GET, MAINSTORE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray mainStoreArr = jsonObject.getJSONArray("store");
                    for(int i = 0; i < mainStoreArr.length(); i++){
                        JSONObject object = mainStoreArr.getJSONObject(i);
                        mainStoreData mainStore = new mainStoreData(object.getInt("storeId")
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

                        main_store.add(mainStore);
                    }
                    MainStoreAdapter = new MainStoreRvAdapter(getActivity());
                    MainStoreAdapter.setStores(main_store);
                    MainStoreRv.setAdapter(MainStoreAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse : " + String.valueOf(error));
            }
        })/* {
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
                //php로 설정값을 보낼 수 있음
                return map;
            }
        }*/;

        MainStoreRequest.setShouldCache(false);
        requestQueue.add(MainStoreRequest);
    }

}