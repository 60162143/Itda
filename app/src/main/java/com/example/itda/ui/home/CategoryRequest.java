package com.example.itda.ui.home;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CategoryRequest extends StringRequest {

    //서버 URL 설정(PHP 파일 연동)
    final static private String PHP_URL = "127.0.0.1/category.php";
    private Map<String, String> map;

    public CategoryRequest(String ImageName, String ImagePath, Response.Listener<String> listener){
        super(Method.GET, PHP_URL, listener, null);

        map = new HashMap<>();
        map.put("ImageName", ImageName);
        map.put("ImagePath", ImagePath);
    }

    public CategoryRequest(Response.Listener<String> listener){
        super(Method.GET, PHP_URL, listener, null);
        Log.d("msg", "여기는 categoryRequest 입니다.");
        //Log.d("msgg", );

    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
