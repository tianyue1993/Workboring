package com.etcomm.dcare.app.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.adapter.DcareBaseAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.StructureContent;
import com.etcomm.dcare.netresponse.StructureItems;
import com.etcomm.dcare.util.AppUtils;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 公司名称根据企业验证码获取 选择自己所在的公司的组织机构
 *
 * @author iexpressbox
 */
public class ChooseComStructureActivity extends BaseActivity {
    SimpleTitleBar systemtitle;
    TextView tv_comname;
    private LayoutInflater inflater;
    ListView listview;
    private String activitioncode;
    private Intent intent;
    private String activitionstructurecode;
    private List<StructureItems> mList = new ArrayList<StructureItems>();
    private ConstructureListAdapter mAdapter;
    private ArrayList<StructureItems> actstructurelist;
    protected List<StructureItems> lists;
    private String structurename;
    private boolean isSettingPersonalData;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    /**
     * 先获取数据，再进入下一级
     *
     * @param subcode
     * @param structure
     */
    private void getSubStructure(final String subcode, final String structure) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("serial_number", activitioncode);
        params.put("parent_id", subcode);
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        String url;
        if (isSettingPersonalData) {
            url = Constants.GetStructure;
            params.put("access_token", SharePreferencesUtil.getToken(mContext));
        } else {
            url = Constants.ActiveStructure;
        }
        DcareRestClient.volleyGet(url, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        StructureContent disscomment = JSON.parseObject(response.getString("content"), StructureContent.class);
                        Log.i(tag, "disscomment: " + disscomment.toString());
                        String laststructurename = disscomment.getCustomer();
                        List<StructureItems> lists = disscomment.getStructure();
                        if (lists == null) {
                            Log.i(tag, "lists null");
                        } else {
                            Log.i(tag, "lists  not null" + lists.size());
                        }
                        if (lists != null && lists.size() > 0) {
                            if (isSettingPersonalData) {
                                intent.putExtra(Preferences.LastStructureName, structure);
                                intent.putExtra(Preferences.LastStructureItems, (Serializable) lists);
                                intent.putExtra(Preferences.ActivitionStructureList, actstructurelist);
                                startActivityForResult(intent, 0);
                            } else {
                                intent.setClass(ChooseComStructureActivity.this, ChooseComStructureActivity.class);
                                intent.putExtra(Preferences.LastStructureName, structure);
                                intent.putExtra(Preferences.LastStructureItems, (Serializable) lists);
                                intent.putExtra(Preferences.ActivitionStructureList, actstructurelist);
                                startActivity(intent);
                            }
                        } else {
                            Log.i(tag, "lists null  choosedStructure");
                            choosedStructure(structure, subcode);
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });
    }

    protected void choosedStructure(String structure, String structureid) {
        // TODO Auto-generated method stub
        if (isSettingPersonalData) {
            Intent data = intent;
            // data.set
            data.putExtra(Preferences.SelectDepart, structure);
            data.putExtra(Preferences.SelectDepart_Id, structureid);
            setResult(RESULT_OK, data);
            finish();
            return;
        } else {
//            Intent intentnext = intent;
            Log.e(tag, "choosedStructure");
            Log.e(tag, structure);
            SharePreferencesUtil.saveDepartmentInfo(this, structure);
            SharePreferencesUtil.saveDepartmentInfoId(this, structureid);
//            intentnext.putExtra(Preferences.ActivitionStructureCode, structure);
//            setResult(0, intentnext);
            AppUtils.finishActivity();
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        if (arg1 == RESULT_OK) {
            Log.i(tag, "onActivityResult ");
            Intent data = arg2;
            setResult(RESULT_OK, data);
            finish();
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    private void initview() {
        // TODO Auto-generated method stub
        systemtitle = (SimpleTitleBar) findViewById(R.id.systemtitle);
        tv_comname = (TextView) findViewById(R.id.tv_comname);
        listview = (ListView) findViewById(R.id.listview);
        if (isSettingPersonalData) {
//            tv_comname.setVisibility(View.GONE);
//            tv_comname.setText("");
            systemtitle.setTitle("部门");
        } else {
            systemtitle.setTitle("注册");
        }
        systemtitle.setLeftLl(backClickListener);

        mList = lists;
        mAdapter = new ConstructureListAdapter(mContext, mList);
        listview.setAdapter(mAdapter);
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        intent = getIntent();
        AppUtils.addActivity(this);
        structurename = intent.getStringExtra(Preferences.LastStructureName);
        lists = (List<StructureItems>) intent.getSerializableExtra(Preferences.LastStructureItems);
        isSettingPersonalData = intent.getBooleanExtra(Preferences.IsSettingPersonalData, false);
        activitioncode = intent.getStringExtra(Preferences.ActivitionCode);
        activitionstructurecode = intent.getStringExtra(Preferences.ActivitionStructureCode);
        if (activitionstructurecode.equals("0")) {
            actstructurelist = new ArrayList<StructureItems>();
            StructureItems item = new StructureItems();
            item.setStructure(structurename);
            item.setStructure_id(activitionstructurecode);
            actstructurelist.add(item);
        } else {
            actstructurelist = (ArrayList<StructureItems>) intent.getSerializableExtra(Preferences.ActivitionStructureList);
            StructureItems item = new StructureItems();
            item.setStructure(structurename);
            item.setStructure_id(activitionstructurecode);
            actstructurelist.add(item);
        }
        initview();
        if (structurename != null) {
            tv_comname.setVisibility(View.VISIBLE);
            tv_comname.setText(structurename);
        } else {
            tv_comname.setVisibility(View.GONE);
        }


    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_choosecomstructrue;
    }


    public class ConstructureListAdapter extends DcareBaseAdapter<StructureItems> {

        public ConstructureListAdapter(Context mContext, List<StructureItems> mList) {
            // TODO Auto-generated constructor stub
            super(mContext);
            this.mList = mList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_structure, null);
                viewHolder.structure_name = (TextView) convertView.findViewById(R.id.structure_name);
                viewHolder.structrue_right_icon = (ImageView) convertView.findViewById(R.id.structrue_right_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final StructureItems mInfo = getItem(position);
            if (null != mInfo) {

                if (mInfo.getChilds().equals("1")) {
                    viewHolder.structrue_right_icon.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.structrue_right_icon.setVisibility(View.GONE);
                }
                //点击获取下一级部门
                viewHolder.structrue_right_icon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getSubStructure(mInfo.getStructure_id(), mInfo.getStructure());

                    }
                });

                //点击选择修改部门
                viewHolder.structure_name.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        choosedStructure(mInfo.getStructure() + "", mInfo.getStructure_id() + "");
                    }
                });
                viewHolder.structure_name.setText(mInfo.getStructure());
            }
            return convertView;
        }


    }

    private static class ViewHolder {
        TextView structure_name;
        ImageView structrue_right_icon;
    }

}
