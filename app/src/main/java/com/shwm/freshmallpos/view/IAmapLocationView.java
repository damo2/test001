package com.shwm.freshmallpos.view;

import com.shwm.freshmallpos.been.AddressEntity;

public interface IAmapLocationView extends IBaseView {

	AddressEntity getAddressEntity();
	void changeBusinessAddressSuccess();
}
