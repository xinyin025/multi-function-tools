package com.xinyin.android.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.xinyin.android.entity.IdentityCard;

public class ServiceCtrl {

	public final static String NAMESPACE = "";
	public final static String NAME = "";

	public String queryPhone(String phone) {
		String str = null;
		return str;
	}

	public String queryPhone2(String phone) {
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
			String str = new String(data,"gbk");
			String tmp = str.substring(str.indexOf("(") + 1, str.indexOf(","));
			if ("1".equals(tmp)) {
				String json = str.substring(str.indexOf("{"),
						str.lastIndexOf("}")+1);
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
}
