<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Enquiry Write | Jo Malone</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-bs4.css" rel="stylesheet">
<script	src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-bs4.js"></script>
<script src="../Resource/lang/summernote-ko-KR.js"></script>
<style>
	* {box-sizing: border-box;}
	#titleBox {margin-top: 5px; margin-bottom: 5px;}
	input[type=text], textarea {margin-top: 3px; margin-bottom: 3px; font-size: 15px;}
	#write-page {margin-top: 80px; height: 900px;}
	#write-title {margin-bottom: 35px; padding: 0px; height: 50px;}
	#write-title>h4 {width: 100%; line-height: 45px; text-align: center;}
	
	input[type=button] {width: 100px; height: 30px; border: 0px; background-color: lightgray; font-size: 13px;}
</style>
</head>
<body>
	<form action="../write.ask" method="post" id="writeFrm">
		<div id="write-page" class=container>
			<div id="write-title" class="row">
				<h4>1:1 ENQUIRY WRITE</h4>
			</div>
			<div class="row" id=askMenuBox>
				<div class="col-12 p-0">
					<select name="askMenu" id="askMenu">
						<option value="a0" disabled selected>문의유형</option>
						<option value="a1">배송</option>
						<option value="a2">주문</option>
						<option value="a3">반품/교환</option>
						<option value="a4">회원정보</option>
						<option value="a5">상품정보</option>
						<option value="a6">결제</option>
						<option value="a7">사이트장애</option>
						<option value="a8">매장문의</option>
						<option value="a9">기타</option>
					</select>
				</div>
			</div>

			<div class="row" id=titleBox style="height: 30px;">
				<input type=text placeholder="제목을 입력해주세요." id="askTitle" name="askTitle" style="margin: 0px; padding: 0px 0px 0px 7px; width: 100%; height: 30px; text-align: left;">
			</div>
			<div class="row" id=contentsBox>
				<div class="col-12 p-0">
					<textarea class="summernote" style="width: 100%;" id="contents"
						name="contents"></textarea>
				</div>
			</div>
			<div class="row" id=emailAlarmBox>
				<div class="col-12 p-0">
					<br> <input type=checkbox id=emailCheck name=emailCheck>
					회원정보에 등록된 메일로 답변 알림을 받겠습니다.
				</div>
			</div>
			<hr>
			<div class="row" id=btnBox>
				<div class="col-12 p-0" style="text-align: center;">
					<input type="button" value="Cancle" id="cancel">
					<input type="button" value="Write" id="toWrite">
				</div>
			</div>
		</div>
	</form>
	
	<script>
		$("#cancel").on("click", function() {
			var check = confirm("정말 취소하시겠습니까?");
			if(check){
				location.href = "/JoMalone/list.ask";//1대1문의 리스트페이지				
			}
		});
		
		$("#toWrite").on("click", function() {
			console.log($("#askMenu").val());
			
	
			if($("#askMenu").val() == null){
				alert("문의유형을 선택해주세요.");
				return false;
			}
			
			if ($("#askTitle").val() == "") {
				alert("제목을 입력해주세요.");
				return false;
			}
			if ($("#contents").val() == "") {
				alert("내용을 입력해주세요.");
				return false;
			}
			document.getElementById("writeFrm").submit();
			
		});

		(function($) {
			$(".summernote").summernote({
				lang : 'ko-KR',
				minHeight: 500,
				maxHeight: 500,    
				placeholder : "문의 내용을 입력해주세요. (사진 업로드 크기는 10MB까지만 가능합니다.)",
				callbacks : {
					onImageUpload : function(files) {
						console.log(files);
						var data = new FormData();
						for (var i = 0; i < files.length; i++) {
							data.append("img" + i, files[i]);
						}
						$.ajax({
							url : "../summernote.ask",
							enctype : "multipart/form-data",
							type : "post",
							data : data,
							contentType : false,
							processData : false,
							cache : false,
							dataType : "json"
						}).done(function(resp) {
							console.log(resp);
							console.log(resp.length);
							for (var i = 0; i < resp.length; i++) {
								var imgBox = document.createElement("p");
								var imgTag = document.createElement("img");
								$(imgTag).attr("src", resp[i].url);
								$(imgTag).attr("style", "max-width: 80%; height: auto;");
								imgBox.append(imgTag);
								$(".summernote").summernote('insertNode', imgBox);
							}
							var pTag = "<p><br></p>";
							$(".summernote").summernote('pasteHTML', pTag);
							$('.summernote').summernote('focus');
						}).fail(function(resp) {

						})
					}
				}
			});
		})(jQuery);
	</script>
</body>
</html>