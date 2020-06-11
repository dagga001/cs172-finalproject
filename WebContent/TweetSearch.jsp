<%@page import="cs172.SearchEngine"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cs172.SearchEngine.*" %>

 <html>
 <head>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <title>Search JSP</title>
 </head>
 <body>

<% String mystring = request.getParameter("query");
%>

<!-- Container element -->
<div class="parallax"></div>
<style type="text/css">
table.example3 {background-color:transparent;border-collapse:collapse;width:100%;}
table.example3 th, table.example3 td {text-align:center;border:1px blue;padding:5px;}
table.example3 th {background-color:Gray;}
table.example3 td:first-child {width:1000%;}
</style>
<table class="example3">
<tr>
<th colspan="2">Twitter Search Engine By: Devang Aggarwal, Usman Ahmed, Luis Chelala, Ulyanna T.</th>
</tr>
<tr>
<td width="1000%">
<form action="TweetSearch.jsp" method = "GET">
                                <input type="text" name = "query" placeholder="Search for tweet..." required>
                                <input type="submit" name="submit" value="Search">
</form>

<%String[] tweets;
if (mystring == null){}
else 
{
	tweets = SearchEngine.search(mystring, 10);

	for (int i = 0; i < tweets.length; i++) 
		{
		out.println(tweets[i] +"<br/><br/>");
		}
}
%>
</tr>
</table>

 </body>
 </html>
 
