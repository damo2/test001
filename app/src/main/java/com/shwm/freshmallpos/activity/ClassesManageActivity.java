package com.shwm.freshmallpos.activity;

import java.io.Serializable;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.ClassesManageAdapter;
import com.shwm.freshmallpos.adapter.ClassesManageAdapter.IOnClassesManage;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.myviewutil.MyLinearLayoutManager;
import com.shwm.freshmallpos.presenter.MClassesManagePresenter;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IClassesManageView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 分类管理
 * 
 * @author wr 2016-12-13
 */
public class ClassesManageActivity extends BaseActivity<IClassesManageView, MClassesManagePresenter> implements IClassesManageView {
	private int editType = ValueType.ADD;
	private int chooseType;// 其他页面跳转过来的选择
	private String title;

	private RecyclerView mRecyclerView;
	private MyLinearLayoutManager mLayoutManager;
	private ClassesManageAdapter mAdapter;
	private List<ClassesEntity> listClasses;
	private String classesnameEdit;
	private Button btnAdd;
	private ClassesEntity classesSup;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mPresenter.getClasses();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public MClassesManagePresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MClassesManagePresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE);
		try {
			chooseType = bundle.getInt(ValueKey.CLASSESMANAGE_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_classesmanage, title);
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getItemId() == R.id.menu_right) {
					if (editType == ValueType.ADD) {
						if (listClasses == null || listClasses.size() == 0) {
							Toast.makeText(context, "请先添加类型", Toast.LENGTH_SHORT).show();
							return true;
						}
						// 添加状态切换成编辑状态
						editType = ValueType.EDIT;
						item.setTitle(getString(R.string.cancel));
						btnAdd.setVisibility(View.GONE);
					} else {
						editType = ValueType.ADD;
						item.setTitle(getString(R.string.edit));
						btnAdd.setVisibility(View.VISIBLE);
					}
					mAdapter.setEditType(editType);
					mAdapter.notifyDataSetChanged();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		getMenuInflater().inflate(R.menu.right, menu);
		MenuItem item = menu.findItem(R.id.menu_right);
		item.setTitle(getString(R.string.edit));
		return true;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_classesmanage;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mRecyclerView = (RecyclerView) findViewById(R.id.rv_classesmanage);
		btnAdd = (Button) findViewById(R.id.btn_classesmanage_submit);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		setAdapter();
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		mAdapter.setIOnClassesManage(iOnClassesManage);
		btnAdd.setOnClickListener(this);
	}

	private void setAdapter() {
		mLayoutManager = new MyLinearLayoutManager(context);
		mAdapter = new ClassesManageAdapter(context, editType);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter.setData(listClasses);
		mRecyclerView.setAdapter(mAdapter);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnAdd.getId()) {
			editClasses(null);
		}
	}

	private IOnClassesManage iOnClassesManage = new IOnClassesManage() {
		@Override
		public void onItemClick(View view, int position) {
			if (chooseType == ValueType.CHOOSE) {
				// Intent intent = new Intent();
				// Bundle bundle = new Bundle();
				// bundle.putSerializable(ValueKeyUtil.CLASSES, listClasses.get(position));
				// intent.putExtras(bundle);
				// setResult(RESULT_OK, intent);
				// finish();
			}
			if (editType != ValueType.EDIT) {
				classesSup = listClasses.get(position);
				// listClasses = classesSup.getListSub();
				// mAdapter.setData(listClasses);
				// mAdapter.notifyDataSetChanged();
				Intent intent = new Intent(ClassesManageActivity.this, ClassesManageActivity1.class);
				intent.putExtra(ValueKey.TITLE, classesSup.getName());
				intent.putExtra(ValueKey.CLASSESMANAGE_TYPE, chooseType);
				intent.putExtra(ValueKey.CLASSES, listClasses.get(position));
				if (chooseType == ValueType.CHOOSE) {
					startActivityForResult(intent, ValueRequest.FoodEdit_Classesmanage);
				} else {
					startActivityForResult(intent, ValueRequest.Classesmanage_Classesmanage1);
				}
			}
		}

		@Override
		public void onDelete(View view, int position) {
			deleteClasses(listClasses.get(position));
		}

		@Override
		public void onEdit(View view, int position) {
			editClasses(listClasses.get(position));
		}
	};

	@Override
	public int getEditType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void showClasses(List<ClassesEntity> listClassesAll) {
		// TODO Auto-generated method stub
		this.listClasses = listClassesAll;
		mAdapter.setData(listClasses);
		mAdapter.notifyDataSetChanged();
	}

	private void editClasses(final ClassesEntity classes) {
		String title = "";
		if (editType == ValueType.ADD) {
			title = getString(R.string.classesmanage_addClassesDialogtitle);
		}
		if (editType == ValueType.EDIT) {
			title = getString(R.string.classesmanage_editClassesDialogtitle);
		}
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
		builder.setTitle(title);
		final EditText edtContent = new EditText(mActivity);
		if (classes != null) {
			edtContent.setText(classes.getName());
		}
		builder.setView(edtContent, ApplicationMy.dp2px(8), ApplicationMy.dp2px(16), ApplicationMy.dp2px(8), ApplicationMy.dp2px(16));
		builder.setCancelable(false);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				classesnameEdit = edtContent.getText().toString().trim();
				if (editType == ValueType.EDIT) {
					mPresenter.editClasses(classes, classesnameEdit);
				} else {
					mPresenter.addClasses(classesnameEdit);
				}
			}
		});
		builder.show();
	}

	private void deleteClasses(final ClassesEntity classes) {
		if (classes.getIsLast() != ValueFinal.CLASSES_ISLAST) {
			Toast.makeText(getApplicationContext(), getString(R.string.classes_del_nolast), Toast.LENGTH_SHORT).show();
			return;
		}
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage("删除  ‘" + classes.getName() + "’?");
		builder.setCancelable(false);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				mPresenter.deleteClasses(classes.getId() + "");
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.FoodEdit_Classesmanage && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			int type = bundle.getInt(ValueKey.TYPE);
			if (type == ValueType.CHOOSE) {
				data.getExtras().putSerializable(ValueKey.CLASSESListAll, (Serializable) listClasses);
				setResult(RESULT_OK, data);
				finish();
			}
		}
		if (requestCode == ValueRequest.Classesmanage_Classesmanage1 && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			int type = bundle.getInt(ValueKey.TYPE);
			if (type == ValueType.EDIT) {
				listClasses = (List<ClassesEntity>) bundle.getSerializable(ValueKey.CLASSESListAll);
				mAdapter.setData(listClasses);
				mAdapter.notifyDataSetChanged();
				UL.e(TAG, "return listall");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public int getClassesLv() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getSupClassesId() {
		// TODO Auto-generated method stub
		// 一级父类Id=0
		return 0;
	}

	@Override
	protected void onBack() {
		// TODO Auto-generated method stub
		super.onBack();
		exit();
	}

	private void exit() {
		if (listClasses != null && (editType == ValueType.ADD || editType == ValueType.EDIT)) {
			Bundle bundle = new Bundle();
			bundle.putInt(ValueKey.TYPE, ValueType.EDIT);
			bundle.putSerializable(ValueKey.CLASSESListAll, (Serializable) listClasses);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
		}
		this.finish();
	}
}
