<%-- 
    Document   : attribute_specific
    Created on : 12 juin 2017, 14:35:30
    Author     : adychka
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/harmtree_update.css" rel="stylesheet">
    </head>
    </head>
    <body onload="init(${id});">
        <%@include file="../basic/header.jsp" %>
        
        <div id='workspace'>
             <div id="cy">  
                
            </div>
            <div id="menu" class='bg-inverse text-white'>
                <h3 class='title'> Actions:</h3>
                <p id='hint' class="text-info">Click the node to show the actions allowed for this element</p>
            </div> 
        </div>

        <%@include file="../basic/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/cytoscape.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/harmtree_update.js"></script>
    </body>

</html>
