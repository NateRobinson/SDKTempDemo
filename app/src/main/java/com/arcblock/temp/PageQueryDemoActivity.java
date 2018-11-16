package com.arcblock.temp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.apollographql.apollo.api.Query;
import com.arcblock.corekit.CoreKitPagedQuery;
import com.arcblock.corekit.CoreKitPagedQueryResultListener;
import com.arcblock.corekit.PagedQueryHelper;
import com.arcblock.temp.btc.BlocksByHeightQuery;
import com.arcblock.temp.btc.type.PageInput;
import com.google.gson.Gson;
import com.yuyh.jsonviewer.library.JsonRecyclerView;

import java.util.List;

public class PageQueryDemoActivity extends AppCompatActivity {

    private Button doQueryBtn;
    private Button nextPageBtn;
    private JsonRecyclerView jsonView;
    private int startIndex = 448244;
    private int endIndex = 448264;
    private boolean isFirstIn;
    private PagedQueryHelper<BlocksByHeightQuery.Data, BlocksByHeightQuery.Datum> dataDatumPagedQueryHelper;
    private CoreKitPagedQuery<BlocksByHeightQuery.Data, BlocksByHeightQuery.Datum> mCoreKitPagedQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_query_demo);

        isFirstIn = true;
        initView();
        initData();
    }

    /**
     * init view
     */
    private void initView() {
        doQueryBtn = (Button) findViewById(R.id.do_query_btn);
        nextPageBtn = (Button) findViewById(R.id.next_page_btn);
        jsonView = (JsonRecyclerView) findViewById(R.id.json_view);
        nextPageBtn.setVisibility(View.GONE);
    }

    /**
     * init data
     */
    private void initData() {

        dataDatumPagedQueryHelper = new PagedQueryHelper<BlocksByHeightQuery.Data, BlocksByHeightQuery.Datum>() {
            @Override
            public Query getInitialQuery() {
                return BlocksByHeightQuery.builder().fromHeight(startIndex).toHeight(endIndex).build();
            }

            @Override
            public Query getLoadMoreQuery() {
                PageInput pageInput = null;
                if (!TextUtils.isEmpty(getCursor())) {
                    pageInput = PageInput.builder().cursor(getCursor()).build();
                }
                return BlocksByHeightQuery.builder().fromHeight(startIndex).toHeight(endIndex).paging(pageInput).build();
            }

            @Override
            public List<BlocksByHeightQuery.Datum> map(BlocksByHeightQuery.Data data) {
                if (data.getBlocksByHeight() != null) {
                    // set page info to CoreKitPagedQuery
                    if (data.getBlocksByHeight().getPage() != null) {
                        // set is have next flag to CoreKitPagedQuery
                        setHasMore(data.getBlocksByHeight().getPage().isNext());
                        // set new cursor to CoreKitPagedQuery
                        setCursor(data.getBlocksByHeight().getPage().getCursor());
                    }
                    return data.getBlocksByHeight().getData();
                }
                return null;
            }
        };

        mCoreKitPagedQuery = new CoreKitPagedQuery<>(this, TempApplication.INSTANCE.abCoreKitClientBtc(), dataDatumPagedQueryHelper);
        mCoreKitPagedQuery.setPagedQueryResultListener(new CoreKitPagedQueryResultListener<BlocksByHeightQuery.Datum>() {
            @Override
            public void onSuccess(List<BlocksByHeightQuery.Datum> list) {
                // you can do your business logic with data here
                jsonView.bindJson(new Gson().toJson(list));

                if (dataDatumPagedQueryHelper.isHasMore()) {
                    nextPageBtn.setVisibility(View.VISIBLE);
                } else {
                    nextPageBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        doQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstIn) {
                    mCoreKitPagedQuery.startInitQuery();
                    isFirstIn = false;
                } else {
                    mCoreKitPagedQuery.startInitQuery();
                }
            }
        });
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCoreKitPagedQuery.startLoadMoreQuery();
            }
        });

    }
}
