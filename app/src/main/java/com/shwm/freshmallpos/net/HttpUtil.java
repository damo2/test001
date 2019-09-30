package com.shwm.freshmallpos.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.manage.UpImageRequestFactory;
import com.shwm.freshmallpos.sys.SysInfo;
import com.shwm.freshmallpos.value.ValueType;

import java.util.HashMap;

/**
 * 请求地址
 */
public class HttpUtil {
    public static final String GBK_ENCODING = "GBK";
    private static final String UTF8_ENCODING = "UTF-8";
    public static final String DEFAULT_ENCODING = UTF8_ENCODING;

    public static Context context;

    public static Context getContxt() {
        synchronized (HttpUtil.class) {
            if (HttpUtil.context == null) {
                HttpUtil.context = ApplicationMy.getContext();
            }
        }
        return context;
    }

    // 取到serverIp
    private static String getServerIp() {
//        return BusinessInfo.getServerIp();
         return AppConfig.SERVER_IP;
    }

    private static String getSysInfoUrl() {
        return "&bdid=" + SysInfo.getBuildID() + "&prdt=" + SysInfo.getProduct() + "&bord=" + SysInfo.getBoard() + "&osno="
                + SysInfo.getOSNo() + "&sdk=" + SysInfo.getSDK() + "&dpi=" + SysInfo.getDensityDpi() + "&sw=" + SysInfo.getScreenWidth()
                + "&sh=" + SysInfo.getScreenHeight();
    }

    private static String getIDInfoUrl() {
        return "&imsi=" + SysInfo.getIMSI() + "&imei=" + SysInfo.getIMEI();
    }

    public static String getBussinessId() {
        return "&businessId=" + BusinessInfo.getBusinessID();
    }

    public static String getAdminNameInId() {
        return "&adminId=" + BusinessInfo.getAdminName();
    }

    public static String getAdminId() {
        return "&adminId=" + BusinessInfo.getAdminID();
    }

    public static String getAdminType() {
        return "&adminType=" + BusinessInfo.getAdminType();
    }

    // app url
    public static HashMap<String, Object> getUpdateUrl(String pkgName) {
        String url = AppConfig.UPDATE_URL + pkgName + getIDInfoUrl() + getSysInfoUrl();
        return HttpRequestFactory.getHttpRequest().requestByGet(url, new HashMap<String, Object>());
    }

    public static String getDownloadUrl(String pkgName) {
        return AppConfig.DOWNLOAD_URL + pkgName + getIDInfoUrl() + getSysInfoUrl();
    }

    // 图片路径
    public static String getImgURl(String img) {
        return getServerIp() + "/" + img;
    }

