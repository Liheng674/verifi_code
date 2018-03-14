package com.atguigu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.utils.VerifyCodeConfig;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CodeVerifyServlet
 */
public class CodeVerifyServlet extends HttpServlet {
	
 
    
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeVerifyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String phoneNo = request.getParameter("phone_no");
		String verifyCode = request.getParameter("verify_code");
		//1验空
		if(phoneNo==null||verifyCode==null) {
			return;
		}
		//2获取校验码
		String codeKey = VerifyCodeConfig.PHONE_PREFIX+phoneNo+VerifyCodeConfig.PHONE_SUFFIX;
		Jedis jedis = new Jedis("192.168.154.148", 6379);
		String code = jedis.get(codeKey);
		jedis.close();
		
		//判断校验码
		if(verifyCode.equals(code)) {
			response.getWriter().print(true);
		}
		
		
		
	}

}
