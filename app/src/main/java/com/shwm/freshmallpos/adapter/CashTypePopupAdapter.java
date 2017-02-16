package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CashTypePopupAdapter.CashTypeViewHolder;
import com.shwm.freshmallpos.been.IconEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;

public class CashTypePopupAdapter extends Adapter<CashTypeViewHolder> {
	private Context context;
	private List<IconEntity> listIcon;
	private IOnItemClickListener iOnItemClickListener;

	public CashTypePopupAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setData(List<IconEntity> listIcon) {
		this.listIcon = listIcon;
	}

	public void setIOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listIcon == null ? 0 : listIcon.size();
	}

	@Override
	public void onBindViewHolder(CashTypeViewHolder viewholder, int position) {
		// TODO Auto-generated method stub
		IconEntity icon = listIcon.get(position);
		int imgRes = icon.getImgRes();
		if (imgRes > 0) {
			viewholder.iv.setImageResource(imgRes);
		}
	}

	@Override
	public CashTypeViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_cashtype, null);
		return new CashTypeViewHolder(view, iOnItemClickListener);
	}

	protected class CashTypeViewHolder extends RecyclerView.ViewHolder {
		private ImageView iv;
		private IOnItemClickListener iOnItemClickListener;

		public CashTypeViewHolder(View view, IOnItemClickListener iOnItemClickListener) {
			super(view);
			this.iOnItemClickListener = iOnItemClickListener;
			// TODO Auto-generated constructor stub
			iv = (ImageView) view.findViewById(R.id.iv_item_cashtype_icon);
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (CashTypeViewHolder.this.iOnItemClickListener != null) {
						CashTypeViewHolder.this.iOnItemClickListener.onItemClick(view, null, getPosition());
					}
				}
			});
		}

	}
}
