package net.yangziwen.httptest.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.yangziwen.httptest.controller.base.BaseController;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.dto.TestCaseDto;
import net.yangziwen.httptest.exception.HttpTestException;
import net.yangziwen.httptest.model.Project;
import net.yangziwen.httptest.model.TestCase;
import net.yangziwen.httptest.service.ProjectService;
import net.yangziwen.httptest.service.TestCaseService;

@Controller
@RequestMapping("/testcase")
public class TestCaseController extends BaseController {
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	private ProjectService projectService;
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelMap list(
			@RequestParam(defaultValue = Page.DEFAULT_OFFSET) int offset,
			@RequestParam(defaultValue = Page.DEFAULT_LIMIT) int limit,
			String projectName,
			String pathKeyword
			) {
		List<Long> projectIdList = null;
		if(StringUtils.isNotBlank(projectName)) {
			projectIdList = Lists.transform(projectService.getProjectListResult(new QueryParamMap()
				.addParam("name__contain", projectName)
			), new Function<Project, Long>() {
				@Override public Long apply(Project project) { return project.getId(); }
			});
		}
		Page<TestCase> page = testCaseService.getTestCasePageResult(offset, limit, new QueryParamMap()
			.addParam(projectIdList != null, "projectId__in", projectIdList)
			.addParam(StringUtils.isNotBlank(pathKeyword), "path__contain", pathKeyword)
		);
		return successResult(TestCaseDto.from(page));
	}
	
	@ResponseBody
	@RequestMapping(value = "/update/{id:\\d+}", method = RequestMethod.POST)
	public ModelMap update(
			@PathVariable("id") long id,
			@RequestParam String path,
			@RequestParam TestCase.Method method,
			@RequestParam String description
			) {
		if(id <= 0 || StringUtils.isBlank(path) || method == null) {
			throw HttpTestException.invalidParameterException("Parameters are invalid!");
		}
		TestCase testCase = testCaseService.getTestCaseById(id);
		if(testCase == null) {
			throw HttpTestException.notExistException("TestCase[%d] does not exist!", id);
		}
		try {
			testCase.setPath(path);
			testCase.setMethod(method);
			testCase.setDescription(description);
			testCaseService.updateTestCase(testCase);
			return successResult("TestCase[%d] is updated successfully!", testCase.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to update testCase[%d]", id);
		}
	}
	
}
