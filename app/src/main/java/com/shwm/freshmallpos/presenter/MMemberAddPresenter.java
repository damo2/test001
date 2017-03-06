package com.shwm.freshmallpos.presenter;

import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.MemberRequest;
import com.shwm.freshmallpos.view.IMemberAddView;

public class MMemberAddPresenter extends MBasePresenter<IMemberAddView>{
	private IMemberAddView mView;
	private Context context;

	private String tel;
	private String name;
	private String cardno;
	private String cardtype;
	private String shopname;
	private String discount;

	public MMemberAddPresenter(IMemberAddView mView) {
		this.mView = mView;
		context = ApplicationMy.getContext();
	}

	public void addMember() {
		tel = mView.getTel();
		name = mView.getName();
		cardno = mView.getCardno();
		cardtype = mView.getCardtype();
		shopname = BusinessInfo.getBusinessName();
		discount = mView.getDiscount();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(context, context.getString(R.string.member_add_name_fail), Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(cardno)) {
			Toast.makeText(context, context.getString(R.string.member_add_cardno_fail), Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(cardtype)) {
			Toast.makeText(context, context.getString(R.string.member_add_cardtype_fail), Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(discount)) {
			Toast.makeText(context, context.getString(R.string.member_add_cardtype_fail), Toast.LENGTH_SHORT).show();
		} else {
			new MyAsyncTaskUtil(new IAsyncListener() {
				@Override
				public void onSuccess(HashMap<String, Object> hashmap) {
					// TODO Auto-generated method stub
					mView.dismissDialogProgress();
					mView.addSuccess();
				}

				@Override
				public void onPreExecute() {
					// TODO Auto-generated method stub
					mView.showDialogProgress();
				}

				@Override
				public void onFail(int statu, Exception exception) {
					// TODO Auto-generated method stub
					mView.dismissDialogProgress();
				}

				@Override
				public HashMap<String, Object> doInBackground() {
					// TODO Auto-generated method stub
					return MemberRequest.addMember(tel, name, cardno, cardtype, discount, shopname);
				}
			}).execute();
		}
	}
}
