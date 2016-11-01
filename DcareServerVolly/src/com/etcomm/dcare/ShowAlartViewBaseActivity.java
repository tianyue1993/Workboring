package com.etcomm.dcare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.etcomm.dcare.listener.OnDismissListener;
import com.etcomm.dcare.listener.OnItemClickListener;
import com.etcomm.dcare.widget.AlertView;

public class ShowAlartViewBaseActivity extends FragmentActivity implements OnItemClickListener, OnDismissListener {
	private Context context;
	private InputMethodManager imm;
	private Object mAlertView;
	private AlertView mAlertViewExt;
	private EditText etName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mAlertView = new AlertView("标题", "内容", "取消", new String[] { "确定" }, null, this, AlertView.Style.Alert, this)
				.setCancelable(true).setOnDismissListener(this);
		// 拓展窗口
		mAlertViewExt = new AlertView("提示", "请完善你的个人资料！", "取消", null, new String[] { "完成" }, this,
				AlertView.Style.Alert, this);
		ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form, null);
		etName = (EditText) extView.findViewById(R.id.etName);
		etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean focus) {
				// 输入框出来则往上移动
				boolean isOpen = imm.isActive();
				mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
				System.out.println(isOpen);
			}
		});
		mAlertViewExt.addExtView(extView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		new AlertView("上传头像", null, "取消", null, new String[] { "拍照", "从相册中选择" }, context, AlertView.Style.ActionSheet,
				this).show();
		return super.onCreateOptionsMenu(menu);
	}

	public void alertShowExt(View view) {
		mAlertViewExt.show();
	}

	private void closeKeyboard() {
		// 关闭软键盘
		imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
		// 恢复位置

		mAlertViewExt.setMarginBottom(0);
	}

	@Override
	public void onDismiss(Object o) {
		closeKeyboard();
		Toast.makeText(this, "消失了", Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(Object o, int position) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		closeKeyboard();
		// 判断是否是拓展窗口View，而且点击的是非取消按钮
		if (o == mAlertViewExt && position != AlertView.CANCELPOSITION) {
			String name = etName.getText().toString();
			if (name.isEmpty()) {
				Toast.makeText(context, "啥都没填呢", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "hello," + name, Toast.LENGTH_SHORT).show();
			}

			return;
		}
		Toast.makeText(context, "点击了第" + position + "个", Toast.LENGTH_SHORT).show();

	}


}
