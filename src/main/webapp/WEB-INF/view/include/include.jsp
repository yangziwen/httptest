<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx_path" value="${pageContext.request.contextPath}" />
<c:set var="static_path" value="${pageContext.request.contextPath}" />
<c:set var="request_uri" value="${pageContext.request.requestURI}" />
<c:set var="static_version" value="20150901" /><%-- 静态文件的版本号 --%>