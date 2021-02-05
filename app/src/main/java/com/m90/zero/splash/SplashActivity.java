package com.m90.zero.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m90.zero.R;
import com.m90.zero.home.HomeActivity;
import com.m90.zero.login.OtpActivity;
import com.m90.zero.request.api.RequestApi;
import com.m90.zero.request.model.RequestModel;
import com.m90.zero.retrofit.RetrofitClientInstance;
import com.m90.zero.splash.api.AppVersionApi;
import com.m90.zero.utils.PrefManager;
import com.m90.zero.utils.SessionHelper;
import com.m90.zero.utils.Utilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    public static String TAG = "SplashActivity";
    Activity activity ;
    Handler handler;
    private PrefManager prefManager;
    SessionHelper sessionHelper;
    ProgressDialog progressDialog;
    ArrayList<AppVersionModel> appVersionModels;
    int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        activity = SplashActivity.this;


        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(activity);
        sessionHelper = new SessionHelper(activity);
        progressDialog = new ProgressDialog(activity);

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = Integer.parseInt(info.versionName);


        Log.e(TAG, "onCreate: "+version);

        versionCheck(version);


    }



    void versionCheck( int version)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            AppVersionApi appVersionApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(AppVersionApi.class);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            appVersionApi.getVersion().
                    enqueue(new Callback<ArrayList<AppVersionModel>>() {
                        @Override
                        public void onResponse(Call<ArrayList<AppVersionModel>> call,
                                               Response<ArrayList<AppVersionModel>> response) {

                            appVersionModels = response.body();
                            // roundarrayList = response.body();

                            Log.e(TAG, appVersionModels.toString());


                            if (version==Integer.parseInt(appVersionModels.get(0).versionno))
                            {
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {

                                        if(sessionHelper.isLoggedIn())
                                        {
                                            Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                                            activity.startActivity(mainIntent);
                                            activity.finish();
                                        }else
                                        {
                                            Intent mainIntent = new Intent(SplashActivity.this, OtpActivity.class);
                                            activity.startActivity(mainIntent);
                                            activity.finish();
                                        }

                                    }
                                }, 3000);

                            }else
                            {
                                versiontDialog();
                            }

                            progressDialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<ArrayList<AppVersionModel>> call, Throwable t) {

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


    private void versiontDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle("Oops! Your application is not updated");

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                //Utilities.launchActivity(activity, SplashActivity.class,true);
            }
        });

        mBuilder.setNegativeButton("Cancel", null);
        AlertDialog mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        mDialog.show();
    }
}