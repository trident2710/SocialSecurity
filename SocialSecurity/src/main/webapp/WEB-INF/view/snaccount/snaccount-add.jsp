<%-- 
    Document   : attribute_specific
    Created on : 12 juin 2017, 14:35:30
    Author     : adychka
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
        <script>var ctx = "${pageContext.request.contextPath}"</script>
        <div class='add_attr_form panel panel-default'>
            <div class="panel panel-heading">
                Create new facebook login account
            </div>
            <div class="panel panel-body">
                <form:form class="form-horizontal input_fields_wrap" method="post" modelAttribute="account" action="${pageContext.request.contextPath}/snaccount/add">
                    <label>Login:</label>
                    <form:input path="login" class="form-control"/>
                    <br>
                    <label>Password:</label>
                    <form:input path="password" class="form-control"/>
                    <br>
                    <label>Friend account:</label>
                    <form:select path="friend" class="form-control">
                        <form:option value="-1" label="none" />
                        <form:options items="${accounts}" itemValue="id" itemLabel="login"/>
                    </form:select>
                    <button style='margin: 10px'class='btn btn-success' type='submit'>Create</button>
                </form:form>
            </div>
            
        </div>
        <%@include file="../basic/footer.jsp" %>
       
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/attribute.js"></script>    
    </body>
</html>
