package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.ApplicationMy;

public class MemberAdapter extends Adapter<ViewHolder> {
	private static final String TAG="MemberAdapter";
	private Context context;
	private List<MemberEntity> listMember;
	// 设置底部布局
	private static final int TYPE_FOOTER = 0;
	// 设置默认布局
	private static final int TYPE_DEFAULT = 1;


	private int loadType = ValueType.LOAD_OVER;

	private IOnItemClickListener iOnItemClickListener;

	public MemberAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setData(List<MemberEntity> listMember) {
		this.listMember = listMember;
	}

	public void setLoadType(int loadType) {
		if (this.loadType != loadType) {
			this.loadType = loadType;
			notifyItemChanged(getItemCount());
		}
	}

	public void setIOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
		this.iOnItemClickListener = iOnItemClickListener;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listMember == null ?  1 : listMember.size()+1;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position+1  == getItemCount()) {
			return TYPE_FOOTER;
		} else {
			return TYPE_DEFAULT;
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int position) {
		// TODO Auto-generated method stub
		if (viewholder instanceof MyViewHolder) {
			MyViewHolder mViewHolder = (MyViewHolder) viewholder;
			MemberEntity member = listMember.get(position);
			mViewHolder.tvNick.setText(member.getNick());
			mViewHolder.tvTel.setText(member.getTel());
		}

		if (viewholder instanceof FootViewHolder) {
			FootViewHolder viewHolder = (FootViewHolder) viewholder;
			switch (loadType) {
			case ValueType.LOAD_LOADING:
				viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loading));
				break;
			case ValueType.LOAD_OVER:
				viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadover));
				break;
			case ValueType.LOAD_OVERALL:
				viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadoverAll));
				break;
			case ValueType.LOAD_FAIL:
				viewHolder.tvTag.setText(ApplicationMy.getContext().getString(R.string.loadfail));
				break;
			default:
				viewHolder.tvTag.setVisibility(View.GONE);
				break;
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		if (viewType == TYPE_DEFAULT) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_member, null);
			return new MyViewHolder(view, iOnItemClickListener);
		} else {
			View view = LayoutInflater.from(context).inflate(R.layout.view_recycle_bottom, parent, false);
			return new FootViewHolder(view);
		}
	}

	private class MyViewHolder extends ViewHolder implements OnClickListener {
		IOnItemClickListener iOnItemClickListener;
		View viewThis;
		TextView tvNick;
		TextView tvTel;

		public MyViewHolder(View view, IOnItemClickListener iOnItemClickListener) {
			super(view);
			// TODO Auto-generated constructor stub
			this.iOnItemClickListener = iOnItemClickListener;
			viewThis = view.findViewById(R.id.ll_item_member_this);
			tvNick = (TextView) view.findViewById(R.id.tv_item_member_name);
			tvTel = (TextView) view.findViewById(R.id.tv_item_member_tel);

			viewThis.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (iOnItemClickListener != null) {
				iOnItemClickListener.onItemClick(view, null, getPosition());
			}
		}
	}

	private class FootViewHolder extends ViewHolder {
		private TextView tvTag;
		public FootViewHolder(View view) {
			super(view);
			tvTag = (TextView) view.findViewById(R.id.tv_recycle_bottom_tag);
		}
	}
}
