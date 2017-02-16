package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.myview.SwipeMenuLayout;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueType;
public class ClassesManageAdapter extends Adapter<ViewHolder> {
	private Context context;
	private List<ClassesEntity> listClasses;
	private IOnClassesManage iOnClassesManage;
	private int editType;

	public ClassesManageAdapter(Context context, int editType) {
		// TODO Auto-generated constructor stub
		this.context = context;
		setEditType(editType);
	}

	public void setData(List<ClassesEntity> listClasses) {
		this.listClasses = listClasses;
	}

	public void setIOnClassesManage(IOnClassesManage iOnClassesManage) {
		this.iOnClassesManage = iOnClassesManage;
	}

	public void setEditType(int editType) {
		this.editType = editType;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listClasses == null ? 0 : listClasses.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewgroup, int positon) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_classesmanage, null);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int positon) {
		// TODO Auto-generated method stub
		if (viewholder instanceof MyViewHolder) {
			MyViewHolder mViewHolder = (MyViewHolder) viewholder;
			ClassesEntity classes = listClasses.get(positon);
			mViewHolder.tvClassesname.setText(classes.getName());
			if (editType == ValueType.EDIT) {
				mViewHolder.btnDelete.setVisibility(View.VISIBLE);
				mViewHolder.ivEdit.setVisibility(View.VISIBLE);
				mViewHolder.ivShowDel.setVisibility(View.VISIBLE);
			} else {
				mViewHolder.btnDelete.setVisibility(View.GONE);
				mViewHolder.ivEdit.setVisibility(View.GONE);
				mViewHolder.ivShowDel.setVisibility(View.GONE);
			}
		}
	}

	private class MyViewHolder extends ViewHolder {
		SwipeMenuLayout viewAll;
		ImageView ivShowDel;
		TextView tvClassesname;
		Button btnDelete;
		ImageButton ivEdit;

		public MyViewHolder(View viewParent) {
			super(viewParent);
			viewAll= (SwipeMenuLayout) viewParent.findViewById(R.id.swipeMenuLayout_item_parent);
			ivShowDel= (ImageView) viewParent.findViewById(R.id.iv_item_showDelete);
			tvClassesname = (TextView) viewParent.findViewById(R.id.tv_item_classesmanage_classesname);
			btnDelete = (Button) viewParent.findViewById(R.id.btn_item_classesmanage_delete);
			ivEdit = (ImageButton) viewParent.findViewById(R.id.iv_item_classesmanage_edit);
			viewParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (view.getId() == view.getId()) {
						if (iOnClassesManage != null)
							iOnClassesManage.onItemClick(view, getPosition());
					}
				}
			});
			btnDelete.setOnClickListener(onClick);
			ivEdit.setOnClickListener(onClick);
			ivShowDel.setOnClickListener(onClick);
		}

		OnClickListener onClick = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (view.getId() == btnDelete.getId()) {
					if (iOnClassesManage != null) {
						iOnClassesManage.onDelete(view, getPosition());
					}
				}
				if (view.getId() == ivEdit.getId()) {
					if (iOnClassesManage != null) {
						iOnClassesManage.onEdit(view, getPosition());
					}
				}
				if (view.getId() == ivShowDel.getId()) {
					viewAll.smoothExpand();//手动显示滑动后内容
				}
			}
		};
	}

	public interface IOnClassesManage {
		void onItemClick(View view, int position);

		void onDelete(View view, int position);

		void onEdit(View view, int position);
	}
}
