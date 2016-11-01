package com.etcomm.dcare;


import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * 选择部门
 *
 * @author iexpressbox
 */
public class DepartMentActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.department_tv)
    TextView department_tv;
    @Bind(R.id.listview)
    ListView listview;
    private String[] curList;

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        titlebar.setTitle("部门");
        titlebar.setLeftLl(backClickListener);
        listview.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, curList));
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
//				curList
                //if
                listview.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, curList));
                department_tv.setText(department_tv.getText().toString() + "-" + curList[position]);
            }
        });
        ;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_department;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }
}
