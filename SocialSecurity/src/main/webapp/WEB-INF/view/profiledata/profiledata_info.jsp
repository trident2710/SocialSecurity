<%-- 
    Document   : userdata_all.jsp
    Created on : 5 juil. 2017, 15:33:34
    Author     : adychka
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Profile data</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/attribute_add.css" rel="stylesheet">
    </head>
    <body>
        <script>var ctx = "${pageContext.request.contextPath}"</script>
        <%@include file="../basic/header.jsp" %>
        <table style="margin:10px" class="table table-bordered table-hover">
            <tr>
                <th>Perspective</th>
            <c:forEach var="attr" items="${attrs}">
                <th>${attr}</th>
            </c:forEach>   
            </tr>
            <c:if test = "${s_attrs.isEmpty() == false}">
                <tr>
                    <td>Stranger</td>
                <c:forEach var="attrs" items="${s_attrs}">
                    <td>${attrs.value}</td>
                </c:forEach>  
                </tr> 
            </c:if>
            <c:if test = "${f_attrs.isEmpty() == false}">
                <tr>
                    <td>Friend</td>
                <c:forEach var="attrs" items="${f_attrs}">
                    <td>${attrs.value}</td>
                </c:forEach>  
                </tr> 
            </c:if>
            <c:if test = "${ff_attrs.isEmpty() == false}">
                <tr>
                    <td>Friend of friend</td>
                <c:forEach var="attrs" items="${ff_attrs}">
                    <td>${attrs.value}</td>
                </c:forEach>  
                </tr> 
            </c:if>
       </table>
        
        
        <%@include file="../basic/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>    
    </body>
</html>
