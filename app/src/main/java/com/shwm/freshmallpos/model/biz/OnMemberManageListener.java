package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;
import java.util.List;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.MemberRequest;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnMemberManageListener implements IMemberManageListener {

	@Override
	public void getMemberList(final int page, final String like, final IRequestListener<List<MemberEntity>> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				if (code == ValueStatu.SUCCESS) {
					List<MemberEntity> listMember = (List<MemberEntity>) hashmap.get(ValueKey.LISTMEMBER);
					if (iRequestListener != null) {
						iRequestListener.onSuccess(listMember);
					}
				} else {
					iRequestListener.onFail(ValueStatu.FAIL, new ExceptionUtil(ApplicationMy.getContext().getString(R.string.statu_fail)));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onPreExecute(0);
				}
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				if (iRequestListener != null) {
					iRequestListener.onFail(statu, exception);
				}
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return MemberRequest.getMemberList(page, like);
			}
		}).execute();
	}

}
