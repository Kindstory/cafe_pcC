<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>예약 수정</title>
<script src="./JavaScript/main.js" defer></script>
<link href="./css/main.css" rel="stylesheet" type="text/css">
<link href="./css/reservation/reservationUpdateForm.css" rel="stylesheet" type="text/css">
<script src="https://kit.fontawesome.com/1e92182c7c.js" crossorigin="anonymous"></script>
</head>
</head>
<body>
<!-- 헤더들어가는 곳 -->
<jsp:include page="../inc/top.jsp" />
<!-- 헤더들어가는 곳 -->
	<h1>reservationUpdateForm.jsp</h1>
	
	<div>
		<form action="./ReservationUpdateAction.re?res_num=${dto.res_num }" method="post">
		<fieldset>
		<legend>예약 수정하기</legend>
			<div>
				예약번호 <input type="text" name="res_num" value="${dto.res_num }"> <br>
			</div>
			<div>
				예약 인원 <input type="number" name="res_persons" min="1" max="8"
					placeholder="숫자만 입력하세요." value="${dto.res_persons }">명
					<div>최대 예약 인원은 8명입니다.</div>
			</div>
			<div>
				예약날짜 <input type="date" name="res_date" id="res_date" value="${dto.res_date }">
			</div>
				<script>
					let dateElement = document.getElementById('res_date');
			        let mindate = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);
			        dateElement.value = mindate;
			        dateElement.setAttribute("min", mindate);
			        
			        function setMinValue() {
			            if(dateElement.value < mindate) {
			                alert('현재 시간보다 이전의 날짜는 설정할 수 없습니다.');
			                dateElement.value = mindate;
			                dateElement.setAttribute("min", mindate);
			            }
			        }
			        
			        let dateElement2 = document.getElementById('res_date');
			        let maxdate = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000*20).toISOString().slice(0, 10);
			        dateElement2.value = maxdate;
			        dateElement2.setAttribute("max", maxdate);
			        
			        function setMaxValue() {
			            if(dateElement2.value > maxdate) {
			                alert('오늘로부터 7일 뒤까지만 선택 가능합니다.');
			                dateElement2.value = maxdate;
			                dateElement2.setAttribute("max", maxdate);
			            }
			        }
				</script>
			<div>
				예약시간 <input type="number" min="10" max="17" name="res_time" value="${dto.res_time }">시
				<div>예약 가능 시간은 오전 10시부터 오후 5시까지입니다.</div>
			</div>
	</fieldset>
			<input type="submit" value="수정하기">
		</form>
	</div>
	
	
	
		
<!-- 푸터들어가는 곳 -->
<jsp:include page="../inc/bottom.jsp" />
<!-- 푸터들어가는 곳 -->
</body>
</html>