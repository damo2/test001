package com.shwm.freshmallpos.been;

import java.io.Serializable;

import com.shwm.freshmallpos.util.StringUtil;

/**
 * 商家
 */
public class BusinessEntity implements Serializable {
	private int id;// 商家ID
	private String name;
	private String tel;
	private String img;
	private String url;
	private int type;
	private String serverIp;
	private String addr;

	private double lng;
	private double lat;
	private double range;
	private String timeOpen;
	private String timeClose;
	private double priceQiSong;
	private double pricePeisong;

	private String desc;
	private String partner;

	private boolean selected;

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = StringUtil.getInt(id);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public String getTimeOpen() {
		return timeOpen;
	}

	public void setTimeOpen(String timeOpen) {
		this.timeOpen = timeOpen;
	}

	public String getTimeClose() {
		return timeClose;
	}

	public void setTimeClose(String timeClose) {
		this.timeClose = timeClose;
	}

	public double getPriceQiSong() {
		return priceQiSong;
	}

	public void setPriceQiSong(double priceQiSong) {
		this.priceQiSong = priceQiSong;
	}

	public double getPricePeisong() {
		return pricePeisong;
	}

	public void setPricePeisong(double pricePeisong) {
		this.pricePeisong = pricePeisong;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

}
