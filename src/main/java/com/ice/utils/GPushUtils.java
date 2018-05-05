package com.ice.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.base.Strings;


/**
 * 个推推送工具类<br>
 * 
 * @author xuyc
 * @date Jan 26, 2016
 */
public abstract class GPushUtils {

	/** Log4j Logger */
	private static final Logger log = Logger.getLogger(GPushUtils.class);

	// 首先定义一些常量, 修改成开发者平台获得的值
	// private static String appId = "VtfL8IIcl0743IdPrRjyB7";
	// private static String appKey = "bDsBly2Yrj85QvWYKFsoy1";
	// private static String appSecret = "h8FB5OSAzH8Ce9z6B2ziO1";
	// private static String masterSecret = "41zGVeQIyk9lei6ml5zYt";

	
	
 
	@Value("")
	private static String appId;
	@Value("")
	private static String appKey;
	@Value("")
	private static String appSecret;
	@Value("")
	private static String masterSecret;

	/** 离线消息保存时间 */
	private final static long OFFLINE_EXPIRE_TIME = 24 * 3600 * 1000;
	/** 消息推送方式：wifi推送 */
	private final static int PUSH_NET_TYPE_WIFI = 1;
	/** 消息推送方式：不限方式 */
	private final static int PUSH_NET_TYPE_OTHER = 0;
	/** 消息设置：立即启动 */
	private final static int TRANSMISSION_TYPE_NOW = 1;
	/** 消息设置：广播等待客户端自启动 */
	private final static int TRANSMISSION_TYPE_WAIT = 2;

	
	
	
	
	
	
	
	// 个推推送对象
	private final static IGtPush push = new IGtPush(appKey, masterSecret);

	public static void main(String[] args) throws IOException {
		Map<String, Object> content = new HashMap<String, Object>();

		content.put("id", new Date().getTime());
		content.put("content", "Tag方式推送消息");

		String[] tags = { "D_18", "D_SH_ENTRANCE_DEV" };

		pushMessage(FastJSONUtil.object2json(content), true, tags);
	}

	/**
	 * 个推推送消息<br>
	 * 
	 * @param content
	 *            推送内容
	 * @param isTag
	 *            是否为tag推送，否则为alias方式
	 * @param auds
	 *            推送对象数组
	 * @return (0:推送成功1:推送失败)
	 */
	public static int pushMessage(String content, boolean isTag, String... auds) {
		// 推送结果
		int ret = 0;
		List<String> list = Arrays.asList(auds);
		List<String> appIds = new ArrayList<String>();
		appIds.add(appId);
		// 判断是否为Tag方式推送
		if (isTag) { // Tag推送
			ret = pushMessage(content, appIds, list) ? 0 : 1;
		} else { // 别名推送
			ret = pushMessage(content, list) ? 0 : 1;
		}
		return ret;
	}

