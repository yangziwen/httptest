<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/include.jsp" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <title>Http接口测试工具</title>
	<%@ include file="../include/includeCss.jsp" %>
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
				     				<th style="width: 200px;">项目名称</th>
				     				<th>接口url</th>
				     				<th style="width: 100px;">请求类型</th>
				     				<th style="width: 200px;">管理</th>
				     			</tr>
				     		</thead>
				     		<tbody id="J_testCaseTbody">
				     		</tbody>
							<script type="text/x-jquery-tmpl" id="J_testCaseTmpl">
								<tr data-id="${'${'}id}" data-path="${'${'}path}" 
										data-baseurl="${'${'}baseUrl}"
										data-description="${'${'}description}"
										title="${'${'} '[' + projectName + '] ' + description}">
				     				<td>${'${'}projectName}</td>
				     				<td class="text-left padding-left-20">${'${'}url}</td>
									<td>${'${'}method}</td>
				     				<td>
				     					<button class="btn btn-primary btn-xs open-update-modal">修改</button>
				     					<button class="btn btn-primary btn-xs delete-testcase">删除</button>
										<button class="btn btn-primary btn-xs open-case-param-modal">参数</button>
										<button class="btn btn-primary btn-xs">测试</button>
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

<!-- 修改testCase的弹出层 -->
<div class="modal" id="J_testCaseModal" tabindex="-1">
    <div class="modal-dialog">
    	<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title"><strong>标题</strong></h4>
			</div>
			<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="TC_id" name="id" />
						<div class="form-group">
							<label for="TC_path" class="col-sm-4 control-label">接口路径：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="TC_path" name="path" placeholder="请输入接口路径" />
							</div>
						</div>
						<div class="form-group">
							<label for="TC_method" class="col-sm-4 control-label">请求类型：</label>
							<div class="col-sm-8">
								<select id="TC_method" name="method" class="form-control">
									<option>GET</option>
									<option>POST</option>
									<option>HEAD</option>
									<option>PUT</option>
									<option>DELETE</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="TC_description" class="col-sm-4 control-label">描述：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="TC_description" name="description" placeholder="请输入描述" />
							</div>
						</div>
					</form>
				</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary update-testcase">更新</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- 修改caseParam的弹出层 -->
<div class="modal" id="J_caseParamModal" tabindex="-1">
    <div class="modal-dialog">
    	<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title"><strong>更新参数</strong></h4>
			</div>
			<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="CP_caseId" name="caseId" />
						<div class="form-group">
							<label for="CP_projectName" class="col-sm-2 control-label">项目名称：</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="CP_projectName" name="projectName" placeholder="请输入项目名称" />
							</div>
							<label for="CP_baseUrl" class="col-sm-2 control-label">项目路径：</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="CP_baseUrl" name="baseUrl" placeholder="请输入项目路径" />
							</div>
						</div>
						<div class="form-group">
							<label for="CP_description" class="col-sm-2 control-label">用例描述：</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="CP_description" name="description" placeholder="请输入用例描述" />
							</div>
							<label for="CP_path" class="col-sm-2 control-label">接口路径：</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="CP_path" name="path" placeholder="请输入接口路径" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">参数设置：</label>
							<div class="col-sm-10">
								<table class="inner-table">
									<thead>
										<tr>
											<th width="30%">参数类型</th>
											<th width="30%">参数名</th>
											<th width="30%">参数值</th>
											<th width="10%" style="line-height: 0px;">
												<a class="add-case-param-btn" href="###" style="font-size: 20px;" title="添加参数">
													<i class="glyphicon glyphicon-plus-sign"></i>
												</a>
											</th>
										</tr>
									</thead>
									<tbody id="CP_caseParamTbody">
									</tbody>
									<script type="text/x-jquery-tmpl" id="CP_caseParamTmpl">
										<tr data-case-param-id="${'${'}id}">
				     						<td>
												<select class="form-control input-sm case-param-type">
													{{html $item.renderParamTypeOptions(type) }}
												</select>
											</td>
				     						<td><input type="text" class="input-sm form-control case-param-name" value="${'${'}name}" /></td>
				     						<td><input type="text" class="input-sm form-control case-param-value" value="${'${'}value}" /></td>
				     						<td style="line-height: 0px;">
												<a tabindex="-1" href="###" style="font-size:20px;" title="删除参数" class="remove-case-param-btn">
													<i class="glyphicon glyphicon-minus-sign"></i>
												</a>
											</td>
				     					</tr>
									</script>
								</table>
							</div>
						</div>
					</form>
				</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary update-case-param-btn">更新</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<%@ include file="../include/includeJs.jsp" %>
<script>
seajs.use('app/testcase/list', function(list) {
	list.init();
});
</script>
</body>
</html>