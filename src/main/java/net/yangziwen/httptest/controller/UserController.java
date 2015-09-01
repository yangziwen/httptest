package net.yangziwen.httptest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.yangziwen.httptest.controller.base.BaseController;
import net.yangziwen.httptest.model.User;
import net.yangziwen.httptest.service.UserService;
import net.yangziwen.httptest.util.AuthUtil;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/detail")
	public ModelMap detail() {
		User user = userService.getUserByUsername(AuthUtil.getUsername());
		return successResult(user);
	}

}
