define(function(require, exports, module) {
	
	"use strict";

	require('jquery.tmpl');
	require('bootstrap.pagebar');
	
	var $ = require('jquery'),
		common = require('app/common');
	
	var projectValidator = require('app/project/validator').validate($('#J_projectModal form'));
	
	var testCaseValidator = require('app/testcase/validator').validate($('#J_testCaseModal form'));
	
	$('#J_projectModal').on('hide.bs.modal', function() {
		$(this).find('form').cleanValidateStyle();
	});
	
	$('#J_testCaseModal').on('hide.bs.modal', function() {
		$(this).find('form').cleanValidateStyle();
	});
	
	var offset = 0, limit = 30;	// 翻页信息
	
	function loadProjectResult(callback) {
		var params = $.extend({
			offset: offset, limit: limit
		}, common.collectParams('#J_queryArea input[type!=button][type!=submit][type!=reset]'));
		var url = CTX_PATH + '/project/list';
		$.get(url, params, function(data) {
			if(!data || !data.result || !data.result.list) {
				alert('failed!');
				return;
			}
			$.isFunction(callback) && callback(data);
		});
	}
	
	$.template('projectTmpl', $('#J_projectTmpl').html().replace(/>(?:\n|\s)+</g, '><'));
	
	function renderProjectTbody(list) {
		$('#J_projectTbody').empty().append($.tmpl('projectTmpl', list));
	}
	
	function refreshProjectTbl() {
		loadProjectResult(function(data) {
			var result = data.result;
			common.buildPageBar('#J_pagebar', result.offset, result.limit, result.count, function(i, pageNum) {
				offset = (pageNum - 1) * limit;
				refreshProjectTbl();
			});
			renderProjectTbody(result.list);
		});
	}
	
	/** 新增project开始 **/
	function initOpenCreateModalBtn() {
		$("#J_openCreateModalBtn").on('click', function() {
			var $modal = $('#J_projectModal');
			common.clearForm($modal.find('form'));
			$modal.find('.modal-title > strong').html('新增项目信息');
			$modal.find('input[name=id]').val('');
			$modal.find('.modal-dialog').css({
				width: 600,
				'margin-top': function() {
					return ( $(window).height() - $(this).height() ) / 3;
				}
			});
			$modal.find('button.create-project').show();
			$modal.find('button.update-project').hide();
			$modal.modal({
				backdrop: 'static'
			});
		});
	}
	
	function initCreateProjectBtn() {
		$('#J_projectModal').on('click', 'button.create-project', function() {
			if(!projectValidator.form()) {
				//common.alertMsg('参数有误，请检查!');
				return;
			}
			var params = {
				name: $('#P_name').val(),
				baseUrl: $('#P_baseUrl').val()
			};
			doCreateProject(params);
		});
	}
	
	function doCreateProject(params) {
		$.ajax({
			url: CTX_PATH + '/project/create',
			type: 'POST',
			dataType: 'json',
			data: params,
			success: function(data) {
				if(data.code !== 0) {
					common.alertMsg('创建失败!');
					return;
				} else {
					common.alertMsg('创建成功!').done(function() {
						$('#J_projectModal').modal('hide');
					});
					refreshProjectTbl();
				}
			},
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 新增project结束 **/
	
	/** 修改project开始 **/
	function initOpenUpdateModalBtn() {
		$('#J_projectTbody').on('click', 'button.open-update-modal', function() {
			var $this = $(this);
			var $tr = $this.parents('tr').eq(0),
				$tds = $tr.children();
			var id = $tr.data('id'),
				name = $tds.eq(0).children('a').html(),
				baseUrl = $tds.eq(1).html();
			var $modal = $('#J_projectModal');
			$modal.find('.modal-title > strong').html('修改项目信息');
			$modal.find('input[name=id]').val(id);
			$modal.find('input[name=name]').val(name);
			$modal.find('input[name=baseUrl]').val(baseUrl);
			$modal.find('.modal-dialog').css({
				width: 600,
				'margin-top': function() {
					return ( $(window).height() - $(this).height() ) / 3;
				}
			});
			$modal.find('button.create-project').hide();
			$modal.find('button.update-project').show();
			$modal.modal({
				backdrop: 'static'
			});
		});
	}
	
	function initUpdateProjectBtn() {
		$('#J_projectModal').on('click', 'button.update-project', function() {
			if(!projectValidator.form()) {
				//common.alertMsg('参数有误，请检查!');
				return;
			}
			var params = {
				id: $('#P_id').val(),
				name: $('#P_name').val(),
				baseUrl: $('#P_baseUrl').val()
			};
			doUpdateProject(params);
		});
	}
	
	function doUpdateProject(params) {
		$.ajax({
			url: CTX_PATH + '/project/update/' + params['id'],
			type: 'POST',
			dataType: 'json',
			data: params,
			success: function(data) {
				if(data.code !== 0) {
					common.alertMsg('更新失败!');
					return;
				} else {
					common.alertMsg('更新成功!').done(function() {
						$('#J_projectModal').modal('hide');
					});
					refreshProjectTbl();
				}
			},
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 修改project结束 **/
	
	/** 删除project开始 **/
	function initDeleteProjectBtn() {
		$('#J_projectTbody').on('click', 'button.delete-project', function() {
			var $tr = $(this).parents('tr').eq(0),
				$tds = $tr.children('td');
			var id = $tr.data('id'),
				name = $tds.eq(0).html();
			var confirmMsg = '确定要删除项目<strong>[' + name +']</strong><br/>及其相关联的测试用例信息?';
			common.confirmMsg(confirmMsg).then(function(confirmed) {
				confirmed && doDeleteProject(id);
			});
		});
	}
	
	function doDeleteProject(id) {
		$.ajax({
			url: CTX_PATH + '/project/delete/' + id,
			type: 'POST',
			dataType: 'json',
			success: function(data) {
				if(data.code !== 0) {
					common.alertMsg('删除失败!');
					return;
				} else {
					common.alertMsg('删除成功!');
					refreshProjectTbl();
				}
			},
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 删除project结束 **/
	
	/** 新增testCase开始 **/
	function initOpenCreateTestCaseModalBtn() {
		$('#J_projectTbody').on('click', 'button.open-create-testcase-modal', function() {
			var $tr = $(this).parents('tr').eq(0),
				$tds = $tr.children();
			var projectId = $tr.data('id'),
				projectName = $tds.eq(0).children('a').html(),
				baseUrl = $tds.eq(1).html();
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
				}
			}, 
			error: function() {
				common.alertMsg('请求失败!');
			}
		});
	}
	/** 新增testCase结束 **/
	
	function initQueryBtn() {
		var $queryBtn = $('#J_queryBtn');
		$('#J_queryArea').on('keyup', 'input[type!=button][type!=submit][type!=reset]', function(ev) {
			if(ev.which == 13) {
				$queryBtn.trigger('click');
			}
		});
		$queryBtn.on('click', function() {
			offset = 0;
			refreshProjectTbl();
		});
	}
	
	function initClearBtn() {
		$('#J_clearBtn').on('click', function() {
			offset = 0;
			common.clearForm($('#J_queryArea form'));
			refreshProjectTbl();
		});
	}
	
	function initProjectOperations() {
		initOpenCreateModalBtn();
		initOpenUpdateModalBtn();
		initCreateProjectBtn();
		initUpdateProjectBtn();
		initDeleteProjectBtn();
	}
	
	function initTestCaseOperations() {
		initOpenCreateTestCaseModalBtn();
		initCreateTestCaseBtn();
	}
	
	function init() {
		refreshProjectTbl();
		initProjectOperations();
		initTestCaseOperations();
		initQueryBtn();
		initClearBtn();
	}
	
	module.exports = {init: init};
	
});