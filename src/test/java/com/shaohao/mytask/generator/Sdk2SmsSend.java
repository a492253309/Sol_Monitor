package com.shaohao.mytask.generator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mascloud.model.MoModel;
import com.mascloud.model.StatusReportModel;
import com.mascloud.sdkclient.Client;
import com.mascloud.util.JsonUtil;



public class Sdk2SmsSend {


	private static final String LOGINURL = "http://112.35.4.197:15000";
	private static final String ACCOUNT = "admin";
	private static final String PASSWORD = "pHr6i!Ht";
	private static final String ECNAME = "浙江恒金融资租赁有限公司";
//	static{
//		APIKEY = MyUtils.getProperty("sms.LOGINURL");
//	}

	public static void main( String[] args ) {
		Client client = Client.getInstance( );
//		JsonView
		// 登录地址需另外提供
		boolean isLoggedin = client.login( LOGINURL, ACCOUNT, PASSWORD, ECNAME );
		if( isLoggedin ) {
			System.out.println( "Login successed" );
		} else {
			System.out.println( "Login failed" );
			return;
		}

		// 普通短信
//		int rt = client.sendDSMS( new String[]{ "18743188784" }, "短信内容", "", 1, "4WPHmmfb7", null, true );
//		System.out.println( rt );

		// 模板短信
//		int rtm = client.sendTSMS( new String[]{ "18743188784" }, "3558fa35effd4af1b29dd1f734e12f41", new String[]{ "应付账款审批1", "HJ2023ZH1" }, "", 1, "4WPHmmfb7", null );
//		System.out.println( rtm );

		// 获取状态报告——开始
//		List<StatusReportModel> statusReportlist = client.getReport( );
//		System.out.println( "getReport : " + JsonUtil.toJsonString( statusReportlist ) );
		// 获取状态报告——结束
//
//		// 获取上行短信——开始
//		List<MoModel> deliverList = client.getMO( );
//		System.out.println( "getMO : " + JsonUtil.toJsonString( deliverList ) );
		// 获取上行短信——结束



	}

	public static int sendSms(String number,String tempID,String paramJson) {
		Client client = Client.getInstance( );
		boolean isLoggedin = client.login( LOGINURL, ACCOUNT, PASSWORD, ECNAME );
		if( isLoggedin ) {
			System.out.println( "10086Login successed" );
		} else {
			System.out.println( "10086Login failed" );
			return -2;
		}
		try {
			// 创建 ObjectMapper 对象
			ObjectMapper objectMapper = new ObjectMapper();
			// 解析 JSON 字符串
			JsonNode jsonNode = objectMapper.readTree(paramJson);
			// 获取属性数量
			int propertyCount = jsonNode.size();
//			System.out.println("属性数量：" + propertyCount);
			String[] params = new String[propertyCount];
			// 获取所有属性的迭代器
			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
			// 按序获取属性值
			int i =0;
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> entry = fieldsIterator.next();
				String key = entry.getKey();
				JsonNode value = entry.getValue();
//				System.out.println("属性名：" + key);
//				System.out.println("属性值：" + value.asText());
				params[i] = value.asText();
				i++;
			}
//			System.out.println("Arrays：" + Arrays.toString(params));
			// 模板短信  返回状态码
			return client.sendTSMS( new String[]{ number }, tempID, params, "", 1, "4WPHmmfb7", null );
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}
}
