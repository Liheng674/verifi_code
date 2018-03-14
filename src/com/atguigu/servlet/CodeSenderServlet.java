package com.atguigu.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.utils.SmsUtil;
import com.atguigu.utils.VerifyCodeConfig;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class VerifiCodeServlet
 */
public class CodeSenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeSenderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
     
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String phoneNo = request.getParameter("phone_no");
		//1验空
		if(phoneNo ==null) {
			return;
		}
	   //1.5校验次数
		//获取计数器
		Jedis jedis = new Jedis("192.168.154.148", 6379);
		String countKey = VerifyCodeConfig.PHONE_PREFIX+phoneNo+VerifyCodeConfig.COUNT_SUFFIX;
		String countStr = jedis.get(countKey);
		
		//判断计数器是否为空，为空创建计数器
		if(countStr==null) {
			jedis.setex(countKey, VerifyCodeConfig.SECONDS_PER_DAY, "1");
		}else {
			//判断是否为3
			int count = Integer.parseInt(countStr);
			if(count==VerifyCodeConfig.COUNT_TIMES_1DAY) {
				response.getWriter().print("limit");
				jedis.close();
				return;
			}else {
				jedis.incr(countKey);
			}
		}
		
		
		
		//2获取6位校验码
		String code = genCode(VerifyCodeConfig.CODE_LEN);
		
		//3保存校验码
		String codeKey = VerifyCodeConfig.PHONE_PREFIX+phoneNo+VerifyCodeConfig.PHONE_SUFFIX;
		
		jedis.setex(codeKey, VerifyCodeConfig.CODE_TIMEOUT, code);
		jedis.close();
		//4发送校验码
		System.out.println(code);
		//5返回
		response.getWriter().print(true);
		
		
	} 
	
	
	private   String genCode(int len){
		 String code="";
		 for (int i = 0; i < len; i++) {
		     int rand=  new Random().nextInt(10);
		     code+=rand;
		 }
		 
		return code;
	}
	
	
 
}
