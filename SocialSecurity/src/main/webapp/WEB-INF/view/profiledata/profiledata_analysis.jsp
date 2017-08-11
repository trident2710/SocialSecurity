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
        <title>Analysis results</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/list_all.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/profiledata.css" rel="stylesheet">
        <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">
    </head>
    <body class="container-fullwidth" >
        <script>var ctx = "${pageContext.request.contextPath}"</script>
        <div style="margin:20px"> 
            <h3>Analysis results for harmtrees:</h3>
            <table id='analysis_res' class="table table-bordered table-hover" data-toggle="table">
                <thead>
                    <tr>
                        <th data-sortable="true">Harm tree name</th>
                        <th data-sortable="true">Severity</th>
                        <th>Likelihood</th>
                        <th>Score</th>
                        <th data-sortable="true">Best case</th>
                        <th data-sortable="true">Worst case</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="a" items="${analysis_data}">
                        <c:if test = "${a.isValid == true}">
                            <tr>
                                <td>${a.harmTreeName}</td>
                                <td>${a.severity}</td>
                                <td>${a.likelihood}</td>
                                <td>${a.score}</td>
                                <td>${a.bestCase}</td>
                                <td>${a.worstCase}</td>
                            </tr>
                        </c:if>
                    </c:forEach>   
                </tbody>
            </table>
            
            <c:if test = "${hasWrong == true}">
                <h3>Unable to calculate for following harmtrees:</h3>
                <table id='analysis_res_false' class="table table-bordered table-hover">
                    <tr>
                        <th>Harm tree name</th>
                        <th>Reason</th>
                    </tr>
                    <c:forEach var="a" items="${analysis_data}">
                        <c:if test = "${a.isValid == false}">
                            <tr>
                                <td>${a.harmTreeName}</td>
                                <td>${a.errMsg}</td>
                            </tr>
                        </c:if>

                    </c:forEach>      
                </table>
            </c:if>
                
            <h2>Calculation details</h2>
            <textarea style="width: 100%; height: 500px;" id="report" value=""></textarea>
            <c:forEach var="a" items="${analysis_data}">
                <c:if test = "${a.isValid == true}">
                    <c:forEach var="s" items="${a.report}">
                        <script>document.getElementById('report').value+='\n'+'${s}';</script>
                    </c:forEach>  
                </c:if>
            </c:forEach>   
            
            
        </div>
        
        
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>

        
    </body>
</html>
