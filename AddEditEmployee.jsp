<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="beans.Employee" %>
<%@ page import="beans.Department" %>
<%@ page import="dao.DepartmentDAO" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.ServletContext" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<c:choose>
    <c:when test="${empty emp}">
      <title>社員情報の新規追加</title>
    </c:when>
    <c:otherwise>
      <title>社員情報の編集</title>
    </c:otherwise>
  </c:choose>

</head>
<body>

  <c:choose>
    <c:when test="${empty emp}">
      <h1>社員情報を新規追加します</h1>
    </c:when>
    <c:otherwise>
      <h1>社員情報を編集します</h1>
    </c:otherwise>
  </c:choose>

  <form action="/FinalEmployee/AddEditControl" method="post" enctype="multipart/form-data" >
  <!-- 隠し社員ID送信 社員登録時に割り振るので新規登録時はなし-->
  <input type="hidden" name="hiddenID" value="${emp.id}">

  <!-- 隠し写真番号送信 -->
  <c:choose>
    <c:when test="${not empty emp.pictureID}">
      <input type="hidden" name="pictureID" value="${emp.pictureID}">
    </c:when>
    <c:otherwise>
      <input type="hidden" name="pictureID" value="${pictureID}">
    </c:otherwise>
  </c:choose>

  <!-- 社員番号表示 -->
   <label for="employeeID">社員番号:</label>
    <c:choose>
      <c:when test="${empty emp}">
        <input type="hidden" name="employeeID" value="${employeeID}">
        <c:out value="${employeeID}"/>
      </c:when>
      <c:otherwise>
        <c:out value="${emp.employeeID}"/>
        <input type="hidden" name="employeeID" value="${emp.employeeID}">
      </c:otherwise>
    </c:choose><br><br>
   <!-- 名前入力テキスト -->
   <label for="name">名前:</label>
   <input type="text" maxlength="40" name="name" id="name" value="${emp.name}">
   <br><br>
   <!-- 年齢入力テキスト -->
   <label for="age">年齢:</label>
   <input type="text" maxlength="3" size="3" name="age" id="age" value="${emp.age}">
   <br><br>

   <div>
   性別:
   <label for="gender_male" accesskey="m">男(M)</label>
   <c:choose>
    <c:when test="${emp.gender==1}">
      <input type="radio" name="gender" id="gender_male" value="1" checked>
    </c:when>
    <c:otherwise>
      <input type="radio" name="gender" id="gender_male" value="1">
    </c:otherwise>
  </c:choose>

  <label for="gender_female" accesskey="w">女(W)</label>
  <c:choose>
    <c:when test="${emp.gender==2}">
      <input type="radio" name="gender" id="gender_female" value="2" checked>
    </c:when>
    <c:otherwise>
      <input type="radio" name="gender" id="gender_female" value="2">
    </c:otherwise>
  </c:choose>
  </div>
  <br>

  <!-- 写真の名前 -->
  写真番号:<c:out value="${pictureName}"/><br>
  <input type="hidden" name="pictureName" value="${pictureName}">

  <!-- 画像の表示 -->
  <c:choose>
    <c:when test="${empty emp}">
    <%
      //新規登録時のデフォルト画像パスを取得
      ServletContext context=this.getServletContext();
      String picturePass=context.getRealPath("/WEB-INF/picture/humanPicture.jpg");
    %>
      <c:set var="picturePass" value="<%= picturePass %>"/>
      <div><img border="1" src="${picturePass}" width="256" height="256"></div>
    </c:when>
    <c:otherwise>
      <%
        //文字コード設定
        request.setCharacterEncoding("UTF-8");
        int picture=(Integer)request.getAttribute("pictureID");
      %>
      <div><img border="1" src=<%="PictureDisplay?id="+picture%> width="256" height="256"></div>
    </c:otherwise>
  </c:choose>
  <!-- 画像ファイル登録 -->
  <input type="file" name="pictureFile" accept="image/*"><br><br>

   <label for="zip">郵便番号:〒</label>
   <input type="text"name="zip" id="zip" value="${emp.zip}">
   <br><br>

   <!-- 都道府県 -->
   <label for="prefCity">都道府県:</label>
   <select name="prefCity" id="prefCity">
   <c:forEach var="PREF" items="${prefList}">
     <c:choose>
       <c:when test="${PREF==emp.pref}">
         <option value="${PREF}" Selected>
           <c:out value="${PREF}"/>
         </option>
       </c:when>
       <c:otherwise>
         <option value="${PREF}">
           <c:out value="${PREF}"/>
         </option>
       </c:otherwise>
     </c:choose>
   </c:forEach>
   </select><br><br>

   <!-- 住所 -->
   <label for="address">住所:</label>
   <input type="text" maxlength="100" name="address" id="address"value="${emp.address}">
   <br><br>

  <!-- 所属部署の選択 -->
  <label for="departmentID">所属部署:</label>
  <%
   //部署DAOを取得
   DepartmentDAO dd=new DepartmentDAO();
	//部署リストを取得
   List<Department>departmentList=dd.allDepartment();
	//未配属部署インスタンスを生成
	Department depart=new Department(0,"未配属");
	//リストの最初に未配属インスタンスを挿入
	departmentList.add(0,depart);
   %>
  <select name="departmentID" id="departmentID">
   <!-- 変数登録 -->
   <c:set var="postList" value="<%= departmentList %>"/>
   <!-- リストを取得 -->
   <c:forEach var="post" items="${postList}">
     <c:choose>
       <c:when test="${post.id==emp.postID}">
         <option value="${post.id}" Selected>
           <c:out value="${post.name}"/>
         </option>
       </c:when>
       <c:otherwise>
         <option value="${post.id}">
           <c:out value="${post.name}"/>
         </option>
       </c:otherwise>
     </c:choose>
   </c:forEach>
  </select><br><br>

   <label for="joinDate">入社日</label>
   <input type="date" name="joinDate" id="joinDate" value="${emp.joinDate}" placeholder="yyyy/dd/mm">
   <br><br>

   <label for="outDate">退社日</label>
   <input type="date" name="outDate" id="outDate" value="${emp.outDate}" placeholder="yyyy/dd/mm">
   <br><br>
   <input type="submit" value="登録">
  </form><br>

  <table border="1">
  <tr>
    <td>
      <!-- 社員リスト一覧へ -->
      <form action="/FinalEmployee/FirstAccess" method="get">
      <input type="submit" value="社員リスト一覧へ">
    </form>
    </td>
    <td>
      <!-- 部署リスト一覧へ -->
      <form action="/FinalEmployee/TopListControl" method="get">
        <input type="hidden" name="action" value="DepartList">
        <input type="submit" value="部署リスト一覧へ">
      </form>
    </td>
  </tr>
</table>
</body>
</html>