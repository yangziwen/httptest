package net.yangziwen.httptest.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.yangziwen.httptest.dao.TestCaseDao;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.model.TestCase;

@Service
public class TestCaseService {

	@Autowired
	private TestCaseDao testCaseDao;
	
	public TestCase getTestCaseById(long id) {
		return testCaseDao.getById(id);
	}
	
	public Page<TestCase> getTestCasePageResult(int offset, int limit, Map<String, Object> params) {
		return testCaseDao.paginate(offset, limit, params);
	}
	
	public void updateTestCase(TestCase testCase) {
		testCaseDao.update(testCase);
	}
	
}
