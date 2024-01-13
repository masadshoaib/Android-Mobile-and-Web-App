<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%= request.getAttribute("doctype") %>
<%--Muhammad Asad Shoaib | mshoaib | Project 4 Task 2--%>
<%--This is the final tab where all results are tabulated--%>
<%--https://docs.oracle.com/cd/E11035_01/workshop102/webapplications/workshopJSP/tutorialJSP/TutorialJSPEditPage.html#AddTable--%>
<%--https://www.w3schools.com/css/css_table_style.asp--%>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<h1 style="text-align:center"><%= "Distributed Systems: Dashboard" %></h1>
<h2 style="text-align:center"><%= "Operational Analytics" %></h2>
<table>
    <tr>
        <th>Metric</th>
        <th>Value(s)</th>
    </tr>
    <tr>
        <td>Total times happiness generated</td>
        <td><%= request.getAttribute("count")%></td>
    </tr>
    <tr>
        <td>Top 2 search terms</td>
        <td><%= request.getAttribute("topTerm")%> </td>
    </tr>
    <tr>
        <td>Average Latency for Search</td>
        <td><%= request.getAttribute("avgTime")%> ms</td>
    </tr>
</table>
<br>
<style>
    table {
        border-collapse: collapse;
        width: 100%;
        text-align: center;
    }

    th, td {
        padding: 8px;
    }

    th {
        background-color: #dddddd;
    }
</style>
<h2 style="text-align:center"><%= "Database Logs" %></h2>

<table>
    <thead>
    <tr>
        <th>Date/time</th>
        <th>Search</th>
        <th>Response From API</th>
        <th>Device</th>
        <th>Time Taken (ms)</th>
        <th>Sent to Phone</th>
    </tr>
    </thead>
    <tbody>
    <% String[] logs = request.getAttribute("log").toString().split("\\|"); %>
    <% for (String l : logs) { %>
    <tr>
        <% String[] cols = l.split("----"); %>
        <% for (String c : cols) { %>
        <td><%= c %></td>
        <% } %>
    </tr>
    <% } %>
    </tbody>
</table>

</body>
</html>
