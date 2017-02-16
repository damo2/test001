package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.OrderEntity;

public interface IMainOrderView extends IBaseView{

	int getPageType();

	void showListOrder(List<OrderEntity> listOrder);

	void setLoadType(int loadType);

	void refreshCancel();

}
