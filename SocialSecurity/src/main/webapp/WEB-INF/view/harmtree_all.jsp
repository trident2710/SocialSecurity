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
      <title>Harm trees</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/css/list_all.css" rel="stylesheet">
    
  </head>
  <body>
     
    <%@include file="header.jsp" %>

    <div id="container">
        <div id="primitive_attr" class="panel panel-default">
            <div class="panel-heading">Harm trees</div>
                <div  class="panel-body">
                    <c:forEach var="ht" items="${harm_trees}">
                                <div id="complex_box" class="panel panel-default">
                                    <div class="panel-body">
                                      <c:out value="${ht.name}"/> 
                                      <button id=chng_btn type="button" class="btn btn-danger" onclick="deleteEntity(${ht.id})">Remove</button>
                                      <a id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/harmtrees-update?id=${ht.id}">View/Update</a>
                                    </div>
                                </div>
                    </c:forEach>
                    <a type="button" class="btn btn-success" href="${pageContext.request.contextPath}/harmtrees-add">Add new</a>
                </div>
        </div> 
    </div>
            

    <%@include file="footer.jsp" %>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="resources/js/bootstrap.min.js"></script>
    <script src="resources/js/harmtree.js"></script>
    <script src="jquery-2.2.2.min.js"/>
  </body>
</html>
