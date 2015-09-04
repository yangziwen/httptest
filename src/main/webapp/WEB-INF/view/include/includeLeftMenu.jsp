<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="col-sm-2">
	<div class="sidebar-nav row">
			<ul class="nav nav-pills nav-stacked">
				<li class="active">
					<a href="${ctx_path}/home.htm">
						<i class="glyphicon glyphicon-home"></i><span> 主页</span>
					</a>
				</li>
				<li class="active">
					<a class="has-submenu" data-toggle="collapse" href="#J_caseSubmenu">
						<i class="glyphicon glyphicon-plus"></i>
						<span> 测试用例</span>
						<span class="pull-right glyphicon glyphicon-chevron-down"></span>
					</a>
					<ul id="J_caseSubmenu" class="nav submenu in" >
						<li><a href="${ctx_path}/project/list.htm"><i class="glyphicon glyphicon-chevron-right"></i> 项目信息</a></li>
						<li><a href="###"><i class="glyphicon glyphicon-chevron-right"></i> 用例信息</a></li>
					</ul>
				</li>
			</ul>
	</div>
</div>
