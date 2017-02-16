package com.shwm.freshmallpos.wechatpay;

public class TwoTuple<A, B> {

	public final A first;
	public final B second;

	public TwoTuple(A a, B b) {
		first = a;
		second = b;
	}

	/**
	 * 创建一个二元组, 用所给参数初始化
	 * 
	 * @param a
	 *            第一个参数
	 * @param b
	 *            第二个参数
	 * @param <A>
	 *            第一个参数的类型
	 * @param <B>
	 *            第二参数的类型
	 * @return 包含所给参数的二元组
	 */
	public static <A, B> TwoTuple<A, B> tuple(A a, B b) {
        return new TwoTuple<A, B>(a, b);
    }
}
