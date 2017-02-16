package com.shwm.freshmallpos.been;

import java.io.Serializable;

import android.text.TextUtils;

public class AddressEntity implements Serializable {
	private double lat;// 纬度
	private double lng;// 经度
	private String address;// 格式化地址。如返回北京市朝阳区方恒国际中心。
	private String province;// 所在省名称、直辖市的名称
	private String city;// 所在城市名称。直辖市的名称参见省名称，此项为空。
	private String district;// 所在区（县）名称。

	public AddressEntity() {
		// TODO Auto-generated constructor stub
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getProvinceCityDistrict() {
		StringBuffer pcd = new StringBuffer();
		pcd.append(province);
		if (TextUtils.isEmpty(city)) {
			pcd.append(" 市辖区");
		} else {
			pcd.append(" " + city);
		}
		pcd.append(" " + district);
		return pcd.toString();
	}
}
