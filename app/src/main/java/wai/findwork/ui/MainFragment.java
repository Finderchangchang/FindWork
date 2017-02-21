package wai.findwork.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wai.findwork.R;
import wai.findwork.adapter.CategoryAdapter;
import wai.findwork.adapter.CommonAdapter;
import wai.findwork.adapter.CommonViewHolder;
import wai.findwork.method.HttpUtil;
import wai.findwork.method.Utils;
import wai.findwork.model.ArticleModel;
import wai.findwork.model.CodeModel;
import wai.findwork.model.Config;
import wai.findwork.model.IPAddress;
import wai.findwork.model.TypeOfWorkModel;
import wai.findwork.model.URL;
import wai.findwork.model.UserInfo;
import wai.findwork.view.GlideCircleTransform;

/**
 * Created by Finder丶畅畅 on 2017/1/17 21:16
 * QQ群481606175
 */
public class MainFragment extends Fragment implements CategoryAdapter.OnItemClickListener {
    RecyclerView recyclerviewCategory;

    private List<CodeModel> categoryList;
    private CategoryAdapter categoryAdapter;
    private int oldSelectedPosition = 0;
    LinearLayout main_ll;
    List<ArticleModel> articleModels;

    public static MainFragment newInstance(int content) {
        MainFragment fragment = new MainFragment();
        fragment.mContent = content;
        return fragment;
    }

