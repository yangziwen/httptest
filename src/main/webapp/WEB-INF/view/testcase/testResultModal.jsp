<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- testCase测试结果的弹出层 -->
<div class="modal" id="J_testResultModal" tabindex="-1">
    <div class="modal-dialog">
    	<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title"><strong>测试结果</strong></h4>
			</div>
			<div class="modal-body" style="height: 500px;">
				<ul class="nav nav-tabs">
					<li class="active test-result-nav"><a href="javascript:void(0);">返回内容</a></li>
					<li class="test-headers-nav"><a href="javascript:void(0);">响应头信息</a></li>
				</ul>
				<div style="margin-top: 10px; height: 410px; overflow: auto;">
					<div class="result-content">
						<div id="J_resultJson" class="result-json" style="width: 100%; max-height: 410px; padding:20px;">
						</div>
						<div id="J_resultHtml">
						</div>
					</div>
					<table style="display:none;" class="table result-headers table-bordered _table-striped table-condensed table-hover ">
						<tbody>
						</tbody>
						<script type="text/x-jquery-tmpl" id="J_testResultHeadersTmpl">
						<tr>
							<td style="width: 40%;">${'${'}name}</td>
							<td class="text-left">${'${'}value}</td>
						</tr> 
					</script>
					</table>
				</div>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script src="${STATIC_PATH}/js/json/inspector-json.js"></script>
<script src="${STATIC_PATH}/js/syntaxhighlighter/shCore.js"></script>
<script src="${STATIC_PATH}/js/syntaxhighlighter/shBrushXml.js"></script>
<script src="${STATIC_PATH}/js/syntaxhighlighter/shBrushCss.js"></script>
<script src="${STATIC_PATH}/js/syntaxhighlighter/shBrushJs.js"></script>
