package com.example.itda.ui.info;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.itda.ui.home.mainStoreData;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itda.R;

public class InfoActivity extends Activity {
    private TextView InfoMainStoreName; //최상단 가게 이름
    private TextView InfoStoreName;     //가게 이름
    private TextView InfoStarScore;     //가게 별점
    private TextView InfoInformation;   //가게 간단한 설명
    private TextView InfoHashtag;       //가게 해시태그
    private TextView InfoWorkingTime;   //가게 운영 시간
    private TextView InfoParking;       //가게 주차 가능 여부
    private TextView InfoAddress;       //가게 주소
    private TextView InfoPhotoTitle;    //사진 타이틀 가게 이름
    private TextView InfoReviewTitle;   //리뷰 타이틀 가게 이름
    private TextView InfoReviewPlusBtn;   //리뷰 쓰기 버튼

    private Button InfoMenuPlusBtn;     //메뉴 더보기 버튼
    private Button InfoPhotoPlusBtn;    //사진 더보기 버튼
    private Button InfoPaymentBtn;      //결제하기 버튼

    private ImageView InfoStoreImage;   //가게 메인 이미지

    private ImageButton InfoBackIc;     //최상단 뒤로가기 버튼
    private ImageButton InfoCallIc;     //최상단 전화하기 버튼
    private ImageButton InfoBookmarkIc; //최상단 찜하기 버튼

    private ListView InfoMenuLv;        //메뉴 리스트뷰(최대 3개까지만 보여짐), 나머지는 더보기 버튼을 누른 후

    private RecyclerView InfoCollaboRv; //협업 리사이클러뷰
    private RecyclerView InfoPhotoRv;   //사진 리사이클러뷰
    private RecyclerView InfoReviewRv;  //리뷰 리사이클러뷰

    private mainStoreData Store;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //텍스트뷰 정의
        InfoMainStoreName = findViewById(R.id.info_main_store_name);
        InfoStoreName = findViewById(R.id.info_store_name);
        InfoStarScore = findViewById(R.id.info_star_score);
        InfoInformation = findViewById(R.id.info_information);
        InfoHashtag = findViewById(R.id.info_hashtag);
        InfoWorkingTime = findViewById(R.id.info_working_time);
        InfoParking = findViewById(R.id.info_parking);
        InfoAddress = findViewById(R.id.info_address);
        InfoPhotoTitle = findViewById(R.id.info_photo_title);
        InfoReviewTitle = findViewById(R.id.info_review_title);

        //버튼 정의
        InfoMenuPlusBtn = findViewById(R.id.info_menu_plus_btn);
        InfoPhotoPlusBtn = findViewById(R.id.info_photo_plus_btn);
        InfoReviewPlusBtn = findViewById(R.id.info_review_plus_btn);
        InfoPaymentBtn = findViewById(R.id.info_payment_btn);

        //이미지 정의
        InfoStoreImage = findViewById(R.id.info_store_image);

        //최상단 아이콘 정의
        InfoBackIc = findViewById(R.id.info_back_ic);
        InfoCallIc = findViewById(R.id.info_call_ic);
        InfoBookmarkIc = findViewById(R.id.info_bookmark_ic);

        //메뉴 리스트 정의
        InfoMenuLv = findViewById(R.id.info_menu_lv);

        //리사이클러뷰 정의
        InfoCollaboRv = findViewById(R.id.info_collabo_rv);
        InfoPhotoRv = findViewById(R.id.info_photo_rv);
        InfoReviewRv = findViewById(R.id.info_review_rv);

        Store = getIntent().getParcelableExtra("Store");

        //텍스트 설정
        InfoMainStoreName.setText(Store.getStoreName());
        InfoStoreName.setText(Store.getStoreName());
        InfoStarScore.setText(String.valueOf(Store.getStoreScore()));
        InfoInformation.setText(Store.getStoreInfo());
        InfoWorkingTime.setText(Store.getStoreWorkingTime());
        if(TextUtils.isEmpty(Store.getStoreParking())){
            InfoParking.setText("업데이트 예정");
        }else {
            InfoParking.setText(Store.getStoreParking());
        }

        //뒤로가기 버튼 클릭 리스너 입니다.
        InfoBackIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

