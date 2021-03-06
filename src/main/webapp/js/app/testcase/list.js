define(function(require, exports, module) {
	
	"use strict";

	require('jquery.tmpl');
	require('bootstrap.pagebar');
	require('syntaxhighlighter/shBrushCss.js');
	require('syntaxhighlighter/shBrushJScript.js');
	require('syntaxhighlighter/shBrushXml.js');
	var $ = require('jquery'),
		common = require('app/common'),
		beautifyJs = require('app/util/beautify.js'),
		beautifyHtml = require('app/util/beautify-html.js');
	var testCaseValidator = require('app/testcase/validator').validate($('#J_testCaseModal form'));
	
	$('#J_testCaseModal').on('hide.bs.modal', function() {
		$('#J_testCaseModal form').cleanValidateStyle();
	});
	
	var offset = 0, limit = 30;	// 翻页信息
	
	function loadTestCaseResult(callback) {
		var params = $.extend({
			offset: offset, limit: limit
		}, common.collectParams('#J_queryArea input[type!=button][type!=submit][type!=reset]'));
		var url = CTX_PATH + '/testcase/list';
		$.get(url, params, function(data) {
			if(!data || !data.result || !data.result.list) {
				alert('failed!');
				return;
			}
			$.isFunction(callback) && callback(data);
		});
	}
	
	$.template('testCaseTmpl', $('#J_testCaseTmpl').html().replace(/>(?:\n|\s)+</g, '><'));
	
	function renderTestCaseTbody(list) {
		$('#J_testCaseTbody').empty().append($.tmpl('testCaseTmpl', list));
	}
	
	function refreshTestCaseTbl() {
		loadTestCaseResult(function(data) {
			var result = data.result;
			common.buildPageBar('#J_pagebar', result.offset, result.limit, result.count, function(i, pageNum) {
				offset = (pageNum - 1) * limit;
				refreshTestCaseTbl();
			});
			renderTestCaseTbody(result.list);
		});
	}
	
	/** 新增testCase开始 **/
	function initOpenCreateModalBtn() {
		$('#J_testCaseTbody').on('click', 'tr td:nth-child(1) a', function() {
			var $tr = $(this).parents('tr').eq(0),
				$tds = $tr.children();
			var projectId = $tr.data('project-id'),
				projectName = $tr.data('project-name'),
				baseUrl = $tr.data('baseurl');
			var $modal = $('#J_testCaseModal');
			common.clearForm($modal.find('form'));
			$modal.find('.modal-title > strong').html('新增用例信息');
			$modal.find('input[name=id]').val('');
			$modal.find('input[name=projectId]').val(projectId);
			$modal.find('input[name=projectName]').val(projectName)
				.attr({disabled: true, title: projectName});
			$modal.find('input[name=baseUrl]').val(baseUrl)
				.attr({disabled: true, title: baseUrl});
			$modal.find('select[name=method]').val('GET');
			$modal.find('.modal-dialog').css({
				width: 500,
				'margin-top': function() {
					return ( $(window).height() - 400 ) / 2;
				}
			});
			$modal.find('button.create-testcase').show();
			$modal.find('button.update-testcase').hide();
			$modal.modal({
				backdrop: 'static'
			});
		});
	}
	
	function initCreateTestCaseBtn() {
		$('#J_testCaseModal').on('click', 'button.create-testcase', function() {
			if(!testCaseValidator.form()) {
				return;
			}
			var params = {
				projectId: $('#TC_projectId').val(),
				path: $('#TC_path').val(),
				description: $('#TC_description').val(),
				method: $('#TC_method').val()
			}
			doCreateTestCase(params);
		});
	}
	
	function doCreateTestCase(params) {
		$.ajax({
			url: CTX_PATH + '/testcase/create',
			type: 'POST',
			dataType: 'json',
			data: params,
			success: function(data) {
				if(data.code !== 0) {
					common.alertMsg('创建失败!');
					return;
				} else {
					common.alertMsg('创建成功!').done(function() {
						$('#J_testCaseModal').modal('hide');
					});
					refreshTestCaseTbl();
				}
			}, 
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 新增testCase结束 **/
	
	/** 修改testCase开始 **/
	function initOpenUpdateModalBtn() {
		$('#J_testCaseTbody').on('click', 'button.open-update-modal', function() {
			var $this = $(this);
			var $tr = $this.parents('tr').eq(0),
				$tds = $tr.children();
			var projectName = $tr.data('project-name'),
				baseUrl = $tr.data('baseurl');
			var id = $tr.data('id'),
				path = $tr.data('path'),
				method = $tds.eq(2).children('span:nth-child(1)').html(),
				description = $tr.data('description');
			var $modal = $('#J_testCaseModal');
			$modal.find('.modal-title > strong').html('修改用例信息');
			$modal.find('input[name=id]').val(id);
			$modal.find('input[name=path]').val(path);
			$modal.find('select[name=method]').val(method);
			$modal.find('input[name=description]').val(description);
			$modal.find('input[name=projectName]').val(projectName)
				.attr({disabled: true, title: projectName});
			$modal.find('input[name=baseUrl]').val(baseUrl)
				.attr({disabled: true, title: baseUrl});
			$modal.find('.modal-dialog').css({
				width: 500,
				'margin-top': function() {
					return ( $(window).height() - 400 ) / 2;
				}
			});
			$modal.find('button.create-testcase').hide();
			$modal.find('button.update-testcase').show();
			$modal.modal({
				backdrop: 'static'
			});
		});
	}
	
	function initUpdateTestCaseBtn() {
		$('#J_testCaseModal').on('click', 'button.update-testcase', function() {
			if(!testCaseValidator.form()) {
				//common.alertMsg('参数有误，请检查!');
				return;
			}
			var params = {
				id: $('#TC_id').val(),
				path: $('#TC_path').val(),
				description: $('#TC_description').val(),
				method: $('#TC_method').val()
			};
			doUpdateTestCase(params);
		});
	}
	
	function doUpdateTestCase(params) {
		$.ajax({
			url: CTX_PATH + '/testcase/update/' + params['id'],
			type: 'POST',
			dataType: 'json',
			data: params,
			success: function(data) {
				if(data.code !== 0) {
					common.alertMsg('更新失败!');
					return;
				} else {
					common.alertMsg('更新成功!').done(function() {
						$('#J_testCaseModal').modal('hide');
					});
					refreshTestCaseTbl();
				}
			},
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 修改testCase结束 **/
	
	/** 删除testCase开始 **/
	function initDeleteTestCaseBtn() {
		$('#J_testCaseTbody').on('click', 'button.delete-testcase', function() {
			var $tr = $(this).parents('tr').eq(0),
				$tds = $tr.children('td');
			var id = $tr.data('id'),
				fullPath = $tds.eq(1).html();
			var confirmMsg = '确定要删除以下测试用例?<br/><strong>[' + fullPath +']</strong>';
			common.confirmMsg(confirmMsg).then(function(confirmed) {
				confirmed && doDeleteTestCase(id);
			});
		});
	}
	
	function doDeleteTestCase(id) {
		$.ajax({
			url: CTX_PATH + '/testcase/delete/' + id,
			type: 'POST',
			dataType: 'json',
			success: function(data) {
				if(data.code !== 0) {
					common.alertMsg('删除失败!');
					return;
				} else {
					common.alertMsg('删除成功!');
					refreshTestCaseTbl();
				}
			},
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 删除testCase结束 **/
	
	/** 更新caesParam开始 **/
	var caseParamTypes = {
		'PARAM': '一般参数',
		'UPLOAD_PARAM': '上传参数',
		'TEXT_ENTITY': '文本请求体',
		'HEADER': 'header参数',
		'COOKIE': 'cookie参数'
	};
	
	var renderParamTypeOptions = function(type) {
		return $.map(caseParamTypes, function(value, key) {
			return '<option ' + ((key == type)? 'selected="selected"': '')
				+ 'value="' + key + '">' + value + '</option>'
		}).join('');
	}
	
	function initOpenUpdateCaseParamModalBtn() {
		
		$('#CP_caseParamTbody').on('click', '.remove-case-param-btn', function() {
			$(this).parents('tr').eq(0).remove();
		});
		
		$('#J_testCaseTbody').on('click', '.open-case-param-modal', function() {
			var $tr = $(this).parents('tr').eq(0),
				$tds = $tr.children();
			var caseId = $tr.data('id'),
				projectName = $tr.data('project-name'), //$tds.eq(0).html(),
				baseUrl = $tr.data('baseurl'),
				description = $tr.data('description'),
				path = $tr.data('path');
			var $modal = $('#J_caseParamModal');
			common.clearForm($modal.find('form'));
			$modal.find('input[name=caseId]').val(caseId);
			$modal.find('input[name=projectName]')
				.val(projectName)
				.attr({disabled: true, title: projectName});
			$modal.find('input[name=baseUrl]')
				.val(baseUrl)
				.attr({disabled: true, title: baseUrl});
			$modal.find('input[name=description]')
				.val(description)
				.attr({disabled: true, title: description});
			$modal.find('input[name=path]')
				.val(path)
				.attr({disabled: true, title: path});
			
			var url = CTX_PATH + '/testcase/params';
			$.get(url, {caseId: caseId}, function(data) {
				var caseParamList = data.result || [];
				$('#CP_caseParamTbody').empty().append($('#CP_caseParamTmpl').tmpl(caseParamList, {
					renderParamTypeOptions: renderParamTypeOptions
				}));
				$modal.find('.modal-dialog').css({
					width: 800,
					'margin-top': 100
				});
				$modal.find('button.update-case-param').show();
				$modal.modal({
					backdrop: 'static'
				});
			});
		});
	}
	
	function initAddCaseParamBtn() {
		$('#J_caseParamModal').on('click', '.add-case-param-btn', function() {
			var $tr = $('#CP_caseParamTmpl').tmpl({type: 'GET'}, {
				renderParamTypeOptions: renderParamTypeOptions
			});
			$tr.appendTo('#CP_caseParamTbody');
			var $backdrop = $('#J_caseParamModal .modal-backdrop.in');
			$backdrop.height($backdrop.height() + $tr.height());
		});
	}
	
	function initUpdateCaseParamBtn() {
		var $modal = $('#J_caseParamModal');
		$modal.on('click', '.update-case-param-btn', function() {
			var caseId = $('#CP_caseId').val();
			var caseParamList = $('#CP_caseParamTbody tr').map(function(i, tr) {
				var $tr = $(tr);
				var caseParam = {
					id: $tr.data('case-param-id'),
					name: $tr.find('.case-param-name').val().trim(),
					value: $tr.find('.case-param-value').val().trim(),
					type: $tr.find('.case-param-type').val(),
					caseId: caseId
				};
				return caseParam.name? caseParam: null; 
			}).toArray();
			var params = {
				caseId: caseId,
				caseParamList: JSON.stringify(caseParamList)
			}
			doUpdateCaseParam(params);
		});
	}
	function doUpdateCaseParam(params) {
		$.ajax({
			url: CTX_PATH + '/testcase/params/update',
			type: 'POST',
			dataType: 'json',
			data: params,
			success: function(data) {
				if(data.code !== 0) {
					common.alertMsg('更新失败!');
					return;
				} else {
					common.alertMsg('更新成功!').done(function() {
						$('#J_caseParamModal').modal('hide');
					});
				}
			},
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 更新caseParam结束 **/
	
	/** 用例测试开始 **/
	function initExecuteTestCaseBtn() {
		var $modal = $('#J_testResultModal');
		var $resultNav = $modal.find('.test-result-nav'),
			$headersNav = $modal.find('.test-headers-nav');
		
		$('#J_testCaseTbody').on('click', 'button.execute-testcase', function() {
			$resultNav.trigger('click');
			var $tr = $(this).parents('tr').eq(0);
			$.get(CTX_PATH + '/testcase/test', {
				caseId: $tr.data('id')
			}, function(data) {
				if(data.code === 0) {
					showTestResult(data.result);
				} else {
					common.alertMsg("测试失败!");
				}
			});
		});
	}
	
	function initTestResultModal() {
		var $modal = $('#J_testResultModal');
		var $resultNav = $modal.find('.test-result-nav'),
			$headersNav = $modal.find('.test-headers-nav');
		$resultNav.on('click', function() {
			$(this).addClass('active').siblings().removeClass('active');
			$modal.find('.result-content').show();
			$modal.find('.result-headers').hide();
		});
		$headersNav.on('click', function() {
			$(this).addClass('active').siblings().removeClass('active');
			$modal.find('.result-content').hide();
			$modal.find('.result-headers').show();
		});
	}
	
	var inspector = new InspectorJSON({
		element: 'J_resultJson',
		collapsed: false
	});
	
	function showTestResult(result) {
		var $modal = $('#J_testResultModal');
		var $title = $modal.find('.modal-title');
		$title.html('<strong>测试结果 ' + 
			[
				'Status:[' + result.statusLine + ']',
				'Time:[' + result.timeSpentMillis + 'ms]',
				'Size:[' + toDisplaySize(result.bodySize) + ']'
			].join(' ')
			+ '</strong>');
		var $resultHeadersTbody = $modal.find('.result-headers tbody');
		var $resultJson = $('#J_resultJson').empty();
		var $resultHtml = $('#J_resultHtml').empty();
		var $resultImg = $('#J_resultImg').empty();
		$resultHeadersTbody.empty().append($('#J_testResultHeadersTmpl').tmpl(result.headers));
		var mimeType = result.contentType.mimeType;
		result.content = result.content === null? "": result.content.trim();
		if(mimeType == 'application/json' 
				|| (mimeType == 'text/plain' && mightBeJson(result.content))) {
			inspector.view($.parseJSON(result.content));
			$resultJson.show().siblings().hide();
		} else if(mimeType.indexOf('text/') == 0) {
			if(mimeType == 'text/javascript') {
				result.content = beautifyJs.js_beautify(result.content);
				$resultHtml.append('<pre class="brush: js"></pre>');
			} else {
				result.content = beautifyHtml.html_beautify(result.content);
				$resultHtml.append('<pre class="brush: html;"></pre>');
			}
			var $pre = $resultHtml.find('pre').text(result.content);
			SyntaxHighlighter.highlight($pre[0]);
			$resultHtml.show().siblings().hide();
		} else if(mimeType.indexOf('image/') == 0) {
			var $img = $('<img/>').css({border: '2px solid #ddd'}).appendTo($resultImg);
			$img.attr('src', 'data:' + mimeType + ';base64, ' + result.content);
			$resultImg.show().siblings().hide();
			$img.on('load', function() {
				$img.attr('title', $img.width() + '×' + $img.height());
			})
		}
		$modal.find('.modal-dialog').css({
			width: 800
		});
		$modal.modal({backdrop: 'static'});
	}
	
	/** 返回true，则有可能是json **/
	function mightBeJson(content) {
		if(!content || (typeof content) != 'string') {
			return false;
		}
		content = content.trim();
		return content.startsWith('{') && content.endsWith('}');
	}
	
	var sizeUnits = ['b', 'KB', 'MB', 'GB', 'TB'];
	
	function toDisplaySize(size) {
		size = parseInt(size);
		if(isNaN(size)) {
			return -1;
		}
		for(var i = 0, l = sizeUnits.length; i < l; i ++) {
			if(size < 1024) {
				break;
			}
			size /= 1024;
		}
		return size.toFixed(2).replace(/\.00/, '') + sizeUnits[i];
	}
	/** 用例测试结束 **/
	
	function initQueryBtn() {
		var $queryBtn = $('#J_queryBtn');
		$('#J_queryArea').on('keyup', 'input[type!=button][type!=submit][type!=reset]', function(ev) {
			if(ev.which == 13) {
				$queryBtn.trigger('click');
			}
		});
		$queryBtn.on('click', function() {
			offset = 0;
			updateHash();
			refreshTestCaseTbl();
		});
	}
	
	function initClearBtn() {
		$('#J_clearBtn').on('click', function() {
			offset = 0;
			common.clearForm($('#J_queryArea form'));
			updateHash();
			refreshTestCaseTbl();
		});
	}
	
	function updateHash() {
		var hashObj = {
			projectName: $('#J_projectName').val() || null,
			pathKeyword: $('#J_pathKeyword').val() || null
		}
		common.hash(hashObj);
	}
	
	function initHashChange() {
		var $window = $(window),
			$projectName = $('#J_projectName'),
			$pathKeyword = $('#J_pathKeyword');
		$window.on('hashchange', function() {
			var hashObj = common.hash();
			$projectName.val(hashObj['projectName'] || '');
			$pathKeyword.val(hashObj['pathKeyword'] || '');
		});
		$(window).trigger('hashchange');
	}
	
	function initTestCaseOperations() {
		initOpenCreateModalBtn();
		initCreateTestCaseBtn();
		
		initOpenUpdateModalBtn();
		initUpdateTestCaseBtn();
		initDeleteTestCaseBtn();
		initExecuteTestCaseBtn();
		initTestResultModal();
	}
	
	function initCaseParamOperations() {
		initOpenUpdateCaseParamModalBtn();
		initAddCaseParamBtn();
		initUpdateCaseParamBtn();
	}
	
	function init() {
		initHashChange();
		refreshTestCaseTbl();
		initTestCaseOperations();
		initCaseParamOperations();
		initQueryBtn();
		initClearBtn();
	}
	
	module.exports = {init: init};
	
});