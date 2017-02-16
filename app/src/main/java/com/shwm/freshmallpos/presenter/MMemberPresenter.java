package com.shwm.freshmallpos.presenter;

import java.util.ArrayList;
import java.util.List;

import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.model.biz.IMemberManageListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnMemberManageListener;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IMemberView;

public class MMemberPresenter extends MBasePresenter<IMemberView>{
	private IMemberView mView;
	private IMemberManageListener iMemberManageListener;
	private List<MemberEntity> listMember;

	private int pageType;// 刷新还是加载还是默认
	private int page = 0;// 当前页数
	private int pageRequest;// 请求的页数

	public MMemberPresenter(IMemberView mView) {
		// TODO Auto-generated constructor stub
		this.mView = mView;
		this.iMemberManageListener = new OnMemberManageListener();
	}

	/** 获取会员列表 显示 */
	public void getListMember() {
		pageType = mView.getPageType();
		String like = mView.getLike();
		if (pageType == ValueType.PAGE_DEFAULT) {
			pageRequest = 1;
		} else if (pageType == ValueType.PAGE_REFRESH) {
			pageRequest = 1;
		} else if (pageType == ValueType.PAGE_LOAD) {
			pageRequest = page + 1;
		}
		iMemberManageListener.getMemberList(pageRequest, like, new IRequestListener<List<MemberEntity>>() {
			@Override
			public void onSuccess(List<MemberEntity> list) {
				// TODO Auto-generated method stub
				if (list != null && list.size() > 0) {
					page = pageRequest;
				}
				if (listMember == null) {
					listMember = new ArrayList<MemberEntity>();
				}
				if (pageType == ValueType.PAGE_REFRESH || pageType == ValueType.PAGE_DEFAULT) {
					listMember.clear();
				}

				int loadType = 0;
				if (pageType == ValueType.PAGE_LOAD) {
					if (list != null && list.size() > 0) {
						loadType = ValueType.LOAD_OVER;
					} else {
						loadType = ValueType.LOAD_OVERALL;
					}
				}

				listMember.addAll(list);
				list.clear();
				mView.showMemberList(loadType, listMember);
				if (pageType == ValueType.PAGE_REFRESH) {
					mView.refreshCancel();
				}
				if (pageType == ValueType.DEFAULT) {
					mView.dissmissDialogProgress();
				}
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				if (pageType == ValueType.DEFAULT) {
					mView.showDialogProgress();
				}
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				if (pageType == ValueType.PAGE_REFRESH) {
					mView.refreshCancel();
				}
				if (pageType == ValueType.DEFAULT) {
					mView.dissmissDialogProgress();
				}
			}
		});
	}
}
