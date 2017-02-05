package wai.findwork.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wai.findwork.R;
import wai.findwork.adapter.CategoryAdapter;
import wai.findwork.adapter.CommonAdapter;
import wai.findwork.adapter.CommonViewHolder;
import wai.findwork.method.Utils;
import wai.findwork.model.ArticleModel;
import wai.findwork.model.CodeModel;
import wai.findwork.model.Config;
import wai.findwork.model.TypeOfWorkModel;
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
        articleModelCommonAdapter = new CommonAdapter<ArticleModel>(MainActivity.main, articleModels, R.layout.item_team) {
            @Override
            public void convert(CommonViewHolder holder, ArticleModel articleModel, int position) {

            }
        };
    }

    LinearLayout no_data_ll;
    ListView right_lv;


    ImageView user_iv;
    TextView user_name_tv;
    TextView user_type_tv;
    TextView id_card_tv;
    LinearLayout user_left_ll;
    LinearLayout user_right_ll;
    TextView tel_tv;
    TextView gz_tv;
    TextView remark_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        switch (mContent) {
            case 0:
                view = inflater.inflate(R.layout.frag_user, container, false);
                user_iv = (ImageView) view.findViewById(R.id.user_iv);
                user_name_tv = (TextView) view.findViewById(R.id.user_name_tv);
                user_type_tv = (TextView) view.findViewById(R.id.user_type_tv);
                id_card_tv = (TextView) view.findViewById(R.id.id_card_tv);
                user_left_ll = (LinearLayout) view.findViewById(R.id.user_left_ll);
                user_right_ll = (LinearLayout) view.findViewById(R.id.user_right_ll);
                tel_tv = (TextView) view.findViewById(R.id.tel_tv);
                gz_tv = (TextView) view.findViewById(R.id.gz_tv);
                remark_tv = (TextView) view.findViewById(R.id.remark_tv);

                break;
            case 4:
                view = inflater.inflate(R.layout.frag_zx, container, false);
                ListView listView = (ListView) view.findViewById(R.id.zx_lv);
                listView.setAdapter(articleModelCommonAdapter);
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
                view = inflater.inflate(R.layout.frag_gz, container, false);
                recyclerviewCategory = (RecyclerView) view.findViewById(R.id.recyclerview_category);
                main_ll = (LinearLayout) view.findViewById(R.id.main_ll);
                no_data_ll = (LinearLayout) view.findViewById(R.id.no_data_ll);
                right_lv = (ListView) view.findViewById(R.id.right_lv);
                categoryList = new ArrayList<>();
                db = FinalDb.create(MainActivity.main);
                initViews();
                break;
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (mContent) {
            case 1:
            case 2:
            case 3:

                break;
            case 0:
                List<UserInfo> list = db.findAll(UserInfo.class);
                if (list.size() > 0) {
                    UserInfo info = list.get(0);
                    Glide.with(MainActivity.main)
                            .load(info.getIconurl()).transform(new GlideCircleTransform(MainActivity.main))
                            .into(user_iv);
                    user_name_tv.setText(info.getRealname());
                    id_card_tv.setText(info.getCardnum());
                    user_type_tv.setText(info.getTypeName());
                    user_left_ll.setOnClickListener(v -> {
                    });
                    user_right_ll.setOnClickListener(v -> {
                    });
                    tel_tv.setText("电话：" + Utils.getCache(Config.KEY_User_ID));
                    gz_tv.setText("工资：" + info.getGongzi());
                    remark_tv.setText("备注：" + info.getRemark());
                }
                user_left_ll.setOnClickListener(v -> {
                    String s = "";
                    Utils.IntentPost(RiLiActivity.class);
                });
                break;
        }
    }

    FinalDb db;


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
            UserInfo userInfo = list.get(position);
            userInfo.setTypeName(categoryList.get(old_position).getName());
            Utils.IntentPost(PersonDetailActivity.class, intent -> intent.putExtra("user", list.get(position)));
        });

        categoryList = db.findAllByWhere(CodeModel.class, "Type='" + mContent + "'");
        categoryAdapter.setCategoryList(categoryList);
        if (categoryList.size() > 0) {
            CodeModel model = categoryList.get(0);
            model.setSeleted(true);
            categoryList.remove(0);
            categoryList.set(0, model);
            changeSelected(0);
        }
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
        codeModel.setObjectId(categoryList.get(position).getObjectid());
        query.addWhereEqualTo("type", codeModel);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> lists, BmobException e) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                boolean result = true;
                if (e == null && lists.size() > 0) {
                    result = false;
                    commonAdapter.refresh(lists);
                    list = lists;
                }
                right_lv.setVisibility(result ? View.GONE : View.VISIBLE);
                no_data_ll.setVisibility(result ? View.VISIBLE : View.GONE);
            }
        });
    }
}
