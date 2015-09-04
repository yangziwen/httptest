package net.yangziwen.httptest.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.yangziwen.httptest.dao.ProjectDao;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.model.Project;

@Service
public class ProjectService {

	@Autowired
	private ProjectDao projectDao;
	
	public Project getProjectById(long id) {
		return projectDao.getById(id);
	}
	
	public Page<Project> getProjectPageResult(int offset, int limit, Map<String, Object> params) {
		return projectDao.paginate(offset, limit, params);
	}
	
	public void createProject(Project project) {
		projectDao.save(project);
	}
	
	public void updateProject(Project project) {
		projectDao.update(project);
	}
	
	public void deleteProject(long id) {
		projectDao.deleteById(id);
	}
	
}
