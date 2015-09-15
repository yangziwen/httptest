<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/include.jsp" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <title>Http接口测试工具</title>
	<%@ include file="../include/includeCss.jsp" %>
</head>
<body>

<%@ include file="../include/includeTopBar.jsp" %>

<div class="container" style="margin-bottom: 50px;">
	<div class="row"><!-- row1 -->
		<%@ include file="../include/includeLeftMenu.jsp" %>
		<div class="col-sm-10">
			<div>
			    <ul class="breadcrumb">
			        <li>
						当前位置: <a data-toggle="collapse" href="#J_caseSubmenu">测试用例</a>
						 &gt; <a href="${ctx_path}/project/list.htm">项目信息</a>
			        </li> 
			    </ul>
			</div>
			<div class="row"><!-- row2 -->
			    <div class="col-sm-12">
			     	<div id="J_queryArea" style="text-align: center;">
			     		<form class="form-horizontal col-md-offset-1 col-md-10" role="form">
							<div class="form-group">
								<label for="J_name" class="col-sm-2 control-label">项目名称：</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" id="J_name" name="name" placeholder="请输入项目名称" />
								</div>
								<label for="J_baseUrl" class="col-sm-2 control-label">页面名称：</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" id="J_baseUrl" name="baseUrl" placeholder="请输入项目路径" />
								</div>
							</div>
							<div class="form-group" style="margin-bottom: 0px;">
								<div class="col-sm-12" style="margin-top: 10px;">
									<button id="J_queryBtn" type="button" class="btn btn-primary btn-lg-font" style="width: 90px;">查&nbsp;&nbsp;询</button>
									<button id="J_clearBtn" type="button"  class="btn btn-primary btn-lg-font" style="width: 90px;">清除条件</button>
								</div>
							</div>
						</form>
			     	</div>
			     </div>
			     <div class="col-sm-12"><hr/></div>
			     <div class="col-sm-12">
			     	<div class="row">
			     		<div class="col-sm-3">
			     			<button class="btn btn-primary btn-lg-font" id="J_openCreateModalBtn" title="新增项目信息">新增</button>
			     		</div>
			     		<div id="J_pagebar" class="col-sm-9"></div>
			     	</div>
			     	<div style="margin-top: 20px;">
				     	<table class="table table-bordered table-striped table-condensed table-hover ">
				     		<thead>
				     			<tr>
				     				<th style="width: 200px;">项目名称</th>
				     				<th>项目路径</th>
				     				<th style="width: 200px;">管理</th>
				     			</tr>
				     		</thead>
				     		<tbody id="J_projectTbody">
				     		</tbody>
							<script type="text/x-jquery-tmpl" id="J_projectTmpl">
								<tr data-id="${'${'}id}">
				     				<td><a href="${ctx_path}/testcase/list.htm#?projectName=${'${'}name}">${'${'}name}</a></td>
				     				<td>${'${'}baseUrl}</td>
				     				<td>
				     					<button class="btn btn-primary btn-xs open-update-modal">修改</button>
										<button class="btn btn-primary btn-xs delete-project">删除</button>
				     					<button class="btn btn-primary btn-xs open-create-testcase-modal">添加用例</button>
				     				</td>
				     			</tr>
							</script>
				     	</table>
			     	</div>
			    </div>
			</div><!--/row2-->
		</div>
	</div><!-- /row1 -->
</div>

<%@ include file="./updateModal.jsp" %>

<%@ include file="../testcase/updateModal.jsp" %>

<%@ include file="../include/includeJs.jsp" %>
<script>
seajs.use('app/project/list', function(list) {
	list.init();
});
</script>
</body>
</html>