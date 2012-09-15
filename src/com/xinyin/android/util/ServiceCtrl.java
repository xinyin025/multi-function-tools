package com.xinyin.android.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.xinyin.android.entity.IdentityCard;
import com.xinyin.android.entity.KuaidiEntity;

/**
 * 工具类
 * 
 * @author Xi
 * 
 */
public class ServiceCtrl {

	/**
	 * 手机号码归属地查询
	 * 
	 * @param phone
	 *            手机号
	 * @return 该手机号对应的归属地信息
	 */
	public String queryPhone(String phone) {
		String str = null;
		String httpUrl = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo?userID=&mobileCode="
				+ phone;
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);// 10秒的连接超时
			if (conn.getResponseCode() != 200) {

			}
			InputStream is = conn.getInputStream();
			byte[] data = StreamTool.readInputStream(is);
			is.close();
			str = new String(data);
			str = getXmlKey(str, "string");
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 从xml中获取某个key所对应的value
	 * 
	 * @param xml
	 *            xml字符串
	 * @param key
	 *            要获取的key
	 * @return 返回该可以对应的value字符串
	 */
	public String getXmlKey(String xml, String key) {
		String str = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new ByteArrayInputStream(xml.getBytes()), "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:
					if (key.equals(parser.getName())) {
						str = parser.nextText();
						return str;
					}
					break;
				case XmlPullParser.END_TAG:

					break;
				case XmlPullParser.END_DOCUMENT:

					break;

				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 根据城市名称查询天气
	 * 
	 * @param cityname
	 *            城市名称或者城市id
	 * @return
	 */
	public List<String> queryWeather(String cityname) {
		List<String> list = null;
		String httpUrl = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theUserID=&theCityCode=";
		try {
			String city = URLEncoder.encode(cityname, "UTF-8");
			URL url = new URL(httpUrl + city);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);// 10秒的连接超时
			if (conn.getResponseCode() != 200) {
				return list;
			}
			InputStream is = conn.getInputStream();
			byte[] data = StreamTool.readInputStream(is);
			is.close();
			String str = new String(data);
			conn.disconnect();
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(str.getBytes()),
						"UTF-8");
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {
					switch (event) {
					case XmlPullParser.START_DOCUMENT:
						list = new ArrayList<String>();
						break;
					case XmlPullParser.START_TAG:
						if ("string".equals(parser.getName())) {
							list.add(parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:

						break;
					case XmlPullParser.END_DOCUMENT:
						if ("ArrayOfString".equals(parser.getName())) {
							return list;
						}
						break;

					}
					event = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取身份证对应的信息
	 * 
	 * @param identity
	 *            身份证号
	 * @return 身份证信息对应的json个数数据
	 */
	public IdentityCard queryIdentity(String identity) {
		IdentityCard id = null;
		String httpUrl = "http://www.youdao.com/smartresult-xml/search.s?jsFlag=true&type=id&q="
				+ identity;
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);// 10秒的连接超时
			if (conn.getResponseCode() != 200) {
			}
			InputStream is = conn.getInputStream();
			byte[] data = StreamTool.readInputStream(is);
			is.close();
			String str = new String(data, "gbk");
			String tmp = str.substring(str.indexOf("(") + 1, str.indexOf(","));
			if ("1".equals(tmp)) {
				String json = str.substring(str.indexOf("{"),
						str.lastIndexOf("}") + 1);
				JSONObject object = new JSONObject(json);
				String code = object.getString("code");
				String location = object.getString("location");
				String birthday = object.getString("birthday");
				String gender = object.getString("gender");
				id = new IdentityCard(code, location, birthday, gender);
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 
	 * @param com
	 *            快递公司简称
	 * @param order
	 *            订单号
	 * @return
	 */
	public List<KuaidiEntity> queryKuaidi(String com, String order) {
		List<KuaidiEntity> list = new ArrayList<KuaidiEntity>();
		KuaidiEntity kuaidiEntity = null;
		String httpUrl = "http://api.kuaidi100.com/api?id=130e7edfbf39e455&com="
				+ com + "&nu=" + order + "&valicode=&show=0&muti=1&order=desc";
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);// 10秒的连接超时
			if (conn.getResponseCode() != 200) {
			}
			InputStream is = conn.getInputStream();
			byte[] data = StreamTool.readInputStream(is);
			is.close();
			String json = new String(data);
			JSONObject object = new JSONObject(json);
			if ("1".equals(object.getString("status"))) {
				JSONArray array = object.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					String time = object2.getString("time");
					String context = object2.getString("context");
					kuaidiEntity = new KuaidiEntity(time, context);
					list.add(kuaidiEntity);
				}
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询QQ在线状态
	 * 
	 * @param qq
	 *            要查询的QQ号
	 * @return 返回该QQ在线状态，Y = 在线；N = 离线；E = QQ号码错误
	 */
	public String queryQQonline(String qq) {
		String httpURL = "http://webservice.webxml.com.cn/webservices/qqOnlineWebService.asmx/qqCheckOnline?qqCode="
				+ qq;
		String str = null;
		try {
			URL url = new URL(httpURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);// 10秒的连接超时
			conn.connect();
			InputStream is = conn.getInputStream();
			byte[] data = StreamTool.readInputStream(is);
			is.close();
			String json = new String(data);
			str = getXmlKey(json, "string");
			conn.disconnect();
		} catch (Exception e) {
			str = "N" ;
		}
		return str;
	}
}
