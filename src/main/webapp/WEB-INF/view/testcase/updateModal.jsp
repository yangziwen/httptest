<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 新增/修改testCase的弹出层 -->
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
					<input type="hidden" id="TC_projectId" name="projectId" />
					<div class="form-group">
						<label for="TC_projectName" class="col-sm-4 control-label">项目名称：</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="TC_projectName" name="projectName" placeholder="请输入项目名称" />
						</div>
					</div>
					<div class="form-group">
						<label for="TC_baseUrl" class="col-sm-4 control-label">项目路径：</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="TC_baseUrl" name="baseUrl" placeholder="请输入项目路径" />
						</div>
					</div>
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
								<option>PUT</option>
								<option>DELETE</option>
								<option>ENTITY_ENCLOSEING_GET</option>
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
				<button type="button" class="btn btn-primary create-testcase">创建</button>
				<button type="button" class="btn btn-primary update-testcase">更新</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->