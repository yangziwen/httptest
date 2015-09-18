package net.yangziwen.httptest.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import net.yangziwen.httptest.controller.base.BaseController;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.dto.TestCaseDto;
import net.yangziwen.httptest.exception.HttpTestException;
import net.yangziwen.httptest.model.CaseParam;
import net.yangziwen.httptest.model.CaseParam.Type;
import net.yangziwen.httptest.model.Project;
import net.yangziwen.httptest.model.TestCase;
import net.yangziwen.httptest.service.ProjectService;
import net.yangziwen.httptest.service.TestCaseService;
import net.yangziwen.httptest.util.ResponseResult;

@Controller
@RequestMapping("/testcase")
public class TestCaseController extends BaseController {
	
	public static final String HTTP_CONTEXT = "http_context";
	
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	private static final CollectionType CASE_PARAM_COLL_TYPE = JSON_MAPPER.getTypeFactory()
			.constructCollectionType(List.class, CaseParam.class);
	
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
			
			List<Project> projectList = projectService.getProjectListResult(new QueryParamMap()
				.addParam("name__contain", projectName)
			);
			
			projectIdList = new ArrayList<Long>();
			for(Project project: projectList) {
				projectIdList.add(project.getId());
			}
			
		}
		Page<TestCase> page = testCaseService.getTestCasePageResult(offset, limit, new QueryParamMap()
			.addParam(projectIdList != null, "projectId__in", projectIdList)
			.addParam(StringUtils.isNotBlank(pathKeyword), "path__contain", pathKeyword)
			.orderByAsc("project_id")
			.orderByAsc("path")
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
		
		List<CaseParam> caseParamList = null;
		try {
			caseParamList = JSON_MAPPER.readValue(caseParamListJson, CASE_PARAM_COLL_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			throw HttpTestException.invalidParameterException("Invalid caseParamListJson [%s]", caseParamListJson);
		}
		
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
		HttpUriRequest request = testCase.getMethod().createRequest(dto.getUrl(), caseParamList);
		HttpClientContext context = getHttpContext(project.getId(), session);
		String host = null; 
		try {
			host = new URL(dto.getUrl()).getHost();
		} catch (Exception e) {
			throw HttpTestException.illegalUrlException("Invalid url[%s]", dto.getUrl());
		}
		addCookies(host, caseParamList, context);
		
		try {
			final long sendTime = System.currentTimeMillis();
			return client.execute(request, new ResponseHandler<ResponseResult>() {
				@Override
				public ResponseResult handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					return new ResponseResult(response, System.currentTimeMillis() - sendTime);
				}
			}, context);
		} catch (Exception e) {
			throw HttpTestException.operationFailedException("Failed to test testCase[%d]", testCase.getId());
		} finally {
			IOUtils.closeQuietly(client);
		}
	}
	
	/**
	 * 此处有些简陋，后续可以考虑进一步加强cookie的机制
	 */
	private void addCookies(String host, List<CaseParam> caseParamList, HttpClientContext context) {
		CookieStore cookieStore = getCookieStore(context);
		Date now = new Date();
		cookieStore.clearExpired(now);
		for(CaseParam param: caseParamList) {
			if(param.getType() == Type.COOKIE) {
				BasicClientCookie cookie = new BasicClientCookie(param.getName(), param.getValue());
				cookie.setDomain(host);
				cookie.setPath("/");
				cookie.setExpiryDate(DateUtils.addSeconds(now, 2));	// 自定义参数2秒后过期
				cookieStore.addCookie(cookie);
			}
		}
	}
	
	private CookieStore getCookieStore(HttpClientContext context) {
		CookieStore cookieStore = context.getCookieStore();
		if(cookieStore != null) {
			return cookieStore;
		}
		synchronized (context) {
			if((cookieStore = context.getCookieStore()) == null) {
				cookieStore = new BasicCookieStore();
				context.setCookieStore(cookieStore);
			}
		}
		return cookieStore;
	}
	
	private HttpClientContext getHttpContext(long projectId, HttpSession session) {
		return (HttpClientContext) session.getAttribute(HTTP_CONTEXT);
	}
	
}