	/**
	 * 单个用户推送透传消息<br>
	 * 
	 * <pre>
	 * 场景：通知APP触发，调用服务器接口，获取数据。
	 * </pre>
	 * 
	 * @param alias
	 *            用户别名
	 * @param content
	 *            推送内容
	 * @return true/false
	 */
	public static boolean pushMessage(String alias, String content) {
		// IGtPush push = new IGtPush(appKey, masterSecret);
		// 获取透传消息模板
		TransmissionTemplate template = getTransmissionTemplate(content);
		// 获取单个消息对象
		SingleMessage message = getSingleMessage(template, false, PUSH_NET_TYPE_OTHER);
		// 消息推送对象
		Target target = new Target();
		target.setAppId(appId);
		// target.setClientId(CID);
		// 用户别名推送，CID和用户别名只能2者选其一
		target.setAlias(alias);
		// 定义推送结果
		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			// 推送异常，从新推送
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
			log.info("GPush result ::::" + ret.getResponse().toString());
			if ("ok".equals(ret.getResponse().get("result"))) {
				return true;
			} else {
				return false;
			}
		} else {
			log.error("服务器响应异常");
			return false;
		}
	}

	/**
	 * 指定列表用户推送透传消息<br>
	 * 
	 * @param content
	 *            推送内容
	 * @param list
	 *            推送别名列表
	 * @return true/false
	 */
	public static boolean pushMessage(String content, List<String> list) {
		// IGtPush push = new IGtPush(appKey, masterSecret);
		// 获取通知消息模板
		TransmissionTemplate template = getTransmissionTemplate(content);
		// 获取列表消息对象
		ListMessage message = getListMessage(template, false, PUSH_NET_TYPE_OTHER);
		// 配置推送目标
		List<Target> targets = new ArrayList<Target>();
		Target target = null;
		for (String alias : list) {
			target = new Target();
			target.setAppId(appId);
			// 用户别名推送，CID和用户别名2者只能选其一
			// target.setClientId(CID);
			// 设置推送用户别名
			target.setAlias(alias);
			targets.add(target);
		}

		// 获取taskID
		String taskId = push.getContentId(message);
		// 使用taskID对目标进行推送
		IPushResult ret = push.pushMessageToList(taskId, targets);
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
			log.info("GPush result ::::" + ret.getResponse().toString());
			if ("ok".equals(ret.getResponse().get("result"))) {
				return true;
			} else {
				return false;
			}
		} else {
			log.error("服务器响应异常");
			return false;
		}
	}

	/**
	 * 指定应用群推透传消息<br>
	 * 
	 * @param content
	 *            推送内容
	 * @param appIds
	 *            应用ID列表
	 * @param tags
	 *            标签列表
	 * @return true/false
	 */
	public static boolean pushMessage(String content, List<String> appIds, List<String> tags) {
		// IGtPush push = new IGtPush(appKey, masterSecret);
		// 获取通知消息模板
		TransmissionTemplate template = getTransmissionTemplate(content);
		// 获取列表消息对象
		AppMessage message = getAppMessage(template, appIds, tags, false, PUSH_NET_TYPE_OTHER);
		// 使用taskID对目标进行推送
		IPushResult ret = push.pushMessageToApp(message);
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
			log.info("GPush result ::::" + ret.getResponse().toString());
			if ("ok".equals(ret.getResponse().get("result"))) {
				return true;
			} else {
				return false;
			}
		} else {
			log.error("服务器响应异常");
			return false;
		}
	}

	/**
	 * 设置客户端用户的Tag<br>
	 * 
	 * @param cid
	 *            目标用户(clientId)
	 * @param tags
	 *            用户tag列表
	 * @return true/false
	 */
	public static boolean setClientTag(String cid, List<String> tags) {
		// IGtPush push = new IGtPush(appKey, masterSecret);
		IQueryResult result = push.setClientTag(appId, cid, tags);
		if (result != null) {
			log.info("GPush Set Client Tag ::::" + result.getResponse().toString());
			if ("Success".equals(result.getResponse().get("result"))) {
				return true;
			} else {
				return false;
			}
		} else {
			log.error("服务器响应异常");
			return false;
		}
	}

	/**
	 * 根据目标用户（clientId）获取别名<br>
	 * 
	 * @param cid
	 *            目标用户(clientId)
	 * @return alias 别名
	 */
	public static String queryAlias(String cid) {
		IAliasResult result = push.queryAlias(appId, cid);
		return result.getAlias();
	}

	/**
	 * 目标用户（clientId）和别名解绑<br>
	 * 
	 * <pre>
	 * (1). cid 为空时，绑定别名的所有ClientID解绑；
	 * (2). cid 不为空时，单个ClientID和别名解绑。
	 * </pre>
	 * 
	 * @param alias
	 *            别名
	 * @param cid
	 *            目标用户(clientId)
	 * @return boolean 成功或失败
	 */
	public static boolean unbindAlias(String alias, String cid) {

		IAliasResult result = null;
		if (Strings.isNullOrEmpty(cid)) {
			result = push.unBindAliasAll(appId, alias);
		} else {
			result = push.unBindAlias(appId, alias, cid);
		}
		return result.getResult();
	}

	/**
	 * 单个用户推送通知应用消息<br>
	 * 
	 * <pre>
	 * 场景：发送消息到APP端，通知栏显示消息信息，点击打开应用程序APP。
	 * </pre>
	 * 
	 * @param alias
	 *            用户别名
	 * @param title
	 *            通知标题
	 * @param content
	 *            通知内容
	 * @param extras
	 *            额外信息
	 * @return true/false
	 */
	public static boolean pushNotify(String alias, String title, String content, String extras) {
		// IGtPush push = new IGtPush(appKey, masterSecret);
		// 获取通知应用消息模板
		NotificationTemplate template = getNotificationTemplate(title, content, extras);
		// 获取单个消息对象
		SingleMessage message = getSingleMessage(template, true, PUSH_NET_TYPE_OTHER);
		// 消息推送对象
		Target target = new Target();
		target.setAppId(appId);
		// target.setClientId(CID);
		// 用户别名推送，CID和用户别名只能2者选其一
		target.setAlias(alias);
		// 定义推送结果
		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			// 推送异常，从新推送
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
			log.info("GPush result ::::" + ret.getResponse().toString());
			if ("ok".equals(ret.getResponse().get("result"))) {
				return true;
			} else {
				return false;
			}
		} else {
			log.error("服务器响应异常");
			return false;
		}
	}

	/**
	 * 指定列表用户推送通知应用消息<br>
	 * 
	 * @param list
	 *            推送别名列表
	 * @param title
	 *            通知标题
	 * @param content
	 *            通知内容
	 * @param extras
	 *            额外信息
	 * @return true/false
	 */
	public static boolean pushNotify(List<String> list, String title, String content, String extras) {
		// IGtPush push = new IGtPush(appKey, masterSecret);
		// 获取通知消息模板
		NotificationTemplate template = getNotificationTemplate(title, content, extras);
		// 获取列表消息对象
		ListMessage message = getListMessage(template, true, PUSH_NET_TYPE_OTHER);
		// 配置推送目标
		List<Target> targets = new ArrayList<Target>();
		Target target = null;
		for (String alias : list) {
			target = new Target();
			target.setAppId(appId);
			// 用户别名推送，CID和用户别名2者只能选其一
			// target.setClientId(CID);
			// 设置推送用户别名
			target.setAlias(alias);
			targets.add(target);
		}

		// 获取taskID
		String taskId = push.getContentId(message);
		// 使用taskID对目标进行推送
		IPushResult ret = push.pushMessageToList(taskId, targets);
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
			log.info("GPush result ::::" + ret.getResponse().toString());
			if ("ok".equals(ret.getResponse().get("result"))) {
				return true;
			} else {
				return false;
			}
		} else {
			log.error("服务器响应异常");
			return false;
		}
	}

	/**
	 * 指定应用群推通知应用消息<br>
	 * 
	 * @param title
	 *            通知标题
	 * @param content
	 *            推送内容
	 * @param extras
	 *            额外信息
	 * @param appIds
	 *            应用ID列表
	 * @param tags
	 *            标签列表
	 * @return true/false
	 */
	public static boolean pushNotify(String title, String content, String extras, List<String> appIds,
			List<String> tags) {
		// IGtPush push = new IGtPush(appKey, masterSecret);
		// 获取通知消息模板
		NotificationTemplate template = getNotificationTemplate(title, content, extras);
		// 获取列表消息对象
		AppMessage message = getAppMessage(template, appIds, tags, true, PUSH_NET_TYPE_OTHER);
		// 使用taskID对目标进行推送
		IPushResult ret = push.pushMessageToApp(message);
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
			log.info("GPush result ::::" + ret.getResponse().toString());
			if ("ok".equals(ret.getResponse().get("result"))) {
				return true;
			} else {
				return false;
			}
		} else {
			log.error("服务器响应异常");
			return false;
		}
	}

	/**
	 * 获取链接模板对象<br>
	 * 
	 * @param title
	 *            通知栏标题
	 * @param content
	 *            通知栏内容
	 * @param linkUrl
	 *            链接网址
	 * @return {@link com.gexin.rp.sdk.template.LinkTemplate}
	 */
	private static LinkTemplate getLinkTemplate(String title, String content, String linkUrl) {
		LinkTemplate template = new LinkTemplate();
		// 设置APPID（设定接收的应用）与APPKEY（用于鉴定身份是否合法）
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 设置定时展示时间
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		// 设置通知栏标题与内容
		template.setTitle(title);
		template.setText(content);
		// 配置通知栏图标（需要在客户端开发时嵌入）
		template.setLogo("icon.png");
		// 配置通知栏网络图标，填写图标URL地址
		template.setLogoUrl("http://cdn-img.easyicon.net/png/5507/550739.gif");
		// 设置通知是否响铃，震动，或者可清除
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// 设置打开的网址地址
		template.setUrl(linkUrl);
		// IOS推送使用该字段 TODO
		// template.setAPNInfo(apn);
		return template;
	}

	/**
	 * 获取通知打开应用模板<br>
	 * 
	 * @param title
	 *            通知栏标题
	 * @param content
	 *            通知栏内容
	 * @param extras
	 *            额外透传内容
	 * @return {@link com.gexin.rp.sdk.template.NotificationTemplate}
	 */
	private static NotificationTemplate getNotificationTemplate(String title, String content, String extras) {
		NotificationTemplate template = new NotificationTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 设置通知栏标题与内容
		template.setTitle(title);
		template.setText(content);
		// 配置通知栏图标
		template.setLogo("icon.png");
		// 配置通知栏网络图标
		// template.setLogoUrl("http://cdn-img.easyicon.net/png/5507/550739.gif");
		// 设置通知是否响铃，震动，或者可清除
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// 收到消息是否立即启动应用：1为立即启动，2则广播等待客户端自启动
		template.setTransmissionType(TRANSMISSION_TYPE_WAIT);
		// 透传内容，不支持转义字符
		template.setTransmissionContent(extras);
		// 设置定时展示时间
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		// IOS推送使用该字段 TODO
		// template.setAPNInfo(apn);
		return template;
	}

	/**
	 * 获取透传模板<br>
	 * 
	 * @param content
	 *            透传内容
	 * @return {@link com.gexin.rp.sdk.template.TransmissionTemplate}
	 */
	private static TransmissionTemplate getTransmissionTemplate(String content) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(TRANSMISSION_TYPE_WAIT);
		// 透传内容，不支持转义字符
		template.setTransmissionContent(content);
		// 设置定时展示时间
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		// IOS推送使用该字段 TODO
		APNPayload payload = new APNPayload();
		payload.setBadge(1); // 应用icon上显示的数字
		payload.setContentAvailable(1); // 推送直接带有透传数据
		payload.setSound("default"); // 通知铃声文件名
		payload.setCategory("$由客户端定义"); // 在客户端通知栏触发特定的action和button显示
		payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello")); // 增加自定义的数据
		// 字典模式使用下者
		// payload.setAlertMsg(getDictionaryAlertMsg());
		// template.setAPNInfo(payload);
		return template;
	}

	private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg() {
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody("body"); // 通知文本消息字符串
		alertMsg.setActionLocKey("ActionLockey"); // (用于多语言支持）指定执行按钮所使用的Localizable.strings
		alertMsg.setLocKey("LocKey"); // (用于多语言支持）指定Localizable.strings文件中相应的key
		alertMsg.addLocArg("loc-args"); // 如果loc-key中使用的占位符，则在loc-args中指定各参数
		alertMsg.setLaunchImage("launch-image"); // 指定启动界面图片名
		// IOS8.2以上版本支持
		alertMsg.setTitle("Title"); // 通知标题
		alertMsg.setTitleLocKey("TitleLocKey"); // (用于多语言支持）对于标题指定执行按钮所使用的Localizable.strings
		alertMsg.addTitleLocArg("TitleLocArg"); // 对于标题，如果loc-key中使用的占位符，则在loc-args中指定各参数
		return alertMsg;
	}

	/**
	 * 获取通知栏弹框下载模板<br>
	 * 
	 * @param title
	 *            通知栏标题
	 * @param content
	 *            通知栏内容
	 * @param popTitle
	 *            弹框标题
	 * @param popContent
	 *            弹框内容
	 * @param loadTitle
	 *            下载标题
	 * @param loadUrl
	 *            下载地址
	 * @param autoInstall
	 *            是否自动安装
	 * @param actived
	 *            是否自启动
	 * @return {@link com.gexin.rp.sdk.template.NotyPopLoadTemplate}
	 */
	private static NotyPopLoadTemplate getNotyPopLoadTemplate(String title, String content, String popTitle,
			String popContent, String loadTitle, String loadUrl, boolean autoInstall, boolean actived) {
		NotyPopLoadTemplate template = new NotyPopLoadTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 设置通知栏标题与内容
		template.setNotyTitle(title);
		template.setNotyContent(content);
		// 配置通知栏图标
		template.setNotyIcon("icon.png");
		// 设置通知是否响铃，震动，或者可清除
		template.setBelled(true);
		template.setVibrationed(true);
		template.setCleared(true);

		// 设置弹框标题与内容
		template.setPopTitle(popTitle);
		template.setPopContent(popContent);
		// 设置弹框显示的图片
		template.setPopImage("http://pic14.nipic.com/20110522/7411759_164157418126_2.jpg");
		// 弹出框左边按钮名称
		template.setPopButton1("下载");
		// 弹出框右边按钮名称
		template.setPopButton2("取消");

		// 设置下载标题
		template.setLoadTitle(loadTitle);
		// 设置下载图标
		template.setLoadIcon("file://icon.png");
		// 设置下载地址
		template.setLoadUrl(loadUrl);
		// 是否自动安装(默认false)
		template.setAutoInstall(autoInstall);
		// 安装完成后是否自动启动应用程序(默认false)
		template.setActived(actived);

		// 设置定时展示时间
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		// IOS推送使用该字段 TODO
		// template.setAPNInfo(apn);
		return template;
	}

	/**
	 * 获取单个消息对象<br>
	 * 
	 * @param template
	 *            消息模板对象
	 * @param offline
	 *            是否离线推送
	 * @param netType
	 *            推送方式（1：wifi推送;0:不限方式）
	 * @return SingleMessage
	 */
	private static SingleMessage getSingleMessage(ITemplate template, boolean offline, int netType) {

		// 单个消息对象
		SingleMessage message = new SingleMessage();
		// 推送消息消息内容
		message.setData(template);
		// 消息离线是否存储
		message.setOffline(offline);
		if (offline) {
			// 消息离线存储多久，单位为毫秒(ms)
			message.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);
		}
		// 消息优先级
		// message.setPriority(0);
		// 是否wifi推送，1：wifi推送；0：不限制推送方式
		message.setPushNetWorkType(netType);
		return message;
	}

	/**
	 * 获取列表消息对象<br>
	 * 
	 * @param template
	 *            消息模板对象
	 * @param offline
	 *            是否离线推送
	 * @param netType
	 *            推送方式（1：wifi推送;0:不限方式）
	 * @return ListMessage
	 */
	private static ListMessage getListMessage(ITemplate template, boolean offline, int netType) {
		// 列表消息对象
		ListMessage message = new ListMessage();
		// 推送消息消息内容
		message.setData(template);
		// 消息离线是否存储
		message.setOffline(offline);
		if (offline) {
			// 消息离线存储多久，单位为毫秒(ms)
			message.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);
		}
		// 消息优先级
		// message.setPriority(0);
		// 是否wifi推送，1：wifi推送；0：不限制推送方式
		message.setPushNetWorkType(netType);
		return message;
	}

	/**
	 * 获取APP消息对象<br>
	 * 
	 * @param template
	 *            模板对象
	 * @param appIds
	 *            APPID列表
	 * @param tags
	 *            标签列表
	 * @param offline
	 *            是否离线推送
	 * @param netType
	 *            推送方式（1：wifi推送;0:不限方式）
	 * @return AppMessage
	 */
	@SuppressWarnings("deprecation")
	private static AppMessage getAppMessage(ITemplate template, List<String> appIds, List<String> tags, boolean offline, int netType) {
		// 列表消息对象
		AppMessage message = new AppMessage();
		// 推送消息消息内容
		message.setData(template);
		// 消息离线是否存储
		message.setOffline(offline);
		if (offline) {
			// 消息离线存储多久，单位为毫秒(ms)
			message.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);
		}
		// 消息优先级
		// message.setPriority(0);
		// 推送速度
		// message.setSpeed(100);
		// 是否wifi推送，1：wifi推送；0：不限制推送方式
		message.setPushNetWorkType(netType);

		// 设置推送目标条件过滤
		if (appIds != null && !appIds.isEmpty()) {
			message.setAppIdList(appIds);
		}
		if (tags != null && !tags.isEmpty()) {
			message.setTagList(tags);
		}
		return message;
	}
}
