package com.l1fan.ane.uc;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import cn.uc.gamesdk.UCCallbackListener;
import cn.uc.gamesdk.UCCallbackListenerNullException;
import cn.uc.gamesdk.UCFloatButtonCreateException;
import cn.uc.gamesdk.UCGameSDK;
import cn.uc.gamesdk.UCGameSDKStatusCode;
import cn.uc.gamesdk.UCLogLevel;
import cn.uc.gamesdk.UCLoginFaceType;
import cn.uc.gamesdk.UCOrientation;
import cn.uc.gamesdk.info.FeatureSwitch;
import cn.uc.gamesdk.info.GameParamInfo;
import cn.uc.gamesdk.info.OrderInfo;
import cn.uc.gamesdk.info.PaymentInfo;

import com.l1fan.ane.SDKContext;

public class SDK extends SDKContext {

	public void init() throws UCCallbackListenerNullException, JSONException {
		JSONObject init = getJsonData();
		Bundle md = getMetaData();
		GameParamInfo gpi = new GameParamInfo();
		gpi.setGameId(init.optInt(APPID,init.optInt("gameId",md.getInt("gameId"))));
		gpi.setFeatureSwitch(new FeatureSwitch(true, true));
		UCGameSDK.defaultSDK().setOrientation(UCOrientation.PORTRAIT);
		UCGameSDK.defaultSDK().setLogoutNotifyListener(logoutListener);
		UCGameSDK.defaultSDK().setLoginUISwitch(UCLoginFaceType.USE_WIDGET);
		UCGameSDK.defaultSDK().initSDK(getActivity(), UCLogLevel.DEBUG, init.optBoolean("debugMode", md.getBoolean(DEBUGMODE,false)), gpi,
				initListener);
	}

	private UCCallbackListener<String> initListener = new UCCallbackListener<String>() {

		@Override
		public void callback(int arg0, String arg1) {
			if (arg0 == 0) {
				dispatchData(EVENT_INIT,arg1);
			}else{
				dispatchError(EVENT_INIT, arg1);
			}
		}
	};

	private UCCallbackListener<String> logoutListener = new UCCallbackListener<String>() {

		@Override
		public void callback(int arg0, String arg1) {
			if (arg0 == 0) {
				dispatchData(EVENT_LOGOUT,arg1);
			}else{
				dispatchError(EVENT_LOGOUT, arg1);
			}
		}
	};

	public void userLogin() throws UCCallbackListenerNullException {

		UCGameSDK.defaultSDK().login(getActivity(), new UCCallbackListener<String>() {

			@Override
			public void callback(int arg0, String arg1) {
				JSONObject json = new JSONObject();
				try {
					json.put(TOKEN, UCGameSDK.defaultSDK().getSid());
					dispatchData(EVENT_LOGIN,json);
					UCGameSDK.defaultSDK().createFloatButton(getActivity(), new UCCallbackListener<String>() {

						@Override
						public void callback(int statuscode, String arg1) {
							if(statuscode == UCGameSDKStatusCode.SDK_OPEN){ //将要打开SDK界面
							}else if(statuscode == UCGameSDKStatusCode.SDK_CLOSE){ //将要关闭SDK界面,返回游戏画面
							}
						}});
					UCGameSDK.defaultSDK().showFloatButton(getActivity(), 100, 50, true);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (UCCallbackListenerNullException e) {
					e.printStackTrace();
				} catch (UCFloatButtonCreateException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void pay() throws UCCallbackListenerNullException, JSONException {
		JSONObject pay = getJsonData();
		PaymentInfo pInfo = new PaymentInfo();
		pInfo.setCustomInfo(pay.optString(EXT));
		pInfo.setAmount(Float.valueOf(pay.optInt(AMOUNT) / 100));
		pInfo.setNotifyUrl(pay.optString(NOTIFY_URL));
		pInfo.setTransactionNumCP(pay.optString(ORDER_ID));
		try {
			UCGameSDK.defaultSDK().pay(getActivity(), pInfo, new UCCallbackListener<OrderInfo>() {

				@Override
				public void callback(int arg0, OrderInfo arg1) {
					dispatchData(EVENT_PAY);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void submitExtendData() throws JSONException {
		UCGameSDK.defaultSDK().submitExtendData("loginGameRole", new JSONObject(getData()));
	}

	public void userLogout() throws UCCallbackListenerNullException {
		UCGameSDK.defaultSDK().logout();
	}

	public void destroy() {
		UCGameSDK.defaultSDK().exitSDK(getActivity(), new UCCallbackListener<String>() {

			@Override
			public void callback(int statuscode, String data) {
				if (UCGameSDKStatusCode.SDK_EXIT_CONTINUE==statuscode) {
					dispatchData(EVENT_DESTROY,"destroy:"+data);
				}else if (UCGameSDKStatusCode.SDK_EXIT==statuscode) {
					dispatchError(EVENT_DESTROY, CODE_ERR_CANCEL,data+":"+statuscode);
				}
			}
		});
	}


}
