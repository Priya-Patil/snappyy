package com.m90.zero.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.m90.zero.Modules.GeocodingLocation;
import com.m90.zero.Modules.LocationTrack;
import com.m90.zero.address.ChooseAddressActivity;
import com.m90.zero.home.adapter.CitynameHomeAdapter;
import com.m90.zero.home.api.SliderApi;
import com.m90.zero.home.model.HomeModel;
import com.m90.zero.home.model.SliderResponse;
import com.m90.zero.home.pickupdrop.SendRecvBuyActivity;
import com.m90.zero.locationtraceandroid.LocationTraceActivity;
import com.m90.zero.login.OtpActivity;
import com.m90.zero.R;
import com.m90.zero.TermsActivity;

import com.m90.zero.home.pickupdrop.NewPickDropActivity;
import com.m90.zero.home.pickupdrop.NewShopActivity;
import com.m90.zero.request.OrderActivity;
import com.m90.zero.request.RequestHistoryActivity;
import com.m90.zero.paymenthistory.PaymentHistoryActivity;
import com.m90.zero.request.api.RequestApi;
import com.m90.zero.request.model.RequestModel;
import com.m90.zero.retrofit.RetrofitClientInstance;
import com.m90.zero.splash.SplashActivity;
import com.m90.zero.utils.PrefManager;
import com.m90.zero.utils.SessionHelper;
import com.m90.zero.utils.Utilities;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener,  GoogleApiClient.OnConnectionFailedListener,
        BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener ,
        LocationListener {

    private static final String TAG = "HomeActivity";
    Activity activity;

    private ImageView mIvCart;
    private SliderLayout mSlider;
    private SliderLayout mSlider1;
    private TextView mTvCategory;
    private CardView ll_sendrecvbuy2;
    private TextView mTvProfileName;
    private TextView mTvMobile;
    private RelativeLayout mRlProfile;
    private RelativeLayout mRlHome;
    private RelativeLayout mRlCategory;
    private TextView mTvCart;
    private RelativeLayout mRlCart;
    private RelativeLayout mRlWishlist;
    private RelativeLayout mRlOrders;
    private RelativeLayout mrl_buynearestcity;
    public static RelativeLayout mRlLogin;
    private ImageView iv_email, iv_facebook;
    private ImageView iv_call;
    private TextView mTvReg;
    public static RelativeLayout mRlReg;
    private TextView mTvContactus;
    private TextView mTvTandc;
    private TextView mTvHelpcenter;
    public static TextView mTvExit;
    private TextView mTvAbtUs;

    private ScrollView mScrllDrawer;
    private DrawerLayout mDrlOpener;
    ImageView btn_closeDrawer;
    SessionHelper sessionHelper;

    CardView ll_sendrecvbuy;
    PrefManager prefManager;
    ProgressDialog progressDialog;
    public static RelativeLayout mRlProfileClick;
    private GoogleApiClient mGoogleApiClient;

    double lat, longi;
    LocationTrack gps;

    private GpsTracker gpsTracker;

    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    String latitude, longitude;
    TextView txt_address,tv_reviews;

    private int[] myImageList ;


    private String[] myImageNameList = new String[]{"Type1","Type2"};

    RecyclerView recyclerViewType;

    CitynameHomeAdapter citynameHomeAdapter;
    LocationManager lm ;
    boolean gps_enabled ;
    boolean network_enabled ;

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    Location location;

    private ArrayList<HomeModel> arrayDailyQuiz(){
        ArrayList<HomeModel> list = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            HomeModel homeModel = new HomeModel();
            homeModel.setName(myImageNameList[i]);
            homeModel.setImage_drawable(myImageList[i]);
            list.add(homeModel);
        }
        return list;
    }
    ArrayList<HomeModel>   imageModelYouTubeArrayList ;


    void setCity()
    {
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        imageModelYouTubeArrayList = arrayDailyQuiz();
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 2);
        recyclerViewType.setLayoutManager(glm);
        recyclerViewType.setItemAnimator(new DefaultItemAnimator());
        recyclerViewType.setHasFixedSize(true);

        citynameHomeAdapter = new CitynameHomeAdapter(this, imageModelYouTubeArrayList,
                new CitynameHomeAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        // prefManager.setCityide(imageModelYouTubeArrayList.get(position).getName());
                        if(imageModelYouTubeArrayList.get(position).getName().equals("Type1"))
                        {
                            prefManager.setRequestType("1");
                            prefManager.setDrop("");
                            prefManager.setPickup("");
                            prefManager.setWeightCategoryType(1);
                            Utilities.launchActivity(activity, NewPickDropActivity.class, false);                        }

                        if(imageModelYouTubeArrayList.get(position).getName().equals("Type2"))
                        {
                            prefManager.setRequestType("2");
                            prefManager.setDrop("");
                            prefManager.setPickup("");
                            prefManager.setWeightCategoryType(1);
                            Utilities.launchActivity(activity, NewShopActivity.class, false);                        }

                    }
                });
        recyclerViewType.setAdapter(citynameHomeAdapter);
        progressDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  setContentView(R.layout.activity_home);

        lm = (LocationManager)HomeActivity.this.getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = false;
        network_enabled = false;

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_home_tab);
            myImageList = new int[]{R.drawable.pickdrop, R.drawable.buy};
            Log.e( "onCreate: ", "tab");
        }
        else
        {
            setContentView(R.layout.activity_home);
            myImageList = new int[]{R.drawable.pickdrop, R.drawable.buy};
            Log.e( "onCreate: ", "main");
        }
        activity = HomeActivity.this;
        requestPermission();
        chkLocationManager();
        InitView();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setCity();
        getSlider();
        getSlider1();

        txt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}


                if(!gps_enabled && !network_enabled) {
                    // notify user
                    Toast.makeText(getApplicationContext(), "gps not enabled", Toast.LENGTH_SHORT).show();
                }
                //enabled
                else {
                    Toast.makeText(getApplicationContext(), "gps enabled", Toast.LENGTH_SHORT).show();
                    // getLocationa();
                 /*   progressDialog.show();
                    progressDialog.setMessage("wait...");
                    gpsTracker = new GpsTracker(activity);
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    String addressHome = getAddress(latitude,longitude);
                    Log.e("TAG", "getLocation: "+latitude+" "+longitude+" "+addressHome);
                    txt_address.setText(addressHome);*/
                    progressDialog.dismiss();
                    fn_getlocation();


                }

            }
        });

     /*   try {
            Log.e( "chkonCreate: ", String.valueOf(NumberFormat.getInstance().parse("8.0").intValue()));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        //prefManager.setUserid("2");
    }

    public void getLocationa(){
        gpsTracker = new GpsTracker(activity);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            String addressHome = getAddress(latitude,longitude);
            Log.e("TAG", "getLocation: "+latitude+" "+longitude+" "+addressHome);

            txt_address.setText(addressHome);
        }else{
            //  gpsTracker.showSettingsAlert();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private void InitView() {

        sessionHelper = new SessionHelper(activity);
        progressDialog = new ProgressDialog(activity);
        prefManager =  new PrefManager(activity);
        btn_closeDrawer = findViewById(R.id.btn_openDrawer);
        txt_address = findViewById(R.id.txt_address);
        mrl_buynearestcity = findViewById(R.id.rl_buynearestcity);
        mRlProfileClick = findViewById(R.id.rl_profile_click);
        mIvCart = findViewById(R.id.iv_cart);
        mSlider = findViewById(R.id.slider);
        mSlider1 = findViewById(R.id.slider1);
        mTvCategory = findViewById(R.id.tv_category);
        ll_sendrecvbuy2 = findViewById(R.id.ll_sendrecvbuy2);
        mTvProfileName = findViewById(R.id.tv_profile_name);
        mTvMobile = findViewById(R.id.tv_mobile);
        mRlProfile = findViewById(R.id.rl_profile);
        mRlHome = findViewById(R.id.rl_home);
        mRlCategory = findViewById(R.id.rl_pick);
        mTvCart = findViewById(R.id.tv_cart);
        mRlCart = findViewById(R.id.rl_cart);
        mRlWishlist = findViewById(R.id.rl_buyany);
        mRlOrders = findViewById(R.id.rl_sendrecv);
        mrl_buynearestcity = findViewById(R.id.rl_buynearestcity);
        mRlLogin = findViewById(R.id.rl_login);
        mTvReg = findViewById(R.id.tv_reg);
        mRlReg = findViewById(R.id.rl_reg);
        iv_call = findViewById(R.id.iv_call);
        mTvContactus = findViewById(R.id.tv_contactus);
        mTvTandc = findViewById(R.id.tv_tandc);
        mTvHelpcenter = findViewById(R.id.tv_helpcenter);
        mTvExit = findViewById(R.id.tv_exit);
        mTvAbtUs = findViewById(R.id.tv_abtUs);
        mScrllDrawer = findViewById(R.id.Scrll_Drawer);
        mDrlOpener = findViewById(R.id.drl_Opener);
        ll_sendrecvbuy = findViewById(R.id.ll_sendrecvbuy);
        recyclerViewType = findViewById(R.id.recyclerViewType);
        iv_facebook = findViewById(R.id.iv_facebook);
        iv_email = findViewById(R.id.iv_email);
        tv_reviews = findViewById(R.id.tv_reviews);

        btn_closeDrawer.setOnClickListener(this);
        mRlHome.setOnClickListener(this);
        mTvTandc.setOnClickListener(this);
        mTvExit.setOnClickListener(this);
        mTvHelpcenter.setOnClickListener(this);
        mRlLogin.setOnClickListener(this);
        mRlReg.setOnClickListener(this);
        mRlProfileClick.setOnClickListener(this);
        mRlCategory.setOnClickListener(this);
        mRlCart.setOnClickListener(this);
        mRlWishlist.setOnClickListener(this);
        mRlOrders.setOnClickListener(this);
        iv_call.setOnClickListener(this);
        ll_sendrecvbuy.setOnClickListener(this);
        mrl_buynearestcity.setOnClickListener(this);
        ll_sendrecvbuy2.setOnClickListener(this);
        iv_facebook.setOnClickListener(this);
        iv_email.setOnClickListener(this);
        tv_reviews.setOnClickListener(this);
    }

    public void slider(ArrayList<SliderResponse> sliderResponse) {
        HashMap<String, Integer> url_maps = new HashMap<String, Integer>();
        url_maps.put("Hurryup", R.drawable.slider);
        url_maps.put("Limited Time Offer", R.drawable.logosnappyy);
        url_maps.put("Most Selling", R.drawable.headpgns);

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mSlider.addSlider(textSliderView);
            mSlider1.addSlider(textSliderView);
        }
        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        //  mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);
        mSlider.addOnPageChangeListener(this);

        mSlider1.setPresetTransformer(SliderLayout.Transformer.Accordion);
        //  mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider1.setCustomAnimation(new DescriptionAnimation());
        mSlider1.setDuration(4000);
        mSlider1.addOnPageChangeListener(this);


    }

    void getSlider()
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            SliderApi sliderApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(SliderApi.class);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            sliderApi.getSlider(RetrofitClientInstance.BASE_URL+"getImgList1").
                    enqueue(new Callback<ArrayList<SliderResponse>>() {
                        @Override
                        public void onResponse(Call<ArrayList<SliderResponse>> call,
                                               Response<ArrayList<SliderResponse>> response) {

                            ArrayList<SliderResponse> sliderResponse = response.body();
                            // roundarrayList = response.body();

                            //slider(sliderResponse);

                            for (SliderResponse name : sliderResponse) {
                                TextSliderView textSliderView = new TextSliderView(activity);
                                // initialize a SliderLayout
                                textSliderView
                                        //.description(name.description)
                                        .image("http://avistore.in/snappy/public/uploads/" + name.imgname)
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(HomeActivity.this::onSliderClick);

                              /*  //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name.imgname);
*/
                                mSlider.addSlider(textSliderView);
                            }
                            mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            //mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            // mSlider.setCustomAnimation(new DescriptionAnimation());
                            mSlider.setDuration(4000);
                            mSlider.addOnPageChangeListener(HomeActivity.this);

                            Log.e("SliderDResponse: ", sliderResponse.toString());

                            progressDialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<ArrayList<SliderResponse>> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.check_internet));

        }
    }

    void getSlider1()
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            SliderApi sliderApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(SliderApi.class);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            sliderApi.getSlider(RetrofitClientInstance.BASE_URL+"getImgList2").
                    enqueue(new Callback<ArrayList<SliderResponse>>() {
                        @Override
                        public void onResponse(Call<ArrayList<SliderResponse>> call,
                                               Response<ArrayList<SliderResponse>> response) {

                            ArrayList<SliderResponse> sliderResponse = response.body();
                            // roundarrayList = response.body();

                            //slider(sliderResponse);

                            for (SliderResponse name : sliderResponse) {
                                TextSliderView textSliderView = new TextSliderView(activity);
                                // initialize a SliderLayout
                                textSliderView
                                        //.description(name.description)
                                        .image("http://avistore.in/snappy/public/uploads/" + name.imgname)
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(HomeActivity.this::onSliderClick);

                                //add your extra information
                               /* textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name.imgname);
*/
                                mSlider1.addSlider(textSliderView);
                            }
                            mSlider1.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            //  mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            // mSlider1.setCustomAnimation(new DescriptionAnimation());
                            mSlider1.setDuration(4000);
                            mSlider1.addOnPageChangeListener(HomeActivity.this);

                            Log.e("SliderDResponse: ", sliderResponse.toString());

                            progressDialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<ArrayList<SliderResponse>> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else
        {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.check_internet));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_cart:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                break;

            case R.id.tv_buy:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Utilities.launchActivity(activity, ChooseAddressActivity.class, false);
                break;

            case R.id.ll_sendrecvbuy2:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=rQd-9vwRr5Q&feature=emb_logo"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent)       ;
                break;

            case R.id.iv_email:
                try {
                    Intent ii = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:m90jobs@gmail.com?subject=" + Uri.encode("") + "&body=" + Uri.encode(""));
                    ii.setData(data);
                    startActivity(ii);
                }
                catch (Exception e)
                {

                }
                break;

            case R.id.iv_facebook:
                Intent intentFacebook = null;
                try {
                    intentFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                } catch (Exception e) {
                    intentFacebook =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                }
                startActivity(intentFacebook);
                break;

            case R.id.ll_sendrecvbuy:
                Utilities.launchActivity(activity, SendRecvBuyActivity.class, false);
                break;

            case R.id.btn_openDrawer:
                mDrlOpener.openDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                break;

            case R.id.rl_home:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                break;


            case R.id.rl_profile:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                break;

            case R.id.rl_login:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Intent ii = new Intent(activity, OtpActivity.class);
                startActivity(ii);
                break;

            case R.id.rl_reg:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                break;

            case R.id.rl_pick:
                Bundle bundle0 = new Bundle();
                bundle0.putInt("rtype",1);;
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Utilities.launchActivity(activity, RequestHistoryActivity.class, false);

                break;

            //Payment History
            case R.id.rl_buyany:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("rtype",2);;
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Utilities.launchActivity(activity, RequestHistoryActivity.class, false,bundle1);
                break;

            //Request History
            case R.id.rl_sendrecv:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("rtype",3);;
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Utilities.launchActivity(activity, RequestHistoryActivity.class, false,bundle3);
                break;

            case R.id.rl_buynearestcity:
                Bundle bundle4 = new Bundle();
                bundle4.putInt("rtype",4);;
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Utilities.launchActivity(activity, RequestHistoryActivity.class, false,bundle4);
                break;

            case R.id.tv_helpcenter:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                // Utilities.launchActivity(activity, FaqActivity.class, false);
                break;

            case R.id.tv_tandc:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                Utilities.launchActivity(activity, TermsActivity.class, false);
                break;

            case R.id.tv_exit:
                mDrlOpener.closeDrawer(Gravity.RIGHT); //Edit Gravity.START need API 14
                logoutDialog(v);
                break;

            case R.id.iv_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:7666593042"));
                startActivity(callIntent);
                break;


             case R.id.tv_reviews:
                 final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                 break;

        }
    }

    private void logoutDialog(View v) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle("Are you sure you want to exit?");
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                sessionHelper.setLogin(false);
                if (prefManager.getLoginType().equals("1"))
                {
                    Utilities.launchActivity(activity, SplashActivity.class,true);
                }
                if (prefManager.getLoginType().equals("2"))
                {
                    disconnectFromFacebook();
                }

                if (prefManager.getLoginType().equals("3"))
                {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    //   updateUI(false);
                                    Utilities.launchActivity(activity, SplashActivity.class,true);

                                }
                            });
                }
            }
        });

        mBuilder.setNegativeButton("Cancel", null);
        AlertDialog mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        mDialog.show();
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                Utilities.launchActivity(activity, SplashActivity.class,true);

            }
        }).executeAsync();
    }



    @Override
    public void onLocationChanged(Location location) {
        Log.e( "","onLocationChanged: Current Location: " + location.getLatitude() + ", " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(activity, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                Log.e("TAG", "getLocation: "+"Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude );
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void chkLocationManager()
    {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(activity)
                    .setMessage("GPS network not enabled")
                    //.setCancelable(false)

                    .setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                            // activity.onBackPressed();
                            //Toast.makeText(activity, "nooo", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
        //enabled
        else {
            Toast.makeText(activity, "enabled", Toast.LENGTH_SHORT).show();


        }
    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitudeGgl", location.getLatitude() + "");
                        Log.e("longitudeGgl", location.getLongitude() + "");

                        String addressHome = getAddress(location.getLatitude(), location.getLongitude());
                        Log.e("TAG", "getLocation: " + latitude + " " + longitude + " " + addressHome);
                        txt_address.setText(addressHome);
                        // latitude = location.getLatitude();
                        // longitude = location.getLongitude();


                    }

                }

                if (isGPSEnable) {
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.e("latitude_service", location.getLatitude() + "");
                            Log.e("longitude_service", location.getLongitude() + "");

                            String addressHome = getAddress(location.getLatitude(), location.getLongitude());
                            Log.e("TAG", "getLocation: " + latitude + " " + longitude + " " + addressHome);
                            txt_address.setText(addressHome);
                        }
                    }
                }

            }}}
}


