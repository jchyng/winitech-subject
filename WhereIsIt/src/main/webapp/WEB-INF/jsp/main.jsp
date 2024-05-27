<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<meta charset="utf-8"/>
	<title>지도</title>
	<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
	<!-- todo: API Key 노출 -->
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f73ed4653665a3ffd7fbf379f71e8d07&libraries=services,clusterer,drawing"></script>\
	<style>
        .info-window {
            padding: 10px;
            font-size: 14px;
        }
        .info-window h4 {
            margin: 0;
            padding: 0;
            font-size: 16px;
            font-weight: bold;
        }
        .info-window p {
            margin: 5px 0;
        }
    </style>
</head>
<body>
	<div id="map" style="width:1000px; height:800px;"></div>
	
	<select id="requestType">
		<option id="toilet" value="/list/toilet.do">화장실</option>
		<option id="parking" value="/list/parking.do">주차장</option>
		<option id="atm" value="/list/atm.do">ATM</option>
	</select>
	
	<button onclick="requestByBounds()">조회</button>
	
	<script src="/js/map.js"></script>
	<script>
	/* 지도가 렌더링된 후 위치 정보를 제공할 경우 해당 위치로 지도를 이동한다. */
	window.onload = function() {
		/*
		 * 위치정보 api가 들어갈 자리
		 * https에서만 가능하기 때문에 위니텍 좌표를 고정으로 주입
		 */
		moveToLocation(35.83593, 128.5418679);
	}	
	
	/* 현재 지도의 노출 범위에 해당하는 데이터를 받아온다. */
	function requestByBounds(){
		var contentType = $("#requestType option:selected").attr('id');
		var url = $("#requestType option:selected").val();
		var bounds = map.getBounds();
		
		$.ajax({
	        url: url,
	        type: 'POST',
	        data: JSON.stringify(bounds),
	        dataType: 'json',
	        contentType: 'application/json',
	        success: function(datas) {
	        	//마커 삭제
	        	clearMarkers();
	        	//인포윈도우 삭제
	        	clearInfowindows();
	        	//마커 추가
        		createMarkers(datas, contentType);
	        },
	        error: function(xhr, status, error) {
	        	console.error('Error:', error);
	        }
	    });
	}
	</script>
</body>
</html>