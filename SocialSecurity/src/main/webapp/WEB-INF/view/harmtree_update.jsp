<%-- 
    Document   : attribute_specific
    Created on : 12 juin 2017, 14:35:30
    Author     : adychka
--%>

<%@page import="inria.socialsecurity.attribute.Attribute"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/hu.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="resources/js/bootstrap.min.js"></script>
        <script src="resources/js/cytoscape.js"></script>
        <script src="resources/js/jquery-3.2.1.min.js"></script>
        <script src="resources/js/harmtree_update.js"></script>
        
       
    </head>
    </head>
    <body onload="init(${id});">
        <%@include file="header.jsp" %>
        

        <div id='workspace'>
             <div id="cy">  
                
            </div>
            <div id="menu" class='jumbotron'>
                <h3> Actions:</h3>
            </div> 
        </div>
             
           

        
        <%@include file="footer.jsp" %>
    </body>
</html>
