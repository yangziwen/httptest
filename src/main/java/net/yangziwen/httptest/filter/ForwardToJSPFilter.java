package net.yangziwen.httptest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 此filter用于将以".htm"结尾的请求forward到对应的jsp上
 * 例如/admin/user/list.htm对应的jsp为/WEB-INF/view/admin/user/list.jsp
 */
public class ForwardToJSPFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		if(StringUtils.isBlank(uri)) {
			request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);
			return;
		}
		if(!uri.endsWith(".htm")) {
			filterChain.doFilter(request, response);
			return;
		}
		// login.htm和logout.htm委托给shiro管理，不适用这种默认的forward方式
		if(uri.endsWith("login.htm") || uri.endsWith("logout.htm")) {
			filterChain.doFilter(request, response);
			return;
		}
		doForwardToJsp(request, response);
	}
	
	private void doForwardToJsp(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if(StringUtils.isNotBlank(contextPath) && !"/".equals(contextPath)) {
			uri = uri.replaceFirst(contextPath, "");
		}
		String forwardPath = "/WEB-INF/view" + uri.replaceFirst("\\.htm$", ".jsp");
		request.getRequestDispatcher(forwardPath).forward(request, response);
	}

}
