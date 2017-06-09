<%-- 
    Document   : header
    Created on : 9 juin 2017, 16:32:49
    Author     : adychka
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="resources/css/navbar-top-fixed.css" rel="stylesheet">
    </head>
    <body>
       
     <nav class="navbar navbar-expand-md navbar-inverse fixed-top bg-inverse">
      <a class="navbar-brand" href="${pageContext.request.contextPath}/">Social Security App</a>
       <ul class="nav navbar-nav">
            <li class="active"><a href="${pageContext.request.contextPath}/homepage">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/view/attr_bld">Attribute builder</a></li>
            <li><a href="${pageContext.request.contextPath}/view/ht_bld">Harm tree builder</a></li>
            <li><a href="${pageContext.request.contextPath}/view/about">About</a></li>
       </ul>
    </nav>
    </body>
</html>