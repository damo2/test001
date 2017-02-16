package com.shwm.freshmallpos.bluetooth;

import java.nio.charset.Charset;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.util.BitmapUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilMath;

/**
 * 蓝牙打印,排版打印格式
 * 
 * @author linjinfa@126.com
 * @date 2013-6-17 下午3:37:10
 */
public class UtilBluetoothPrintFormat {

	/**
	 * 打印纸一行最大的字节
	 */
	private static final int LINE_BYTE_SIZE = 30;
	/**
	 * 分隔符
	 */
	public static final String SEPARATOR = "$";

	public static final int KongSize = 8;// 前面空的字节数
	public static final int OneSize = 6;
	public static final int TwoSize = 10;
	public static final int ThreeSize = 6;

	/**
	 * 排版居中标题
	 * 
	 * @param title
	 * @return
	 */
	public static String printTitle(String title) {
		return printTitle(title, 1);
	}

	public static String printTitle(String title, int maxsizeInLine) {
		StringBuffer sb = new StringBuffer();
		sb.append(printBlank((maxsizeInLine - getBytesLength(title)) / 2));
		sb.append(title);
		sb.append("\n");
		return sb.toString();
	}

	public static String printTitle(String title, double textSizeToDefault) {
		StringBuffer sb = new StringBuffer();
		sb.append(printBlank((LINE_BYTE_SIZE - (getBytesLength(title) * textSizeToDefault)) / 2));
		sb.append(title);
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 打印间隔 - - - - - - -
	 * 
	 * @param title
	 * @return
	 */
	public static String printLine() {
		StringBuffer sb = new StringBuffer();
		sb.append(printLineLength(LINE_BYTE_SIZE));
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 打印 多个符号
	 * 
	 * @param length
	 * @return
	 */
	public static String printLineLength(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i % 2 == 0) {
				sb.append("-");
			} else {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * 打印**********
	 * 
	 * @param length
	 * @return
	 */
	public static String printXingLength(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append("*");
		}
		return sb.toString();
	}

	public static String printBlank(double d) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < d; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * 打印间隔标题 - - -title - - -
	 * 
	 * @param title
	 * @return
	 */
	public static String printTitleLine(String title) {
		StringBuffer sb = new StringBuffer();
		int leftL = (LINE_BYTE_SIZE - getBytesLength(title)) / 2;
		int rightL = LINE_BYTE_SIZE - leftL - getBytesLength(title);
		if (leftL % 2 == 0) {
			leftL++;
			rightL--;
		}
		sb.append(printLineLength(leftL));
		sb.append(title + " ");
		rightL--;
		sb.append(printLineLength(rightL - 1));
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 打印间隔标题 ****** title ******
	 * 
	 * @param title
	 * @return
	 */
	public static String printTitleXing(String title) {
		StringBuffer sb = new StringBuffer();
		int leftL = (LINE_BYTE_SIZE - getBytesLength(title)) / 2;
		int rightL = LINE_BYTE_SIZE - leftL - getBytesLength(title);
		leftL--;
		rightL--;
		sb.append(printXingLength(leftL));
		sb.append(" " + title + " ");
		sb.append(printXingLength(rightL));
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 打印左右对齐
	 * 
	 * @param title
	 * @return
	 */
	public static String printLeftRight(String leftS, String rightS) {
		StringBuffer sb = new StringBuffer();
		int leftL = getBytesLength(leftS);
		int rightL = getBytesLength(rightS);
		int centerL = LINE_BYTE_SIZE - leftL - rightL;
		if (centerL > 0) {
			sb.append(leftS);
			sb.append(printBlank(centerL));
			sb.append(rightS + "\n");
		} else {
			sb.append(leftS + "\n");
			sb.append(printBlank(LINE_BYTE_SIZE - rightL));
			sb.append(rightS + "\n");
		}
		return sb.toString();
	}

	/**
	 * 打印3列信息标题
	 * 
	 * @param one
	 * @param two
	 * @param three
	 * @return
	 */
	public static String printFoodInfoTitle(String oneTitle, String twoTitle, String threeTitle) {
		StringBuffer sb = new StringBuffer();
		int leftOneL = (OneSize - getBytesLength(oneTitle)) / 2;
		int rightOneL = OneSize - leftOneL - getBytesLength(oneTitle);
		int leftTwoL = (TwoSize - getBytesLength(twoTitle)) / 2;
		int rightTwoL = TwoSize - leftTwoL - getBytesLength(twoTitle);
		int leftThreeL = ThreeSize - getBytesLength(threeTitle);

		sb.append(printBlank(KongSize));
		//
		sb.append(oneTitle);
		sb.append(printBlank(leftOneL));
		sb.append(printBlank(rightOneL));
		//
		sb.append(printBlank(leftTwoL));
		sb.append(twoTitle);
		sb.append(printBlank(rightTwoL));
		//
		sb.append(printBlank(leftThreeL));
		sb.append(threeTitle);
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 打印3列信息
	 * 
	 * @param one
	 * @param two
	 * @param three
	 * @return
	 */
	public static String printFoodInfo(String one, String two, String three) {
		StringBuffer sb = new StringBuffer();
		sb.append(printBlank(KongSize));
		// 第1列左对齐
		sb.append(one);
		sb.append(printBlank(OneSize - getBytesLength(one)));
		// 第2列居中
		int twoLeft = (TwoSize - getBytesLength(two)) / 2;
		int twoRight = TwoSize - twoLeft - getBytesLength(two);
		sb.append(printBlank(twoLeft));
		sb.append(two);
		sb.append(printBlank(twoRight));
		// 第3列右对齐
		sb.append(printBlank(ThreeSize - getBytesLength(three)));
		sb.append(three);
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 获取最大长度
	 * 
	 * @param msgs
	 * @return
	 */
	private static int getMaxLength(Object[] msgs) {
		int max = 0;
		int tmp;
		for (Object oo : msgs) {
			tmp = getBytesLength(oo.toString());
			if (tmp > max) {
				max = tmp;
			}
		}
		return max;
	}

	/**
	 * 获取数据长度
	 * 
	 * @param msg
	 * @return
	 */
	private static int getBytesLength(String msg) {
		return msg.getBytes(Charset.forName("GB2312")).length;
	}

	public static String getPrintForOrderTop(Context context, OrderEntity orderinfo) {
		StringBuffer sb = new StringBuffer();
		// 排版标题
		sb.append(printTitleXing("#" + orderinfo.getOrderno()));
		sb.append(printLine());
		return sb.toString();
	}

	public static String getPrintForOrderHead(Context context, OrderEntity orderinfo) {
		StringBuffer sb = new StringBuffer();
		// 排版标题
		sb.append(printTitle(BusinessInfo.getBusinessName(), 2.5) + "\n");
		sb.append(printTitle(" - " + orderinfo.getMoney() + " - " + "\n", 2.5));
		return sb.toString();
	}

	public static String getPrintForOrderInfo(Context context, OrderEntity orderinfo, List<FoodEntity> listfood) {
		StringBuffer sb = new StringBuffer();
		// 排版标题
		// sb.append(printTitleXing("#" + orderinfo.getNoNum()));
		// sb.append(printTitle(ShopInfo.getShopName(context)));
		// sb.append(printTitle(" - " + orderinfo.getPayTypeString() + " - " + "\n"));
		// sb.append(printLine());
		sb.append("订单：" + orderinfo.getOrderno() + "\n");
		sb.append("时间：" + orderinfo.getTimeCreat() + "\n");
		// sb.append("预约时间：" + orderinfo.getTimePre() + "\n");
		// sb.append(printLine());
		// UserEntity user = orderinfo.getUser();
		// if (user != null) {
		// sb.append("姓名：" + user.getName() + "\n");
		// sb.append("电话：" + user.getTel() + "\n");
		// sb.append("地址：" + user.getAddress() + "\n");
		// } else {
		// sb.append("\n没用用户信息\n");
		// }
		// sb.append("备注：  无\n");
		// sb.append("发票：  无\n");

		sb.append(printLine());
		sb.append(printFoodInfoTitle("单价", "数量", "小计"));
		sb.append(printLine());
		if (listfood != null && listfood.size() > 0) {
			for (FoodEntity food : listfood) {
				sb.append(food.getName() + "\n");
				sb.append(printFoodInfo(
						StringUtil.doubleTrans(food.getPrice()),
						StringUtil.doubleTrans(food.getNum()) + (StringUtil.isExistNum(food.getUnit()) == true ? "x" : "") + food.getUnit(),
						StringUtil.doubleTrans(UtilMath.mul(food.getPrice(), food.getNum()))));
			}
		}
		// sb.append(printLine());
		// sb.append(printTitleLine("其他费用"));
		// sb.append(printLeftRight("包装", UtilMath.doubleTrans(0)));
		// sb.append(printLeftRight("配送费", UtilMath.doubleTrans(orderinfo.getPriceSend())));
		// sb.append(printLeftRight("使用优惠券", "-" + UtilMath.doubleTrans(0)));
		// sb.append(printLeftRight("使用优质币", "-" + UtilMath.doubleTrans(0)));
		sb.append(printLine());
		sb.append(printLeftRight("总计", "￥" + orderinfo.getMoney() + ""));
		sb.append(printLine());
		sb.append(printLeftRight("优惠", "￥" + "0.00"));
		sb.append(printLeftRight("应收", "￥" + orderinfo.getMoney() + ""));
		sb.append(printLeftRight("实收", "￥" + orderinfo.getPayMoney() + ""));
		sb.append(printLeftRight("找零", "￥" + UtilMath.sub(orderinfo.getPayMoney(), orderinfo.getMoney()) + ""));
		sb.append(printLeftRight("支付方式", orderinfo.getPayTypeTag() + ""));
		sb.append(printLine());
		sb.append(printTitle("谢谢惠顾，欢迎下次光临！\n"));
		sb.append(printTitleXing("#" + orderinfo.getOrderno()));
		sb.append("\n\n\n");
		return sb.toString();
	}

	/**
	 * 一维码
	 * 
	 * @return
	 */
	public static Bitmap getPrintOneDCode(String OrderNo) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapUtil.CreateOneDCode(OrderNo);
			bitmap = PicFromPrintUtils.compressPic(bitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	public static String getPrintBotton() {
		StringBuffer sb = new StringBuffer();
		sb.append(printTitle("谢谢惠顾，欢迎下次光临！\n"));
		sb.append(printTitleXing("#66"));
		sb.append("\n\n\n");
		return sb.toString();
	}
}
