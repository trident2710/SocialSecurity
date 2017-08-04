<%-- 
    Document   : welcomepage
    Created on : 9 juin 2017, 11:55:15
    Author     : adychka
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/welcomepage.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="header.jsp" %>
        <script>var ctx = "${pageContext.request.contextPath}"</script>
        <div class="container">
            <div class="jumbotron">
                <h1>Social Security</h1>
                <a id=btn_nav class="btn btn-lg btn-primary" href="${pageContext.request.contextPath}/attributes/all" role="button">Go to attributes</a>
                <br>
                <a id=btn_nav class="btn btn-lg btn-primary" href="${pageContext.request.contextPath}/harmtrees/all" role="button">Go to harm trees</a>
                <br>
                <a id=btn_nav class="btn btn-lg btn-primary" href="${pageContext.request.contextPath}/profiledata/all" role="button">Go to data analysis</a>
            </div>
        </div>
        <%@include file="footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    </body>
</html>
