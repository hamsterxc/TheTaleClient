package com.lonebytesoft.thetaleclient.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.DrawerItem;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.fragment.dialog.ChoiceDialog;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.model.AccountShortInfo;
import com.lonebytesoft.thetaleclient.sdk.response.AccountsListResponse;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.sdkandroid.ApiCallback;
import com.lonebytesoft.thetaleclient.sdkandroid.request.AccountsListRequestBuilder;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hamster
 * @since 04.02.2015
 */
public class FindPlayerFragment extends WrapperFragment {

    private static final int ACCOUNTS_COUNT_THRESHOLD = 500;
    private static final int ACCOUNTS_COUNT_GRANULARITY = 100;

    private LayoutInflater layoutInflater;
    private View rootView;

    private TextView textQuery;
    private View actionSearch;
    private ListView accountsList;
    private TextView textError;
    private TextView textDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_find_player, container, false);

        textQuery = (TextView) rootView.findViewById(R.id.find_player_prefix);
        actionSearch = rootView.findViewById(R.id.find_player_search);
        accountsList = (ListView) rootView.findViewById(R.id.find_player_choices);
        textError = (TextView) rootView.findViewById(R.id.find_player_error);
        textDescription = (TextView) rootView.findViewById(R.id.find_player_description);

        textQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    actionSearch.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

        actionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.hideKeyboard(getActivity());

                setMode(DataViewMode.LOADING);
                accountsList.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);
                textDescription.setVisibility(View.GONE);

                final String prefix = textQuery.getText().toString();
                final List<AccountShortInfo> accounts = new ArrayList<>();

                RequestExecutor.execute(
                        getActivity(),
                        new AccountsListRequestBuilder().setPrefix(prefix).setPage(1),
                        RequestUtils.wrapCallback(new ApiCallback<AccountsListResponse>() {
                            @Override
                            public void onSuccess(final AccountsListResponse response) {
                                final int approximate = response.accounts.size() * response.pagesCount;
                                if (approximate > ACCOUNTS_COUNT_THRESHOLD) {
                                    final Runnable cancelRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            setMode(DataViewMode.DATA);
                                        }
                                    };
                                    DialogUtils.showConfirmationDialog(
                                            getChildFragmentManager(),
                                            getString(R.string.common_dialog_attention_title),
                                            getString(R.string.find_player_too_many,
                                                    (approximate / ACCOUNTS_COUNT_GRANULARITY) * ACCOUNTS_COUNT_GRANULARITY),
                                            null, new Runnable() {
                                                @Override
                                                public void run() {
                                                    accounts.addAll(response.accounts);
                                                    loadAccounts(accounts, prefix, 2, response.pagesCount);
                                                }
                                            },
                                            null, cancelRunnable, cancelRunnable);
                                } else {
                                    accounts.addAll(response.accounts);
                                    loadAccounts(accounts, prefix, 2, response.pagesCount);
                                }
                            }

                            @Override
                            public void onError(AbstractApiResponse response) {
                                setError(response.errorMessage);
                            }
                        }, FindPlayerFragment.this));
            }
        });

        rootView.findViewById(R.id.find_player_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.hideKeyboard(getActivity());

                final List<Pair<Integer, String>> accounts = PreferencesManager.getFindPlayerHistory();
                if (accounts.size() == 0) {
                    setErrorText(getString(R.string.find_player_history_empty));
                } else {
                    Collections.reverse(accounts);
                    final List<AccountShortInfo> accountsList = new ArrayList<>(accounts.size());
                    final JSONObject accountJson = new JSONObject();
                    for(final Pair<Integer, String> account : accounts) {
                        try {
                            accountJson.put("id", account.first);
                            accountJson.put("name", account.second);
                            accountJson.put("might", 0.0);
                            accountJson.put("time_registered", 0);
                            accountsList.add(ObjectUtils.getModelFromJson(AccountShortInfo.class, accountJson));
                        } catch (JSONException ignored) {
                        }
                    }
                    setItems(accountsList);
                }
            }
        });

        rootView.findViewById(R.id.find_player_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAccount(0, null);
            }
        });

        return wrapView(layoutInflater, rootView);
    }

    private void loadAccounts(final List<AccountShortInfo> accounts, final String prefix,
                              final int pageStart, final int pageEnd) {
        if(pageEnd < pageStart) {
            UiUtils.runChecked(FindPlayerFragment.this, new Runnable() {
                @Override
                public void run() {
                    setItems(accounts);
                    setMode(DataViewMode.DATA);
                }
            });
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(R.string.game_find_player_progress);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(pageEnd);
        progressDialog.setProgress(pageStart - 1);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<AccountShortInfo> accountsSynchronized =
                        Collections.synchronizedList(new ArrayList<>(accounts));
                final ExecutorService executorService = Executors.newFixedThreadPool(10);
                final AtomicInteger progress = new AtomicInteger(pageStart - 1);
                final CountDownLatch latch = new CountDownLatch(pageEnd - pageStart + 1);
                for(int i = pageStart; i <= pageEnd; i++) {
                    final int page = i;
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            RequestExecutor.execute(
                                    getActivity(),
                                    new AccountsListRequestBuilder().setPrefix(prefix).setPage(page),
                                    new ApiCallback<AccountsListResponse>() {
                                        @Override
                                        public void onSuccess(AccountsListResponse response) {
                                            accountsSynchronized.addAll(response.accounts);
                                            UiUtils.runOnUiThread(FindPlayerFragment.this, new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.setProgress(progress.incrementAndGet());
                                                }
                                            });
                                            latch.countDown();
                                        }

                                        @Override
                                        public void onError(AbstractApiResponse response) {
                                            UiUtils.runOnUiThread(FindPlayerFragment.this, new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.setProgress(progress.incrementAndGet());
                                                }
                                            });
                                            latch.countDown();
                                        }
                                    });
                        }
                    });
                }

                executorService.shutdown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    UiUtils.runOnUiThread(FindPlayerFragment.this, new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            setError(getString(R.string.common_error));
                        }
                    });
                    return;
                }

                accounts.clear();
                accounts.addAll(accountsSynchronized);
                Collections.sort(accounts, new Comparator<AccountShortInfo>() {
                    @Override
                    public int compare(AccountShortInfo lhs, AccountShortInfo rhs) {
                        return lhs.name.compareTo(rhs.name);
                    }
                });
                UiUtils.runOnUiThread(FindPlayerFragment.this, new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        setItems(accounts);
                        setMode(DataViewMode.DATA);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textQuery.setText(PreferencesManager.getFindPlayerLastQuery());
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        textQuery.requestFocus();
        UiUtils.showKeyboard(getActivity());

        setMode(DataViewMode.DATA);
    }

    @Override
    public void onOffscreen() {
        super.onOffscreen();

        PreferencesManager.setFindPlayerLastQuery(textQuery.getText().toString());
        UiUtils.hideKeyboard(getActivity());
    }

    private void setItems(final List<AccountShortInfo> items) {
        final List<String> itemValues = new ArrayList<>(items.size());
        for(final AccountShortInfo item : items) {
            itemValues.add(item.name);
        }

        accountsList.setAdapter(new ChoiceDialog.ChoiceAdapter(
                layoutInflater,
                itemValues,
                new ChoiceDialog.ItemChooseListener() {
                    @Override
                    public void onItemSelected(int position) {
                        final AccountShortInfo item = items.get(position);
                        setAccount(item.id, item.name);
                    }
                }));

        textError.setVisibility(View.GONE);
        textDescription.setVisibility(View.GONE);
        accountsList.setVisibility(View.VISIBLE);
    }

    private void setErrorText(final String error) {
        textError.setText(error);

        textDescription.setVisibility(View.GONE);
        accountsList.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
    }

    private void setDescriptionText(final String description) {
        textDescription.setText(description);

        accountsList.setVisibility(View.GONE);
        textError.setVisibility(View.GONE);
        textDescription.setVisibility(View.VISIBLE);
    }

    private void setAccount(final int id, final String name) {
        PreferencesManager.setWatchingAccount(id, name);
        if((id != 0) && !TextUtils.isEmpty(name)) {
            PreferencesManager.addFindPlayerHistory(id, name);
        }

        final Activity activity = getActivity();
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).onNavigationDrawerItemSelected(DrawerItem.GAME);
        }
    }

}
