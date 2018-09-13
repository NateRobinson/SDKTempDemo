package com.arcblock.temp;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.arcblock.corekit.ABCoreKitClient;
import com.arcblock.corekit.CoreKitQuery;
import com.arcblock.corekit.bean.CoreKitBean;
import com.arcblock.temp.btc.BlockByHashQuery;
import com.google.gson.Gson;
import com.yuyh.jsonviewer.library.JsonRecyclerView;

public class QueryDemoActivity extends AppCompatActivity {

    private Button doQueryBtn;
    private JsonRecyclerView jsonView;
    private BlockByHashQueryHelper mBlockByHashQueryHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_demo);

        initView();
        initData();
    }

    /**
     * init view
     */
    private void initView() {
        doQueryBtn = (Button) findViewById(R.id.do_query_btn);
        jsonView = (JsonRecyclerView) findViewById(R.id.json_view);
    }

    /**
     * init data
     */
    private void initData() {
        doQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQuery();
            }
        });

        // init BlockByHashQueryHelper
        mBlockByHashQueryHelper = new BlockByHashQueryHelper(this, this, TempApplication.INSTANCE.abCoreKitClientBtc());
    }

    /**
     * do query fetch
     */
    private void doQuery() {
        mBlockByHashQueryHelper.setObserve(new Observer<CoreKitBean<BlockByHashQuery.BlockByHash>>() {
            @Override
            public void onChanged(@Nullable CoreKitBean<BlockByHashQuery.BlockByHash> coreKitBean) {
                // you can do your business logic with data here
                jsonView.bindJson(new Gson().toJson(coreKitBean));
            }
        });
    }

    /**
     * BlockByHashQueryHelper for BlockByHashQuery
     */
    private class BlockByHashQueryHelper extends CoreKitQuery<BlockByHashQuery.Data, BlockByHashQuery.BlockByHash> {

        public BlockByHashQueryHelper(FragmentActivity activity, LifecycleOwner lifecycleOwner, ABCoreKitClient client) {
            super(activity, lifecycleOwner, client);
        }

        @Override
        public BlockByHashQuery.BlockByHash map(Response<BlockByHashQuery.Data> dataResponse) {
            if (dataResponse != null) {
                return dataResponse.data().getBlockByHash();
            }
            return null;
        }

        @Override
        public Query getQuery() {
            return BlockByHashQuery.builder().hash("0000000000000000000787c49ad1d41e05b124dee0280ef37d26684a9df0ee0e").build();
        }
    }
}
