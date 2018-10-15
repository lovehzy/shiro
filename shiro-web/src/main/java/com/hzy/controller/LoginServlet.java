package com.hzy.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LoginServlet  extends HttpServlet {
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
		
		if(exceptionClassName!=null) {
			if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
				req.setAttribute("errorMsg", "账号不存在！");
			}else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
				req.setAttribute("errorMsg", "密码有误!");
			}else {
				req.setAttribute("errorMsg", "请填写账号和密码！");
			}
		}
		req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
	}
	
}
