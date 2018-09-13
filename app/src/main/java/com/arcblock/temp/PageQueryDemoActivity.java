package com.arcblock.temp;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.arcblock.corekit.ABCoreKitClient;
import com.arcblock.corekit.CoreKitPagedQuery;
import com.arcblock.corekit.bean.CoreKitPagedBean;
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
    private BlocksByHeightQueryHelper mBlocksByHeightQueryHelper;

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
        doQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstIn) {
                    doQuery();
                    isFirstIn = false;
                } else {
                    mBlocksByHeightQueryHelper.refresh();
                }
            }
        });
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlocksByHeightQueryHelper.loadMore();
            }
        });


        // init BlocksByHeightQueryHelper
        mBlocksByHeightQueryHelper = new BlocksByHeightQueryHelper(this, this, TempApplication.INSTANCE.abCoreKitClientBtc());
    }

    /**
     * do query fetch
     */
    private void doQuery() {
        mBlocksByHeightQueryHelper.setObserve(new Observer<CoreKitPagedBean<List<BlocksByHeightQuery.Datum>>>() {
            @Override
            public void onChanged(@Nullable CoreKitPagedBean<List<BlocksByHeightQuery.Datum>> coreKitPagedBean) {
                // you can do your business logic with data here
                jsonView.bindJson(new Gson().toJson(coreKitPagedBean));

                if (mBlocksByHeightQueryHelper.isHasMore()) {
                    nextPageBtn.setVisibility(View.VISIBLE);
                } else {
                    nextPageBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * BlocksByHeightQueryHelper for BlocksByHeightQuery
     */
    private class BlocksByHeightQueryHelper extends CoreKitPagedQuery<BlocksByHeightQuery.Data, BlocksByHeightQuery.Datum> {

        public BlocksByHeightQueryHelper(FragmentActivity activity, LifecycleOwner lifecycleOwner, ABCoreKitClient client) {
            super(activity, lifecycleOwner, client);
        }

        @Override
        public List<BlocksByHeightQuery.Datum> map(Response<BlocksByHeightQuery.Data> dataResponse) {
            if (dataResponse != null && dataResponse.data().getBlocksByHeight() != null) {
                // set page info to CoreKitPagedQuery
                if (dataResponse.data().getBlocksByHeight().getPage() != null) {
                    // set is have next flag to CoreKitPagedQuery
                    setHasMore(dataResponse.data().getBlocksByHeight().getPage().isNext());
                    // set new cursor to CoreKitPagedQuery
                    setCursor(dataResponse.data().getBlocksByHeight().getPage().getCursor());
                }
                return dataResponse.data().getBlocksByHeight().getData();
            }
            return null;
        }

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
        public Query getRefreshQuery() {
            return BlocksByHeightQuery.builder().fromHeight(startIndex).toHeight(endIndex).build();
        }
    }
}
