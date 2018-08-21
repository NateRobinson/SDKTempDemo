package com.arcblock.temp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.arcblock.corekit.bean.CoreKitBean;
import com.arcblock.corekit.viewmodel.CoreKitSubViewModel;
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
		doSubBtn = (Button)findViewById(R.id.do_sub_btn);
		jsonView = (JsonRecyclerView)findViewById(R.id.json_view);
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
	private void doSub(){
		NewBlockMinedSubscription newBlockMinedSubscription = new NewBlockMinedSubscription();

		CoreKitSubViewModel.CustomClientFactory<NewBlockMinedSubscription.Data, NewBlockMinedSubscription> factory =
				new CoreKitSubViewModel.CustomClientFactory<>(TempApplication.INSTANCE.abCoreKitClientEth(), newBlockMinedSubscription, NewBlockMinedSubscription.Data.class);

		CoreKitSubViewModel<NewBlockMinedSubscription.Data, NewBlockMinedSubscription> mDataCoreKitSubViewModel = CoreKitSubViewModel.getInstance(this, factory);
		mDataCoreKitSubViewModel.subscription()
				.setCoreKitSubCallBack(new CoreKitSubViewModel.CoreKitSubCallBack<NewBlockMinedSubscription.Data>() {
					@Override
					public void onNewData(CoreKitBean<NewBlockMinedSubscription.Data> coreKitBean) {
						// you can do your business logic with data here
						jsonView.bindJson(new Gson().toJson(coreKitBean));
					}
				});
	}
}
