package com.arcblock.temp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.arcblock.corekit.ABCoreKitClient;
import com.arcblock.corekit.CoreKitSubscription;
import com.arcblock.corekit.bean.CoreKitBean;
import com.arcblock.corekit.viewmodel.CoreKitSubscriptionViewModel;
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
        NewBlockMinedSubscriptionHelper newBlockMinedSubscriptionHelper = new NewBlockMinedSubscriptionHelper(this, TempApplication.INSTANCE.abCoreKitClientEth());
        newBlockMinedSubscriptionHelper.setCoreKitSubCallBack(new CoreKitSubscriptionViewModel.CoreKitSubCallBack<NewBlockMinedSubscription.Data>() {
            @Override
            public void onNewData(CoreKitBean<NewBlockMinedSubscription.Data> coreKitBean) {
                // you can do your business logic with data here
                jsonView.bindJson(new Gson().toJson(coreKitBean));
            }
        });
    }

    /**
     * NewBlockMinedSubscriptionHelper for NewBlockMinedSubscription
     */
    private class NewBlockMinedSubscriptionHelper extends CoreKitSubscription<NewBlockMinedSubscription.Data, NewBlockMinedSubscription> {

        public NewBlockMinedSubscriptionHelper(FragmentActivity activity, ABCoreKitClient client) {
            super(activity, client);
        }

        @Override
        public NewBlockMinedSubscription getSubscription() {
            return new NewBlockMinedSubscription();
        }

        @Override
        public Class<NewBlockMinedSubscription.Data> getResultDataClass() {
            return NewBlockMinedSubscription.Data.class;
        }
    }
}
