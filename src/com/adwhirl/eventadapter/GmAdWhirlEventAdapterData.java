package com.adwhirl.eventadapter;

import java.util.List;

import android.util.Log;

import com.adwhirl.eventadapter.GmAdWhirlEventHandler.GmEventADType;
import com.adwhirl.obj.Extra;
import com.adwhirl.obj.Ration;
import com.adwhirl.util.AdWhirlUtil;

public class GmAdWhirlEventAdapterData {

	// --------------------------------------------------------------------------------
	// ����Ϊ����ѡ��
	// --------------------------------------------------------------------------------
	// �Ƿ�Ϊ������״̬
	public static final boolean CONST_ADDEBUG_ENABLED = false;

	// �Ƿ��ӡ���ӿ�EventAdapter������Ϣ�����ڲ��ҹ��SDK�ӿڵ�����
	public static final boolean CONST_EVENTADAPTERLOG_ENABLED = true;

	// �Ƿ��Զ�����ֻ�LOCALE������������չʾ������͡�
	// ��;���ֻ�����ΪӢ������£���չʾ���Ĺ�棬��չʾӢ�Ĺ�档
	// ע�⣺ʹ�ô˹��ܣ�Ҫ����Ĺ����������������һ�����ǲ�ͬĿ�����Եģ�
	// ������Ϊ�����������Ҫ������һ��Ӣ�Ĺ�棨��֧�ֶ����ԵĹ�棩���磺ADMOB��
	public static final boolean CONST_FITADLANGUAGE_ENABLED = false;

	// �Ƿ�Ϊǿ�ƶ�ȡ����������
	public static final boolean CONST_FORCEFETCHSERVER_ENABLED = false;

	// �Ƿ�ΪĬ�Ϲ�棨����admob��ǿ�Ƶ���ģʽ������������ĳ��Ĭ�����͵Ĺ�棬��ѡ��ǿ��ģʽ��
	// ע�⣺��ѡ������Զ��������ģʽ��
	public static final boolean CONST_DEFAULTADDEBUG_FORCEMODE_ENABLED = false;
	// Ĭ�Ϲ�棨����admob��ǿ�Ƶ���ģʽ����
	public static final int CONST_DEFAULTADDEBUG_FORCEMODE_TYPE = AdWhirlUtil.NETWORK_TYPE_ADMOB;

	// �Ƿ�Ϊ�Զ�����ǿ�Ƶ���ģʽ������������ĳ�����͵Ĺ�棬��ѡ��ǿ��ģʽ��
	public static final boolean CONST_EVENTADDEBUG_FORCEMODE_ENABLED = false;
	// �Ƿ����Զ�����ǿ�Ƶ���ģʽ�»�ȡ���ʧ��ʱ��Ȼ����ѭ��ǿ����ʾ�ù��
	// Ĭ��Ϊֱ���л���һ����̣������ý�����30������³��Ի�ȡ��ǰ��棬
	// ���������CONST_EVENTADDEBUG_FORCEMODE_ENABLEDѡ�����̶��汾������̲���ʹ�á�
	public static final boolean CONST_EVENTADDEBUG_FORCELOOPMODE_ENABLED = false;
	// �Զ�����ǿ�Ƶ���ģʽ����
	public static final GmEventADType CONST_EVENTADDEBUG_FORCEMODE_TYPE1 = GmEventADType.adwo;

	// �Ƿ�Ϊ����Ĭ�ϵ��Զ��������
	public static final boolean CONST_ADDEBUG_USEDEFAULTRATION_ENABLED = true;

	// --------------------------------------------------------------------------------
	// ���¾�Ϊ��Ӧ��ID��ʹ�����뽫�任���Լ��ڸ���վ�������ID��
	// --------------------------------------------------------------------------------
	// Adwhirl
	/*
	 * changed by anffernee
	 */
	public static final String CONST_STR_APPID_ADWHIRL = "5a4af8e8b3e74ef98e0c91e4e8f5c8b5";

	// Admob
	private static final String CONST_STR_APPID_ADMOB = "a14e1bdfa12f0d1";
	// mobfox
	private static final String CONST_STR_APPID_MOBFOX = "c3589a9025e1671b3b1164cf33622a64";
	// tapjoy
	private static final String CONST_STR_APPID_TAPJOY = "bba49f11-b87f-4c0f-9632-21aa810dd6f1";// "deed7c0e-223f-4b49-a858-10edd54c4258"
	private static final String CONST_STR_KEY_TAPJOY = "yiQIURFEeKm0zbOggubu";// "u5D9G7UnDXCdddOhnN9Q"

