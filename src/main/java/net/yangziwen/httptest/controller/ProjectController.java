package net.yangziwen.httptest.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.yangziwen.httptest.controller.base.BaseController;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.exception.HttpTestException;
import net.yangziwen.httptest.model.Project;
import net.yangziwen.httptest.service.ProjectService;

@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
	
	@Autowired
	private ProjectService projectService;

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelMap list(
			@RequestParam(defaultValue = Page.DEFAULT_OFFSET) int offset,
			@RequestParam(defaultValue = Page.DEFAULT_LIMIT) int limit,
			String name,
			String baseUrl
			) {
		Page<Project> page = projectService.getProjectPageResult(offset, limit, new QueryParamMap()
			.addParam(StringUtils.isNotBlank(name), "name__contain", name)
			.addParam(StringUtils.isNotBlank(baseUrl), "baseUrl__contain", baseUrl)
			.orderByAsc("id")
		);
		return successResult(page);
	}
	
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelMap create(
			@RequestParam String name,
			@RequestParam String baseUrl
			) {
		if(StringUtils.isBlank(name) || StringUtils.isBlank(baseUrl)) {
			throw HttpTestException.invalidParameterException("Parameters are invalid!");
		}
		try {
			Project project = new Project(name, baseUrl);
			projectService.createProject(project);
			return successResult("Project[%d] is created!", project.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to create Project[%s, %s]", name, baseUrl);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/update/{id:\\d+}", method = RequestMethod.POST)
	public ModelMap update(
			@PathVariable("id") long id,
			@RequestParam String name,
			@RequestParam String baseUrl
			) {
		if(id <= 0 || StringUtils.isBlank(name) || StringUtils.isBlank(baseUrl)) {
			throw HttpTestException.invalidParameterException("Parameters are invalid!");
		}
		Project project = projectService.getProjectById(id);
		if(project == null) {
			throw HttpTestException.notExistException("Project[%d] does not exist!", id);
		}
		try {
			project.setName(name);
			project.setBaseUrl(baseUrl);
			projectService.updateProject(project);
			return successResult("Project[%d] is updated!", project.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to update project[%d]!", id);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.POST)
	public ModelMap delete(@PathVariable("id") long id) {
		if(id <= 0) {
			throw HttpTestException.invalidParameterException("Parameter is invalid!");
		}
		try {
			projectService.deleteProject(id);
			return successResult("Project[%d] is deleted!", id);
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to delete project[%d]!", id);
		}
	}
	
	
}
