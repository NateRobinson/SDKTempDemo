package com.arcblock.temp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
		findViewById(R.id.go_query_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, QueryDemoActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.go_page_query_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, PageQueryDemoActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.go_subscription_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SubscriptionDemoActivity.class);
				startActivity(intent);
			}
		});
	}
}
