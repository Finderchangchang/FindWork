package wai.findwork.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.adapter.CommonAdapter;
import wai.findwork.adapter.CommonViewHolder;
import wai.findwork.method.Utils;
import wai.findwork.model.Config;
import wai.findwork.model.RiLi;
import wai.findwork.model.UserBuy;
import wai.findwork.model.UserInfo;

/**
 * 记事本列表
 * Created by Finder丶畅畅 on 2017/2/4 22:48
 * QQ群481606175
 */

public class RiLiActivity extends BaseActivity {
    CommonAdapter<RiLi> commonAdapter;
    CommonAdapter<UserBuy> rightAdapter;
    List<RiLi> list;
    List<UserBuy> right_list;
    @Bind(R.id.title_bar)
    TitleBar titleBar;
    //@Bind(R.id.rili_lv)
    PullToRefreshListView riliLv;
    String type;
    private int totalNum = 1;
    private int page = 1;

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        titleBar.setRightClick(() -> startActivityForResult(new Intent(this, AddRiLiActivity.class), 11));
        list = new ArrayList<>();
        riliLv = (PullToRefreshListView) findViewById(R.id.rili_lv);
        riliLv.setMode(PullToRefreshBase.Mode.BOTH);
        right_list = new ArrayList<>();
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void initEvents() {
        switch (type) {
            case "left":
                titleBar.setCentertv("私密工作日志");
                titleBar.setRightClose(true);
                commonAdapter = new CommonAdapter<RiLi>(this, list, R.layout.item_new) {
                    @Override
                    public void convert(CommonViewHolder holder, RiLi riLi, int position) {
                        holder.setText(R.id.title_tv, riLi.getTitle());
                        if (riLi.getContent().length() > 20) {
                            holder.setText(R.id.content_tv, riLi.getContent().substring(0, 20));
                        } else {
                            holder.setText(R.id.content_tv, riLi.getContent());
                        }
                        holder.setText(R.id.content_create_time, riLi.getUpdatedAt());
                    }
                };
                riliLv.setAdapter(commonAdapter);
                refresh();
                riliLv.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(RiLiActivity.this, AddRiLiActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RILIMODEL", list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 11);
                });
                break;
            default:
                titleBar.setCentertv("交易记录");
                titleBar.setRightClose(false);
                rightAdapter = new CommonAdapter<UserBuy>(this, right_list, R.layout.item_new) {
                    @Override
                    public void convert(CommonViewHolder holder, UserBuy riLi, int position) {
                        holder.setText(R.id.title_tv, riLi.getBuyer().getRealname());
                        holder.setText(R.id.content_tv, riLi.getBuyer().getUsername());
                        holder.setText(R.id.content_create_time, riLi.getBuyer().getCreatedAt());
                        holder.setOnClickListener(R.id.call_tel_btn, v -> {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + riLi.getBuyer().getUsername());
                            intent.setData(data);
                            startActivity(intent);
                        });
                        holder.setVisible(R.id.call_tel_btn, true);
                    }
                };
                riliLv.setAdapter(rightAdapter);
                riliLv.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(RiLiActivity.this, PersonDetailActivity.class);
                    intent.putExtra("user", right_list.get(position - 1).getBuyer());
                    startActivity(intent);
                });
                loadRight();
                break;
        }
        riliLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //刷新
                page = 1;
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载下一页
                if (page <= totalNum) {
                    page = page + 1;
                    refresh();
                } else {
                    ToastShort("已经是最后一页");
                }

            }
        });
    }

    private void loadRight() {
        page = 1;
        BmobQuery<UserBuy> query = new BmobQuery<>();
        UserInfo buyer = new UserInfo();
        buyer.setObjectId(Utils.getCache(Config.KEY_ID));
        query.addWhereEqualTo("user", buyer);
        query.include("buyer.type");
        query.count(UserBuy.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {

                if (e == null) {
                    totalNum = integer;
                    query.setLimit(20);
                    query.setSkip((page - 1) * 20);
                    query.findObjects(new FindListener<UserBuy>() {
                        @Override
                        public void done(List<UserBuy> lists, BmobException e) {
                            riliLv.onRefreshComplete();
                            if (e == null && lists.size() > 0) {
                                if (page == 1) {
                                    right_list.removeAll(right_list);
                                }
                                right_list.addAll(lists);
                                rightAdapter.refresh(right_list);

                            }
                        }
                    });
                } else {
                    riliLv.onRefreshComplete();
                    ToastShort("加载失败");
                }
            }
        });

    }

    private void refresh() {
        page = 1;
        BmobQuery<RiLi> query = new BmobQuery<>();
        UserInfo buyer = new UserInfo();
        buyer.setObjectId(Utils.getCache(Config.KEY_ID));
        query.addWhereEqualTo("user", buyer);
        query.count(RiLi.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    totalNum = integer;
                    query.setLimit(20);
                    query.setSkip((page - 1) * 20);
                    query.findObjects(new FindListener<RiLi>() {
                        @Override
                        public void done(List<RiLi> lists, BmobException e) {
                            riliLv.onRefreshComplete();
                            if (e == null && lists.size() > 0) {
                                if (page == 1) {
                                    list.removeAll(list);
                                }
                                list.addAll(lists);
                                commonAdapter.refresh(list);
                            }
                        }
                    });
                    
                } else {
                    riliLv.onRefreshComplete();
                    ToastShort("加载失败");
                }
            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.ac_ri_li;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99) {//保存成功，执行刷新操作
            page = 1;
            refresh();
        }
    }
}
