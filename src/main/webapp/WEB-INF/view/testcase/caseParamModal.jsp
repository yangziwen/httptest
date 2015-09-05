<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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