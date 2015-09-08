package net.yangziwen.httptest.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.yangziwen.httptest.controller.base.BaseController;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.dto.TestCaseDto;
import net.yangziwen.httptest.exception.HttpTestException;
import net.yangziwen.httptest.model.CaseParam;
import net.yangziwen.httptest.model.Project;
import net.yangziwen.httptest.model.TestCase;
import net.yangziwen.httptest.service.ProjectService;
import net.yangziwen.httptest.service.TestCaseService;
import net.yangziwen.httptest.util.HttpContextStore;
import net.yangziwen.httptest.util.ResponseResult;

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
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelMap create(
			@RequestParam long projectId,
			@RequestParam String path,
			@RequestParam TestCase.Method method,
			@RequestParam String description
			) {
		if(projectId <= 0 || StringUtils.isBlank(path) || method == null) {
			throw HttpTestException.invalidParameterException("Parameters are invalid!");
		}
		try {
			TestCase testCase = new TestCase(projectId, path, method, description);
			testCaseService.createTestCase(testCase);
			return successResult("TestCase[%d] is created!", testCase.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to create testCase!");
		}
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
			return successResult("TestCase[%d] is updated!", testCase.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to update testCase[%d]", id);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.POST)
	public ModelMap delete(
			@PathVariable("id") long id
			) {
		if(id <= 0) {
			throw HttpTestException.invalidParameterException("Parameter is invalid!");
		}
		try {
			testCaseService.deleteTestCase(id);
			return successResult("TestCase[%d] is deleted!", id);
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to delete testCase[%d]", id);
		}
	}
	
	@ResponseBody
	@RequestMapping("/params")
	public ModelMap getParams(
			@RequestParam long caseId
			) {
		if(caseId <= 0) {
			throw HttpTestException.invalidParameterException("Parameter is invalid!");
		}
		Map<String, Object> params = new QueryParamMap()
				.addParam("caseId", caseId)
				.orderByAsc("type");
		List<CaseParam> caseParamList = testCaseService.getCaseParamListResult(params);
		return successResult(caseParamList);
	}
	
	@ResponseBody
	@RequestMapping("/params/update")
	public ModelMap updateParams(
			@RequestParam long caseId,
			@RequestParam(value = "caseParamList", required = false) 
			String caseParamListJson 
			) {
		if(caseId <= 0) {
			throw HttpTestException.invalidParameterException("Parameters are invalid!");
		}
		TestCase testCase = testCaseService.getTestCaseById(caseId);
		if(testCase == null) {
			throw HttpTestException.notExistException("TestCase[%d] does not exist!", caseId);
		}
		List<CaseParam> caseParamList = JSON.parseArray(caseParamListJson, CaseParam.class);
		try {
			testCaseService.renewCaseParams(caseId, caseParamList);
			return successResult("CaseParams of testCase[%d] are renewed!", caseId);
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.operationFailedException("Failed to renew caseParams for testCase[%d]", caseId);
		}
	}
	
	@ResponseBody
	@RequestMapping("/test")
	public ModelMap test(@RequestParam long caseId, HttpSession session) {
		if(caseId <= 0) {
			throw HttpTestException.invalidParameterException("Parameter is invalid!");
		}
		TestCase testCase = testCaseService.getTestCaseById(caseId);
		if(testCase == null) {
			throw HttpTestException.notExistException("TestCase[%d] does not exist!", caseId);
		}
		return successResult(doTest(testCase, session));
	}
	
	private ResponseResult doTest(TestCase testCase, HttpSession session) {
		List<CaseParam> caseParamList = testCaseService.getCaseParamListResult(new QueryParamMap()
				.addParam("caseId", testCase.getId()));
		Project project = projectService.getProjectById(testCase.getProjectId());
		
		TestCaseDto dto = TestCaseDto.from(testCase, project);
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpClientContext context = getHttpContext(project.getId(), session);
		HttpUriRequest request = testCase.getMethod().createRequest(dto.getUrl(), caseParamList);
		
		try {
			return client.execute(request, new ResponseHandler<ResponseResult>() {
				@Override
				public ResponseResult handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					return new ResponseResult(response);
				}
			}, context);
		} catch (Exception e) {
			throw HttpTestException.operationFailedException("Failed to test testCase[%d]", testCase.getId());
		} finally {
			IOUtils.close(client);
		}
	}
	
	private HttpClientContext getHttpContext(long projectId, HttpSession session) {
		HttpContextStore store = (HttpContextStore) session.getAttribute(HttpContextStore.CONTEXT_STORE);
		return store.getContext(projectId);
	}
	
}
