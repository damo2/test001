/**
 * contentprovider共享信息
 */
/**
 
 <provider
 android:name="com.shwm.freshmallpos.contentprovider.FreshmallPosContentProvider"
 android:authorities="com.shwm.freshmallpos.authority"
 android:exported="true"
 android:multiprocess="false" />

 android:authorities 和 FreshmallPosContent.AUTHORITY 一定要相同。
 android:exported="true" 让另一个程序能访问到共享的信息
 */

package com.shwm.freshmallpos.contentprovider;