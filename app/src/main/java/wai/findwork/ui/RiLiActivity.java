package wai.findwork.ui;

import android.content.Intent;
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

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        titleBar.setRightClick(() -> startActivityForResult(new Intent(this, AddRiLiActivity.class), 11));
        list = new ArrayList<>();
        riliLv=(PullToRefreshListView)findViewById(R.id.rili_lv);
        riliLv.setMode(PullToRefreshBase.Mode.BOTH);
        right_list = new ArrayList<>();
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void initEvents() {
        switch (type) {
            case "left":
                commonAdapter = new CommonAdapter<RiLi>(this, list, R.layout.item_new) {
                    @Override
                    public void convert(CommonViewHolder holder, RiLi riLi, int position) {
                        holder.setText(R.id.title_tv, riLi.getTitle());
                        if(riLi.getContent().length()>20){
                            holder.setText(R.id.content_tv, riLi.getContent().substring(0,20));
                        }else {
                            holder.setText(R.id.content_tv, riLi.getContent());
                        }
                        holder.setText(R.id.content_create_time,riLi.getUpdatedAt());
                    }
                };
                riliLv.setAdapter(commonAdapter);
                refresh();
                break;
            default:
                rightAdapter = new CommonAdapter<UserBuy>(this, right_list, R.layout.item_new) {
                    @Override
                    public void convert(CommonViewHolder holder, UserBuy riLi, int position) {
                        holder.setText(R.id.title_tv, riLi.getUser().getRealname());
                        holder.setText(R.id.content_tv, riLi.getUser().getUsername());
                        holder.setText(R.id.content_create_time,riLi.getUser().getCreatedAt());
                    }
                };
                riliLv.setAdapter(rightAdapter);
                loadRight();
                break;
        }
        riliLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(RiLiActivity.this,AddRiLiActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("RILIMODEL",list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void loadRight() {
        BmobQuery<UserBuy> query = new BmobQuery<>();
        UserInfo buyer = new UserInfo();
        buyer.setObjectId(Utils.getCache(Config.KEY_ID));
        query.addWhereEqualTo("buyer", buyer);
        query.include("user");
        query.findObjects(new FindListener<UserBuy>() {
            @Override
            public void done(List<UserBuy> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    rightAdapter.refresh(list);
                }
            }
        });
    }

    private void refresh() {
        BmobQuery<RiLi> query = new BmobQuery<>();
        UserInfo buyer = new UserInfo();
        buyer.setObjectId(Utils.getCache(Config.KEY_ID));
        query.addWhereEqualTo("user", buyer);
        query.findObjects(new FindListener<RiLi>() {
            @Override
            public void done(List<RiLi> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    commonAdapter.refresh(list);
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
            refresh();
        }
    }
}