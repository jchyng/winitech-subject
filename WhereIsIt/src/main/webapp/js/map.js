/* 지도 생성 */
var container = document.getElementById('map');
var options = {
	center: new kakao.maps.LatLng(35.865496405, 128.593444283),
	level: 3
};
var map = new kakao.maps.Map(container, options);


/* 마커와 인포윈도우 관리를 위한 전역 변수 */
var markers = [];
var infowindows = [];



/* 지도 위치 이동 */
function moveToLocation(lat, lng){
  var moveLatLon = new kakao.maps.LatLng(lat, lng);
  map.setCenter(moveLatLon);
}


/* 모든 데이터에 대해 마커 생성 */
function createMarkers(datas, contentType){
	for(var data of datas){
		createMarker(data, contentType);
	}
}

/* 마커 생성 */
function createMarker(data, contentType){
	//마커 생성
    var marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(data.latitude, data.longitude),
        clickable: true,	// 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정
    });
	//인포윈도우 생성
	var infowindow = createInfowindow(data, contentType);

    //마커에 클릭 이벤트 연결
    kakao.maps.event.addListener(marker, 'click', function() {
		  clearInfowindows();	// 모든 인포윈도우 삭제
	      infowindow.open(map, marker);  // 마커 위에 인포윈도우 표시
	});
    markers.push(marker);	//마커 관리를 위해 전역으로 선언된 배열에 추가
}

/* 전체 마커 삭제 */
function clearMarkers(){
	for(var marker of markers){
		marker.setMap(null);
	}
}


/* 인포 윈도우 생성 */
function createInfowindow(data, contentType){
	/* 데이터에 맞는 형식의 인포윈도우 태그 생성 (다른 타입 추가 예정)  */
	var infoWindowEl = '';
	if(contentType === 'toilet'){
		infoWindowEl = createInfoWindowElForToilet(data);
	}
	//인포윈도우 생성
	var infowindow = new kakao.maps.InfoWindow({
	    content : infoWindowEl, 
	    removable : true	//인포윈도우를 닫을 수 있는 x버튼
	});
	//인포위도우 관리를 위해 전역으로 선언된 배열에 추가
	infowindows.push(infowindow);	
	return infowindow;
}

/* 전체 인포윈도우 지우기 */
function clearInfowindows(){
	for(var infowindow of infowindows){
		infowindow.close();
	}
}

/* 화장실 인포 윈도우 태그 생성 */
function createInfoWindowElForToilet(data){
    return '<div class="info-window">' +
            '<h4>' + data.toiletName + '</h4>' +
            '<p>도로명 주소: ' + data.roadNameAddress + '</p>' +
            '<p>지번 주소: ' + data.lotNumberAddress + '</p>' +
            '<p>관리 기관: ' + data.managementAgency + '</p>' +
            '<p>전화번호: ' + data.telephone + '</p>' +
            '<p>개방 유형: ' + data.openingType + '</p>' +
            '<p>개방 시간: ' + data.openingHour + '</p>' +
            '</div>';
}

