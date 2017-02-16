package com.shwm.freshmallpos.model.biz;

import java.util.List;

import com.shwm.freshmallpos.been.MemberEntity;

public interface IMemberManageListener {
	void getMemberList(int page, String like, IRequestListener<List<MemberEntity>> iRequestListener);
}
