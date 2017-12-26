package com.wangweijun.structure.ui.contributor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wangweijun.structure.R;
import com.wangweijun.structure.data.local.db.Account;
import com.wangweijun.structure.ui.base.BaseActivity;
import com.wangweijun.structure.ui.blacklist.BlacklistAdapter;
import com.wangweijun.structure.ui.blacklist.BlacklistMvpView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * Created by wangweijun1 on 2017/12/7.
 */

public class ContributorActivity extends BaseActivity implements BlacklistMvpView{

    @Inject
    ContributorPresenter blacklistPresenter;

    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    BlacklistAdapter blacklistAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_contributor_list);

        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(blacklistAdapter);

        blacklistPresenter.attachView(this);
        blacklistPresenter.load();
    }

    @OnClick(R.id.query_db)
    public void queryBtn() {

        blacklistPresenter.loadContributorsFromDBOnly();
    }

    @Override
    public void showLoading() {
        View loading = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_loading, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(CENTER_IN_PARENT);
        root.addView(loading, params);
    }

    @Override
    public void hideLoading() {
        root.removeView(root.findViewById(R.id.network_loading_pb));
    }

    @Override
    public void showErrorUI() {
        View errorContainer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_error, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(CENTER_IN_PARENT);
        root.addView(errorContainer, params);

        errorContainer.findViewById(R.id.view_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wang", "refresh ...");
//                ribotPresenter.refresh();
            }
        });
    }

    @Override
    public void hideErrorUI() {
        root.removeView(root.findViewById(R.id.view_container));
    }

    @Override
    public void showEmptyUI() {
        View errorContainer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_no_data, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(CENTER_IN_PARENT);
        root.addView(errorContainer, params);
    }

    @Override
    public void showDataLoadSuccess(List<Account> accounts) {
        blacklistAdapter.addModels(accounts);
    }
}
