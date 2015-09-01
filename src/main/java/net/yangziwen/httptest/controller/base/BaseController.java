package net.yangziwen.httptest.controller.base;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import net.yangziwen.httptest.exception.HttpTestException;

public abstract class BaseController {
	
	public static final int CODE_SUCCESS = 0;
	
	protected ModelMap successResult(String result) {
		return new ModelMap("code", CODE_SUCCESS).addAttribute("result", result);
	}
	
	protected ModelMap successResult(String result, Object... args) {
		return new ModelMap("code", CODE_SUCCESS).addAttribute("result", String.format(result, args));
	}
	
	protected ModelMap successResult(Object result) {
		return new ModelMap("code", CODE_SUCCESS).addAttribute("result", result);
	}
	
	/**
	 * 统一处理LogReplayException
	 */
	@ResponseBody
	@ExceptionHandler(HttpTestException.class)
	protected ModelMap handleLogReplayException(HttpTestException e) {
		return new ModelMap("code", e.getErrorId()).addAttribute("errorMsg", e.getErrorMsg());
	}
}
