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
        <link href="${pageContext.request.contextPath}/resources/css/attribute_add.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="../basic/header.jsp" %>
        <div class='add_attr_form panel panel-default'>
            <div class="panel panel-heading">
                Collect new facebook dataset 
            </div>
            <div class="panel panel-body">
                <form action="${pageContext.request.contextPath}/profiledata/add" method="POST" class="input_fields_wrap" onsubmit="alert('Collecting the data started. It might take some time, up to couple of HOURS')">
                    <input id ='btn_create' type="submit" class="btn btn-success" value="Start collecting">
                    <br>
                    <label for="name" data-toggle="tooltip" data-placement="top" title="Label which will be used to identify collected data. For example, 'Analize of John Smith'">Name:</label>
                    <input type="text" name='name' id="name" class="form-control"></input>
                    <br>
                    <label for="fb_url" data-toggle="tooltip" data-placement="top" title="URL in form: https://www.facebook.com/name or https://www.facebook.com/profile.php?id=12345">Facebook profile url:</label>
                    <input type="text" name='fb_url' id="fb_url" class="form-control"></input>
                    <br>
                    <label for="depth" data-toggle="tooltip" data-placement="top" title="Crawling depth i.e. how deep crawler will go to friendship chain. !IMPORTANT! consider exponential time increase, and check number of friends capture in settings'">Depth:</label>
                    <input type="number" min="1" max="10" name='depth' id="depth" class="form-control"/>
                    <br>
                    <label for="acc_friend" data-toggle="tooltip" data-placement="top" title="For being able to collect data from 'friend' point of view, it is needed have one of the accounts in friends with the specified target">Crawling account in friends:</label>
                    <select id ="acc_friend" name="acc_friend" class="form-control">
                        <option value='-1' selected='selected'>none</option>
                        <c:forEach var="a" items="${accounts}">
                            <option value="${a.id}">${a.login}</option>
                        </c:forEach> 
                    </select>
                </form>
            </div>
        </div>
        
        <%@include file="../basic/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        
            
    </body>
</html>
