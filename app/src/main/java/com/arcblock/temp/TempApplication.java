package com.arcblock.temp;

import android.app.Application;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.arcblock.corekit.ABCoreKitClient;
import com.arcblock.corekit.config.CoreKitConfig;
import com.arcblock.temp.btc.type.CustomType;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;

public class TempApplication extends Application {

	public static TempApplication INSTANCE = null;
	/**
	 * the BTC ABCoreClient singleton
	 */
	private ABCoreKitClient mABCoreClientBtc;
	/**
	 * the ETH ABCoreClient singleton
	 */
	private ABCoreKitClient mABCoreClientEth;

	@Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;

		/**
		 * for output logs
		 */
		Timber.plant(new Timber.DebugTree());

		/**
		 * ABCoreKitClient debug switch, control log output...
		 */
		ABCoreKitClient.IS_DEBUG = true;

		initBtcClient();
		initEthClient();
	}

	/**
	 * init a BTC ABCoreClient
	 */
	private void initBtcClient(){
		/**
		 * custom a date type for code gen
		 */
		CustomTypeAdapter dateCustomTypeAdapter = new CustomTypeAdapter<Date>() {
			@Override
			public Date decode(CustomTypeValue value) {
				try {
					SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000000'Z'");
					utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));//时区定义并进行时间获取
					Date gpsUTCDate = utcFormat.parse(value.value.toString());
					return gpsUTCDate;
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public CustomTypeValue encode(Date value) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000000'Z'");
				return new CustomTypeValue.GraphQLString(sdf.format(value));
			}
		};

		/**
		 * build a ABCoreClient
		 */
		mABCoreClientBtc = ABCoreKitClient.builder(this, CoreKitConfig.ApiType.API_TYPE_BTC)
				.addCustomTypeAdapter(CustomType.DATETIME, dateCustomTypeAdapter)
				.setOpenOkHttpLog(true)
				.setDefaultResponseFetcher(ApolloResponseFetchers.CACHE_AND_NETWORK)
				.build();
	}

	/**
	 * init a ETH ABCoreClient
	 */
	private void initEthClient(){
		/**
		 * build a ABCoreClient
		 */
		mABCoreClientEth = ABCoreKitClient.builder(this, CoreKitConfig.ApiType.API_TYPE_ETH)
				.setOpenOkHttpLog(true)
				.setOpenSocket(true)
				.setDefaultResponseFetcher(ApolloResponseFetchers.CACHE_AND_NETWORK)
				.build();
	}

	@NotNull
	public ABCoreKitClient abCoreKitClientBtc() {
		if (mABCoreClientBtc == null) {
			initBtcClient();
		}
		return mABCoreClientBtc;
	}

	@NotNull
	public ABCoreKitClient abCoreKitClientEth() {
		if (mABCoreClientEth == null) {
			initEthClient();
		}
		return mABCoreClientEth;
	}
}
