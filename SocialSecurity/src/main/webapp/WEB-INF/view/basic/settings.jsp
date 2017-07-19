<%-- 
    Document   : welcomepage
    Created on : 9 juin 2017, 11:55:15
    Author     : adychka
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/settings.css" rel="stylesheet">
    </head>
    <body >
        <%@include file="header.jsp" %>
        <div class="s_panel" >
            <nav class="col-sm-3 s_menu" id="myScrollspy">
                <ul class="nav nav-pills nav-stacked">
                    <li class="active"><a href="#sec_attr">Primitive attributes</a></li>
                    <li><a href="#sec_crawl">Crawling settings</a></li>
                    <li><a href="#sec_crawl_acc">Crawling accounts</a></li>
                    
                </ul>
            </nav>
            <div  class="s_content"  data-spy="scroll" data-target="#myScrollspy">
                <div id="sec_attr" class='s_section panel panel-default'>
                    <div class="panel panel-heading">Primitive attributes</div>
                    <div class="panel panel-body">
                        
                    </div> 
                </div>
                <div id="sec_crawl" class='s_section panel panel-default'>
                    <div class="panel panel-heading">Crawling settings </div>
                    <div class="panel panel-body">
                        <form action="${pageContext.request.contextPath}/settings/crawling" method="POST" class="input_fields_wrap">
                            <input id ='btn_save' type="submit" class="btn btn-info" value="Update">
                            <br>
                            <label for="longWaitMillis" data-toggle="tooltip" data-placement="top" title="The upper border for the long delay while crawling attribute">Long wait in millis:</label>
                            <input type="number" min="1" max="10000000" name='longWaitMillis' id="longWaitMillis" class="form-control" value="${crawling_settings.longWaitMillis}"></input>
                            <br>
                            <label for="shortWaitMillis" data-toggle="tooltip" data-placement="top" title="The upper border for the short delay while crawling attribute">Short wait in millis:</label>
                            <input type="number" min="1" max="100000" name='shortWaitMillis' id="shortWaitMillis" class="form-control" value="${crawling_settings.shortWaitMillis}"></input>
                            <br>
                            <label for="waitForElemLoadSec" data-toggle="tooltip" data-placement="top" title="How much seconds should selenium retry if element is not appearing in HTML ">Wait for element load in seconds:</label>
                            <input type="number" min="1" max="1000" name='waitForElemLoadSec' id="waitForElemLoadSec" class="form-control" value="${crawling_settings.waitForElemLoadSec}"></input>
                            <br>
                            <label for="requestDelay" data-toggle="tooltip" data-placement="top" title="The upper border for the delay between crawling the attributes needed to simulate human behavior">Request delay:</label>
                            <input type="number" min="1" max="10000000" name='requestDelay' id="requestDelay" class="form-control" value="${crawling_settings.requestDelay}"></input>
                            <br>
                            <label for="changeAccountAfter" data-toggle="tooltip" data-placement="top" title="Change login account after this number of crawlings">Change account after:</label>
                            <input type="number" min="1" max="10000000" name='changeAccountAfter' id="changeAccountAfter" class="form-control" value="${crawling_settings.changeAccountAfter}"></input>
                            <br>
                            <label for="delayBeforeRunInMillis" data-toggle="tooltip" data-placement="top" title="Explicit wait before start crawling">Delay before run in millis:</label>
                            <input type="number" min="1" max="10000000" name='delayBeforeRunInMillis' id="delayBeforeRunInMillis" class="form-control" value="${crawling_settings.delayBeforeRunInMillis}"></input>
                            <br>
                            <label for="maxFriendsToCollect" data-toggle="tooltip" data-placement="top" title="Maximum friends to collect from account">Max friends to collect:</label>
                            <input type="number" min="1" max="10000" name='maxFriendsToCollect' id="maxFriendsToCollect" class="form-control" value="${crawling_settings.maxFriendsToCollect}"></input>
                            <br>
                            <label for="webDriverOption" data-toggle="tooltip" data-placement="top" title="Web driver which will be used while crawling">Web driver option:</label>
                            <select id ="webDriverOption" name="webDriverOption" class="form-control">
                               <c:forEach var="d" items="${web_driver_options}">
                                   <option value="${d}" ${d == crawling_settings.webDriverOption?'selected="selected"':''}>${d}</option>
                               </c:forEach> 
                           </select>
                        </form>
                    </div>
                </div>
                <div id="sec_crawl_acc" class='s_section panel panel-default'>
                    <div class="panel panel-heading">Facebook login accounts</div>
                    <div class="panel panel-body">
                        <c:forEach var="a" items="${accounts}">
                            <div class="panel panel-default">
                                <div id=hover_box class="panel-body">
                                    <c:out value="${a.login}"/> 
                                    <a id=chng_btn type="button" class="btn btn-danger" href="${pageContext.request.contextPath}/snaccount/delete/${a.id}">Remove</button>
                                    <a id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/snaccount/page_update/${a.id}">View/Update</a>
                                </div>
                            </div>
                        </c:forEach>   
                    </div>
                    <a type="button" class="btn btn-success" href="${pageContext.request.contextPath}/snaccount/page_add">Add new</a>
                </div> 
            </div>        
        </div>
        <%@include file="footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/attribute.js"></script>
    </body>
</html>
