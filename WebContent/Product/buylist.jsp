<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Buy List | Jo Malone</title>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="/JoMalone/Resource/css/buylist.css">
<style>
	*{font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";}
	#writereview{background-color:black; color:white;}
	#writeA:visited, #writeA:link{color:white;}
	#cjInfo:hover{color:red;}
	#writereview:hover{color:red;}
	#styleA:visited, #styleA:link{color:black;}
	.date {margin: 10px 0px 0px 0px; width: 50px; border: 0px; boakground-color: #F6F6F6; font-size: 13px; text-align: center;}
	#first {border-radius: 5px 0px 0px 5px;}
	#last {border-radius: 0px 5px 5px 0px;}
</style>

</head>
<body>
	<jsp:include page="/Resource/key/top.jsp" flush="false" />

	<div id="cart-page" class="container">
		<div id="cart-title" class="row">
			<h4>BUY LIST</h4>
		</div>
		<div id="cart-list" class="row">
			<ul class="nav nav-tabs" id="myTab" role="tablist">
				<li class="nav-item"><a class="nav-link active"
					id="korea-title" data-toggle="tab" href="#korea" role="tab"><h6>주문
							내역 조회</h6></a></li>
				<li class="nav-item"><a class="nav-link" id="country-title"
					data-toggle="tab" href="#country" role="tab"><h6>취소/반품 내역</h6></a></li>
				<li>
			  	<h6 style="width: 760px; line-height: 50px; text-align: right; font-size: 12px;">※ 운송장 번호를 클릭시 배송 조회가 가능합니다.</h6> 
			  </li>
			</ul>
			<input id="first" class="date" type="button" value="오늘">
			<input id="second" class="date" type="button" value="일주일">
			<input id="third"class="date" type="button" value="1개월">
			<input id="four"class="date" type="button" value="3개월">
			<input id="last" class="date" type="button" value="6개월">
			<div class="tab-content" id="myTabContent">
				<div class="tab-pane fade show active" id="korea" role="tabpanel">
					<table class="cart-table">
						<tr
							style="border-top: 1px solid lightgray; border-bottom: 1px solid lightgray;">
			  	  		<td style="width: 130px;">주문날짜
			  	  		<td style="width: 200px;">상품이미지
			  	  		<td style="width: 330px;">상품명
			  	  		<td style="width: 130px;">수량
			  	  		<td style="width: 130px;">가격
			  	  		<td>배송상태
						</tr>
						<c:choose>
							<c:when test="${fn:length(list) == 0}">
								<td colspan="7"
									style="height: 100px; border-bottom: 1px solid lightgray; text-align: center;">주문
									내역이 없습니다.
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${list}" var="dto">
									<tr class="my-item">
										<td style="width: 150px;">${dto.date}
										<td style="width: 130px;"><a href="#"><img class="item-img" src="/JoMalone/Resource/img/img.jpg"></a> <!-- 상품 이미지 href -->
										<td style="width: 150px;"><a href="#" id="styleA">${dto.prod_name}</a> <!-- 상품명 href -->
										<td style="width: 100px;">${dto.prod_quantity}
										<td style="width: 130px;">${dto.price }
										<td style="width: 160px;">CJ 대한통운 <br>
										
										<div id="cjInfo">[<a class="button view" href="https://www.cjlogistics.com/ko/tool/parcel/tracking?gnbInvcNo=626978391140" target="_blank" style="cursor: pointer;">626978391140</a>]</div>
										<button id=writereview><a href="write.review?seq=${dto.order_seq}&prodName=${dto.prod_name}" id="writeA">리뷰쓰기</a></button>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</table>
				</div>
				<div class="tab-pane fade" id="country" role="tabpanel">
					<table class="cart-table">
						<tr
							style="border-top: 1px solid lightgray; border-bottom: 1px solid lightgray;">
			  	  		<td style="width: 130px;">주문날짜
			  	  		<td style="width: 200px;">상품이미지
			  	  		<td style="width: 330px;">상품명
			  	  		<td style="width: 130px;">수량
			  	  		<td style="width: 130px;">가격
			  	  		<td>배송상태
						</tr>
						<tr>
							<td colspan="7"
								style="height: 100px; border-bottom: 1px solid lightgray; text-align: center;">취소/반품/교환
								내역이 없습니다.
						</tr>
					</table>
				</div>
			</div>
			<div id="page-navi" align="center">${pageNavi}</div>
		</div>
	</div>

	<jsp:include page="/Resource/key/bottom.jsp" flush="false" />
	<script>
		$("#first").on("click",function(){
			location.href= "search.buy?period=0"
		});
		$("#second").on("click",function(){
			location.href= "search.buy?period=7"
		});
		$("#third").on("click",function(){
			location.href= "search.buy?period=30"
		});
		$("#four").on("click",function(){
			location.href= "search.buy?period=90"
		});
		$("#last").on("click",function(){
			location.href= "search.buy?period=180"
		});
	</script>
</body>
</html>