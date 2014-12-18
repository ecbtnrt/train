package tony.train.worker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;

import tony.train.HttpHeader;
import tony.train.Ticket;
import tony.train.Maps.Station;
import tony.train.handler.CheckCodeHandler;
import tony.train.handler.PrintResponseHandler;
import tony.train.handler.SubmitOrderRequest;
import tony.train.handler.VerifyCheckCodeHandler;
import tony.train.json.TicketDTO;
import tony.train.json.TicketInfoForPassengerForm;
import tony.train.json.order.CheckOrderResponse;
import tony.train.utils.HttpUtil;
import tony.train.utils.PropertiesUtil;

import com.google.common.collect.Maps;

public class WorkThread implements Runnable {

	CloseableHttpClient httpclient;

	TicketDTO ticketDTO;

	SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat parse = new SimpleDateFormat("yyyyMMdd");

	@Override
	public void run() {

		// submit order
		String submitOrderUrl = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";

		LinkedHashMap<String, String> orderParam = Maps.newLinkedHashMap();

		try {
			// orderParam.put(key, Base32.calculateParam(value, key));
			orderParam.put("myversion", "undefined");
			orderParam.put("secretStr", ticketDTO.getSecretStr());
			orderParam.put("train_date", smf.format(parse.parse(ticketDTO.getQueryLeftNewDTO().getStart_train_date())));
			orderParam.put("back_train_date", "2014-12-15");
			orderParam.put("tour_flag", "dc");
			orderParam.put("purpose_codes", "ADULT");
			orderParam.put("query_from_station_name", Station.getName(PropertiesUtil.getValue("start_station")));
			orderParam.put("query_to_station_name", Station.getName(PropertiesUtil.getValue("to_station")));
			boolean isSubmitSucc = HttpUtil.doPostWithParam(httpclient, submitOrderUrl, HttpHeader.commonHeader(), orderParam,
					new SubmitOrderRequest());

			String initDcUrl = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
			// post
			LinkedHashMap<String, String> initDcParam = Maps.newLinkedHashMap();
			initDcParam.put("_json_att", "");
			String initDcContent = HttpUtil.doPostWithParam(httpclient, initDcUrl, HttpHeader.commonHeader(), initDcParam,
					new PrintResponseHandler());

			String tokenRegex= "var(\\s*)globalRepeatSubmitToken(\\s*)=(\\s*)'(\\w+)';";
			String repeatToken = extractContent(initDcContent, tokenRegex);
			
			if (!StringUtils.isBlank(repeatToken)) {
				throw new Exception("get token failed!");
			}
			// get ticketInfoForPassengerForm
			String formRegex = "var(\\s*)ticketInfoForPassengerForm(\\s*)=(\\s*)'(\\w+)';";
			String passengerForm = extractContent(initDcContent, formRegex);
			TicketInfoForPassengerForm form = Ticket.mapper.readValue(passengerForm, TicketInfoForPassengerForm.class);
			
			String purpose_codes = form.getPurpose_codes();
			String key_check_isChange = form.getKey_check_isChange();
			String leftTicketStr = form.getLeftTicketStr(); 
			String train_location = form.getTrain_location();
			
			boolean verifyRet = false;
			String submitCode; 
			do {
				String submitCheckCodeUrl = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&";
				submitCode = HttpUtil.goGet(httpclient, submitCheckCodeUrl, HttpHeader.commonHeader(), new CheckCodeHandler());

				String verifyUrl = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";

				LinkedHashMap<String, String> param = Maps.newLinkedHashMap();
				param.put("randCode", submitCode);
				param.put("rand", "randp");
				param.put("_json_att", "");

				param.put("REPEAT_SUBMIT_TOKEN", repeatToken);

				verifyRet = HttpUtil.doPostWithParam(httpclient, verifyUrl, HttpHeader.commonHeader(), param, new VerifyCheckCodeHandler());
				
				if(verifyRet) {
					break;
				} 
				Thread.sleep(500);
				
			} while (!verifyRet);
			
			boolean submitStatus = false;
			int count =3;
			 do {
				 String checkOrderInfo = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
					
					LinkedHashMap<String, String> param = Maps.newLinkedHashMap();
					param.put("cancel_flag", "2");
					param.put("bed_level_order_num", "000000000000000000000000000000");
					// TODO 
					// 座位类型，0，车票类型，姓名，身份正号，电话，N（多个的话，以逗号分隔）
					// 3,0,1,崔海龙,1,420881198708053738,13524165011,N
					// 3,0,1,高云,1,610502199111180222,18792558138,N
					param.put("passengerTicketStr", passengerTicketStr(PropertiesUtil.getValue("name")));
					// 姓名，证件类别，证件号码，用户类型
					// 崔海龙,1,420881198708053738,1_
					param.put("oldPassengerStr", oldPassengerStr(PropertiesUtil.getValue("name")));
					param.put("tour_flag", "dc");
					param.put("randCode", submitCode);
					param.put("_json_att", "");
					param.put("REPEAT_SUBMIT_TOKEN", repeatToken);
					
					String checkOrderResponse = HttpUtil.doPostWithParam(httpclient, checkOrderInfo, HttpHeader.commonHeader(), param, new PrintResponseHandler());
					// {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"submitStatus":true},"messages":[],"validateMessages":{}}
					CheckOrderResponse checkOrderBean = Ticket.mapper.readValue(checkOrderResponse, CheckOrderResponse.class);
					submitStatus = checkOrderBean.getData().isSubmitStatus();
					if(!submitStatus) {
						System.err.println("提交失败!");
					} else {
						System.out.println("check order success!");
					}
			 } while (!submitStatus && count++ < 3);
			
			// TODO json format
			
//			String getQCount = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
//			LinkedHashMap<String, String> getQParam = Maps.newLinkedHashMap();
//			// TODO 
//			getQParam.put("train_date", "");		
//			getQParam.put("train_no", "");		
//			getQParam.put("stationTrainCode", "");		
//			getQParam.put("seatType", "");		
//			getQParam.put("fromStationTelecode", "");		
//			getQParam.put("toStationTelecode", "");		
//			getQParam.put("leftTicket", leftTicketStr);		
//			getQParam.put("purpose_codes", purpose_codes);		
//			getQParam.put("_json_att", "");		
//			getQParam.put("REPEAT_SUBMIT_TOKEN", repeatToken);	
			
			String confirUrl = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
			LinkedHashMap<String, String> confirmParam = Maps.newLinkedHashMap();
			// TODO 
			 //3,0,1,崔海龙,1,420881198708053738,13524165011,N
			confirmParam.put("passengerTicketStr", passengerTicketStr(PropertiesUtil.getValue("name")));		
			//崔海龙,1,420881198708053738,1_
			confirmParam.put("oldPassengerStr", oldPassengerStr(PropertiesUtil.getValue("name")));	
			// TODO ????
			confirmParam.put("randCode", submitCode);		
			confirmParam.put("purpose_codes", purpose_codes);	
			
			confirmParam.put("key_check_isChange", key_check_isChange);		
			confirmParam.put("leftTicketStr", leftTicketStr);	
			
			confirmParam.put("train_location", train_location);	

			confirmParam.put("_json_att", "");		
			confirmParam.put("REPEAT_SUBMIT_TOKEN", repeatToken);
			
			String confirmResponse = HttpUtil.doPostWithParam(httpclient, confirUrl, HttpHeader.commonHeader(), confirmParam, new PrintResponseHandler());
			// TODO 
			
			StringBuilder sb = new StringBuilder();
			sb.append(Ticket.root);
			sb.append("otn/confirmPassenger/queryOrderWaitTime?");
			sb.append("random=" + System.currentTimeMillis());
			sb.append("&tourFlag=dc&_json_att=");
			sb.append("REPEAT_SUBMIT_TOKEN=" + repeatToken);
			
			//String waitUrl = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?random=1418824930573&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN=6a5782795beb480cff9c3c75878a8b54";
			String waitResponse = HttpUtil.goGet(httpclient, sb.toString(), HttpHeader.commonHeader(), new PrintResponseHandler());
			// TODO
			
			// last step, query incompletely order
			String incompletelyUrl = "https://kyfw.12306.cn/otn/queryOrder/queryMyOrderNoComplete";
			
			LinkedHashMap<String, String> incompletelyParam = Maps.newLinkedHashMap();
			incompletelyParam.put("_json_att", "");
			
			String doPostWithParam = HttpUtil.doPostWithParam(httpclient, incompletelyUrl, HttpHeader.commonHeader(), incompletelyParam, new PrintResponseHandler());
			
		
					

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private String passengerTicketStr(String name) {
		// 座位类型，0，车票类型，姓名，身份正号，电话，N（多个的话，以逗号分隔）
		// 3,0,1,崔海龙,1,420881198708053738,13524165011,N
		if("CHL".equals(name)) {
			return ticketDTO.getSeatType()  + ",0,1," + name + "崔海龙,1,420881198708053738,13524165011,N";
		} else if("GY".equals(name)) {
			return ticketDTO.getSeatType()  + ",0,1," + name + "高云,1,610502199111180222,18792558138,N";
		} else {
			return "";
		}
	}
	
	private String oldPassengerStr(String name) {
		if("CHL".equals(name)) {
			return "崔海龙,1,420881198708053738,1_";
		} else if("GY".equals(name)) {
			return "高云,1,610502199111180222,1_";
		} else {
			return "";
		}
	}
 
	/**
	 * 
	 * @param initDcContent
	 * @param regex
	 * @return
	 */
	private String extractContent(String initDcContent , String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(initDcContent);
		String repeatToken = "";
		if (m.matches()) {
			// 4th
			repeatToken = m.group(4);
		}
		return repeatToken;
	}

	public WorkThread() {
		super();
	}

	public WorkThread(CloseableHttpClient httpclient, TicketDTO ticketDTO) {
		super();
		this.httpclient = httpclient;
		this.ticketDTO = ticketDTO;
	}

	/**
	 * @return the httpclient
	 */
	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	/**
	 * @param httpclient
	 *            the httpclient to set
	 */
	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	/**
	 * @return the ticketDTO
	 */
	public TicketDTO getTicketDTO() {
		return ticketDTO;
	}

	/**
	 * @param ticketDTO
	 *            the ticketDTO to set
	 */
	public void setTicketDTO(TicketDTO ticketDTO) {
		this.ticketDTO = ticketDTO;
	}

}

