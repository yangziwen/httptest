package net.yangziwen.httptest.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.model.Project;
import net.yangziwen.httptest.model.TestCase;
import net.yangziwen.httptest.model.TestCase.Method;
import net.yangziwen.httptest.service.ProjectService;
import net.yangziwen.httptest.util.SpringUtil;

public class TestCaseDto {
	
	private static ProjectService projectService = null;
	
	private TestCase testCase;
	
	private Project project;
	
	private TestCaseDto(TestCase testCase, Project project) {
		this.testCase = testCase;
		this.project = project;
	}

	public long getId() {
		return testCase.getId();
	}

	public long getProjectId() {
		return testCase.getProjectId();
	}

	public String getPath() {
		return testCase.getPath();
	}
	
	public String getBaseUrl() {
		return project != null? project.getBaseUrl(): "";
	}
	
	public String getUrl() {
		return getBaseUrl() + testCase.getPath();
	}

	public Method getMethod() {
		return testCase.getMethod();
	}

	public String getDescription() {
		return testCase.getDescription();
	}
	
	public String getProjectName() {
		return project != null? project.getName(): null;
	}
	
	public static TestCaseDto from(TestCase testCase, Project project) {
		return new TestCaseDto(testCase, project);
	}
	
	public static List<TestCaseDto> from(List<TestCase> list) {
		Set<Long> projectIdSet = new HashSet<Long>();
		for(TestCase tc: list) {
			projectIdSet.add(tc.getProjectId());
		}
		List<Project> projectList = ensureProjectService().getProjectListResult(new QueryParamMap()
			.addParam("id__in", projectIdSet)
		);
		
		Map<Long, Project> projectMap = new HashMap<Long, Project>();
		for(Project project: projectList) {
			projectMap.put(project.getId(), project);
		}
		
		List<TestCaseDto> dtoList = new ArrayList<TestCaseDto>(list.size());
		for(TestCase tc: list) {
			dtoList.add(from(tc, projectMap.get(tc.getProjectId())));
		}
		return dtoList;
	}
	
	public static Page<TestCaseDto> from(Page<TestCase> page) {
		return new Page<TestCaseDto>(
			page.getOffset(), 
			page.getLimit(), 
			page.getCount(),
			from(page.getList())
		);
	}
	
	private static ProjectService ensureProjectService() {
		if(projectService != null) {
			return projectService;
		}
		synchronized (TestCaseDto.class) {
			if(projectService == null) {
				projectService = SpringUtil.getBean(ProjectService.class);
			}
		}
		return projectService;
	}

}
