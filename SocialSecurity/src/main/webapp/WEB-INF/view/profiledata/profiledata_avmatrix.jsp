<%-- 
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
        <link href="${pageContext.request.contextPath}/resources/css/profiledata.css" rel="stylesheet">
    </head>
    <body class="container-fullwidth">
        <script>var ctx = "${pageContext.request.contextPath}"</script>
        <div style="margin:20px">
            <c:if test = "${self != null}">
                <h2>Attributes visibility of target:</h2>
                <table class="table table-bordered table-hover">
                    <tr>
                    <c:forEach var="attr" items="${self.entrySet()}">
                        <th>${attr.getKey()}</th>
                    </c:forEach>   
                    </tr>

                    <tr>
                    <c:forEach var="attr" items="${self.entrySet()}">
                        <td>${attr.getValue().getAsString()}</td>
                    </c:forEach>  
                    </tr>   
                </table>
            </c:if>
            <c:if test = "${avmatrix.isEmpty() == false}">
                <h2>Attributes visibility matrix:</h2>
                <table class="table table-bordered table-hover">
                    <tr>
                        <th>Facebook Profile</th>
                    <c:forEach var="attr" items="${avmatrix.entrySet().iterator().next().getValue().keySet()}">
                        <th>${attr}</th>
                    </c:forEach>   
                    </tr>

                    <c:forEach var="item" items="${avmatrix}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/profiledata/fbprofile?pdid=${pdid}&id=${item.key}">${item.key}</a></td>
                        <c:forEach var="attrs" items="${item.value}">
                            <td>${attrs.value}</td>
                        </c:forEach>  
                        </tr>
                    </c:forEach>      
                </table>
            </c:if>
            
           
        </div>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        
    </body>
</html>
