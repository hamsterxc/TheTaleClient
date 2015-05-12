package com.lonebytesoft.thetaleclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.lonebytesoft.thetaleclient.BuildConfig;
import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.apisdk.interceptor.GameInfoRequestCacheInterceptor;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.response.AuthResponse;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.ThirdPartyAuthResponse;
import com.lonebytesoft.thetaleclient.sdk.response.ThirdPartyAuthStateResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.ApiCallback;
import com.lonebytesoft.thetaleclient.sdkandroid.request.AuthRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.request.GameInfoRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.request.ThirdPartyAuthRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.request.ThirdPartyAuthStateRequestBuilder;
import com.lonebytesoft.thetaleclient.service.WatcherService;
import com.lonebytesoft.thetaleclient.service.widget.AppWidgetHelper;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.List;

/**
 * @author Hamster
 * @since 05.10.2014
 */
public class LoginActivity extends FragmentActivity {

    private static final String URL_HOME = "http://the-tale.org/?action=the-tale-client";
    private static final String URL_REGISTRATION = "http://the-tale.org/accounts/registration/fast?action=the-tale-client";
    private static final String URL_PASSWORD_REMIND = "http://the-tale.org/accounts/profile/reset-password?action=the-tale-client";
    private static final long THIRD_PARTY_AUTH_STATE_TIMEOUT = 5000; // 5 s

    private final Handler handler = new Handler(Looper.getMainLooper());

    private TextView textLogin;
    private TextView textPassword;
    private TextView textError;
    private TextView textErrorLogin;
    private TextView textErrorPassword;

    private View dataView;
    private View loadingView;
    private View errorView;

    private View contentStart;
    private View contentLoginPassword;
    private View actionRetry;
    private TextView textErrorGlobal;

    private boolean wasError = false;
    private boolean isStartLoginContainerVisible = true;
    private boolean isThirdPartyAuthInProgress = false;

    private Runnable thirdPartyAuthStateRequester = new Runnable() {
        @Override
        public void run() {
            RequestExecutor.execute(
                    LoginActivity.this,
                    new ThirdPartyAuthStateRequestBuilder(),
                    RequestUtils.wrapCallback(new ApiCallback<ThirdPartyAuthStateResponse>() {
                        @Override
                        public void onSuccess(ThirdPartyAuthStateResponse response) {
                            switch (response.authState) {
                                case NOT_REQUESTED:
                                case REJECT:
                                    isThirdPartyAuthInProgress = false;
                                    UiUtils.setText(textError, response.authState.name);
                                    setLoginContainersVisibility(true);
                                    setMode(DataViewMode.DATA);
                                    break;

                                case SUCCESS:
                                    isThirdPartyAuthInProgress = false;
                                    onSuccessfulLogin();
                                    break;

                                default:
                                    isThirdPartyAuthInProgress = true;
                                    handler.postDelayed(thirdPartyAuthStateRequester, THIRD_PARTY_AUTH_STATE_TIMEOUT);
                            }
                        }

                        @Override
                        public void onError(AbstractApiResponse response) {
                            isThirdPartyAuthInProgress = true;
                            handler.postDelayed(thirdPartyAuthStateRequester, THIRD_PARTY_AUTH_STATE_TIMEOUT);
                        }
                    }, LoginActivity.this));
        }
    };

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

        dataView = findViewById(R.id.login_content);
        loadingView = findViewById(R.id.login_progressbar);
        errorView = findViewById(R.id.login_error_global);

        contentStart = findViewById(R.id.login_content_start);
        contentLoginPassword = findViewById(R.id.login_content_login_password);

        actionRetry = findViewById(R.id.login_error_global_retry);
        textErrorGlobal = (TextView) findViewById(R.id.login_error_global_text);

