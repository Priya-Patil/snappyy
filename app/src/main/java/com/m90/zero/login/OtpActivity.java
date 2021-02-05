package com.m90.zero.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.m90.zero.R;
import com.m90.zero.databinding.ActivityMainBinding;
import com.m90.zero.facebooklogin.FActivity;
import com.m90.zero.glogin.GoolgeLoginActivity;
import com.m90.zero.home.HomeActivity;
import com.m90.zero.interfaces.ApiStatusCallBack;
import com.m90.zero.login.api.OTPApi;
import com.m90.zero.login.model.OTPResponce;
import com.m90.zero.myConfig.OTPServices;
import com.m90.zero.retrofit.RetrofitClientInstance;
import com.m90.zero.utils.PrefManager;
import com.m90.zero.utils.SessionHelper;
import com.m90.zero.utils.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.m90.zero.facebooklogin.FActivity.printHashKey;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = OtpActivity.class.getSimpleName();

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;

    //String OTP="1234";
    String OTP="";
    Activity activity;

    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic,googlelog;
    private TextView txtName, txtEmail;


    ProgressDialog progressDialog;
    PrefManager prefManager;
    SessionHelper sessionHelper;

    Button fb;
    Button gmail;
    // facebook
    LoginButton loginButton;
    CallbackManager callbackManager;

    String email;
    Button submit1, submit2;
    EditText et_mobile,et_otp;
    RelativeLayout layout1;
    LinearLayout layout2;
    TextView text_otp_expire,tv_sms_recv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_tabmain);
            Log.e( "onCreate: ", "tab");
        }
        else
        {
            setContentView(R.layout.activity_main);

            Log.e( "onCreate: ", "main");
        }
        activity = OtpActivity.this;
        InitView();
        printHashKey(activity);


        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);
        sessionHelper=new SessionHelper(activity);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);

        requestPermission();
        googleBtnui();
        /*-----------------------Google---------------------------------*/
        btnSignIn.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidPhoneNumber(et_mobile.getText().toString()))
                {
                    //  Utility.launchActivity(activity, HomeActivity.class, true);

                    OTP = GenerateRandomNumber(6);
                    Log.e( "onClick: ",  OTP);

                    String message = "Thank you for visiting Snappyy Application! \n Your OTP Is :" + OTP;

                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    timerForOtp(et_mobile.getText().toString(), message);
                 }
                else {
                    Toast.makeText(activity, "Enter proper mobile number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_otp.getText().toString().equals(""))
                {
                    Toast.makeText(activity, "Enter otp", Toast.LENGTH_SHORT).show();

                }
                else {
                    if (et_otp.getText().toString().equals(OTP))
                    {
                        checkLogin(et_mobile.getText().toString());

                    } else {
                        Toast.makeText(activity, "Enter proper otp", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        googlelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.launchActivity(activity, GoolgeLoginActivity.class,true);
            }
        });


        loginButton = findViewById(R.id.login_button);


        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        Log.e("onCreate: ", String.valueOf(loggedOut));
        if (!loggedOut) {
            // Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());

            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken());
        }

        //facebook
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.e("API123", loggedIn + " ??");

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {

                Log.e("onError: ", exception.toString());
                // App code
            }
        });

    }

    private void InitView() {

        sessionHelper = new SessionHelper(activity);
        progressDialog = new ProgressDialog(activity);
        prefManager =  new PrefManager(activity);
        submit1 = findViewById(R.id.submit1);
        submit2 = findViewById(R.id.submit2);
        et_mobile = findViewById(R.id.et_mobile);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        et_otp = findViewById(R.id.et_otp);
        fb = findViewById(R.id.fb);
        googlelog = findViewById(R.id.googlelog);
        text_otp_expire = findViewById(R.id.text_otp_expire);
        tv_sms_recv = findViewById(R.id.tv_sms_recv);

    }

    public void onClickFacebookButton(View view) {
        if (view == fb) {
            loginButton.performClick();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }


    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            email = acct.getEmail();

         /*   Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);*/

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " );

           // txtName.setText(personName);
            //txtEmail.setText(email);
           /* Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                   *//* .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                   *//*
                    .into(imgProfilePic);
*/
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateCountDownText(long millisUntilFinished) {
        int minutes = (int) (millisUntilFinished / 1000) / 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%d:%d", minutes, seconds);
        //text_otp_expire.setText(timeLeftFormatted);
        Spannable WordtoSpan = new SpannableString("OTP expire after "+timeLeftFormatted+"  ");
        WordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 17, 21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_otp_expire.setText(WordtoSpan);

        //text_otp_expire.setText("OTP expire after  "+timeLeftFormatted+"  Seconds");

    }

    private void timerForOtp(String mobileNumber,String message) {


        tv_sms_recv.setVisibility(View.GONE);
        SpannableString span = new SpannableString("Didn't receive SMS ? Resend");
        span.setSpan(new ForegroundColorSpan(Color.GRAY), 21, 27, 0);
        tv_sms_recv.setTextColor(Color.parseColor("#48494b"));

        mCountDownTimer = new CountDownTimer(120000, 1000) {
            // mCountDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText(millisUntilFinished);
                //120 sec=120000ms
                if ((millisUntilFinished / 1000) == 117) {

                    /*binding.layout1.setVisibility(View.GONE);
                    binding.layout2.setVisibility(View.VISIBLE);*/
                    sendOTP(mobileNumber,message);

                }
                else {

                    //  sendCodeButton.setVisibility(View.VISIBLE);
                }

            }

            public void onFinish() {
                //sendOTP(mobileNumber, message);
                //Toast.makeText(AuthenticationActivity.this,"Time Out!! Resend OTP",Toast.LENGTH_LONG).show();
                SpannableString span = new SpannableString("Didn't receive SMS ? Resend");
                span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 21, 27, 0);
                tv_sms_recv.setText(span, TextView.BufferType.SPANNABLE);
                tv_sms_recv.setVisibility(View.VISIBLE);
                //tv_sms_recv.setTextColor(Color.parseColor("#002266"));
                tv_sms_recv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        OTP = GenerateRandomNumber(6);
                        Log.e("ChkOTP",""+OTP);
                        String message2 = "Thank you for visiting app! \n Your OTP Is :" + OTP;
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        timerForOtp(mobileNumber,message2);

                    }
                });

            }

        }.start();
    }

    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    String GenerateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }

    void checkLogin(String mobile)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            OTPApi loginApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(OTPApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            // pbLoading.setProgressStyle(R.id.abbreviationsBar);
            progressDialog.show();
            loginApi.checkLogin(mobile).
                    enqueue(new Callback<OTPResponce>() {

                        @Override
                        public void onResponse(Call<OTPResponce> call, Response<OTPResponce> response) {

                            OTPResponce otpResponce = response.body();
                            sessionHelper.setLogin(true);
                            Log.e("otpResponce: ", String.valueOf(otpResponce.id));
                            prefManager.setUserid(String.valueOf(otpResponce.id));
                            prefManager.setMobile(otpResponce.mobile);

                            prefManager.setLoginType("1");
                            // Utility.launchActivity(activity, HomeActivity.class, true);
                            progressDialog.dismiss();
                            Utilities.launchActivity(activity, HomeActivity.class, true);
                        }

                        @Override
                        public void onFailure(Call<OTPResponce> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        }
    }

    void sendOTP(String mobileNumber, String message) {
        OTPServices.getInstance(activity).SendOTP(mobileNumber, message, new ApiStatusCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(activity, "sent otp", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                Log.e( "onError: ", anError.toString());
                //   Toast.makeText(activity, "Failed "+anError.toString(), Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onUnknownError(Exception e) {
                // Toast.makeText(activity, "Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                et_otp.setText(message);
                Log.e( "onReceive: ", message );
                // message is the fetching OTP
            }
        }
    };

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                Log.e(TAG, "onActivityResult: " + email);
                prefManager.setLoginType("3");
                checkLoginGoogle(email);

            }
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            Log.e( "loginchk" , "Signed Out");

        } else {
            Log.e( "loginchk" , "Signed In");
            getUserProfile(AccessToken.getCurrentAccessToken());
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

           // showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                   // hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.VISIBLE);
            //btnSignOut.setVisibility(View.VISIBLE);
           // btnRevokeAccess.setVisibility(View.VISIBLE);
           // llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
           // btnSignOut.setVisibility(View.GONE);
           // btnRevokeAccess.setVisibility(View.GONE);
           // llProfileLayout.setVisibility(View.GONE);
        }
    }


    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            Log.e( "onCompleted:FB ",email );
                            //txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            // txtEmail.setText(email);
                            //Picasso.with(FActivity.this).load(image_url).into(imageView);

                           // prefManager.setLoginType("2");
                            checkLoginFb(email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e( "HashKey: " , hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e( "printHashKey()", e.getMessage());
        } catch (Exception e) {
            Log.e( "printHashKey()", e.getMessage());
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
                case R.id.btn_sign_in:
                signIn();
                break;

           /* case R.id.btn_sign_out:
                signOut();
                break;*/

                case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }


    void checkLoginFb(String email)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            OTPApi loginApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(OTPApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            // pbLoading.setProgressStyle(R.id.abbreviationsBar);
            progressDialog.show();
            loginApi.checkGoogleLogin(email).
                    enqueue(new Callback<OTPResponce>() {

                        @Override
                        public void onResponse(Call<OTPResponce> call, Response<OTPResponce> response) {

                            OTPResponce otpResponce = response.body();
                            sessionHelper.setLogin(true);
                            Log.e("otpResponce: ", otpResponce.toString()+" "+String.valueOf(otpResponce.id));
                            prefManager.setUserid(String.valueOf(otpResponce.id));
                            // prefManager.setMobile(otpResponce.mobile);
                            prefManager.setLoginType("2");

                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(),""+email,Toast.LENGTH_SHORT).show();

                            Utilities.launchActivity(activity, HomeActivity.class, true);
                        }

                        @Override
                        public void onFailure(Call<OTPResponce> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        }
    }

    void checkLoginGoogle(String email)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            OTPApi loginApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(OTPApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            // pbLoading.setProgressStyle(R.id.abbreviationsBar);
            progressDialog.show();
            loginApi.checkGoogleLogin(email).
                    enqueue(new Callback<OTPResponce>() {

                        @Override
                        public void onResponse(Call<OTPResponce> call, Response<OTPResponce> response) {

                            OTPResponce otpResponce = response.body();
                            sessionHelper.setLogin(true);
                            Log.e("otpResponce: ", otpResponce.toString()+" "+String.valueOf(otpResponce.id));
                            prefManager.setUserid(String.valueOf(otpResponce.id));
                            // prefManager.setMobile(otpResponce.mobile);
                            prefManager.setLoginType("3");

                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(),""+email,Toast.LENGTH_SHORT).show();

                            Utilities.launchActivity(activity, HomeActivity.class, true);
                        }

                        @Override
                        public void onFailure(Call<OTPResponce> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        }
    }

    void googleBtnui()
    {
        for (int i = 0; i < btnSignIn.getChildCount(); i++) {
        View v = btnSignIn.getChildAt(i);

        if (v instanceof TextView)
        {
            TextView tv = (TextView) v;
            tv.setTextSize(14);
            tv.setTypeface(null, Typeface.NORMAL);
            //tv.setText("My Text");
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.ic_google_plus));
            tv.setSingleLine(true);
            tv.setPadding(15, 15, 15, 15);
            ViewGroup.LayoutParams params = tv.getLayoutParams();
            params.width = 100;
            params.height = 70;
            tv.setLayoutParams(params);

            return;
        }
    }
    }


}