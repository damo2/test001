package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;

/**
 * 微信商户平台开发文档地址 {@link}https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1
 * 
 * @author wr 2017-2-7
 */
public interface IPayWechatListener {
	/**
	 * 扫描用户的二位码
	 * 
	 * @param trimCode
	 *            扫码支付授权码，设备读取用户微信中的条码或者二维码信息 String(128)
	 * @param totalFee
	 *            订单总金额，单位为分，只能为整数
	 * @param listFood
	 *            商品列表
	 * @param detail
	 *            <p>
	 *            商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。 goods_detail []： <br>
	 *            └ goods_id String 必填 32 商品的编号<br>
	 *            └wxpay_goods_id String 可选 32 微信支付定义的统一商品编号<br>
	 *            └ goods_name String 必填 256 商品名称 └ goods_num Int 必填 商品数量<br>
	 *            └price Int 必填 商品单价，单位为分<br>
	 *            注意： a、单品总金额应<=订单总金额total_fee，否则会导致下单失败。 b、 单品单价，如果商户有优惠，需传输商户优惠后的单价<br>
	 *            {"goods_detail":[<br>
	 *            { "goods_id":"iphone6s_16G",
	 *            "wxpay_goods_id":"1001","goods_name":"iPhone6s 16G","goods_num":1,"price":528800, },<br>
	 *            { "goods_id":"iphone6s_32G","wxpay_goods_id":"1002", "goods_name":"iPhone6s 32G",
	 *            "quantity":1,"price":608800, } ] }<br>
	 */
	void scanCodeUser(String trimCode, int totalFee, String detail, IRequestListener<HashMap<String, Object>> iRequestListener);

	/**
	 * 商家展示二维码
	 * 
	 * @param totalFee
	 *            订单总金额，单位为分，只能为整数
	 * @param detail
	 * @param iRequestListener
	 *            ValueKey.CODERQ 二维码
	 */
	void getScanCodeBusiness(int totalFee, String detail, IRequestListener<String> iRequestListener);

}