    CommonAdapter<ArticleModel> articleModelCommonAdapter;
    private int mContent = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FinalDb.create(MainActivity.main);
        list = new ArrayList<>();
        articleModels = new ArrayList<>();
        commonAdapter = new CommonAdapter<UserInfo>(MainActivity.main, list, R.layout.item_team) {
            @Override
            public void convert(CommonViewHolder holder, UserInfo userInfo, int position) {
                holder.setText(R.id.name_tv, userInfo.getRealname());
                holder.setGliImage(R.id.user_iv, userInfo.getIconurl());
                holder.setText(R.id.price_tv, userInfo.getGongzi());
                holder.setText(R.id.content_tv, userInfo.getRemark());
            }
        };
        articleModelCommonAdapter = new CommonAdapter<ArticleModel>(MainActivity.main, articleModels, R.layout.item_new) {
            @Override
            public void convert(CommonViewHolder holder, ArticleModel articleModel, int position) {
                holder.setText(R.id.title_tv, articleModel.getTitle());
                holder.setText(R.id.content_tv, articleModel.getContent());
            }
        };
    }

    LinearLayout no_data_ll;
    PullToRefreshListView right_lv;
    TextView no_data_mes;
    ImageView user_iv;
    TextView user_name_tv;
    TextView user_type_tv;
    TextView id_card_tv;
    LinearLayout user_left_ll;
    LinearLayout user_right_ll;
    TextView tel_tv;
    TextView gz_tv;
    TextView remark_tv;
    TextView ts_tv;
    TextView tq_tv;
    LinearLayout user_center_ll;
    LinearLayout user_bottom_ll;
    TextView exit_tv;
    TextView about_us_tv;
    TextView location_tv;
    TextView qq_wx_tv;
    int page = 1;
    int positionIndex = 0;
    int totalPage = 0;
    TextView title_tv;
    LinearLayout grzl_ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        switch (mContent) {
            case 0:
                view = inflater.inflate(R.layout.frag_user, container, false);
                grzl_ll = (LinearLayout) view.findViewById(R.id.grzl_ll);
                about_us_tv = (TextView) view.findViewById(R.id.about_us_tv);
                location_tv = (TextView) view.findViewById(R.id.location_tv);
                qq_wx_tv = (TextView) view.findViewById(R.id.qq_wx_tv);
                exit_tv = (TextView) view.findViewById(R.id.exit_tv);
                user_iv = (ImageView) view.findViewById(R.id.user_iv);
                user_name_tv = (TextView) view.findViewById(R.id.user_name_tv);
                user_type_tv = (TextView) view.findViewById(R.id.user_type_tv);
                id_card_tv = (TextView) view.findViewById(R.id.id_card_tv);
                user_left_ll = (LinearLayout) view.findViewById(R.id.user_left_ll);
                user_right_ll = (LinearLayout) view.findViewById(R.id.user_right_ll);
                tel_tv = (TextView) view.findViewById(R.id.tel_tv);
                gz_tv = (TextView) view.findViewById(R.id.gz_tv);
                remark_tv = (TextView) view.findViewById(R.id.remark_tv);
                ts_tv = (TextView) view.findViewById(R.id.ts_tv);
                tq_tv = (TextView) view.findViewById(R.id.tq_tv);
                user_center_ll = (LinearLayout) view.findViewById(R.id.user_center_ll);
                user_bottom_ll = (LinearLayout) view.findViewById(R.id.user_bottom_ll);
                break;
            case 4:
                view = inflater.inflate(R.layout.frag_zx, container, false);
                ListView listView = (ListView) view.findViewById(R.id.zx_lv);
                listView.setAdapter(articleModelCommonAdapter);
                listView.setOnItemClickListener((parent, view1, position, id) ->
                        Utils.IntentPost(NewDetailActivity.class, intent -> intent.putExtra(Config.KEY_NEW_ID, articleModels.get(position)))
                );
                break;
            default:
                view = inflater.inflate(R.layout.frag_gz, container, false);
                title_tv = (TextView) view.findViewById(R.id.title_tv);
                switch (mContent) {
                    case 1:
                        title_tv.setText("最美建设者工种大全");
                        break;
                    case 2:
                        title_tv.setText("最美建设者班组大全");
                        break;
                    default:
                        title_tv.setText("最美建设者施工项目大全");
                        break;
                }
                recyclerviewCategory = (RecyclerView) view.findViewById(R.id.recyclerview_category);
                main_ll = (LinearLayout) view.findViewById(R.id.main_ll);
                no_data_ll = (LinearLayout) view.findViewById(R.id.no_data_ll);
                no_data_mes = (TextView) view.findViewById(R.id.no_data_mes);
                right_lv = (PullToRefreshListView) view.findViewById(R.id.right_lv);
                right_lv.setMode(PullToRefreshBase.Mode.BOTH);
                categoryList = new ArrayList<>();
                break;
        }
        return view;
    }

    UserInfo info;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (mContent) {
            case 0:
                refrush();
                location_tv.setOnClickListener(v -> {
                });//定位
                user_left_ll.setOnClickListener(v ->
                        Utils.IntentPost(RiLiActivity.class, intent -> intent.putExtra("type", "left"))
                );
                user_right_ll.setOnClickListener(v ->
                        Utils.IntentPost(RiLiActivity.class, intent -> intent.putExtra("type", "right"))
                );
                user_center_ll.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.main, RegPersonActivity.class);
                    intent.putExtra("UserInfo", info);
                    startActivityForResult(intent, 102);
                });
                grzl_ll.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.main, RegPersonActivity.class);
                    intent.putExtra("UserInfo", info);
                    startActivityForResult(intent, 102);
                });
                about_us_tv.setOnClickListener(v -> Utils.IntentPost(WebActivity.class, intent -> intent.putExtra("url", "about_us")));
                exit_tv.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.main);
                    builder.setTitle("提示");
                    builder.setMessage("确定要退出当前账号吗？");
                    builder.setPositiveButton("取消", (dialog1, which) -> {

                    });
                    builder.setNegativeButton("确定", (dialogInterface, i) -> {
                        if (Utils.getCache(Config.KEY_CHECK).equals("0")) {
                            Utils.putCache(Config.KEY_PassWord, "");
                        }

                        Utils.putCache(Config.KEY_ID, "");
                        Utils.putCache(Config.KEY_NEW_ID, "");
                        BmobUser.logOut();
                        MainActivity.main.finish();
                    });
                    builder.show();
                });
                HttpUtil.load(URL.ip_address)
                        .getIpAddress()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ipAddress -> {
                            Utils.putCache(Config.KEY_CITY, ipAddress.getCity());
                            location_tv.setText(ipAddress.getCity());
                            HttpUtil.load(URL.city_weather)
                                    .getWeather(ipAddress.getCity())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(model -> {
                                        tq_tv.setText(model.getData().getForecast().get(0).getType() + "\n" + model.getData().getWendu() + "℃");
                                        ts_tv.setText("温馨提示：" + model.getData().getGanmao());
                                    }, throwable -> {
                                    });
                        }, throwable -> {
                        });
                break;
            case 4:
                BmobQuery<ArticleModel> query = new BmobQuery<>();
                query.findObjects(new FindListener<ArticleModel>() {
                    @Override
                    public void done(List<ArticleModel> list, BmobException e) {
                        if (e == null) {
                            articleModelCommonAdapter.refresh(list);
                            articleModels = list;
                        }
                    }
                });

                break;
            default:
                initViews();
                break;
        }
    }

    FinalDb db;

    //刷新个人信息
    private void refrush() {
        List<UserInfo> list = db.findAll(UserInfo.class);
        if (list.size() > 0) {
            info = list.get(0);
            if (!info.getIconurl().equals("")) {
                Glide.with(MainActivity.main)
                        .load(info.getIconurl()).transform(new GlideCircleTransform(MainActivity.main))
                        .into(user_iv);
            } else {
                user_iv.setImageResource(R.mipmap.myheader);
            }
            user_name_tv.setText(info.getRealname());
            qq_wx_tv.setText("QQ或微信：" + info.getQq_wx());
            if (!TextUtils.isEmpty(info.getCardnum())) {
                String val = info.getCardnum();
                id_card_tv.setText(val.substring(0, val.length() - 4) + "****");
            }

            user_type_tv.setText(info.getTypeName());
            tel_tv.setText("电话：" + Utils.getCache(Config.KEY_User_ID));
            switch (Utils.getCache(Config.KEY_TYPE_STATE)) {
                case "1":
                    gz_tv.setText("工资：" + info.getGongzi());
                    remark_tv.setText("简历：" + info.getRemark());
                    break;
                case "2":
                    gz_tv.setText("日工资：" + info.getGongzi());
                    remark_tv.setText("班组简介：" + info.getRemark());
                    break;
                default:
                    gz_tv.setText("所需班组：" + info.getGongzi());
                    remark_tv.setText("工程概况：" + info.getRemark());
                    break;
            }
        }
    }

    private void initViews() {
        recyclerviewCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryAdapter = new CategoryAdapter(MainActivity.main, categoryList);
        categoryAdapter.setOnItemClickListener(this);
        recyclerviewCategory.setAdapter(categoryAdapter);
        right_lv.setAdapter(commonAdapter);
        dialog = new ProgressDialog(MainActivity.main);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
        right_lv.setOnItemClickListener((parent, view, position, id) -> {
            UserInfo userInfo = list.get(position - 1);
            userInfo.setTypeName(categoryList.get(old_position).getName());
            Utils.IntentPost(PersonDetailActivity.class, intent -> {
                intent.putExtra("user", list.get(position - 1));
                intent.putExtra("index", mContent + "");
            });
        });
        right_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //刷新
                page = 1;
                changeSelected(positionIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载下一页
                if (page <= totalPage) {
                    page = page + 1;
                    changeSelected(positionIndex);
                } else {
                    Toast.makeText(MainActivity.main, "已经是最后一页", Toast.LENGTH_SHORT).show();
                }
            }
        });
        categoryList = db.findAllByWhere(CodeModel.class, "Type='" + mContent + "' order by sorts");
        if (categoryList.size() > 0) {
            CodeModel model = categoryList.get(0);
            model.setSeleted(true);
            //categoryList.remove(0);
            categoryList.set(0, model);
            changeSelected(0);
        }

        categoryAdapter.setCategoryList(categoryList);
    }

    CommonAdapter<UserInfo> commonAdapter;
    List<UserInfo> list;
    private int old_position = 0;

    @Override
    public void onItemClick(int position) {
        if (position != old_position) {
            changeSelected(position);
            old_position = position;
        }
    }

    private ProgressDialog dialog;

    /**
     * 控制当前item选择
     *
     * @param position 当前点击位置
     */
    private void changeSelected(int position) {
        this.positionIndex = position;
        categoryList.get(oldSelectedPosition).setSeleted(false);
        categoryList.get(position).setSeleted(true);
        if (position < 7 || (categoryList.size() - position) < 5) {
            recyclerviewCategory.scrollToPosition(position);
        } else {
            recyclerviewCategory.scrollToPosition(position + 5);
        }
        oldSelectedPosition = position;
        categoryAdapter.notifyDataSetChanged();
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        //根据id查询人员列表
        BmobQuery<UserInfo> query = new BmobQuery<>();
        CodeModel codeModel = new CodeModel();
        codeModel.setObjectId(categoryList.get(position).getOId());
        query.addWhereEqualTo("type", codeModel);
        if (!Utils.getCache(Config.KEY_CITY).equals("")) {
            query.addWhereEqualTo("nowcity", Utils.getCache(Config.KEY_CITY));
        }
        query.count(UserInfo.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if (e == null) {
                    totalPage = count;
                    if (totalPage == 0) {
                        no_data_ll.setVisibility(View.VISIBLE);
                        no_data_mes.setText("未查询到相关数据");
                        right_lv.setVisibility(View.GONE);
                    } else {
                        query.setLimit(20);//20
                        if (page > 1) {
                            query.setSkip(page * 20);//1*20
                        }
                    }
                    if (page <= totalPage) {
                        query.findObjects(new FindListener<UserInfo>() {
                            @Override
                            public void done(List<UserInfo> lists, BmobException e) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                boolean result = true;
                                if (e == null && lists.size() > 0) {
                                    result = false;
                                    if (page == 1) {
                                        list.removeAll(list);
                                    }
                                    list.addAll(lists);
                                    commonAdapter.refresh(list);
                                    list = lists;
                                } else if (e == null && lists.size() == 0) {
                                    page = page - 1;
                                    result = false;
                                    //no_data_mes.setText("未查询到相关数据");
                                } else {
                                    no_data_mes.setText("网络请求失败");
                                    result = true;
                                }
                                right_lv.onRefreshComplete();
                                right_lv.setVisibility(result ? View.GONE : View.VISIBLE);
                                no_data_ll.setVisibility(result ? View.VISIBLE : View.GONE);
                            }
                        });
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        right_lv.onRefreshComplete();
                    }
                    //toast("count对象个数为："+count);
                } else {
                    //Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    no_data_ll.setVisibility(View.VISIBLE);
                    no_data_mes.setText("网络请求失败");
                    right_lv.setVisibility(View.GONE);
                }
            }
        });
        //query.setLimit(10); // 限制最多10条数据结果作为一页
        //query.setSkip(10); // 忽略前10条数据（即第一页数据结果）
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101 && requestCode == 102) {
            refrush();
        }
    }
}
