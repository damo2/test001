package com.shwm.freshmallpos.wechatpay;

import java.io.Serializable;

public class ResultInfo implements Serializable {
	private String returnCode;// SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	private String returnMsg;// 返回信息，如非空，为错误原因
	private String resultCode;// 业务结果 SUCCESS/FAIL
	private String errCode;// 错误代码
	private String errCodeDes;// 错误代码描述
	private String tradeType;// 支付类型为MICROPAY(即扫码支付)
	private int totalFee;// 订单总金额，单位为分，只能为整数
	private int settlementTotalFee;// 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
	private int cashFee;// 订单现金支付金额

	private String prepayId;// 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	private String codeQR;// 二维码

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public int getCashFee() {
		return cashFee;
	}

	public void setCashFee(int cashFee) {
		this.cashFee = cashFee;
	}

	public String getCodeQR() {
		return codeQR;
	}

	public void setCodeQR(String codeQR) {
		this.codeQR = codeQR;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public int getSettlementTotalFee() {
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(int settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}

}
