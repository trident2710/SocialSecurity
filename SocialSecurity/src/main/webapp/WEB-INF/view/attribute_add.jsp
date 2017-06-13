<%-- 
    Document   : attribute_specific
    Created on : 12 juin 2017, 14:35:30
    Author     : adychka
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/attribute_add.css" rel="stylesheet">
    </head>
    </head>
   <body>
        <%@include file="header.jsp" %>

        <div class='add_attr_form'>
            <form action="${pageContext.request.contextPath}/attributes-add" method="POST" class="input_fields_wrap">
                <input id ='btn_create' type="submit" class="btn btn-success" value="Create">
                <br>
                <label for="name">Name:</label>
                <input type="text" name='name' id="name" class="form-control"></input>
                <br>
                <label>Primitive attributes:</label>
                <br>
                <select id ='selectPrimitive' name="primitiveAttribut1" class="form-control">
                    <c:forEach var="attr" items="${prim_attrs}">
                        <option value="${attr.id}">${attr.displayName}</option>
                    </c:forEach> 
                </select>
            </form>
            <button class="btn add_field_button">Add Primitive attribute</button>
        </div>
        <%@include file="footer.jsp" %>
       
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="resources/js/bootstrap.min.js"></script>
        <script src="resources/js/attribute.js"></script>
        <script src="jquery-2.2.2.min.js"/>
        
    </body>
</html>
