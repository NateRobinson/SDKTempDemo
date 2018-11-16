package com.arcblock.temp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.arcblock.corekit.CoreKitQuery;
import com.arcblock.corekit.CoreKitResultListener;
import com.arcblock.temp.btc.BlockByHashQuery;
import com.google.gson.Gson;
import com.yuyh.jsonviewer.library.JsonRecyclerView;

public class QueryDemoActivity extends AppCompatActivity {

    private Button doQueryBtn;
    private JsonRecyclerView jsonView;
    private CoreKitQuery coreKitQuery;

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
        coreKitQuery = new CoreKitQuery(this, TempApplication.INSTANCE.abCoreKitClientBtc());
    }

    /**
     * do query fetch
     */
    private void doQuery() {
        coreKitQuery.query(BlockByHashQuery.builder().hash("0000000000000000000787c49ad1d41e05b124dee0280ef37d26684a9df0ee0e").build(), new CoreKitResultListener<BlockByHashQuery.Data>() {
            @Override
            public void onSuccess(BlockByHashQuery.Data data) {
                jsonView.bindJson(new Gson().toJson(data));
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
