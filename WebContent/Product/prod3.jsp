<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<title>Insert title here</title>
<style>
* {
	margin: auto;
}
</style>
</head>
<body>
	<form action=../insert.ca method=post id="frm">
		<table bordercolor=black border=1 cellspacing=1 >
			
			<input type=hidden name=seq value="3">	    <!-- 시퀀스 값? -->
			<tr align=center>
				<td colspan=2>
					<img src="${pageContext.request.contextPath}/Resource/img/event03.jpg" width=100 height=100>
				</td>
			</tr>
			<tr align=center>
				<td>품명</td>
				<td><input type=hidden name=prod_name value="향수3">향수3</td>
			</tr>
			<tr align=center>
				<td>가격</td>
				<td><input type=hidden name=price value="250">250</td>
			</tr>
			<tr align=center>
				<td>수량</td>
				<td><input type=text size=5 name=prod_quantity>개</td>
			</tr>
			<tr>
				<td colspan=2 align="center"><input type=button value="장바구니에 담기" id="btnCart">
			</tr>
		</table>
	</form>
	<script>      
    $("#btnCart").on("click",function(){
    	var cf = confirm("장바구니 상품이 정상적으로 담겼습니다.\n장바구니로 이동 하시겠습니까?");
        if(cf){
        	$("#frm").submit();
        }
        else{
        	 return false;
        }
    })
    </script>
</body>
</html>