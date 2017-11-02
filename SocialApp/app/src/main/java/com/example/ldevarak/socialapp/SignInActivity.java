package com.example.ldevarak.socialapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.ldevarak.socialapp.database.DatabaseUtils;
import com.example.ldevarak.socialapp.database.UserDetails;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //Signin button
    private SignInButton signInButton;

    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;
    private NetworkImageView profilePhoto;
    private ImageLoader imageLoader;
    Bitmap bitmap;


    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    CallbackManager callbackManager;
    DatabaseUtils databaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());


        databaseUtils = new DatabaseUtils(getApplication());
        List<UserDetails> userDetails = databaseUtils.getUserDetails();
        if (userDetails != null && userDetails.size() > 0) {

            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.putExtra("firstName", userDetails.get(0).getFirstName());
            intent.putExtra("lastName", userDetails.get(0).getLastName());
            intent.putExtra("email", userDetails.get(0).getEmail());
            intent.putExtra("id", userDetails.get(0).getUserId());
            startActivity(intent);
            finish();

        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
            //Setting onclick listener to signing button
            signInButton.setOnClickListener(this);
            callbackManager = CallbackManager.Factory.create();
            LoginButton fbLogin = (LoginButton) findViewById(R.id.fb_login_button);
            fbLogin.setReadPermissions("email");

            callbackManager = CallbackManager.Factory.create();
            fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {


                    // Facebook Email address
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    Log.v("LoginActivity Response ", response.toString());

                                    try {
                                        System.out.println("object : " + object);
                                        String id = object.getString("id");
                                        String firstName = object.getString("first_name");
                                        String lastName = object.getString("last_name");
                                        String email = object.getString("email");
                                        String gender = object.getString("gender");

                                        if (android.os.Build.VERSION.SDK_INT > 9) {
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                        }

                                        URL imgUrl = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                                        bitmap = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());


                                        Log.v("firstName = ", " " + firstName);
                                        Log.v("lastName = ", " " + lastName);
                                        Log.v("email = ", " " + email);
                                        Log.v("gender = ", " " + gender);
                                        Log.v("imgUrl = ", " " + imgUrl.toString());
                                        Log.v("bitmap = ", " " + bitmap);
                                        Log.v("FB ", "FBEND:");
                                        UserDetails userDetails = new UserDetails();
                                        userDetails.setUserId(id);
                                        userDetails.setFirstName(firstName);
                                        userDetails.setLastName(lastName);
                                        userDetails.setEmail(email);
                                        //userDetails.setImgUrl(imgUrl.toString());

                                       // databaseUtils.addUserDetails(userDetails);

                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.putExtra("firstName", firstName);
                                        intent.putExtra("lastName", lastName);
                                        intent.putExtra("email", email);
                                        intent.putExtra("id", id);
                                        intent.putExtra("bitmap", bitmap);
                                        intent.putExtra("type", "Fb");

                                        startActivity(intent);
                                        finish();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,first_name,last_name,email,gender, birthday, picture.type(large)");
                    request.setParameters(parameters);
                    request.executeAsync();

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

        }


    }

    private void getHashCode() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.ldevarak.userlogin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


    //This function will option signing intent
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            //Displaying name and email
            String firstName = acct.getGivenName();
            String lastName = acct.getFamilyName();
            String email = acct.getEmail();
            Uri imgUrl = acct.getPhotoUrl();
            try {
                // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUrl);
                //  Bitmap  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUrl);

                if (imgUrl != null) {
                    bitmap = getBitmapFromURL(imgUrl.toString());
                }


                UserDetails userDetails = new UserDetails();
                userDetails.setUserId(acct.getId());
                userDetails.setFirstName(firstName);
                userDetails.setLastName(lastName);
                userDetails.setEmail(email);

              //  databaseUtils.addUserDetails(userDetails);

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("email", email);
                intent.putExtra("id", acct.getId());
                intent.putExtra("imgUrl", imgUrl);
                intent.putExtra("bitmap", bitmap);
                intent.putExtra("type", "Google");

                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin
            signIn();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            System.out.printf("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            System.out.printf("Bitmap", "returned");
            myBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 100, false);//This is only if u want to set the image size.
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Exception", e.getMessage());
            return null;
        }

    }
}
