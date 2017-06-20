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
        <link href="${pageContext.request.contextPath}/resources/css/attribute_add.css" rel="stylesheet">
    </head>
    </head>
   <body>
        <%@include file="../basic/header.jsp" %>

        <div class='add_attr_form'>
            <form action="${pageContext.request.contextPath}/attributes/form-post" method="POST" class="input_fields_wrap">
                <input id ='btn_create' type="submit" class="btn btn-success" value="Update"/>
                <input name="id" type="hidden" value="${attribute.id}"/>
                <br>
                <label for="name">Name:</label>
                <input type="text" name='name' id="name" class="form-control" value="${attribute.displayName}"></input>
                <br>
                <label>Primitive attributes:</label>
                <br>
                <c:set var="count" value="0" scope="page" />
                <c:forEach var="p_attr" items="${attribute.primitiveAttributes}">
                    <div class='removable_attr'>
                        <select id ='selectPrimitive' name="primitiveAttribut${count}" class="form-control">
                        <c:forEach var="attr" items="${primitive_attributes}">
                            <option value="${attr.id}" ${attr.id == p_attr.id?'selected="selected"':''}>${attr.displayName}</option>
                        </c:forEach> 
                        </select>
                        <c:if test="${count>0}">
                            <a href='#' class='remove_field'>Remove</a>
                        </c:if>                       
                    </div>
                        
                    <c:set var="count" value="${count + 1}" scope="page"/>    
                </c:forEach>
            </form>
            <button class="btn add_field_button">Add Primitive attribute</button>
        </div>
        <%@include file="../basic/footer.jsp" %>
       
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/attribute.js"></script>
        <script>
            $x = parseInt("${fn:length(attribute.primitiveAttributes)}",10);
        </script>
        
    </body>
</html>