    // 登录
    public static HashMap<String, Object> getLogUrl(String username, String password) {
        String url = AppConfig.SERVER_IP + AppConfig.LOGIN_URL;
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("username", username);
        hashmap.put("password", password);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    // 获取验证码
    public static HashMap<String, Object> getCode(String mobi) {
        String url = AppConfig.SERVER_IP + AppConfig.REG_GETCODE;
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("mobi", mobi);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 注册
     */
    public static HashMap<String, Object> regin(String mobi, String code, String password, String storeName, String storeAddr,
                                                String storeDesc, String storeContact, double lat, double lng) {
        String url = AppConfig.SERVER_IP + AppConfig.REGIN_URL;
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("mobi", mobi);
        hashmap.put("code", code);
        hashmap.put("pwd", password);
        hashmap.put("storeName", storeName);
        hashmap.put("address", storeAddr);
        hashmap.put("desc", storeDesc);
        hashmap.put("contact", storeContact);
        hashmap.put("lat", lat);
        hashmap.put("lng", lng);
        HttpOkRequest.typeH = ValueType.HttpType_Regin;
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 修改密码
     */
    public static HashMap<String, Object> changePassword(int adminId, String adminName, String pwdOld, String pwdNew) {
        String url = getServerIp() + AppConfig.CHANGE_PWD;
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", adminId);
        hashmap.put("adminName", adminName);
        hashmap.put("oldpwd", pwdOld);
        hashmap.put("pwd", pwdNew);
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 得到Classes列表 一级下面有二级
     */
    public static HashMap<String, Object> getClasses() {
        // return getServerIp() + AppConfig.GET_CLASSES + "&getAll=yes";
        String url = getServerIp() + AppConfig.GET_CLASSES + "&getAll=yes" + getBussinessId() + getAdminType();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 获取对应类型商品列表
     */
    public static HashMap<String, Object> getFoodsByClasses(int page, String tag) {
        // return getServerIp() + AppConfig.GET_FOODSBYCLASSES + "&page=" + page + "&tag=" + tag;
        String url = getServerIp() + AppConfig.GET_FOODSBYCLASSES + getBussinessId() + getAdminNameInId() + getAdminType();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("page", page);
        hashmap.put("tag", tag);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);

    }

    /**
     * 提交订单
     */
    public static HashMap<String, Object> setOrderSubmit(String items, int uid, String memCardNo, String mobile, int payType,
                                                         double receiveMoney) {
        String url = getServerIp() + AppConfig.ORDER_SUBMIT + getBussinessId() + getAdminId() + getAdminType();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("items", items);
        hashmap.put("uid", uid);
        if (!TextUtils.isEmpty(memCardNo)) {
            hashmap.put("memCardNo", memCardNo);
        }
        if (!TextUtils.isEmpty(mobile)) {
            hashmap.put("mobile", mobile);
        }
        hashmap.put("payType", payType);
        hashmap.put("receiveMoney", receiveMoney);
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 删除 content_type ids
     */
    public static HashMap<String, Object> delete(int content_type, String ids) {
        String url = getServerIp() + AppConfig.DEL + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("content_type", content_type);
        hashmap.put("ids", ids);
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 删除类型
     */
    public static HashMap<String, Object> deleteClasses(int content_type, String ids) {
        String url = getServerIp() + AppConfig.DEL + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("content_type", content_type);
        hashmap.put("ids", ids);
        hashmap.put("checkItem", "true");
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 添加商品
     **/
    public static HashMap<String, Object> setFoodAdd(String name, String barcode, String typeTag, double price, double priceMember,
                                                     String unit, double numSum, int type, String comefrom, String desc, String eatIds, String eatIdsDel) {
        String url = getServerIp() + AppConfig.MANAGER_FOOD_ADD + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("name", name);
        hashmap.put("barcode", barcode);
        hashmap.put("type", type + "");
        hashmap.put("typeTag", typeTag);
        hashmap.put("price", price + "");
        hashmap.put("memPrice", priceMember + "");
        hashmap.put("unit", unit);
        hashmap.put("leftCount", numSum + "");
        hashmap.put("comefrom", comefrom);
        hashmap.put("desc1", desc);
        hashmap.put("eatId", eatIds);
        hashmap.put("eatId2", eatIdsDel + "");
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 编辑商品
     **/
    public static HashMap<String, Object> setFoodEdit(int foodId, String name, String barcode, String typeTag, double price,
                                                      double priceMember, String unit, double numSum, int type, String comefrom, String desc, String eatIds, String eatIdsDel) {
        String url = getServerIp() + AppConfig.MANAGER_FOOD_EDIT + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", foodId + "");
        hashmap.put("name", name + "");
        hashmap.put("barcode", barcode + "");
        hashmap.put("typeTag", typeTag + "");
        hashmap.put("price", price + "");
        hashmap.put("memPrice", priceMember + "");
        hashmap.put("unit", unit + "");
        hashmap.put("comefrom", comefrom + "");
        hashmap.put("desc1", desc + "");
        hashmap.put("eatId", eatIds + "");
        hashmap.put("eatId2", eatIdsDel + "");
        hashmap.put("leftCount", numSum + "");
        if (type >= 0) {
            hashmap.put("type", type + "");
        }
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 添加分类
     */
    public static HashMap<String, Object> addClasses(int pid, String typeName, int typeLevel) {
        String url = getServerIp() + AppConfig.CLASSES_ADD + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("pid", pid);
        hashmap.put("typeName", typeName);
        hashmap.put("typeLevel", typeLevel);
        hashmap.put("typeName", typeName);
        hashmap.put("img", 0);// 0表示没有图片
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 编辑分类
     */
    public static HashMap<String, Object> editClasses(int id, int pid, String typeName, int typeLevel) {
        String url = getServerIp() + AppConfig.CLASSES_UPDATE + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", id);
        hashmap.put("pid", pid);
        hashmap.put("typeName", typeName);
        hashmap.put("typeLevel", typeLevel);
        hashmap.put("typeName", typeName);
        hashmap.put("img", 0);// 0表示没有图片
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 添加图片
     */
    public static HashMap<String, Object> addFoodImage(int itemId, int index, Bitmap bitmap) {
        String url = getServerIp() + AppConfig.MANAGER_FOOD_ADD_IMGS + getBussinessId() + getAdminId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("itemId", itemId);
        hashmap.put("index", index);
        return UpImageRequestFactory.getUpImageRequest().setImgUploadPost(url, hashmap, bitmap);
    }

    public static HashMap<String, Object> addFoodImage(int itemId, int index, Uri bitmapuri) {
        String url = getServerIp() + AppConfig.MANAGER_FOOD_ADD_IMGS + getBussinessId() + getAdminId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("itemId", itemId);
        hashmap.put("index", index);
        return UpImageRequestFactory.getUpImageRequest().setImgUploadPost(url, hashmap, bitmapuri);
    }

    /**
     * 获取商品详情
     */
    public static HashMap<String, Object> getFoodDetail(int foodId) {
        String url = getServerIp() + AppConfig.GET_FOODDETAIL + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("iid", foodId);
        hashmap.put("isp", 0);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * adminId String 登录后返回的adminID mobile String 描述 name String 手机号 memCardNo String 地址 memCardType String 纬度
     * memDiscount String 经度 memCreateStore String 联系人
     */
    /**
     * 添加会员列表
     */
    public static HashMap<String, Object> setAddMember(String tel, String name, String cardno, String cardtype, String discount,
                                                       String shopname) {
        String url = getServerIp() + AppConfig.MEMBER_ADD + getBussinessId() + getAdminId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("mobile", tel);
        hashmap.put("name", name);
        hashmap.put("memCardNo", cardno);
        hashmap.put("memCardType", cardtype);
        hashmap.put("memDiscount", discount);
        hashmap.put("memCreateStore", shopname);
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 获取会员列表
     */
    public static HashMap<String, Object> getMemberList(int page, String like) {
        String url = getServerIp() + AppConfig.MEMBER_SEARCH + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(like)) {
            hashmap.put("keyword", like);
        }
        hashmap.put("page", page);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 得到流水
     */
    public static HashMap<String, Object> getListOrder(int page, String dayNo, int dayNearly) {
        String url = getServerIp() + AppConfig.GET_ORDER + getBussinessId() + getAdminId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(dayNo)) {
            hashmap.put("dayNo", dayNo);
        }
        if (dayNearly >= 0) {
            hashmap.put("nearDay", dayNearly);
        }
        hashmap.put("page", page);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 得到流水详情
     */
    public static HashMap<String, Object> getOrderDetail(int orderId) {
        String url = getServerIp() + AppConfig.GET_ORDER_DETAIL + getBussinessId() + getAdminId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("oid", orderId);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 获取商品通过条形码 @param code 条形码
     */
    public static HashMap<String, Object> getFoodByCode(String code) {
        String url = getServerIp() + AppConfig.GET_FOOD_BYCODE + getBussinessId() + getAdminId() + getAdminType();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("barcode", code);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 订单退款 @param orderNo 订单编号
     */
    public static HashMap<String, Object> OrderRefund(String orderNo) {
        String url = getServerIp() + AppConfig.ORDER_REFUND + getBussinessId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("orderNo", orderNo);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 收入}
     */
    public static HashMap<String, Object> getIncome() {
        String url = getServerIp() + AppConfig.GET_INCOME + getBussinessId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 查找商品通过商品名
     */
    public static HashMap<String, Object> getFoodByName(String foodname, int page) {
        // searchItem?condition=foodname&page=1&businessId=0
        String url = getServerIp() + AppConfig.SEARCH_FOOD_LIKE + getBussinessId() + getAdminType();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("condition", foodname);
        hashmap.put("page", page);
        return HttpRequestFactory.getHttpRequest().requestByGet(url, hashmap);
    }

    /**
     * 设置商店名称
     */
    public static HashMap<String, Object> setBusinessName(String businessname) {
        String url = getServerIp() + AppConfig.BUSINESS_CHANGE + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("name", businessname);
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 设置商店地址
     */
    public static HashMap<String, Object> setBusinessAddress(String businessAddress, double lat, double lng) {
        String url = getServerIp() + AppConfig.BUSINESS_CHANGE + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("addr", businessAddress);
        hashmap.put("lat", lat);
        hashmap.put("lng", lng);
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }

    /**
     * 设置商店logo
     */
    public static HashMap<String, Object> setBusinessLogo(Uri bitmapUri) {
        String url = getServerIp() + AppConfig.BUSINESS_CHANGE + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        return new ImageUploadClientRequest().setImgUploadPost(url, hashmap, bitmapUri);
    }

    /**
     * 设置昵称
     */
    public static HashMap<String, Object> setAdminName(String adminName) {
        String url = getServerIp() + AppConfig.BUSINESS_CHANGE + getBussinessId() + getAdminNameInId();
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("adminName", adminName);
        return HttpRequestFactory.getHttpRequest().requestByPost(url, hashmap);
    }
}
