package wai.findwork.ui;

import android.widget.ImageView;

import butterknife.Bind;
import wai.findwork.BaseActivity;
import wai.findwork.R;

/**
 * Created by Administrator on 2017/1/17.
 */

public class RegPersonActivity extends BaseActivity {
    @Bind(R.id.rp_iv_header)
    ImageView ivHeader;

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public int setLayout() {
        return R.layout.ac_reg_person;
    }
}
