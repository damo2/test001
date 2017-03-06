package com.shwm.freshmallpos.wechatpay;

import java.util.HashMap;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.shwm.freshmallpos.model.biz.IPayWechatListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.net.HttpUrlRequest;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;

public class AlipayPay implements IPayWechatListener {
    private static final String TAG = AlipayPay.class.getSimpleName();

    @Override
    public void scanCodeUser(final String trimCode, final int totalFee, final String detail,
                             final IRequestListener<HashMap<String, Object>> iRequestListener) {
        // TODO Auto-generated method stub

        final double money = UtilMath.div(totalFee + "", "100", 2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url=AlipayConfig.URL;
                String urlParam = AlipayConfig.getURlParam(money, trimCode);
                UL.e(TAG,urlParam);
                HashMap<String, Object> hashmap = new HttpUrlRequest().requestByPost(url,urlParam);
            }
        }
        ).start();

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APP_ID,
//						AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
//						AlipayConfig.SIGN_TYPE); // 获得初始化的AlipayClient
//				StringBuilder sb=new StringBuilder();
//				sb.append("{\"out_trade_no\":\"" + AlipayConfig.getOrderNoForRecharge(totalFee) + "\",");
//				sb.append("\"total_amount\":\""+money+"\",");
//				sb.append("\"subject\":\""+AlipayConfig.Subject+"\",");
//				sb.append("\"time_expire\":\""+AlipayConfig.getTimeExpire()+"\"}");
//				AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();// 创建API对应的request类
//				request.setBizContent(sb.toString());// 设置业务参数
//				AlipayTradePrecreateResponse response;
//				try {
//					response = alipayClient.execute(request);
//					UL.e(TAG, response.isSuccess()?"调用成功":"调用失败"+"\n"+response.getBody());
//				} catch (AlipayApiException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
    }

    @Override
    public void getScanCodeBusiness(int totalFee, String detail, IRequestListener<String> iRequestListener) {
        // TODO Auto-generated method stub


    }

}