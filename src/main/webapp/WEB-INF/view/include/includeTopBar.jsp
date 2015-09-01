<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<nav class="navbar navbar-default navbar-fixed-top top-bar" role="navigation">
	<div class="container-fluit">
		<div class="collapse navbar-collapse">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">
				<ul class="nav navbar-left text-center" style="margin-top:6px; width: 100%;">
					<li class="title"><strong>Http接口校验工具</strong></li>
				</ul>
			</div>
			<shiro:user>
				<ul class="nav navbar-nav navbar-right">
					<li><a id="J_openProfileModalBtn" href="javascript:void(0);"><shiro:principal/></a></li>
					<li><a href="${ctx_path}/logout.htm">退出</a></li>
					<li style="width: 10px;"></li>
				</ul>
			</shiro:user>
		</div>
	</div>
</nav>
