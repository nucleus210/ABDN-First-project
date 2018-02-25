package com.example.android.renovo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    AnimationDrawable myAnim;
    Activity activity;
    int stringPosition = 0;
    public EditText customerName;
    public EditText customerSubject;
    public EditText customerEmail;
    public EditText customerData;
    public String mCustomerName;
    public String mCustomerSubject;
    public String mCustomerEmail;
    public String mCustomerData;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int myTime = 600000;

        /**
         * Declare views and button variables
         */

//------------------------------------------------------------------------------------------------//
        ImageView interiorView = findViewById(R.id.render_view);
        RelativeLayout myLayout = findViewById(R.id.main_view);
        ImageButton downArrow = findViewById(R.id.down_button);
        interiorView.setBackgroundResource(R.drawable.visualization_list);

        customerName = findViewById(R.id.contact_name);
        customerSubject = findViewById(R.id.subject_data);
        customerEmail = findViewById(R.id.email_address);
        customerData = findViewById(R.id.contact_data);
        ImageButton youTube = findViewById(R.id.you_tube_button);
        ImageButton eMail = findViewById(R.id.email_button);
        ImageButton faceBook = findViewById(R.id.facebook_button);
        ImageButton googleMaps = findViewById(R.id.google_maps_button);
        Button sendEmailButton = findViewById(R.id.send_email_button);
        final TextView mainTextInfo = findViewById(R.id.main_message_view);

//-------------------Change Text with On Tick Listener and countdown timer------------------------//
        new CountDownTimer(myTime, 5000) {

            public void onTick(long millisUntilFinished) {
                final String[] textArray = getResources().getStringArray(R.array.main_text_array);
                final android.os.Handler handler = new android.os.Handler();

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                            if (stringPosition != textArray.length) {
                                mainTextInfo.setText(null);
                                mainTextInfo.setText(textArray[stringPosition]);
                                stringPosition++;

                            } else {
                                stringPosition = 0;
                                mainTextInfo.setText(textArray[stringPosition]);
                                stringPosition++;
                            }
                    }
                });
            }

          public void onFinish() {
             //TODO
            }
        }.start();


        /**
         * Fade animation
         */
//------------------------------------------------------------------------------------------------//
        myAnim = (AnimationDrawable) interiorView.getDrawable();
        myAnim.setEnterFadeDuration(2000);
        myAnim.setExitFadeDuration(2000);
        myAnim.start();


        /**
         * Get device screen resolution to set empty image on screen view to add Parallax effect
         * on Scroll View
         */

//------------------------------------------------------------------------------------------------//
        int rawWidth = 0, rawHeight = 0;
        final DisplayMetrics metrics = new DisplayMetrics();
        final Display display = getWindowManager().getDefaultDisplay();
        Method mGetRawH, mGetRawW;

        try {
            // For JellyBeans and onward
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                display.getRealMetrics(metrics);

                rawWidth = metrics.widthPixels;
                rawHeight = metrics.heightPixels;

            } else {
                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                try {
                    rawWidth = (Integer) mGetRawW.invoke(display);
                    rawHeight = (Integer) mGetRawH.invoke(display);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        /**
         * Simple Parallax Effect on Scroll View. Work on build SDK v15 +
         *
         * To create Parallax effect on Scroll View I decide to set empty transparent
         * image on screen. But first I set empty Linear Layout /parent/ and Linear Layout child
         * with weight = 1. In that way i get exact screen resolution for view group. Then on runtime
         * i check real device screen orientation and set image with real resolution.
         */

//------------------------------------------------------------------------------------------------//
        final ImageView mImage = findViewById(R.id.empty_helper);

// Use a layout change listener to re-scale the empty image and get Parallax effect---------------//

        final int finalRawWidth = rawWidth;
        final int finalRawHeight = rawHeight;

        myLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                //get screen resolution
                int orientation = display.getRotation();

//-------------check screen current screen orientation to get empty image resolution--------------//

                if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
                    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(finalRawWidth, finalRawHeight);
                    // Changes the height and width to the specified *pixels*
                    parameters.height = finalRawHeight - ((finalRawHeight * 25) / 100);
                    parameters.width = finalRawWidth;
                    mImage.setLayoutParams(parameters);
                } else if (orientation == Surface.ROTATION_180 || orientation == Surface.ROTATION_0) {

                    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(finalRawWidth, finalRawHeight);
                    // Changes the height and width to the specified *pixels*
                    parameters.height = finalRawHeight - ((finalRawHeight * 21) / 100);
                    parameters.width = finalRawWidth;
                    mImage.setLayoutParams(parameters);
                }
            }
        });

//--------------------------------Down Button On Click Listener-----------------------------------//

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView sv = findViewById(R.id.main_scroll_view);
                sv.scrollTo(0, sv.getBottom());
            }
        });

//--------------------------------Send eMail On Click Listener-----------------------------------//
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check Edit Text user input data
                if (customerEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.error_fill_email, Toast.LENGTH_LONG).show();

                } else if (customerName.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.error_fill_name, Toast.LENGTH_LONG).show();

                } else if (customerSubject.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.error_fill_subject, Toast.LENGTH_LONG).show();

                } else if (customerData.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.error_fill_message, Toast.LENGTH_LONG).show();

                } else {
                    getEditTextValues(); // get values from edit text views
                    composeMail(mCustomerEmail, mCustomerSubject, mCustomerName, mCustomerData);
                }
            }
        });

//---------------------Intent to Google maps Button On Click Listener-----------------------------//
        googleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionMap();
            }
        });

//-----------------------Intent to Facebook Button On Click Listener------------------------------//
        faceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFaceBook();
            }
        });
//--------------------------Intent to eMail Button On Click Listener------------------------------//
        eMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = "";
                String name = "";
                String data = "";
                composeMail(mCustomerEmail, subject, name, data);
            }
        });
//-------------------------Intent to YouTube Button On Click Listener-----------------------------//
        youTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToYouTube();
            }
        });
    }

    /**
     * This method is used to get Google maps location
     */

//------------------------------------------------------------------------------------------------//

    public void directionMap() {

        String strUri = getString(R.string.google_link) + 43.216839 + "," + 27.928100;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    /**
     * This method is used to get YouTube Page
     */

//------------------------------------------------------------------------------------------------//
    public void goToYouTube() {

        String strUri = getString(R.string.youtube_link);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        startActivity(intent);
    }

    /**
     * This method is used to get Facebook page
     */

//------------------------------------------------------------------------------------------------//
    public void goToFaceBook() {

        String strUri = getString(R.string.facebook_link);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        startActivity(intent);
    }

    /**
     * This method is used to send email message
     */

//------------------------------------------------------------------------------------------------//
    public void composeMail(String mMail, String mSubject, String mName, String mBody) {

        Intent eMailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mMail));
        eMailIntent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        eMailIntent.putExtra(Intent.EXTRA_TEXT, mName);
        eMailIntent.putExtra(Intent.EXTRA_TEXT, mBody);
        if (eMailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(eMailIntent);
        }
    }

    /**
     * This method is used to get user input data
     */

//------------------------------------------------------------------------------------------------//
    public void getEditTextValues() {

        mCustomerName = customerName.getText().toString();
        mCustomerEmail = customerEmail.getText().toString();
        mCustomerSubject = customerSubject.getText().toString();
        mCustomerData = customerData.getText().toString();
    }

    /**
     * This method is used to get activity
     */

//------------------------------------------------------------------------------------------------//
    public Activity getActivity() {
        return activity;
    }
}