        findViewById(R.id.login_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UiUtils.getOpenLinkIntent(URL_HOME));
            }
        });

        findViewById(R.id.login_action_authorization_site).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequestAuthSite();
            }
        });

        findViewById(R.id.login_action_authorization_login_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearErrors();
                setLoginContainersVisibility(false);
                textLogin.requestFocus();
            }
        });

        findViewById(R.id.login_action_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(DataViewMode.LOADING);
                clearErrors();
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
        PreferencesManager.setShouldExit(false);
        AppWidgetHelper.update(this, DataViewMode.LOADING, null);
        if(isThirdPartyAuthInProgress) {
            thirdPartyAuthStateRequester.run();
        } else {
            startRequestInit();
        }
    }

    private void startRequestInit() {
        setMode(DataViewMode.LOADING);
        RequestUtils.restoreSession();
        RequestExecutor.execute(
                this,
                new GameInfoRequestBuilder(),
                RequestUtils.wrapCallback(new ApiCallback<GameInfoResponse>() {
                    @Override
                    public void onSuccess(GameInfoResponse response) {
                        if (response.account == null) {
                            setLoginContainersVisibility(true);
                            setMode(DataViewMode.DATA);
                            AppWidgetHelper.updateWithError(LoginActivity.this, getString(R.string.game_not_authorized));
                        } else {
                            onSuccessfulLogin();
                        }
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        actionRetry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startRequestInit();
                            }
                        });
                        setError(response.errorMessage);
                    }
                }, LoginActivity.this));
    }

    private void startRequestAuthSite() {
        setMode(DataViewMode.LOADING);
        RequestExecutor.execute(
                this,
                new ThirdPartyAuthRequestBuilder().setDefaults(this),
                RequestUtils.wrapCallback(new ApiCallback<ThirdPartyAuthResponse>() {
                    @Override
                    public void onSuccess(final ThirdPartyAuthResponse response) {
                        DialogUtils.showMessageDialog(
                                getSupportFragmentManager(),
                                getString(R.string.common_dialog_attention_title),
                                getString(R.string.login_dialog_confirm_authorization),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        isThirdPartyAuthInProgress = true;
                                        thirdPartyAuthStateRequester.run();
                                        startActivity(UiUtils.getOpenLinkIntent(
                                                RequestUtils.URL_BASE + response.nextUrl));
                                    }
                                },
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        setLoginContainersVisibility(true);
                                        setMode(DataViewMode.DATA);
                                    }
                                });
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        actionRetry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startRequestAuthSite();
                            }
                        });
                        setError(response.errorMessage);
                    }
                }, LoginActivity.this));
    }

    private void sendAuthRequest(final String login, final String password) {
        RequestExecutor.execute(
                this,
                new AuthRequestBuilder().setLogin(login).setPassword(password),
                RequestUtils.wrapCallback(new ApiCallback<AuthResponse>() {
                    @Override
                    public void onSuccess(AuthResponse response) {
                        onSuccessfulLogin();
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        LoginActivity.this.processError(response.errorMessage, login, password);
                        final List<String> errorsLogin;
                        final List<String> errorsPassword;
                        if(response.errors == null) {
                            errorsLogin = null;
                            errorsPassword = null;
                        } else {
                            errorsLogin = response.errors.get("email");
                            errorsPassword = response.errors.get("password");
                        }
                        if (errorsLogin != null) {
                            UiUtils.setText(textErrorLogin, TextUtils.join("\n", errorsLogin));
                            textLogin.requestFocus();
                        }
                        if (errorsPassword != null) {
                            UiUtils.setText(textErrorPassword, TextUtils.join("\n", errorsPassword));
                            if(errorsLogin == null) {
                                textPassword.requestFocus();
                            } else {
                                textLogin.requestFocus();
                            }
                        }
                    }
                }, LoginActivity.this));
    }

    private void authorize(final String login, final String password) {
        sendAuthRequest(login, password);
    }

    private void setMode(final DataViewMode mode) {
        dataView.setVisibility(mode == DataViewMode.DATA ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(mode == DataViewMode.LOADING ? View.VISIBLE : View.GONE);
        errorView.setVisibility(mode == DataViewMode.ERROR ? View.VISIBLE : View.GONE);
    }

    private void setLoginContainersVisibility(final boolean isStartContainer) {
        isStartLoginContainerVisible = isStartContainer;
        contentStart.setVisibility(isStartContainer ? View.VISIBLE : View.GONE);
        contentLoginPassword.setVisibility(isStartContainer ? View.GONE : View.VISIBLE);
    }

    private void setError(final String error) {
        textErrorGlobal.setText(error);
        setMode(DataViewMode.ERROR);
        AppWidgetHelper.updateWithError(this, error);
    }

    private void processError(final String error, final String login, final String password) {
        wasError = true;
        setMode(DataViewMode.DATA);
        UiUtils.setText(textError, error);
        textLogin.setText(login);
        textPassword.setText(password);
    }

    private void clearErrors() {
        UiUtils.setText(textError, null);
        UiUtils.setText(textErrorLogin, null);
        UiUtils.setText(textErrorPassword, null);
    }

    private void onSuccessfulLogin() {
        RequestExecutor.execute(
                this,
                new GameInfoRequestBuilder().setStaleTime(0),
                new GameInfoRequestCacheInterceptor(),
                new ApiCallback<GameInfoResponse>() {
                    @Override
                    public void onSuccess(GameInfoResponse response) {
                        startService(new Intent(LoginActivity.this, WatcherService.class));
                        startMainActivity();
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        setError(response.errorMessage);
                    }
                });
    }

    private void startMainActivity() {
        AppWidgetHelper.updateWithRequest(this);

        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(getIntent());

        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(thirdPartyAuthStateRequester);
    }

    @Override
    public void onBackPressed() {
        if(isThirdPartyAuthInProgress) {
            isThirdPartyAuthInProgress = false;
            handler.removeCallbacks(thirdPartyAuthStateRequester);
            setLoginContainersVisibility(true);
            setMode(DataViewMode.DATA);
        } else {
            if (isStartLoginContainerVisible) {
                PreferencesManager.setShouldExit(true);
                finish();
            } else {
                setLoginContainersVisibility(true);
            }
        }
    }

}
