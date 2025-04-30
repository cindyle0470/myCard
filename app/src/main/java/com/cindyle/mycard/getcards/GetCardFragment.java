package com.cindyle.mycard.getcards;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cindyle.mycard.bean.PlatformData;
import com.cindyle.mycard.room.RecordBean;
import com.example.mycard.R;
import com.cindyle.mycard.room.RecordDb;
import com.cindyle.mycard.tools.DataInfo;
import com.cindyle.mycard.tools.PhyImgUtils;
import com.cindyle.mycard.tools.RandomUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class GetCardFragment extends Fragment {
    public GetCardViewModel mVModel;
    private View view;
    private int platform;
    // 宮位 layout
    private ConstraintLayout platform1, platform2, platform3;
    // 抽取宮位
    private ImageView imgPhy1, imgPhy2, imgPp1, imgPp2, imgOther, imgStar, imgGod, imgDoor, imgSpace;
    private ImageView imgPhy21, imgPhy22, imgPp21, imgPp22, imgOther2, imgStar2, imgGod2, imgDoor2, imgSpace2;
    private ImageView imgPhy31, imgPhy32, imgPp31, imgPp32, imgOther3, imgStar3, imgGod3, imgDoor3, imgSpace3;
    private boolean isHorse1Show, isHorse2Show, isHorse3Show;
    private ConstraintLayout bigImgLayout;
    private String currentTime;
    private TextView btnGet, btnSave, btnAdd, btnClear, groupNum2, groupNum3;
    private TextView txtTime, txtQ, txtRecord;
    private TextInputLayout qInput, rInput;
    private EditText edtQ;
    private EditText edtRecord;  // 解析記錄
    private ImageView imgBig, imgCancel;
    private NestedScrollView scrollView;
    private List<ImageView> plat1, plat2, plat3;
    private List<Integer> platImg1, platImg2, platImg3;   // 每組牌的 resource id
    private PlatformData platformData, platformData2, platformData3;
    private RecordDb database;
    public MutableLiveData<Long> cardsId = new MutableLiveData<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_getcard, container, false);
        mVModel = new ViewModelProvider(this).get(GetCardViewModel.class);

        findViews(view);

        plat1 = Arrays.asList(imgPhy1, imgPhy2, imgPp1, imgPp2, imgOther, imgStar, imgGod, imgDoor, imgSpace);
        plat2 = Arrays.asList(imgPhy21, imgPhy22, imgPp21, imgPp22, imgOther2, imgStar2, imgGod2, imgDoor2, imgSpace2);
        plat3 = Arrays.asList(imgPhy31, imgPhy32, imgPp31, imgPp32, imgOther3, imgStar3, imgGod3, imgDoor3, imgSpace3);

        return view;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 預設抽牌組數1
        setPlatform(1);

        setImgOnClick();
        imgOnclickIsEnable(false);
        setBigImgCancel();

        // init DB
        database = Room.databaseBuilder(getActivity(), RecordDb.class, "data_cards.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        btnGet.setOnClickListener(v -> {
            getCurrentTime();
            imgOnclickIsEnable(true);
            btnSave.setEnabled(true);
            btnSave.setBackground(getResources().getDrawable(R.drawable.tv_click_style));
            btnAdd.setVisibility(View.VISIBLE);
            btnClear.setVisibility(View.VISIBLE);
            btnGet.setVisibility(View.INVISIBLE);
            platformData = new PlatformData();
            platform = 1;
            getCards();
        });

        btnAdd.setOnClickListener(v -> {
            platform = platform + 1;
            if (platform == 3){
                groupNum3.setText("第三組牌卡");
                btnAdd.setVisibility(View.GONE);
                btnClear.setVisibility(View.VISIBLE);
                platformData3 = new PlatformData();
                getCards();
                setPlatform(3);
            } else {
                groupNum2.setText("第二組牌卡");
                platformData2 = new PlatformData();
                btnClear.setVisibility(View.VISIBLE);
                getCards();
                setPlatform(2);
            }
        });

        btnClear.setOnClickListener(v -> {
            clearPage();
        });

        btnSave.setEnabled(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = System.currentTimeMillis();

                if (platformData2 == null){
                    setDefaultData(2);
                }

                if (platformData3 == null){
                    setDefaultData(3);
                }

                RecordBean data = new RecordBean(id, platform, currentTime, edtQ.getText().toString(),
                        platformData.phy1, platformData.phy2, platformData.pp1, platformData.pp2, platformData.star, platformData.god, platformData.door, platformData.space, platformData.other,
                        platformData2.phy1, platformData2.phy2, platformData2.pp1, platformData2.pp2, platformData2.star, platformData2.god, platformData2.door, platformData2.space, platformData2.other,
                        platformData3.phy1, platformData3.phy2, platformData3.pp1, platformData3.pp2, platformData3.star, platformData3.god, platformData3.door, platformData3.space, platformData3.other,
                        edtRecord.getText().toString());
                // 新增
                database.recordDao().insert(data);

                clearPage();
            }
        });

        cardsId.observe(getActivity(), id ->{
            if (id != 0 ) {
                RecordBean cardInfo = database.recordDao().getInfo(id);
                int platformOpen = checkPlatform(cardInfo);
                txtTime.setText(cardInfo.time);
                txtQ.setText(getString(R.string.question) + cardInfo.question);
                txtRecord.setText(getString(R.string.record) + cardInfo.record);
                openRecord(true);

                platformData = new PlatformData(cardInfo.phy1, cardInfo.phy2, cardInfo.pp1, cardInfo.pp2, cardInfo.star, cardInfo.god, cardInfo.door, cardInfo.space, cardInfo.other);
                platformData2 = new PlatformData(cardInfo.phy21, cardInfo.phy22, cardInfo.pp21, cardInfo.pp22, cardInfo.star2, cardInfo.god2, cardInfo.door2, cardInfo.space2, cardInfo.other2);
                platformData3 = new PlatformData(cardInfo.phy31, cardInfo.phy32, cardInfo.pp31, cardInfo.pp32, cardInfo.star3, cardInfo.god3, cardInfo.door3, cardInfo.space3, cardInfo.other3);

                btnGet.setVisibility(View.INVISIBLE);
                btnAdd.setVisibility(View.GONE);
                btnClear.setVisibility(View.VISIBLE);

                switch (platformOpen) {
                    case 1:
                        isHorse1Show = checkHorse(cardInfo.space);
                        getCardsImg(1);
                        setPlatform(1);
                        break;
                    case 2:
                        isHorse1Show = checkHorse(cardInfo.space);
                        isHorse2Show = checkHorse(cardInfo.space2);
                        getCardsImg(1);
                        getCardsImg(2);
                        setPlatform(2);
                        groupNum2.setText("第二組牌卡");
                        break;
                    case 3:
                        isHorse1Show = checkHorse(cardInfo.space);
                        isHorse2Show = checkHorse(cardInfo.space2);
                        isHorse3Show = checkHorse(cardInfo.space3);
                        getCardsImg(1);
                        getCardsImg(2);
                        getCardsImg(3);
                        setPlatform(3);
                        groupNum2.setText("第二組牌卡");
                        groupNum3.setText("第三組牌卡");
                        break;
                }
                imgOnclickIsEnable(true);

                qInput.setVisibility(View.GONE);
                rInput.setVisibility(View.GONE);
            }
        });

        clearHint(qInput);
        clearHint(rInput);
    }

    private void clearHint(TextInputLayout input) {
        input.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 0) {
                    input.setHintEnabled(false);
                }
            }
        });
    }

    private int checkPlatform(RecordBean cardInfo) {
        boolean is2 = cardInfo.door2 != 0 && cardInfo.god2 != 0 && cardInfo.star2 != 0;
        boolean is3 = cardInfo.door3 != 0 && cardInfo.god3 != 0 && cardInfo.star3 != 0;

        if (is2 && is3) {
            return 3;
        } else if(is2){
            return 2;
        } else {
            return 1;
        }
    }

    public void getCardInfo(long id){
        cardsId.postValue(id);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void clearPage() {
        imgOnclickIsEnable(false);

        setPlatform(1);
        platformData.dataClear();

        if (platformData2 != null) {
            platformData2.dataClear();
        }

        if (platformData3 != null) {
            platformData3.dataClear();
        }

        getCardsImg(1);
        btnGet.setText("抽牌");
        btnGet.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.GONE);

        btnSave.setEnabled(false);
        btnSave.setBackground(getResources().getDrawable(R.drawable.btn_unenable_style));

        btnAdd.setVisibility(View.GONE);

        edtQ.setText("");
        edtRecord.setText("");
        txtTime.setText("");
        txtQ.setText("");
        txtRecord.setText("");
        openRecord(false);

        rInput.setVisibility(View.VISIBLE);
        qInput.setVisibility(View.VISIBLE);
    }

    private void openRecord(boolean isOpen){
        if (isOpen){
            txtQ.setVisibility(View.VISIBLE);
            txtRecord.setVisibility(View.VISIBLE);

            edtQ.setVisibility(View.GONE);
            edtRecord.setVisibility(View.GONE);
        } else {
            edtQ.setVisibility(View.VISIBLE);
            edtRecord.setVisibility(View.VISIBLE);

            txtQ.setVisibility(View.GONE);
            txtRecord.setVisibility(View.GONE);
        }
    }

    private void setDefaultData(int platform){
        if(platform == 2){
            platformData2 = new PlatformData();
            platformData2.phy1 = 0;
            platformData2.phy2 = 0;
            platformData2.pp1 = 0;
            platformData2.pp2 = 0;
            platformData2.star = 0;
            platformData2.god = 0;
            platformData2.door = 0;
            platformData2.space = 0;
            platformData2.other = 0;
        } else {
            platformData3 = new PlatformData();
            platformData3.phy1 = 0;
            platformData3.phy2 = 0;
            platformData3.pp1 = 0;
            platformData3.pp2 = 0;
            platformData3.star = 0;
            platformData3.god = 0;
            platformData3.door = 0;
            platformData3.space = 0;
            platformData3.other = 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 抓手機系統時間
        getCurrentTime();
    }

    private void getCards(){
        //btnGet.setVisibility(View.INVISIBLE);
        // 天盤干：1   地盤干：2    天盤寄干：21、    地盤寄干：22
        getPhyCards(1);
        getPhyCards(2);
    }

    private int getRanom(int times){
        int cardNum = new RandomUtils().getRandomNum(times);
        return cardNum;
    }

    private void getNoneCards(){
        int cardNum = getRanom(3);
        Log.i(TAG, "getCardsImg_getNone: " + cardNum);
         //1：空亡   2：馬星   3：無
        switch (platform) {
            case 1:
                platformData.other = cardNum;
                break;
            case 2:
                platformData2.other = (platformData.other == 2)? 3: cardNum;
                break;
            case 3:
                platformData3.other = (platformData.other == 2 || platformData2.other == 2)? 3: cardNum;
                break;
        }

        getCardsImg(platform);
    }

    private void getCardsImg(int platform){
        switch (platform) {
            case 1:
                imgPhy1.setBackgroundResource(PhyImgUtils.getPhy(platformData.phy1));
                imgPhy2.setBackgroundResource(PhyImgUtils.getPhy(platformData.phy2));
                imgPp1.setBackgroundResource(PhyImgUtils.getPpy(platformData.pp1));
                imgPp2.setBackgroundResource(PhyImgUtils.getPpy(platformData.pp2));
                imgStar.setBackgroundResource(PhyImgUtils.getStar(platformData.star));
                imgGod.setBackgroundResource(PhyImgUtils.getGod(platformData.god));
                imgDoor.setBackgroundResource(PhyImgUtils.getDoor(platformData.door));
                imgSpace.setBackgroundResource(PhyImgUtils.getSpace(platformData.space));
                imgOther.setBackgroundResource(PhyImgUtils.getOther(platformData.other, isHorse1Show));

                platImg1 = Arrays.asList(PhyImgUtils.getPhy(platformData.phy1),
                        PhyImgUtils.getPhy(platformData.phy2),
                        PhyImgUtils.getPpy(platformData.pp1),
                        PhyImgUtils.getPpy(platformData.pp2),
                        PhyImgUtils.getOther(platformData.other, isHorse1Show),
                        PhyImgUtils.getStar(platformData.star),
                        PhyImgUtils.getGod(platformData.god),
                        PhyImgUtils.getDoor(platformData.door),
                        PhyImgUtils.getSpace(platformData.space));

                setImgLayout(platformData, 1);

                checkAndLog(1);
                break;
            case 2:
                imgPhy21.setBackgroundResource(PhyImgUtils.getPhy(platformData2.phy1));
                imgPhy22.setBackgroundResource(PhyImgUtils.getPhy(platformData2.phy2));
                imgPp21.setBackgroundResource(PhyImgUtils.getPpy(platformData2.pp1));
                imgPp22.setBackgroundResource(PhyImgUtils.getPpy(platformData2.pp2));
                imgStar2.setBackgroundResource(PhyImgUtils.getStar(platformData2.star));
                imgGod2.setBackgroundResource(PhyImgUtils.getGod(platformData2.god));
                imgDoor2.setBackgroundResource(PhyImgUtils.getDoor(platformData2.door));
                imgSpace2.setBackgroundResource(PhyImgUtils.getSpace(platformData2.space));
                imgOther2.setBackgroundResource(PhyImgUtils.getOther(platformData2.other, isHorse2Show));

                platImg2 = Arrays.asList(PhyImgUtils.getPhy(platformData2.phy1),
                        PhyImgUtils.getPhy(platformData2.phy2),
                        PhyImgUtils.getPpy(platformData2.pp1),
                        PhyImgUtils.getPpy(platformData2.pp2),
                        PhyImgUtils.getOther(platformData2.other, isHorse2Show),
                        PhyImgUtils.getStar(platformData2.star),
                        PhyImgUtils.getGod(platformData2.god),
                        PhyImgUtils.getDoor(platformData2.door),
                        PhyImgUtils.getSpace(platformData2.space));

                setImgLayout(platformData2, 2);
                checkAndLog(2);

                break;
            case 3:
                imgPhy31.setBackgroundResource(PhyImgUtils.getPhy(platformData3.phy1));
                imgPhy32.setBackgroundResource(PhyImgUtils.getPhy(platformData3.phy2));
                imgPp31.setBackgroundResource(PhyImgUtils.getPpy(platformData3.pp1));
                imgPp32.setBackgroundResource(PhyImgUtils.getPpy(platformData3.pp2));
                imgStar3.setBackgroundResource(PhyImgUtils.getStar(platformData3.star));
                imgGod3.setBackgroundResource(PhyImgUtils.getGod(platformData3.god));
                imgDoor3.setBackgroundResource(PhyImgUtils.getDoor(platformData3.door));
                imgSpace3.setBackgroundResource(PhyImgUtils.getSpace(platformData3.space));
                imgOther3.setBackgroundResource(PhyImgUtils.getOther(platformData3.other, isHorse3Show));

                platImg3 = Arrays.asList(PhyImgUtils.getPhy(platformData3.phy1),
                        PhyImgUtils.getPhy(platformData3.phy2),
                        PhyImgUtils.getPpy(platformData3.pp1),
                        PhyImgUtils.getPpy(platformData3.pp2),
                        PhyImgUtils.getOther(platformData3.other, isHorse3Show),
                        PhyImgUtils.getStar(platformData3.star),
                        PhyImgUtils.getGod(platformData3.god),
                        PhyImgUtils.getDoor(platformData3.door),
                        PhyImgUtils.getSpace(platformData3.space));

                setImgLayout(platformData3, 3);
                checkAndLog(3);
                break;
        }
    }

    private void checkAndLog(int platform) {
        if (platform == 1){
            String phy1 = platformData.phy1 != 10 && platformData.phy1 != 0 ? DataInfo.phy[platformData.phy1] : DataInfo.pp[platformData.pp1];
            String phy2 = platformData.phy2 != 10 && platformData.phy2 != 0 ? DataInfo.phy[platformData.phy2] : DataInfo.pp[platformData.pp2];
            String star = DataInfo.star[platformData.star];
            String god = DataInfo.god[platformData.god];
            String door = DataInfo.door[platformData.door];
            String space = DataInfo.space[platformData.space];
            String other = DataInfo.other[platformData.other];

            String info = phy1 + " , " + phy2 + " , " + star + " , " + god + " , " + door + " , " + space + " , " + other;
            String info2 = platformData.phy1 + " , " + platformData.phy2 + " , " + platformData.star + " , " + platformData.god + " , " + platformData.door + " , " + platformData.space + " , " + other;

            Log.i(TAG, "getCardsImg: " + info );
            Log.i(TAG, "getCardsImg: " + info2 + "寄干1:" + platformData.pp1 + "寄干2:" + platformData.pp2 );
        }

        if (platform == 2){
            String phy21 = platformData2.phy1 != 10 ? DataInfo.phy[platformData2.phy1] : DataInfo.pp[platformData2.pp1];
            String phy22 = platformData2.phy2 != 10 ? DataInfo.phy[platformData2.phy2] : DataInfo.pp[platformData2.pp2];
            String star2 = DataInfo.star[platformData2.star];
            String god2 = DataInfo.god[platformData2.god];
            String door2 = DataInfo.door[platformData2.door];
            String space2 = DataInfo.space[platformData2.space];
            String other2 = DataInfo.other[platformData2.other];

            String info2 = phy21 + " , " + phy22 + " , " + star2 + " , " + god2 + " , " + door2 + " , " + space2 + " , " + other2;
            Toast.makeText(getActivity(), info2, Toast.LENGTH_LONG).show();
            Log.i(TAG, "getCardsImg: " + info2);
        }

        if (platform == 3){

            String phy31 = platformData3.phy1 != 10 ? DataInfo.phy[platformData3.phy1] : DataInfo.pp[platformData3.pp1];
            String phy32 = platformData3.phy2 != 10 ? DataInfo.phy[platformData3.phy2] : DataInfo.pp[platformData3.pp2];
            String star3 = DataInfo.star[platformData3.star];
            String god3 = DataInfo.god[platformData3.god];
            String door3 = DataInfo.door[platformData3.door];
            String space3 = DataInfo.space[platformData3.space];
            String other3 = DataInfo.other[platformData3.other];

            String info3 = phy31 + " , " + phy32 + " , " + star3 + " , " + god3 + " , " + door3 + " , " + space3 + " , " + other3;
            Toast.makeText(getActivity(), info3, Toast.LENGTH_LONG).show();
            Log.i(TAG, "getCardsImg: " + info3);
        }

    }

    private void setImgLayout(PlatformData platformData, int platform) {
        if (platformData.pp1 != 0){
            openPpLayout(1, platform);
        } else {
            closePpLayout(1, platform);
        }

        if (platformData.pp2 != 0) {
            openPpLayout(2, platform);
        } else {
            closePpLayout(2, platform);
        }
    }

    private void getOtherCards(int kind){
        int cardNum = getRanom(8);
        Log.i(TAG, "getCardsImg_getOther: " + cardNum + ", kind = " + kind);
        // 星：1    神：2    門：3    宮：4
        if (kind == 1){
            switch (platform) {
                case 1:
                    platformData.star = cardNum;
                    break;
                case 2:
                    platformData2.star = cardNum;
                    break;
                case 3:
                    platformData3.star = cardNum;
                    break;
            }

            // 星：1    神：2    門：3    宮：4
            getOtherCards(2);
        }

        if (kind == 2){
            switch (platform) {
                case 1:
                    platformData.god = cardNum;
                    break;
                case 2:
                    platformData2.god = cardNum;
                    break;
                case 3:
                    platformData3.god = cardNum;
                    break;
            }
            getOtherCards(3);
        }

        if (kind == 3){
            switch (platform) {
                case 1:
                    platformData.door = cardNum;
                    break;
                case 2:
                    platformData2.door = cardNum;
                    break;
                case 3:
                    platformData3.door = cardNum;
                    break;
            }
            getOtherCards(4);
        }

        if (kind == 4){
            switch (platform) {
                case 1:
                    platformData.space = cardNum;
                    isHorse1Show = checkHorse(cardNum);
                    break;
                case 2:
                    platformData2.space = cardNum;
                    isHorse2Show = checkHorse(cardNum);
                    break;
                case 3:
                    platformData3.space = cardNum;
                    isHorse3Show = checkHorse(cardNum);
                    break;
            }

            // 四害
            getNoneCards();
        }
    }

    private boolean checkHorse(int cardNum) {
        boolean isHorseShow = cardNum == 1 || cardNum == 5 || cardNum == 7 || cardNum == 8;
        return isHorseShow;
    }

    private void getPhyCards(int kind){
        int cardNum = getRanom(10);
        Log.i(TAG, "getCardsImg_getPhy: " + cardNum + ", kind = " + kind);
        // 天盤干
        if (kind == 1){
            if (cardNum == 10) {
                getPpyCards(21);
                Log.i(TAG, "getPp1Cards: "+ cardNum);
            } else {
                Log.i(TAG, "getPhyCards: "+ cardNum);
                switch (platform) {
                    case 1:
                        platformData.phy1 = cardNum;
                        break;
                    case 2:
                        platformData2.phy1 = cardNum;
                        break;
                    case 3:
                        platformData3.phy1 = cardNum;
                        break;
                }
            }
        } else {
            // 地盤干
            if (cardNum == 10) {
                getPpyCards(22);
                Log.i(TAG, "getPp2Cards: "+ cardNum);
            } else {
                switch (platform) {
                    case 1:
                        platformData.phy2 = cardNum;
                        break;
                    case 2:
                        platformData2.phy2 = cardNum;
                        break;
                    case 3:
                        platformData3.phy2 = cardNum;
                        break;
                }
            }

            // 星：1    神：2    門：3    宮：4
            getOtherCards(1);
        }


    }

    // 寄干  天盤寄干：21    地盤寄干：22
    private void getPpyCards(int kind){
        int cardNum = getRanom(9);
        Log.i(TAG, "getCardsImg_getPpy: " + cardNum + ", kind = " + kind);
        if (kind == 21){
            switch (platform) {
                case 1:
                    platformData.pp1 = cardNum;
                    break;
                case 2:
                    platformData2.pp1 = cardNum;
                    break;
                case 3:
                    platformData3.pp1 = cardNum;
                    break;
            }
        } else {
            switch (platform) {
                case 1:
                    platformData.pp2 = (platformData.pp1 != 0)? platformData.pp1 : cardNum;
                    break;
                case 2:
                    platformData2.pp2 = (platformData2.pp1 != 0)? platformData2.pp1 : cardNum;
                    break;
                case 3:
                    platformData3.pp2 = (platformData3.pp1 != 0)? platformData3.pp1 : cardNum;
                    break;
            }
        }

    }

    // 寄干界面 kind  天盤寄干：1    地盤寄干：2
    private void openPpLayout(int kind, int platform){
        switch (platform){
            case 1:
                if (kind == 1) {
                    imgPhy1.setVisibility(View.INVISIBLE);
                    imgPp1.setVisibility(View.VISIBLE);
                } else {
                    imgPhy2.setVisibility(View.INVISIBLE);
                    imgPp2.setVisibility(View.VISIBLE);
                }
                break;

            case 2:
                if (kind == 1) {
                    imgPhy21.setVisibility(View.INVISIBLE);
                    imgPp21.setVisibility(View.VISIBLE);
                } else {
                    imgPhy22.setVisibility(View.INVISIBLE);
                    imgPp22.setVisibility(View.VISIBLE);
                }
                break;

            case 3:
                if (kind == 1) {
                    imgPhy31.setVisibility(View.INVISIBLE);
                    imgPp31.setVisibility(View.VISIBLE);
                } else {
                    imgPhy32.setVisibility(View.INVISIBLE);
                    imgPp32.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void closePpLayout(int kind, int platform){
        switch (platform){
            case 1:
                if (kind == 1) {
                    imgPhy1.setVisibility(View.VISIBLE);
                    imgPp1.setVisibility(View.INVISIBLE);
                } else {
                    imgPhy2.setVisibility(View.VISIBLE);
                    imgPp2.setVisibility(View.INVISIBLE);
                }
                break;

            case 2:
                if (kind == 1) {
                    imgPhy21.setVisibility(View.VISIBLE);
                    imgPp21.setVisibility(View.INVISIBLE);
                } else {
                    imgPhy22.setVisibility(View.VISIBLE);
                    imgPp22.setVisibility(View.INVISIBLE);
                }
                break;

            case 3:
                if (kind == 1) {
                    imgPhy31.setVisibility(View.VISIBLE);
                    imgPp31.setVisibility(View.INVISIBLE);
                } else {
                    imgPhy32.setVisibility(View.VISIBLE);
                    imgPp32.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private void imgOnclickIsEnable(boolean isEnable){
        for (ImageView img : plat1) {
            img.setEnabled(isEnable);
        }
        for (ImageView img : plat2) {
            img.setEnabled(isEnable);
        }
        for (ImageView img : plat3) {
            img.setEnabled(isEnable);
        }
    }

    private void setImgOnClick(){
        for (ImageView img : plat1) {
            img.setOnClickListener(v -> {
                // 打開 big img 視窗
                bigImgLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);

                int selected = plat1.indexOf(img);
                Glide.with(GetCardFragment.this).load(platImg1.get(selected)).into(imgBig);
            });
        }

        for (ImageView img : plat2) {
            img.setOnClickListener(v -> {
                // 打開 big img 視窗
                bigImgLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);

                int selected = plat2.indexOf(img);

                Glide.with(GetCardFragment.this).load(platImg2.get(selected)).into(imgBig);
            });
        }

        for (ImageView img : plat3) {
            img.setOnClickListener(v -> {
                // 打開 big img 視窗
                bigImgLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);

                int selected = plat3.indexOf(img);

                Glide.with(GetCardFragment.this).load(platImg3.get(selected)).into(imgBig);
            });
        }
    }

    private void setBigImgCancel() {
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 關閉 big img 視窗
                scrollView.setVisibility(View.VISIBLE);
                bigImgLayout.setVisibility(View.GONE);

                Glide.with(GetCardFragment.this).clear(imgBig);
            }
        });
    }

    private void getCurrentTime() {
        txtTime = view.findViewById(R.id.txt_time);
        long time = System.currentTimeMillis();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd   HH:mm");

        Date date = new Date(time);
        currentTime = format.format(date);

        txtTime.setText(currentTime);
    }

    // 抽牌宮位數
    private void setPlatform(int platform) {
        this.platform = platform;
        if (platform == 1) {
            // 單宮
            platform2.setVisibility(View.GONE);
            platform3.setVisibility(View.GONE);
        } else if (platform == 2){
            // 雙宮
            platform2.setVisibility(View.VISIBLE);
            platform3.setVisibility(View.GONE);
        } else {
            // 3
            platform2.setVisibility(View.VISIBLE);
            platform3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @SuppressLint("CutPasteId")
    private void findViews(View v){
        // 宮位 layout
        platform1 = v.findViewById(R.id.platform1);

        // 干
        imgPhy1 = v.findViewById(R.id.platform1).findViewById(R.id.img_phy1);
        imgPhy2 = v.findViewById(R.id.platform1).findViewById(R.id.img_phy2);
        // 寄干
        imgPp1 = v.findViewById(R.id.platform1).findViewById(R.id.img_pp1);
        imgPp2 = v.findViewById(R.id.platform1).findViewById(R.id.img_pp2);
        // 其他
        imgOther = v.findViewById(R.id.platform1).findViewById(R.id.img_horse);
        imgStar = v.findViewById(R.id.platform1).findViewById(R.id.img_star);
        imgGod = v.findViewById(R.id.platform1).findViewById(R.id.img_god);
        imgDoor = v.findViewById(R.id.platform1).findViewById(R.id.img_door);
        imgSpace = v.findViewById(R.id.platform1).findViewById(R.id.img_space);

        // 宮位 layout
        platform2 = v.findViewById(R.id.platform2);

        // 第幾組
        groupNum2 = v.findViewById(R.id.platform2).findViewById(R.id.group_num);

        // 干
        imgPhy21 = v.findViewById(R.id.platform2).findViewById(R.id.img_phy1);
        imgPhy22 = v.findViewById(R.id.platform2).findViewById(R.id.img_phy2);
        // 寄干
        imgPp21 = v.findViewById(R.id.platform2).findViewById(R.id.img_pp1);
        imgPp22 = v.findViewById(R.id.platform2).findViewById(R.id.img_pp2);
        // 其他
        imgOther2 = v.findViewById(R.id.platform2).findViewById(R.id.img_horse);
        imgStar2 = v.findViewById(R.id.platform2).findViewById(R.id.img_star);
        imgGod2 = v.findViewById(R.id.platform2).findViewById(R.id.img_god);
        imgDoor2 = v.findViewById(R.id.platform2).findViewById(R.id.img_door);
        imgSpace2 = v.findViewById(R.id.platform2).findViewById(R.id.img_space);

        // 宮位 layout
        platform3 = v.findViewById(R.id.platform3);

        // 第幾組
        groupNum3 = v.findViewById(R.id.platform3).findViewById(R.id.group_num);

        // 干
        imgPhy31 = v.findViewById(R.id.platform3).findViewById(R.id.img_phy1);
        imgPhy32 = v.findViewById(R.id.platform3).findViewById(R.id.img_phy2);
        // 寄干
        imgPp31 = v.findViewById(R.id.platform3).findViewById(R.id.img_pp1);
        imgPp32 = v.findViewById(R.id.platform3).findViewById(R.id.img_pp2);
        // 其他
        imgOther3 = v.findViewById(R.id.platform3).findViewById(R.id.img_horse);
        imgStar3 = v.findViewById(R.id.platform3).findViewById(R.id.img_star);
        imgGod3 = v.findViewById(R.id.platform3).findViewById(R.id.img_god);
        imgDoor3 = v.findViewById(R.id.platform3).findViewById(R.id.img_door);
        imgSpace3 = v.findViewById(R.id.platform3).findViewById(R.id.img_space);

        btnAdd = v.findViewById(R.id.btn_add);

        btnSave = v.findViewById(R.id.btn_save);
        edtQ = v.findViewById(R.id.edt_question);
        btnGet = v.findViewById(R.id.btn_get);
        edtRecord = v.findViewById(R.id.edt_record);
        btnClear = v.findViewById(R.id.btn_clear);

        bigImgLayout = v.findViewById(R.id.big_img_view);
        imgBig = v.findViewById(R.id.img_big);
        imgCancel = v.findViewById(R.id.img_cancel);

        txtQ = v.findViewById(R.id.txt_question);
        txtRecord = v.findViewById(R.id.txt_record);

        scrollView = v.findViewById(R.id.scrollView);

        qInput = v.findViewById(R.id.edt_q_layout);
        rInput = v.findViewById(R.id.edt_r_layout);
    }
}