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
        <script>var ctx = "${pageContext.request.contextPath}"</script>
        <div class='add_attr_form panel panel-default'>
            <div class="panel panel-heading">
                Collect new facebook dataset 
            </div>
            <div class="panel panel-body">
                <form action="${pageContext.request.contextPath}/profiledata/add" method="POST" class="input_fields_wrap" onsubmit="alert('Collecting the data started. It might take some time, up to couple of HOURS')">
                    <input id ='btn_create' type="submit" class="btn btn-success" value="Start collecting">
                    <br>
                    <label for="name" data-toggle="tooltip" data-placement="top" title="Label which will be used to identify collected data. For example, 'Analize of John Smith'">Name:</label>
                    <input type="text" name='name' id="name" class="form-control"/>
                    <br>
                    <input type="checkbox" name="col_ff" value="true"> Should collect friends of friends (needed for friendship graph but long)<br>
                    
                    <label for="fburl" data-toggle="tooltip" data-placement="top">Facebook page url:</label>
                    <input type="text" name='fburl' id="fburl" class="form-control"/>
                    <br>
                    <label for="t_login" data-toggle="tooltip" data-placement="top" >T profile login:</label>
                    <input type="text" name='t_login' id="t_login" class="form-control"/>
                    <br>
                    <label for="t_pass" data-toggle="tooltip" data-placement="top" >T profile password:</label>
                    <input type="password" name='t_pass' id="t_pass" class="form-control"/>
                    <br>
                    <label for="t_prime_login" data-toggle="tooltip" data-placement="top" >T' profile login:</label>
                    <input type="text" name='t_prime_login' id="t_prime_login" class="form-control"/>
                    <br>
                    <label for="t_prime_pass" data-toggle="tooltip" data-placement="top" >T' profile password:</label>
                    <input type="password" name='t_prime_pass' id="t_prime_pass" class="form-control"/>
                    <br>
                    <label for="t_sec_login" data-toggle="tooltip" data-placement="top" >T'' profile login:</label>
                    <input type="text" name='t_sec_login' id="t_sec_login" class="form-control"/>
                    <br>
                    <label for="t_sec_pass" data-toggle="tooltip" data-placement="top" >T'' profile password:</label>
                    <input type="password" name='t_sec_pass' id="t_sec_pass" class="form-control"/>
                    
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
