package com.lonebytesoft.thetaleclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.lonebytesoft.thetaleclient.ApplicationPart;
import com.lonebytesoft.thetaleclient.BuildConfig;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.ApiResponseStatus;
import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.api.request.AuthRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.InfoRequest;
import com.lonebytesoft.thetaleclient.api.response.AuthResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.service.NotificationService;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 05.10.2014
 */
public class LoginActivity extends Activity {

    private static final String URL_REGISTRATION = "http://the-tale.org/accounts/registration/fast";
    private static final String URL_PASSWORD_REMIND = "http://the-tale.org/accounts/profile/reset-password";

    private TextView textLogin;
    private TextView textPassword;

    private TextView textError;
    private TextView textErrorLogin;
    private TextView textErrorPassword;

    private View viewProgress;
    private View viewContent;

    private boolean wasError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!BuildConfig.DEBUG) {
            Crashlytics.start(this);
        }
        setContentView(R.layout.activity_login);

        textLogin = (TextView) findViewById(R.id.login_text_login);
        textPassword = (TextView) findViewById(R.id.login_text_password);

        textError = (TextView) findViewById(R.id.login_error);
        textErrorLogin = (TextView) findViewById(R.id.login_error_login);
        textErrorPassword = (TextView) findViewById(R.id.login_error_password);

        viewProgress = findViewById(R.id.login_progressbar);
        viewContent = findViewById(R.id.login_content);

        findViewById(R.id.login_action_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLoadingMode(true);
                UiUtils.setText(textError, null);
                UiUtils.setText(textErrorLogin, null);
                UiUtils.setText(textErrorPassword, null);

                authorize(textLogin.getText().toString(), textPassword.getText().toString());
            }
        });

        findViewById(R.id.login_action_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UiUtils.getOpenLinkIntent(URL_REGISTRATION));
            }
        });
        findViewById(R.id.login_action_password_remind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UiUtils.getOpenLinkIntent(URL_PASSWORD_REMIND));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        switchLoadingMode(true);

        new InfoRequest().execute(new ApiResponseCallback<InfoResponse>() {
            @Override
            public void processResponse(InfoResponse response) {
                new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(GameInfoResponse response) {
                        if (response.account == null) {
                            final String login = PreferencesManager.getLogin();
                            final String password = PreferencesManager.getPassword();
                            if(!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
                                authorize(login, password);
                            } else {
                                switchLoadingMode(false);
                                textLogin.requestFocus();
                            }
                        } else {
                            onSuccessfulLogin();
                        }
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
                        LoginActivity.this.processError(response.errorMessage, PreferencesManager.getLogin(), PreferencesManager.getPassword());
                    }
                });
            }

            @Override
            public void processError(InfoResponse response) {
                LoginActivity.this.processError(response.errorMessage, PreferencesManager.getLogin(), PreferencesManager.getPassword());
            }
        });
    }

    private void sendAuthRequest(final String login, final String password) {
        new AuthRequest().execute(login, password, true,
                new ApiResponseCallback<AuthResponse>() {
                    @Override
                    public void processResponse(AuthResponse response) {
                        if (response.status == ApiResponseStatus.OK) {
                            PreferencesManager.setCredentials(login, password);
                            onSuccessfulLogin();
                        } else {
                            switchLoadingMode(false);
                        }
                    }

                    @Override
                    public void processError(AuthResponse response) {
                        LoginActivity.this.processError(response.errorMessage, login, password);
                        if (response.errorsLogin != null) {
                            UiUtils.setText(textErrorLogin, TextUtils.join("\n", response.errorsLogin));
                            textLogin.requestFocus();
                        }
                        if (response.errorsPassword != null) {
                            UiUtils.setText(textErrorPassword, TextUtils.join("\n", response.errorsPassword));
                            if(response.errorsLogin == null) {
                                textPassword.requestFocus();
                            } else {
                                textLogin.requestFocus();
                            }
                        }
                    }
                });
    }

    private void authorize(final String login, final String password) {
        RequestCacheManager.invalidate();
        if(wasError) {
            new InfoRequest().execute(new ApiResponseCallback<InfoResponse>() {
                @Override
                public void processResponse(InfoResponse response) {
                    sendAuthRequest(login, password);
                }

                @Override
                public void processError(InfoResponse response) {
                    LoginActivity.this.processError(response.errorMessage, login, password);
                    textLogin.requestFocus();
                }
            });
        } else {
            sendAuthRequest(login, password);
        }
    }

    private void switchLoadingMode(final boolean isLoading) {
        viewProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        viewContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void processError(final String error, final String login, final String password) {
        wasError = true;
        switchLoadingMode(false);
        UiUtils.setText(textError, error);
        textLogin.setText(login);
        textPassword.setText(password);
    }

    private void onSuccessfulLogin() {
        startService(new Intent(this, NotificationService.class));
        startMainActivity();
    }

    private void startMainActivity() {
        TheTaleClientApplication.onApplicationPartSelected(ApplicationPart.GAME_INFO);
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
