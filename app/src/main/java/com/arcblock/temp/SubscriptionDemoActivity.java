package com.arcblock.temp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.arcblock.corekit.CoreKitSubscription;
import com.arcblock.corekit.CoreKitSubscriptionResultListener;
import com.arcblock.temp.eth.NewBlockMinedSubscription;
import com.google.gson.Gson;
import com.yuyh.jsonviewer.library.JsonRecyclerView;

public class SubscriptionDemoActivity extends AppCompatActivity {

    private Button doSubBtn;
    private JsonRecyclerView jsonView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_demo);

        initView();
        initData();
    }

    /**
     * init view
     */
    private void initView() {
        doSubBtn = (Button) findViewById(R.id.do_sub_btn);
        jsonView = (JsonRecyclerView) findViewById(R.id.json_view);
    }

    /**
     * init data
     */
    private void initData() {
        doSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSub();
            }
        });
    }

    /**
     * do sub
     */
    private void doSub() {
        CoreKitSubscription<NewBlockMinedSubscription.Data, NewBlockMinedSubscription> coreKitSubscription = new CoreKitSubscription<>(this, TempApplication.INSTANCE.abCoreKitClientEth()
                , new NewBlockMinedSubscription(), NewBlockMinedSubscription.Data.class);
        coreKitSubscription.setResultListener(new CoreKitSubscriptionResultListener<NewBlockMinedSubscription.Data>() {
            @Override
            public void onSuccess(NewBlockMinedSubscription.Data data) {
                jsonView.bindJson(new Gson().toJson(data));
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }
}
