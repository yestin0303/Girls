package yestin.girls.ui.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yestin.girls.R;
import yestin.girls.beans.firstPage.FirstPageImageList;
import yestin.girls.beans.firstPage.FirstPageInfo;
import yestin.girls.network.RetrofitUtils;
import yestin.girls.utils.ConstantUtil;
import yestin.girls.utils.GirlsUtil;
import yestin.girls.utils.LogUtil;
import yestin.girls.widget.RatioImageView;

/**
 * Created by yinlu on 2016/11/26.
 * 首页
 */

public class FirstPageFragment extends BaseFragment {
    String TAG = getClass().getSimpleName();

    @BindView(R.id.firstpage_rcleview)
    RecyclerView recyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsLoadMore = true;
    private int pageNum = 20;
    private int page = 1;
    private FirstPageAdapter mAdapter;
    private int type;
    private Realm realm;
    private List<FirstPageImageList> imageInfoList = new ArrayList<>();
    private boolean mIsRefreshing = false;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static final int PRELOAD_SIZE = 6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.firstpage_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mAdapter = new FirstPageAdapter();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addOnScrollListener(OnLoadMoreListener(staggeredGridLayoutManager));
        recyclerView.setAdapter(mAdapter);
        showProgress();
    }

    private void showProgress() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mIsRefreshing = true;
                clearRealmCache();
                getData();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);
        mIsRefreshing = true;
        getData();
    }

    private void getData() {
        RetrofitUtils.getFirstPageService()
                .getHuaBanMeizi(pageNum + "", page + "",
                        ConstantUtil.APP_ID,
                        ConstantUtil.APP_SIGN)
                .filter(new Func1<FirstPageInfo, Boolean>() {
                    @Override
                    public Boolean call(FirstPageInfo firstPageInfo) {
                        return firstPageInfo.getShowapi_res_code() == 0 ? true : false;
                    }
                })
                .map(new Func1<FirstPageInfo, List<FirstPageInfo.ShowapiResBodyBean.NewslistBean>>() {

                    @Override
                    public List<FirstPageInfo.ShowapiResBodyBean.NewslistBean> call(FirstPageInfo info) {

                        return info.getShowapi_res_body().getNewslist();
                    }
                })
                .doOnNext(new Action1<List<FirstPageInfo.ShowapiResBodyBean.NewslistBean>>() {
                    @Override
                    public void call(List<FirstPageInfo.ShowapiResBodyBean.NewslistBean> newslistBeen) {
                        GirlsUtil.getInstance().putFirstImageCache(newslistBeen);
                        LogUtil.i(TAG, "进到doonnext" + newslistBeen);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FirstPageInfo.ShowapiResBodyBean.NewslistBean>>() {
                    @Override
                    public void call(List<FirstPageInfo.ShowapiResBodyBean.NewslistBean> newslistBeen) {
                        if (newslistBeen.size() > 0) {

                            realm = Realm.getDefaultInstance();
                            imageInfoList = realm.where(FirstPageImageList.class).findAll();
                            LogUtil.i(TAG, "得到的imageinfo=" + imageInfoList);
                        }
                        finishTask();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


    }

    private void finishTask() {
        mAdapter.notifyDataSetChanged();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mIsRefreshing = false;
    }

    private void clearRealmCache() {
        LogUtil.i(TAG, "开始clearRealmCache");
        try {
            realm.beginTransaction();
            realm.where(FirstPageImageList.class)
                    .findAll().deleteAllFromRealm();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    RecyclerView.OnScrollListener OnLoadMoreListener(final StaggeredGridLayoutManager layoutManager) {

        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(
                        new int[2])[1] >= mAdapter.getItemCount() - PRELOAD_SIZE;
                if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
                    if (!mIsLoadMore) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        page++;
                        getData();
                    } else {
                        mIsLoadMore = false;
                    }
                }
            }
        };
    }


    class FirstPageAdapter extends RecyclerView.Adapter<FirstPageAdapter.ItemViewHolder> {
        @Override
        public FirstPageAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.card_item_firstpage, parent, false));

        }

        @Override
        public void onBindViewHolder(final FirstPageAdapter.ItemViewHolder itemViewHolder, int position) {
            itemViewHolder.imageName.setText(imageInfoList.get(position).getTitle());
            Glide.with(getContext())
                    .load(imageInfoList.get(position).getPicUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_image)
                    .into(itemViewHolder.imageView)
                    .getSize(new SizeReadyCallback() {

                        @Override
                        public void onSizeReady(int width, int height) {

                            if (!itemViewHolder.item.isShown()) {
                                itemViewHolder.item.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            LogUtil.i(TAG, "count=" + (imageInfoList == null ? 0 : imageInfoList.size()));
            return imageInfoList == null ? 0 : imageInfoList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            RatioImageView imageView;
            TextView imageName;
            public View item;

            public ItemViewHolder(View itemView) {
                super(itemView);
                item = itemView;
                imageView = (RatioImageView) itemView.findViewById(R.id.item_image);
                imageName = (TextView) itemView.findViewById(R.id.item_title);
                imageView.setOriginalSize(50, 50);
            }
        }
    }
}
