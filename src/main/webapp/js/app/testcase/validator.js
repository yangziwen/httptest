define(function(require, exports, module) {
	
	"use strict";
	
	var $ = require('jquery'),
		coreValidator = require('app/core-validator');
	
	var options = {
		rules: {
			path: {
				required: true
			}
		}, 
		messages: {
			path: {
				required: '接口路径不能为空!'
			}
		}
	};
	
	module.exports = {
		validate: function(form) {
			return coreValidator.validate(form, options);
		}
	};
	
});