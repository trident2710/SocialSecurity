<%-- 
    Document   : welcomepage
    Created on : 9 juin 2017, 11:55:15
    Author     : adychka
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/list_all.css" rel="stylesheet">
    
  </head>
  <body>
     
    <%@include file="../basic/header.jsp" %>

    <div id="container">
        <div id="complex_attr" class="panel panel-default">
                <div class="panel-heading">Primitive attributes</div>
                <div class="panel-body">
                    <c:forEach var="attr" items="${primitive_attributes}">
                        <div>
                            <c:out value="${attr.displayName}"/> 
                        </div>
                    </c:forEach>
                </div>
            </div> 
        <div id="primitive_attr" class="panel panel-default">
            <div class="panel-heading">Complex attributes</div>
                <div  class="panel-body">
                    <c:forEach var="attr" items="${complex_attributes}">
                                <div id="complex_box" class="panel panel-default">
                                    <div class="panel-body">
                                      <c:out value="${attr.displayName}"/> 
                                      <button id=chng_btn type="button" class="btn btn-danger" onclick="deleteEntity(${attr.id})">Remove</button>
                                      <a id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/attributes/update/${attr.id}">View/Update</a>
                                    </div>
                                </div>
                    </c:forEach>
                    <a type="button" class="btn btn-success" href="${pageContext.request.contextPath}/attributes/add?type=complex">Add new</a>
                </div>
        </div> 
    </div>
            

    <%@include file="../basic/footer.jsp" %>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/attribute.js"></script>
  </body>
</html>
