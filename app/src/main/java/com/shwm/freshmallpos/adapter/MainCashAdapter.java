package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.MainCashAdapter.MViewHolder;
import com.shwm.freshmallpos.been.IconEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;

public class MainCashAdapter extends RecyclerView.Adapter<MViewHolder> {
	private Context context;
	private List<IconEntity> listIcon;
	private IOnItemClickListener iOnItemClickListener;

	public MainCashAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setData(List<IconEntity> listIcon) {
		this.listIcon = listIcon;
	}

	public void setIOnItemClick(IOnItemClickListener iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

	@Override
	public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MViewHolder holder = new MViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_cash, parent, false),
				iOnItemClickListener);
		return holder;
	}

	@Override
	public void onBindViewHolder(MViewHolder holder, int position) {
		holder.tvText.setText(listIcon.get(position).getText());
		holder.ivIcon.setImageResource(listIcon.get(position).getImgRes());
	}

	@Override
	public int getItemCount() {
		return listIcon == null ? 0 : listIcon.size();
	}

	protected class MViewHolder extends ViewHolder implements OnClickListener {
		private TextView tvText;
		private ImageView ivIcon;
		private IOnItemClickListener iOnItemClickListener;

		public MViewHolder(View view, IOnItemClickListener iOnItemClickListener) {
			super(view);
			this.iOnItemClickListener = iOnItemClickListener;
			tvText = (TextView) view.findViewById(R.id.tv_main_cash_gv_text);
			ivIcon = (ImageView) view.findViewById(R.id.iv_main_cash_gv_icon);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (iOnItemClickListener != null) {
				iOnItemClickListener.onItemClick(view,null, getPosition());
			}
		}
	}
}