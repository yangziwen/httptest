<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 新增/修改project的弹出层 -->
<div class="modal" id="J_projectModal" tabindex="-1">
    <div class="modal-dialog">
    	<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title"><strong>标题</strong></h4>
			</div>
			<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="P_id" name="id" />
						<div class="form-group">
							<label for="P_name" class="col-sm-3 control-label">项目名称：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="P_name" name="name" placeholder="请输入项目名称" />
							</div>
						</div>
						<div class="form-group">
							<label for="P_baseUrl" class="col-sm-3 control-label">项目路径：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="P_baseUrl" name="baseUrl" placeholder="请输入项目路径" />
							</div>
						</div>
					</form>
				</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary create-project">创建</button>
				<button type="button" class="btn btn-primary update-project" >更新</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->