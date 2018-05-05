package com.ice.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.ice.domain.CIResult;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.WinphoneNotification;

/**
 * 极光推送工具类 推送的message都是JSON的{@link cn.com.huiju.platform.basic.vo.CIResult CIResult}
 * 对象。 其中code为推送动作代码. msg为服务器请求极光推送的时间，毫秒数。 result为额外的推送信息
 * 
 * @author luoyh
 * @date Apr 1, 2015
 */
public abstract class JPushUtils {

	private static ExecutorService  pool = Executors.newCachedThreadPool(); 
	
	/**
	 * 推送对象为设备. -> TARGET_DEVS 推送对象为APP. -> TARGET_APP
	 * 
	 * @author luoyh
	 * @date Jun 24, 2015
	 */
	public static enum PushTarget {
		/** 推送对象为设备 */
		TARGET_DEVS(0),
		/** 推送对象为APP */
		TARGET_APP(1);
		private int code;

		private PushTarget(int code) {
			this.code = code;
		}
	}

	// 【20150619 add by xuyc start】
	/** 设备远程重启推送 */
	public final static int DEVICE_REBOOT_PUSH_CNT = 10001;
	/** 设备重置密码推送 */
	public final static int DEVICE_RESET_PUSH_CNT = 10002;
	/** 设备更新联系人推送 */
	public final static int DEVICE_CONTACTS_PUSH_CNT = 10003;
	/** 设备更新卡推送 */
	@Deprecated
	public final static int DEVICE_CARD_PUSH_CNT = 10004;
	/** 设备开门推送 */
	public final static int DEVICE_OPEN_PUSH_CNT = 10005;
	/** 人脸识别验证推送 */
	public final static int FACE_VERIFY_PUSH_CNT = 10015;
	// 【20150619 add by xuyc end】
	/** 管理卡更新推送 */
	public final static int PUSH_DEVICE_CARD_M = 10006;
	/** 业主卡更新推送 */
	public final static int PUSH_DEVICE_CARD_O = 10007;
	/** 消息发布推送 */
	public final static int PUSH_APP_MSG = 10008;
	/** 保修处理通知 */
	public final static int PUSH_APP_SER = 10009;
	// 【20151109 add by xuyc start】
	/** 设备获取公告推送 */
	public final static int PUSH_DEVICE_NOTICE = 10010;
	// 【20151109 add by xuyc end】
	/**意见反馈回复通知 */
	public final static int PUSH_APP_FEED = 100011;
	// 【20160218 add by tml start】
	/**设备音量调节通知 */
	public final static int PUSH_DEVICE_VOLUME = 10012;
	// 【20160218 add by tml end】
	// 【20160509 add by xuyc start】
	/** 设备同步数据推送 */
	public final static int PUSH_DEVICE_SYNCHRON = 10013;
	// 【20160509 add by xuyc end】
	// 【201600805 add by xuyc start】
	/** 设备SIP账户信息推送 */
	public final static int PUSH_DEVICE_SIP = 10014;
	// 【20160805 add by xuyc end】

	/** 手机端通知类型: 消息 */
	public final static String APP_NOTIFY_TYPE_MSG = "0";//小区公告（type= ）
	/** 手机端通知类型: 投票 */
	public final static String APP_NOTIFY_TYPE_VOTE = "1";
	/** 手机端通知类型: 报事报修 */
	public final static String APP_NOTIFY_TYPE_SER = "2";
	/** 手机端通知类型: 投诉建议 */
	public final static String APP_NOTIFY_TYPE_ADVICE = "3";
	/** 手机端通知类型:意见反馈回复 */
	public final static String APP_NOTIFY_TYPE_FEED = "4";
	/** 手机端通知类型:车位申请处理 */
	public final static String APP_PARK_APPLY = "5";
	/** 手机端通知类型:有新快递 */
	public final static String APP_EXPRESS_ENTER = "6";
	/** 手机端通知类型:业主审核消息 */
	public final static String APP_NOTIFY_TYPE_AUDIT = "7";
//  //小区公告（type= ），商城消息（type= ）,系统消息（type= ）,报警消息（type=0 ）
//  //投诉建议（type=3），报事报修（type=2）,意见征集（type=1）
	public static final String APP_NOTIFY_SYS = "8";//系统消息（type= ）
	public static final String APP_NOTIFY_ALARM = "9";//报警消息（type=0 ）
	public static final String APP_NOTIFY_EBUSINESS = "10";//商城消息（type= ）
	
	private static final Logger log = Logger.getLogger(JPushUtils.class);

	@Value("jpush.ios_sound")
	private static  String IOS_SOUND;
	@Value("jpush.secret")
	private static  String JPUSH_SECRET;
	@Value("jpush.app_key")
	private static  String JPUSH_APPKEY;

