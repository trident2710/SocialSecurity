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
        <link href="${pageContext.request.contextPath}/resources/css/list_all.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="../basic/header.jsp" %>
        <div id="primitive_attr" class="panel panel-default">
            <div class="panel-heading">Profile data</div>
                <div  class="panel-body attr-container">
                    <c:forEach var="d" items="${profile_data}">
                        <div id="complex_box" class="panel panel-default">
                            <div class="panel-body">
                              <c:out value="${d.name}"/> 
                              <a id=chng_btn type="button" class="btn btn-danger" href="${pageContext.request.contextPath}/profiledata/remove/${d.id}">Remove</button>
                              <a id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/profiledata/view/${d.id}">View</a>
                            </div>
                        </div>
                    </c:forEach>   
                    <a type="button" class="btn btn-success" href="${pageContext.request.contextPath}/profiledata/add">Collect new</a>
                </div>
        </div> 
        <%@include file="../basic/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    </body>
</html>
