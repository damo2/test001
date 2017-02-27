package com.shwm.freshmallpos.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.value.EnumMoneyColor;
import com.shwm.freshmallpos.value.ValueFuhao;
import com.shwm.freshmallpos.base.ApplicationMy;

public class StringFormatUtil {
	private static final String strFail = ApplicationMy.getContext().getResources().getString(R.string.statu_fail_info_statu);

	public static String getFailInfoStatu(int statu, Exception info) {
		String resurnInfo = "";
		if (info != null) {
			resurnInfo = info.toString();
		}
		resurnInfo = String.format(strFail, resurnInfo, "statu=" + statu);
		return resurnInfo;
	}



	/** “20.00” 转成“￥20.00” ￥为黑色0.6小字、 “20.00” 为黄色 */
	public static SpannableString moneyFormat(double money) {
		return moneyFormat(money, EnumMoneyColor.ORANGE, false);
	}

	public static SpannableString moneyFormat(double money, EnumMoneyColor moneyColor, boolean clear0) {
		return moneyFormat(money + "", moneyColor, clear0);
	}

	public static SpannableString moneyFormat(String moneyStr, EnumMoneyColor moneyColor, boolean clear0) {
		SpannableString spannableString = null;
		if (clear0 && StringUtil.getDouble(moneyStr) == 0) {
			moneyStr = ValueFuhao.FUHAO_RMB;
		} else {
			moneyStr = ValueFuhao.FUHAO_RMB + moneyStr;
		}spannableString = new SpannableString(moneyStr);

		spannableString.setSpan(new ForegroundColorSpan(ColorUtil.ColorBlack), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new RelativeSizeSpan(0.6f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (spannableString.length() > 1) {
			ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ColorUtil.ColorBlack);
			if (moneyColor == EnumMoneyColor.ORANGE) {
				foregroundColorSpan = new ForegroundColorSpan(ColorUtil.ColorOrange);
			} else if (moneyColor == EnumMoneyColor.RED) {
				foregroundColorSpan = new ForegroundColorSpan(ColorUtil.ColorRed);
			} else if (moneyColor == EnumMoneyColor.BLACK) {
				foregroundColorSpan = new ForegroundColorSpan(ColorUtil.ColorBlack);
			}
			spannableString.setSpan(foregroundColorSpan, 1, moneyStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spannableString;
	}

	/**
	 * 整单折扣
	 * 
	 * @param money
	 * @return
	 */
	public static SpannableString couponDiscount(double money) {
		SpannableString spannableString = new SpannableString(money + " 折");
		int length = spannableString.length();
		spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new RelativeSizeSpan(0.6f), length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}

	/**
	 * 整单满减
	 * 
	 * @param money
	 * @return
	 */
	public static SpannableString couponMoneydown(double money) {
		SpannableString spannableString = new SpannableString(UtilMath.currency(money));
		int length = spannableString.length();
		spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		int pointPos = spannableString.toString().indexOf(ValueFuhao.FUHAO_NUM_POINT);
		spannableString.setSpan(new RelativeSizeSpan(0.6f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new RelativeSizeSpan(0.6f), pointPos, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}
}