	private  static JPushClient jpushClient = new JPushClient(JPUSH_SECRET,JPUSH_APPKEY );
	@Value("jpush.devs_time_to_live")
	private  static long TIME_LIVE ;
	
	
    
	/**
	 * 推送对象前缀(D_：设备， A_：APP).<br>
	 */
	private final static String[] PREFIXS = { "D_", "A_" };

	private static void showAPIRequestExceptionMessage(APIRequestException e) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("errorCode", e.getErrorCode());
		result.put("errorMessage", e.getErrorMessage());
		result.put("status", e.getStatus());
 	}

	private static boolean validate(PushTarget target) {
		if (target.code >= PREFIXS.length) {
			log.info("发送对象target[" + target + "]不存在");
			return false;
		}
		return true;
	}

	/**
	 * 极光推送消息
	 * 
	 * @see cn.com.huiju.platform.basic.util.JPushUtils.PushTarget PushTarget
	 * @param code
	 *            推送代码
	 * @param target
	 *            推送对象，0：设备，1：APP see
	 *            {@link cn.com.huiju.platform.basic.util.JPushUtils.PushTarget
	 *            PushTarget}
	 * @param isTag
	 *            是否是tag推送，否则为alias方式
	 * @param auds
	 *            推送对象集合
	 * @return
	 */
	public static int pushMessage(int code, PushTarget target, boolean isTag, Map<String, String> extras, String... auds) {
		if (!validate(target)) {
			return 1;
		}
		CIResult cir = new CIResult();
		cir.setCode(code);
		cir.setMsg(String.valueOf(DateUtils.getTime()));
		cir.setData(extras);
		return pushMessage(FastJSONUtil.object2json(cir), target, isTag, extras, auds);
	}
	public static int pushMessage(int code, PushTarget target, boolean isTag, String... auds) {
		if (!validate(target)) {
			return 1;
		}
		CIResult cir = new CIResult();
		cir.setCode(code);
		cir.setMsg(String.valueOf(DateUtils.getTime()));
		return pushMessage(FastJSONUtil.object2json(cir), target, isTag, null, auds);
	}
	public static int pushMessage(int slaveId,int code, PushTarget target, boolean isTag, String... auds) {
		if (!validate(target)) {
			return 1;
		}
		CIResult cir = new CIResult();
		cir.setCode(code);
		cir.setData(slaveId);
		cir.setMsg(String.valueOf(DateUtils.getTime()));
		return pushMessage(FastJSONUtil.object2json(cir), target, isTag, null, auds);
	}

	/**
	 * 极光推送消息
	 * 
	 * @see cn.com.huiju.platform.basic.util.JPushUtils.PushTarget PushTarget
	 * @param result
	 *            推送内容
	 * @param target
	 *            推送对象，0：设备，1：APP see
	 *            {@link cn.com.huiju.platform.basic.util.JPushUtils.PushTarget
	 *            PushTarget}
	 * @param isTag
	 *            是否是tag推送，否则为alias方式
	 * @param auds
	 *            推送对象集合
	 * @return
	 */
	public static int pushMessage(CIResult result, PushTarget target, boolean isTag, Map<String, String> extras, String... auds) {
		if (!validate(target)) {
			return 1;
		}
		result.setMsg(String.valueOf(DateUtils.getTime()));
		result.setData(extras);
		
		return pushMessage(FastJSONUtil.object2json(result), target, isTag, extras, auds);
	}

	private static int jpushMessage(String content, PushTarget target, boolean isTag, Map<String, String> extras, String... auds) {
		if (!validate(target)) {
			return 1;
		}
		if(null == extras) extras = new HashMap<String, String>();
		buildAudiences(target, auds);
		int jRet = 0;
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(isTag ? Audience.tag(auds) : Audience.alias(auds))
				.setMessage(Message.newBuilder().setMsgContent(content).addExtras(extras).build())
				.setOptions(Options.newBuilder().setTimeToLive(TIME_LIVE).build())
				.build();
		try {
			PushResult result = jpushClient.sendPush(payload);
			log.info(result);
			// 1:连接超时。2:读取超时。3:无法解析域名。
		} catch (APIConnectionException e) {
			log.info(e.getMessage());
			jRet = 1;
			// 请求异常
		} catch (APIRequestException e) {
			showAPIRequestExceptionMessage(e);
			jRet = 1;
		}
		return jRet;
		
		// 【add by xuyc 2016/02/24 start】
		// 只有设备需要二次推送（个推）
//		int gRet = 0;
//		if (target.code == PushTarget.TARGET_DEVS.code) {
//			gRet = GPushUtils.pushMessage(content, isTag, auds);
//		}
		// 只要其中之一推送成功，则提示推送成功
//		if (jRet == 0 || gRet == 0) {
//			return 0;
//		} else {
//			return 1;
//		}
		// 【add by xuyc 2016/02/24 end】
	}
	
	/**
	 * 极光推送消息
	 * 
	 * @see cn.com.huiju.platform.basic.util.JPushUtils.PushTarget PushTarget
	 * @param content
	 *            推送内容
	 * @param target
	 *            推送对象，0：设备，1：APP see
	 *            {@link cn.com.huiju.platform.basic.util.JPushUtils.PushTarget
	 *            PushTarget}
	 * @param isTag
	 *            是否是tag推送，否则为alias方式
	 * @param auds
	 *            推送对象集合
	 * @return
	 */
	public static int pushMessage(String content, PushTarget target, boolean isTag, Map<String, String> extras, String... auds) {
		if (target.code == PushTarget.TARGET_DEVS.code) {
			return devsPushMessage(content, target, isTag, extras, auds);
		} else {
			return jpushMessage(content, target, isTag, extras, auds);
		}
		
	}
	
	private static int devsPushMessage(String content, PushTarget target, boolean isTag, Map<String, String> extras, String... auds) {
		CountDownLatch cdl = new CountDownLatch(2);
		Runner jpush = new Runner(cdl, 0, content, target, isTag, extras, auds);
		Runner gpush = new Runner(cdl, 1, content, target, isTag, extras, auds);
		pool.execute(jpush);
		pool.execute(gpush);
		try {
			cdl.await();
			if(jpush.getResult() == 0 || gpush.getResult() == 0) {
				return 0;
			}
		} catch (InterruptedException e) {
			log.info(e.getMessage());
		}
		return 1;
	}

	/**
	 * 极光推送通知
	 * 
	 * @see cn.com.huiju.platform.basic.util.JPushUtils.PushTarget PushTarget
	 * @param content
	 *            通知内容
	 * @param title
	 *            通知标题
	 * @param target
	 *            推送对象 see
	 *            {@link cn.com.huiju.platform.basic.util.JPushUtils.PushTarget
	 *            PushTarget}
	 * @param extras 推送额外参数
	 * @param isTag
	 *            是否是Tag方式推送，否则为alias
	 * @param auds
	 *            推送对象的Tag或alias数组
	 * @return
	 */
	public static int pushNotify(String content, String title, PushTarget target, Map<String, String> extras, boolean isTag, String... auds) {
		if (!validate(target)) {
			return 1;
		}
		if(null == extras) extras = new HashMap<String, String>();
		buildAudiences(target, auds);
		int ret = 0;
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(isTag ? Audience.tag(auds) : Audience.alias(auds))
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(IosNotification.newBuilder()
								.setAlert(content).addExtras(extras).setSound(IOS_SOUND).build())
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setAlert(content).setTitle(title).addExtras(extras).build())
						.addPlatformNotification(WinphoneNotification.newBuilder()
								.setAlert(content).setTitle(title).addExtras(extras).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(false).build())
				.build();
		try {
			PushResult result = jpushClient.sendPush(payload);
			log.info(result);
		} catch (APIConnectionException e) {
			log.info(e.getMessage());
			ret = 1;
		} catch (APIRequestException e) {
			showAPIRequestExceptionMessage(e);
			ret = 1;
		}
		return ret;
	}

	private static void buildAudiences(PushTarget target, String... auds) {
		for (int i = 0, l = auds.length; i < l; i++) {
			auds[i] = PREFIXS[target.code] + auds[i];
		}
	}

	public static Map<String, String> buildExtras(String[] keys, String[] vals) {
		Map<String, String> extras = new HashMap<String, String>();
		if (keys == null || vals == null) {
			return extras;
		}
		if (keys.length != vals.length) {
			return extras;
		}
		int i = 0;
		for (String key : keys) {
			extras.put(key, vals[i++]);
		}
		return extras;
	}
	

	private static class Runner implements Runnable {
		private int result = -1;
		private CountDownLatch cdl;
		private int type;
		private String content;
		private PushTarget target;
		private boolean isTag;
		private Map<String, String> extras;
		private String[] auds;
		public Runner(CountDownLatch cdl, int type, String content, PushTarget target, boolean isTag, Map<String, String> extras, String... auds) {
			this.cdl = cdl;
			this.result = -1;
			this.type = type;
			this.content = content;
			this.target = target;
			this.isTag = isTag;
			this.extras = extras;
			this.auds = auds;
		}

		@Override
		public void run() {
			try {
				if(type == 0) { //jpush
					result = jpushMessage(content, target, isTag, extras, auds);
				} else { // gpush
					result = GPushUtils.pushMessage(content, isTag, auds);
				}
			} catch(Exception e) {
				log.info(e.getMessage());
				result = 1;
			}
			cdl.countDown();
			if(result == 0) {
				cdl.countDown();
			}
		}
		
		public int getResult() {
			return result;
		}
	}
}
