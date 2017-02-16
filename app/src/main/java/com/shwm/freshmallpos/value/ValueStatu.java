package com.shwm.freshmallpos.value;

public class ValueStatu {
	/** 异步任务 成功 */
	public static final int SUCCESS = 1001;
	/** 异步任务 失败 */
	public static final int FAIL = 1002;
	/** 异步任务 异常 hashmap==null */
	public static final int Execute = 1006;

	/** http 网络请求成功 */
	public static final int REQUEST_SUCCESS = 2001;
	/** http 网络请求失败 */
	public static final int REQUEST_FAIL = 2002;
	/** http 网络请求异常 */
	public static final int REQUEST_Execute = 2003;
	/** http 网络请求超时 */
	public static final int REQUEST_TIMEOUT = 2004;

	/***********************************************************************************/
	/** 用户名或密码错误 */
	public static final int LOGIN_NamePwdError = 10020;

	/**不支持蓝牙*/
	public static final int BluetoothNotsupported = 3001;
}
