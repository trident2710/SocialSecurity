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
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/attribute_add.css" rel="stylesheet">
    </head>
    </head>
   <body>
        <%@include file="../basic/header.jsp" %>

        <div class='add_attr_form panel panel-default'>
            <div class="panel panel-heading">
                Create new primitive attribute
            </div>
            <div class="panel panel-body">
                <form action="${pageContext.request.contextPath}/attributes/form-post" method="POST" class="input_fields_wrap">
                    <input id ='btn_create' type="submit" class="btn btn-success" value="Create">
                    <input name="type" type="hidden" value="primitive"/>
                    <br>
                    <label for="name">Name:</label>
                    <input type="text" name='name' id="name" class="form-control"></input>
                    <label for="data_type">Data Type:</label>
                    <select id="data_type" name="data_type" class="form-control">
                        <c:forEach var="t" items="${data_types}">
                            <option value="${t.name}">${t.name}</option>
                        </c:forEach> 
                    </select>
                    <br>
                     <c:forEach var="ds" items="${default_data_sources}">
                         <label for="${ds.name}" data-toggle="tooltip" data-placement="top" title="Synonim is the name of the property in the file which contains the data from this source">
                             Synonim for ${ds.name}:</label>
                         <input type="text" name='${ds.name}' id="name" class="form-control"></input>
                         <br>
                    </c:forEach> 
                </form>
            </div>
            
        </div>
        <%@include file="../basic/footer.jsp" %>
       
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script> 
    </body>
</html>