	// ����
	/*
	 * changed by anffernee
	 */
	private static final String CONST_STR_APPID_DOMOB = "56OJyDZouMIwFJDupT";
	// �ڶ�
	private static final String CONST_STR_APPID_YIDONG = "3e915ac4e83edf08";
	private static final String CONST_STR_BANNERID_YIDONG = "90003888";
	// ����
	/*
	 * changed by anffernee
	 */
	private static final String CONST_STR_APPID_ADWO = "6f35d72763964fdf8ce19f947305d948";
	// �۰�
	private static final String CONST_STR_APPID_WOOBOO = "7352eeaac8854f669f87d3458fd2567f";
	// ΢��
	private static final String CONST_STR_APPID_WIYUN = "492b775415093733";
	private static final String CONST_STR_PWDID_WIYUN = "jfAHEgrgGNwHrkmEGvug3Eby32auguZw";
	private static final String CONST_STR_BANNERID_WIYUN = "b89b05a342381c69";
	// ���ף�Ŀǰ����BUG��
	private static final String CONST_STR_APPID_YOUMI = "8c74fbb755fbdade";
	private static final String CONST_STR_PASSWORD_YOUMI = "18c26c9df856a9b2";
	// VPON
	private static final String CONST_STR_KEY_VPON = "f507039031350e1e013145934bdb024c";
	private static final String CONST_STR_KEY_VPONTW = "ff808081312213590131462263440303";
	// �״�ý
	private static final String CONST_STR_APPID_ADCHINA = "73464";
	// ����
	private static final String CONST_STR_APPID_LMMOB = "e3645094096181c0bcfccc0cb28a289f";
	// AirAD
	private static final String CONST_STR_APPID_AIRAD = "8c8123e9-01da-4a12-87df-da1e53c4c37a";
	// ����
	// ע�⣺��Ϊ���ɵ�APPIDʹ�õ���������ֵ�������ʹ�õ����ַ�
	// ���������ַ���ʽ��д�������ڲ������ʵ�ʱ��ת����ֵ��
	private static final String CONST_STR_APPID_DIPAI = "1050";// "8";
	private static final String CONST_STR_KEY_DIPAI = "u2da5rbl4zxwcx7m";// "8888888888888888";//
	// ����
	private static final String CONST_STR_APPID_ANJI = null;// "S_189_0903162610";
	// AppMedia
	private static final String CONST_STR_APPID_APPMEDIA = "531908c9a2643abd";
	// ָ�㴫ý
	// ע�⣺ָ�㴫ý��APPID��Ҫ�����AndroidManifest.xml��
	@SuppressWarnings("unused")
	private static final String CONST_STR_APPID_ZHIDIAN = "AA6D47747386B5C99ABDE1A023136537";

	// --------------------------------------------------------------------------------
	// �Զ�����ǿ�Ƶ���ģʽ���ú���
	// --------------------------------------------------------------------------------
	private static boolean mForceEventADMode = CONST_EVENTADDEBUG_FORCEMODE_ENABLED;

	public static boolean isForceEventADMode() {
		return mForceEventADMode;
	}

	public static void setForceEventADMode(boolean value) {
		mForceEventADMode = value;
	}

	private static GmEventADType mForceEventADType = CONST_EVENTADDEBUG_FORCEMODE_TYPE1;

	public static GmEventADType getForceEventADType() {
		return mForceEventADType;
	}

	public static void setForceEventADType(GmEventADType value) {
		mForceEventADType = value;
	}

	// --------------------------------------------------------------------------------
	// ���޷����ӹ��ۺ���վʱ��ʹ������Ĭ��ɫ�ʡ��������á�
	// --------------------------------------------------------------------------------
	public static void initDefaultExtra(Extra extra) {
		extra.bgRed = 0;
		extra.bgGreen = 0;
		extra.bgBlue = 0;
		extra.bgAlpha = 1;

		extra.fgRed = 255;
		extra.fgGreen = 255;
		extra.fgBlue = 255;
		extra.fgAlpha = 1;

		extra.cycleTime = 30;
		extra.locationOn = 1;
		extra.transition = 1;
	}

