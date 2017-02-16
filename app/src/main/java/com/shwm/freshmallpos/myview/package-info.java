/**自定义View
 * 这个包名和类名不能改变
 */
package com.shwm.freshmallpos.myview;

/**
 *
 *          RoundImageView 使用
 *          
 *          
 <!-- 没有指定圆形ImageView属性时，默认没有外边圆颜色 --> 
 <!-- 需要将图片资源自定为src ，或在程序中setImageResource(res) 不能设置background为图片，这样不能达到圆形效果--> 
 <com.dxd.roundimageview.RoundImageView  
 android:layout_width="100dp" 
 android:layout_height="100dp" 
 android:src="@drawable/img" 
 /> 

 <!-- border_outside_color 外部圆圈的颜色 --> 
 <!-- border_inside_color 内部部圆圈的颜色 --> 
 <!-- border_thickness 外圆和内圆的宽度 --> 
 <com.dxd.roundimageview.RoundImageView  
 android:layout_width="100dp" 
 android:layout_height="100dp" 
 android:src="@drawable/img" 
 app:border_inside_color="#bc0978" 
 app:border_outside_color="#ba3456" 
 app:border_thickness="1dp" 
 /> 
 * 
 */
