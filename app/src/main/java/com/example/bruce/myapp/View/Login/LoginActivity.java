package com.example.bruce.myapp.View.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.Login.PLogin;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.HistoryAndHobby.HistoryAndHobbyActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity implements IViewLogin,GoogleApiClient.OnConnectionFailedListener {

    ImageView imgLogo;
    LoginButton btnLoginFB;
    CallbackManager callbackManager;
    SignInButton btnloginGG;
    public static int RC_SIGN_IN = 111;
    GoogleApiClient mGoogleApiClient;


    EditText edtUsername,edtPass;
    Button btnCreate,btnLogin;

    UserProfile userProfile;
    ProgressDialog progressDialoglogin;
    //MVP
    PLogin presenterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //ánh xạ
        initialize();
        presenterLogin = new PLogin(this);
        presenterLogin.receivedLoginChecker();
        loginWithFB();
        loginWithGG();
        HandleLoginEvent(btnLogin);
    }

    private void initialize(){

        btnLoginFB = findViewById(R.id.login_fb);
        btnloginGG= findViewById(R.id.Login_gg);
        btnCreate =  findViewById(R.id.Create);
        btnLogin =  findViewById(R.id.Login);
        edtUsername = findViewById(R.id.editTextUserName);
        edtPass=  findViewById(R.id.editTextPassword);
        imgLogo = findViewById(R.id.imageView2);
    }

    //đăng nhập khi đã đăng nhập tài khoản Facebook
    public void loginWithFB()
    {
        callbackManager = CallbackManager.Factory.create();
        btnLoginFB.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        btnLoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //handleFacebookAccessToken lấy token đăng nhập bằng facebook
                presenterLogin.receivedHandleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {

                Toast.makeText(LoginActivity.this, "Login Facebook Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(LoginActivity.this, "Error:"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loginWithGG() {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(LoginActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        btnloginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

    }
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if(requestCode==RC_SIGN_IN) {
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                presenterLogin.receivedFirebaseAuthWithGoogle(account);
            }
            else
            {
                Toast.makeText(LoginActivity.this, "Login with google failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //bấm nút đang nhâp
    private void HandleLoginEvent(Button btnLogin){
        btnLogin.setOnClickListener((v)->{

            progressDialoglogin = new ProgressDialog(this);
            progressDialoglogin.show();
            progressDialoglogin.setMessage("Login by Gmail....");

            String email = edtUsername.getText().toString();
            String password = edtPass.getText().toString();
            presenterLogin.receivedHandelLoginEvent(email,password);
        });
    }

    //đăng nhập bằng tài khoản firebase
    @Override
    public void LoginSuccess() {
        Toast.makeText(this, "Login "+ userProfile.Email, Toast.LENGTH_SHORT).show();
        if(progressDialoglogin.isShowing()){
            progressDialoglogin.dismiss();
        }
        finish();

        Intent target = new Intent(LoginActivity.this, HistoryAndHobbyActivity.class);
        target.putExtra("hobby",userProfile.Hobbie);
        startActivity(target);
    }

    @Override
    public void LoginFailed() {
        if(progressDialoglogin.isShowing()){
            progressDialoglogin.dismiss();
        }
        Toast.makeText(this, "Wrong email or password. Please retype", Toast.LENGTH_SHORT).show();
        edtPass.setText(null);
    }

    @Override
    public void EmailIsEmpty() {

        if(progressDialoglogin.isShowing()){
            progressDialoglogin.dismiss();
        }

        Toast.makeText(this, "Please insert your email !!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void PasswordIsEmpty() {
        if(progressDialoglogin.isShowing()){
            progressDialoglogin.dismiss();
        }
        Toast.makeText(this, "Please insert your password !!!", Toast.LENGTH_SHORT).show();
    }

    //lấy thông tin user truyền ra UI
    @Override
    public UserProfile User_Profile(UserProfile user) {

        return this.userProfile = user;
    }

    //xử lý đăng nhập facebook
    @Override
    public void LoginFacebookSuccess() {

        finish();
        Intent target = new Intent(LoginActivity.this, HistoryAndHobbyActivity.class);
        target.putExtra("hobby",userProfile.Hobbie);
        startActivity(target);

    }

    @Override
    public void LoginFacebookFailed() {

        Toast.makeText(this, "Login facebook failed", Toast.LENGTH_SHORT).show();

    }

    //xử lý đăng nhập bằng google
    @Override
    public void LoginGoogleSuccess() {
        Toast.makeText(this, userProfile.Name + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void LoginChecker() {

        Intent target = new Intent(LoginActivity.this, HistoryAndHobbyActivity.class);
        startActivity(target);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}