	// --------------------------------------------------------------------------------
	// ���޷����ӹ��ۺ���վʱ��ʹ������Ĭ�Ϲ�������������á�
	// --------------------------------------------------------------------------------
	public static void initDefaultRationList(List<Ration> rationsList) {
		if (CONST_ADDEBUG_USEDEFAULTRATION_ENABLED) {
			Ration ration = null;

			rationsList.clear();

			/*
			// --------------------------------------------------------------------------------
			// ADMOB DATA
			// --------------------------------------------------------------------------------
			ration = new Ration();
			// NIDΪ�ۺ���վΪÿ����ͬ�Ĺ�������������չʾ�͵�������ID
			// ���������Ӧ��������LOGCAT������Ϣ�￴����ֵ������д�ڴ˼��ɣ�
			// ����Ҳû�й�ϵ��ֻ��û��չʾ�����������ѣ�����Ҳ����ô���ף���
			ration.nid = "7fd107d4ca0144e6ace0a9cb1314cfea";
			ration.type = AdWhirlUtil.NETWORK_TYPE_ADMOB;
			ration.name = "admob";
			ration.weight = 50;
			ration.priority = 1;
			// admob key
			ration.key = CONST_STR_APPID_ADMOB;
			rationsList.add(ration);

			// --------------------------------------------------------------------------------
			// DOMOB DATA
			// --------------------------------------------------------------------------------
			ration = new Ration();
			ration.nid = "23c170d68c1f49c3b4922de030f4baf6";
			ration.type = AdWhirlUtil.NETWORK_TYPE_EVENT;
			ration.name = "event";
			ration.weight = 50;
			ration.priority = 2;
			// ��ֵ���������ھۺ���վ����д��һ�£�ע�⣺�������뺯�����ԡ�|;|���ָ
			ration.key = "cn_domob|;|adWhirlEventInterstitial_domob";
			rationsList.add(ration);
			*/
			
			ration = new Ration();
			ration.nid = "23c170d68c1f49c3b4922de030f4baf6";
			ration.type = AdWhirlUtil.NETWORK_TYPE_EVENT;
			ration.name = "event";
			ration.weight = 50;
			ration.priority = 1;
			ration.key = "cn_domob|;|adWhirlEventInterstitial_domob";
			rationsList.add(ration);
			
			ration = new Ration();
			ration.nid = "23c170d68c1f49c3b4922de030f4baf6";
			ration.type = AdWhirlUtil.NETWORK_TYPE_EVENT;
			ration.name = "event";
			ration.weight = 50;
			ration.priority = 2;
			ration.key = "cn_adwo|;|adWhirlEventInterstitial_adwo";
			rationsList.add(ration);
		}
	}

	// --------------------------------------------------------------------------------
	// Ĭ�Ϲ��ǿ�Ƶ���ģʽ���?��
	// --------------------------------------------------------------------------------
	public static void debugDefaultAdapter(Ration ration) {
		if (CONST_DEFAULTADDEBUG_FORCEMODE_ENABLED) {
			switch (CONST_DEFAULTADDEBUG_FORCEMODE_TYPE) {
			case AdWhirlUtil.NETWORK_TYPE_ADMOB:
				ration.type = AdWhirlUtil.NETWORK_TYPE_ADMOB;
				ration.name = "admob";
				ration.key = CONST_STR_APPID_ADMOB;
				break;
			}
		}
	}

	// --------------------------------------------------------------------------------
	// �Զ�����ǿ�Ƶ���ģʽ���?��
	// --------------------------------------------------------------------------------
	public static void debugCustomEventAdapter(Ration ration) {
		if (mForceEventADMode) {
			Log.i(AdWhirlUtil.ADWHIRL,
					"!!!!!!!!AD_DEBUG_FORCE_MODE_ACTIVE!!!!!!!!");

			ration.type = AdWhirlUtil.NETWORK_TYPE_EVENT;
			ration.name = "event";
			// ��ֵ���������ھۺ���վ����д��һ�£�ע�⣺�������뺯�����ԡ�|;|���ָ
			if (mForceEventADType == GmEventADType.tapjoy) {
				ration.key = "tapjoy|;|adWhirlEventInterstitial_tapjoy";
			} else if (mForceEventADType == GmEventADType.mobfox) {
				ration.key = "cn_mobfox|;|adWhirlEventInterstitial_mobfox";
			} else if (mForceEventADType == GmEventADType.smartmad) {
				ration.key = "cn_smartmad|;|adWhirlEventInterstitial_smartmad";
			} else if (mForceEventADType == GmEventADType.adwo) {
				ration.key = "cn_adwo|;|adWhirlEventInterstitial_adwo";
			} else if (mForceEventADType == GmEventADType.domob) {
				ration.key = "cn_domob|;|adWhirlEventInterstitial_domob";
			} else if (mForceEventADType == GmEventADType.wooboo) {
				ration.key = "cn_wooboo|;|adWhirlEventInterstitial_wooboo";
			} else if (mForceEventADType == GmEventADType.wiyun) {
				ration.key = "cn_wiyun|;|adWhirlEventInterstitial_wiyun";
			} else if (mForceEventADType == GmEventADType.youmi) {
				ration.key = "cn_youmi|;|adWhirlEventInterstitial_youmi";
			} else if (mForceEventADType == GmEventADType.vpon) {
				ration.key = "cn_vpon|;|adWhirlEventInterstitial_vpon";
			} else if (mForceEventADType == GmEventADType.vpontw) {
				ration.key = "cn_vpontw|;|adWhirlEventInterstitial_vpontw";
			} else if (mForceEventADType == GmEventADType.dipai) {
				ration.key = "cn_dipai|;|adWhirlEventInterstitial_dipai";
			} else if (mForceEventADType == GmEventADType.lmmob) {
				ration.key = "cn_lmmob|;|adWhirlEventInterstitial_lmmob";
			} else if (mForceEventADType == GmEventADType.baidu) {
				ration.key = "cn_baidu|;|adWhirlEventInterstitial_baidu";
			} else if (mForceEventADType == GmEventADType.waps) {
				ration.key = "cn_waps|;|adWhirlEventInterstitial_waps";
			} else if (mForceEventADType == GmEventADType.anji) {
				ration.key = "cn_anji|;|adWhirlEventInterstitial_anji";
			} else if (mForceEventADType == GmEventADType.adchina) {
				ration.key = "cn_adchina|;|adWhirlEventInterstitial_adchina";
			} else if (mForceEventADType == GmEventADType.airad) {
				ration.key = "cn_airad|;|adWhirlEventInterstitial_airad";
			} else if (mForceEventADType == GmEventADType.mobwin) {
				ration.key = "cn_mobwin|;|adWhirlEventInterstitial_mobwin";
			} else if (mForceEventADType == GmEventADType.appmedia) {
				ration.key = "cn_appmedia|;|adWhirlEventInterstitial_appmedia";
			} else if (mForceEventADType == GmEventADType.zhidian) {
				ration.key = "cn_zhidian|;|adWhirlEventInterstitial_zhidian";
			}
		}
	}

