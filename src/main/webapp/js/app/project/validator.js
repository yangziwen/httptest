define(function(require, exports, module) {
	
	"use strict";
	
	var $ = require('jquery'),
		coreValidator = require('app/core-validator');
	
	var options = {
		rules: {
			baseUrl: {
				required: true
			},
			name: {
				required: true
			}
		}, 
		messages: {
			baseUrl: {
				required: '页面路径不能为空!'
			},
			name: {
				required: '项目名称不能为空!'
			}
		}
	};
	
	module.exports = {
		validate: function(form) {
			return coreValidator.validate(form, options);
		}
	};
	
});