<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="beans.Employee" %>
<%@ page import="beans.Department" %>
<%@ page import="dao.EmployeeDAO" %>
<%@ page import="dao.DepartmentDAO" %>
<%@ page import="java.util.List,java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>社員情報一覧</title>
</head>
<body>
<%

%>
<!-- 見出し -->
<h1>社員情報リスト</h1>
<!-- 社員情報の表示 -->

  <!-- 検索条件がある場合 -->
  <c:if test="${not empty Employee}">
  *検索条件は、<br>
  <c:if test="${not empty Employee.name}">
    社員の名前の一部:<c:out value="${Employee.name}"/><br>
  </c:if>
  <c:if test="${not empty Employee.employeeID}">
    社員番号:<c:out value="${Employee.employeeID}"/><br>
  </c:if>
  <c:if test="${not empty departName}">
    所属部署:<c:out value="${departName}"/>
  </c:if>
  <!-- 検索条件の削除ボタン -->
  <form action="/FinalEmployee/TopListControl" method="get">
    <input type="hidden" name="action" value="removeSession">
  <input type="submit" value="検索条件を削除">
  </form>
  </c:if>

  <!-- 社員リストが存在した場合 -->
  <c:choose>
    <c:when test="${not empty empList}">
      <table border="1" class="list">
        <tr>
            <th style="background:steelblue;">社員ID</th>
            <th style="background:steelblue;">名前</th>
            <th style="background:steelblue;">年齢</th>
        </tr>
        <c:forEach var="employee" items="${empList}">
          <tr>
            <td><c:out value="${employee.employeeID}"/></td>
            <td><c:out value="${employee.name}"/></td>
            <td><c:out value="${employee.age}"/>歳</td>
            <td>
                <form method="get" action="/FinalEmployee/AddEditControl">
                    <input type="hidden" name="hiddenID" value="${employee.id}">
                    <input type="submit" value="編集">
                </form>
            </td>
                 <td>
                    <form method="post" action="/FinalEmployee/TopListControl">
                      <input type="hidden" name="employeeID" value="${employee.id}">
                      <input type="hidden" name="pictureID" value="${employee.pictureID}">
                      <input type="submit" value="削除">
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </table>
        </c:when>
      <c:otherwise>
      登録されている社員がいません
    </c:otherwise>
 </c:choose>

<!-- 新規追加ボタン -->
<form action="/FinalEmployee/AddEditControl" method="get">
  <input type="submit" value="新規追加">
</form>
<!-- 検索用のボタン -->
<form action="/FinalEmployee/SearchControl" method="get">
  <input type="submit" value="検索">
</form>
<!-- 部署編集ページに移行 -->
<form action="/FinalEmployee/TopListControl">
  <input type="hidden" name="action" value="DepartList">
  <input type="submit" value="部署リスト一覧へ">
</form>
</body>
</html>