	// --------------------------------------------------------------------------------
	// �����Ų�����ۺ���վ��ص�Ĭ�Ϲ���̣���ADMOB����IDʱ���˴����Խ�����֤������
	// --------------------------------------------------------------------------------
	public static void validateRation(Ration ration) {
		switch (ration.type) {
		case AdWhirlUtil.NETWORK_TYPE_ADMOB:
			ration.key = CONST_STR_APPID_ADMOB;
			break;
		}
	}

	// --------------------------------------------------------------------------------
	// ��ȡ��������̵�APPID
	// --------------------------------------------------------------------------------
	public static String getPublishID(GmEventADType type) {
		String ret = "";

		switch (type) {
		case mobfox:
			ret = CONST_STR_APPID_MOBFOX;
			break;
		case tapjoy:
			ret = CONST_STR_APPID_TAPJOY;
			break;
		case domob:
			ret = CONST_STR_APPID_DOMOB;
			break;
		case adchina:
			ret = CONST_STR_APPID_ADCHINA;
			break;
		case adwo:
			ret = CONST_STR_APPID_ADWO;
			break;
		case airad:
			ret = CONST_STR_APPID_AIRAD;
			break;
		case appmedia:
			ret = CONST_STR_APPID_APPMEDIA;
			break;
		case smartmad:
			ret = CONST_STR_APPID_YIDONG;
			break;
		case wooboo:
			ret = CONST_STR_APPID_WOOBOO;
			break;
		case wiyun:
			ret = CONST_STR_APPID_WIYUN;
			break;
		case youmi:
			ret = CONST_STR_APPID_YOUMI;
			break;
		case vpon:
			ret = CONST_STR_KEY_VPON;
			break;
		case vpontw:
			ret = CONST_STR_KEY_VPONTW;
			break;
		case dipai:
			ret = CONST_STR_APPID_DIPAI;
			break;
		case lmmob:
			ret = CONST_STR_APPID_LMMOB;
			break;
		case anji:
			ret = CONST_STR_APPID_ANJI;
			break;
		}
		return ret;
	}

	// --------------------------------------------------------------------------------
	// ��ȡ��������̵�PWDID
	// --------------------------------------------------------------------------------
	public static String getDefaultPwdID(GmEventADType type) {
		String ret = "";

		switch (type) {
		case tapjoy:
			ret = CONST_STR_KEY_TAPJOY;
			break;
		case smartmad:
			ret = CONST_STR_BANNERID_YIDONG;
			break;
		case youmi:
			ret = CONST_STR_PASSWORD_YOUMI;
			break;
		case dipai:
			ret = CONST_STR_KEY_DIPAI;
			break;
		case wiyun:
			ret = CONST_STR_PWDID_WIYUN;
			break;
		}
		return ret;
	}

	// --------------------------------------------------------------------------------
	// ��ȡ��������̵�BANNER ID
	// --------------------------------------------------------------------------------
	public static String getDefaultBannerID(GmEventADType type) {
		String ret = "";

		switch (type) {
		case wiyun:
			ret = CONST_STR_BANNERID_WIYUN;
			break;
		}
		return ret;
	}

	// --------------------------------------------------------------------------------
	// �����Ƿ�Ϊ����ģʽ����������CONST_ADDEBUG_ENABLED���˴������ģ�
	// --------------------------------------------------------------------------------
	public static boolean getDebugEnabled(GmEventADType type) {
		return CONST_ADDEBUG_ENABLED;
	}
}
