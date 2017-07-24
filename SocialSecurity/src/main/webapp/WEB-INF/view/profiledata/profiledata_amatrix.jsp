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
        <div style="margin:20px">
            <c:if test = "${stranger_perspective.isEmpty() == false}">
                <h2>Attributes from stranger perspective:</h2>
                <table class="table table-bordered table-hover">
                    <tr>
                        <th>Facebook Profile</th>
                    <c:forEach var="attr" items="${stranger_perspective.entrySet().iterator().next().getValue().keySet()}">
                        <th>${attr}</th>
                    </c:forEach>   
                    </tr>

                    <c:forEach var="profile" items="${stranger_perspective}">
                        <tr>
                            <td>${profile.key}</td>
                        <c:forEach var="attrs" items="${profile.value}">
                            <td>${attrs.value}</td>
                        </c:forEach>  
                        </tr>
                    </c:forEach>      
                </table>
            </c:if>
            <c:if test = "${friend_perspective.isEmpty() == false}">
                <h2>Attributes from friend perspective:</h2>
                <table class="table table-bordered table-hover">
                    <tr>
                        <th>Facebook Profile</th>
                    <c:forEach var="attr" items="${friend_perspective.entrySet().iterator().next().getValue().keySet()}">
                        <th>${attr}</th>
                    </c:forEach>   
                    </tr>

                    <c:forEach var="profile" items="${friend_perspective}">
                        <tr>
                            <td>${profile.key}</td>
                        <c:forEach var="attrs" items="${profile.value}">
                            <td>${attrs.value}</td>
                        </c:forEach>  
                        </tr>
                    </c:forEach>      
                </table>
            </c:if>
            <c:if test = "${ffriend_perspective.isEmpty() == false}">
                <h2>Attributes from friend of friend perspective:</h2>
                <table class="table table-bordered table-hover">
                    <tr>
                        <th>Facebook Profile</th>
                    <c:forEach var="attr" items="${ffriend_perspective.entrySet().iterator().next().getValue().keySet()}">
                        <th>${attr}</th>
                    </c:forEach>   
                    </tr>

                    <c:forEach var="profile" items="${ffriend_perspective}">
                        <tr>
                            <td>${profile.key}</td>
                        <c:forEach var="attrs" items="${profile.value}">
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
