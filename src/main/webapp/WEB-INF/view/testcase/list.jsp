<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/include.jsp" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <title>Http接口测试工具</title>
	<%@ include file="../include/includeCss.jsp" %>
	<link rel="stylesheet" href="${static_path}/css/inspector-json.css" />
	<link rel="stylesheet" href="${static_path}/css/shCore.css" />
	<link rel="stylesheet" href="${static_path}/css/shThemeDefault.css" />
	<style type="text/css">
		.padding-left-20 {
			padding-left: 20px !important;
		}
		table.inner-table {
			width: 100%;
		}
		table.inner-table tr > th, table.inner-table tr > td {
			text-align: center;
		}
		table.inner-table td {
			padding: 5px;
		}
		#J_testCaseTbody tr td:nth-child(2) {
			max-width: 520px;
			word-wrap: break-word;
		}
		#J_testCaseTbody tr td span {
			vertical-align: middle; 
			display: inline-block; 
		}
		#J_testCaseTbody tr td span:nth-child(1) {
			word-wrap:break-word; 
		}
		#J_testCaseTbody tr td span:nth-child(2) {
			height:100%;
			width:0px;
		}
		#J_testCaseTbody tr td button {
			margin-right: 4px;
		}
		#J_testCaseTbody tr td:nth-last-child(1) {
			padding-right: 0px;
		}
	</style>
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
						 &gt; <a href="${ctx_path}/testcase/list.htm">用例信息</a>
			        </li> 
			    </ul>
			</div>
			<div class="row"><!-- row2 -->
			    <div class="col-sm-12">
			     	<div id="J_queryArea" style="text-align: center;">
			     		<form class="form-horizontal col-md-offset-1 col-md-10" role="form">
							<div class="form-group">
								<label for="J_projectName" class="col-sm-2 control-label">项目名称：</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" id="J_projectName" name="projectName" placeholder="请输入项目名称" />
								</div>
								<label for="J_pathKeyword" class="col-sm-2 control-label">路径关键字：</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" id="J_pathKeyword" name="pathKeyword" placeholder="请输入路径关键字" />
								</div>
							</div>
							<div class="form-group" style="margin-bottom: 0px;">
								<div class="col-sm-12" style="margin-top: 10px;">
									<button id="J_queryBtn" type="button" class="btn btn-primary btn-lg-font" style="width: 90px;">查&nbsp;&nbsp;询</button>
									&nbsp;
									<button id="J_clearBtn" type="button"  class="btn btn-primary btn-lg-font" style="width: 90px;">清除条件</button>
								</div>
							</div>
						</form>
			     	</div>
			     </div>
			     <div class="col-sm-12"><hr/></div>
			     <div class="col-sm-12">
			     	<div class="row">
			     		<div id="J_pagebar" class="col-sm-12"></div>
			     	</div>
			     	<div style="margin-top: 20px;">
				     	<table class="table table-bordered table-striped table-condensed table-hover ">
				     		<thead>
				     			<tr>
				     				<th style="width: 240px; min-width: 140px;">项目名称</th>
				     				<th style="max-width: 520px;">接口url</th>
				     				<th style="width: 80px; min-width: 65px;">请求类型</th>
				     				<th style="width: 100px;">管理</th>
				     			</tr>
				     		</thead>
				     		<tbody id="J_testCaseTbody">
				     		</tbody>
							<script type="text/x-jquery-tmpl" id="J_testCaseTmpl">
								<tr data-id="${'${'}id}" data-path="${'${'}path}" 
										data-baseurl="${'${'}baseUrl}" 
										data-project-id="${'${'}projectId}"
										data-project-name="${'${'}projectName}"
										data-description="${'${'}description}"
										title="${'${'} '[' + projectName + '] ' + description}">
				     				<td>
										<span><a href="javascript:void(0);">[ ${'${'}projectName} ]</a> <br/> ${'${'}description}</span>
										<span></span>
									</td>
				     				<td class="text-left padding-left-20">
										<span style="width: 97%;">${'${'}url}</span>
										<span></span>
									</td>
									<td>
										<span>${'${'}method}</span>
										<span></span>
									</td>
				     				<td>
				     					<button class="btn btn-primary btn-xs open-update-modal">修改</button>
				     					<button class="btn btn-primary btn-xs delete-testcase">删除</button>
										<button class="btn btn-primary btn-xs open-case-param-modal">参数</button>
										<button class="btn btn-primary btn-xs execute-testcase">测试</button>
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

<%@ include file="./caseParamModal.jsp" %>

<%@ include file="./testResultModal.jsp" %>

<%@ include file="../include/includeJs.jsp" %>
<script>
seajs.use('app/testcase/list', function(list) {
	list.init();
});
</script>
</body>
</html>