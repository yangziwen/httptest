package net.yangziwen.httptest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.yangziwen.httptest.dao.CaseParamDao;
import net.yangziwen.httptest.dao.ProjectDao;
import net.yangziwen.httptest.dao.TestCaseDao;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.model.Project;
import net.yangziwen.httptest.model.TestCase;

@Service
public class ProjectService {

	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private TestCaseDao testCaseDao;
	
	@Autowired
	private CaseParamDao caseParamDao;
	
	public Project getProjectById(long id) {
		return projectDao.getById(id);
	}
	
	public Page<Project> getProjectPageResult(int offset, int limit, Map<String, Object> params) {
		return projectDao.paginate(offset, limit, params);
	}
	
	public List<Project> getProjectListResult(Map<String, Object> params) {
		return projectDao.list(params);
	}
	
	public void createProject(Project project) {
		projectDao.save(project);
	}
	
	public void updateProject(Project project) {
		projectDao.update(project);
	}
	
	@Transactional
	public void deleteProject(long id) {
		List<TestCase> testCaseList = testCaseDao.list(new QueryParamMap().addParam("projectId", id));
		
		List<Long> caseIdList = new ArrayList<Long>();
		for(TestCase testCase: testCaseList) {
			caseIdList.add(testCase.getId());
		}
		
		projectDao.deleteById(id);
		testCaseDao.deleteByProjectId(id);
		caseParamDao.deleteByCaseIds(caseIdList);
	}
	
}
