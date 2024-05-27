package egovframework.mvc.domain;

import egovframework.mvc.domain.enums.OpeningType;
import egovframework.mvc.domain.enums.ToiletType;
import egovframework.mvc.domain.enums.WasteTreatmentType;

public class Toilet {
	private int id; // 번호
	private ToiletType toiletType; // 화장실 구분
	private String relatedLaw; // 법령
	private String toiletName; // 화장실 이름

	private String roadNameAddress; // 소재지 도로명 주소
	private String lotNumberAddress; // 소재지 지번 주소

	private int maleToiletCount; // 남성용 대변기 수
	private int maleUrinalCount; // 남성용 소변기 수
	private int maleDisabledToiletCount; // 남성 장애인용 대변기 수
	private int maleDisabledUrinalCount; // 남성 장애인용 소변기 수
	private int maleChildToiletCount; // 남성 어린이용 대변기 수
	private int maleChildUrinalCount; // 남성 어린이용 소변기 수
	private int femaleToiletCount; // 여성용 변기 수
	private int femaleDisabledToiletCount; // 여성용 장애인 변기 수
	private int femaleChildToiletCount; // 여성 어린이용 변기 수

	private String managementAgency; // 관리 기관
	private String telephone; // 전화번호
	private String owner; // 화장실 소유 기관

	private OpeningType openingType; // 개방 유형
	private String openingHour; // 개방 시간

	private String latitude; // 위도
	private String longitude; // 경도

	private WasteTreatmentType wasteTreatmentType; // 오물 처리 방식

	private char safetyFacilityTargetYn; // 안전관리시설 설치대상 여부
	private char cctvYn; // 입구 CCTV 설치 유무

	private char emergencyBellYn; // 비상벨 설치 여부
	private String emergencyBellLocation; // 비상벨 설치 장소

	private char diaperChangingStationYn; // 기저귀 교환대 유무
	private String diaperChangingStationLocation; // 기저귀교환대 장소

	private String installationDate; // 설치 연월
	private String remodelingDate; // 리모델링 연월
	private String dataReferenceDate; // 데이터 기준 일자

	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ToiletType getToiletType() {
		return toiletType;
	}

	public void setToiletType(ToiletType toiletType) {
		this.toiletType = toiletType;
	}

	public String getRelatedLaw() {
		return relatedLaw;
	}

	public void setRelatedLaw(String relatedLaw) {
		this.relatedLaw = relatedLaw;
	}

	public String getToiletName() {
		return toiletName;
	}

	public void setToiletName(String toiletName) {
		this.toiletName = toiletName;
	}

	public String getRoadNameAddress() {
		return roadNameAddress;
	}

	public void setRoadNameAddress(String roadNameAddress) {
		this.roadNameAddress = roadNameAddress;
	}

	public String getLotNumberAddress() {
		return lotNumberAddress;
	}

	public void setLotNumberAddress(String lotNumberAddress) {
		this.lotNumberAddress = lotNumberAddress;
	}

	public int getMaleToiletCount() {
		return maleToiletCount;
	}

	public void setMaleToiletCount(int maleToiletCount) {
		this.maleToiletCount = maleToiletCount;
	}

	public int getMaleUrinalCount() {
		return maleUrinalCount;
	}

	public void setMaleUrinalCount(int maleUrinalCount) {
		this.maleUrinalCount = maleUrinalCount;
	}

	public int getMaleDisabledToiletCount() {
		return maleDisabledToiletCount;
	}

	public void setMaleDisabledToiletCount(int maleDisabledToiletCount) {
		this.maleDisabledToiletCount = maleDisabledToiletCount;
	}

	public int getMaleDisabledUrinalCount() {
		return maleDisabledUrinalCount;
	}

	public void setMaleDisabledUrinalCount(int maleDisabledUrinalCount) {
		this.maleDisabledUrinalCount = maleDisabledUrinalCount;
	}

	public int getMaleChildToiletCount() {
		return maleChildToiletCount;
	}

	public void setMaleChildToiletCount(int maleChildToiletCount) {
		this.maleChildToiletCount = maleChildToiletCount;
	}

	public int getMaleChildUrinalCount() {
		return maleChildUrinalCount;
	}

	public void setMaleChildUrinalCount(int maleChildUrinalCount) {
		this.maleChildUrinalCount = maleChildUrinalCount;
	}

	public int getFemaleToiletCount() {
		return femaleToiletCount;
	}

	public void setFemaleToiletCount(int femaleToiletCount) {
		this.femaleToiletCount = femaleToiletCount;
	}

	public int getFemaleDisabledToiletCount() {
		return femaleDisabledToiletCount;
	}

	public void setFemaleDisabledToiletCount(int femaleDisabledToiletCount) {
		this.femaleDisabledToiletCount = femaleDisabledToiletCount;
	}

	public int getFemaleChildToiletCount() {
		return femaleChildToiletCount;
	}

	public void setFemaleChildToiletCount(int femaleChildToiletCount) {
		this.femaleChildToiletCount = femaleChildToiletCount;
	}

	public String getManagementAgency() {
		return managementAgency;
	}

	public void setManagementAgency(String managementAgency) {
		this.managementAgency = managementAgency;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public OpeningType getOpeningType() {
		return openingType;
	}

	public void setOpeningType(OpeningType openingType) {
		this.openingType = openingType;
	}

	public String getOpeningHour() {
		return openingHour;
	}

	public void setOpeningHour(String openingHour) {
		this.openingHour = openingHour;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public WasteTreatmentType getWasteTreatmentType() {
		return wasteTreatmentType;
	}

	public void setWasteTreatmentType(WasteTreatmentType wasteTreatmentType) {
		this.wasteTreatmentType = wasteTreatmentType;
	}

	public char getSafetyFacilityTargetYn() {
		return safetyFacilityTargetYn;
	}

	public void setSafetyFacilityTargetYn(char safetyFacilityTargetYn) {
		this.safetyFacilityTargetYn = safetyFacilityTargetYn;
	}

	public char getCctvYn() {
		return cctvYn;
	}

	public void setCctvYn(char cctvYn) {
		this.cctvYn = cctvYn;
	}

	public char getEmergencyBellYn() {
		return emergencyBellYn;
	}

	public void setEmergencyBellYn(char emergencyBellYn) {
		this.emergencyBellYn = emergencyBellYn;
	}

	public String getEmergencyBellLocation() {
		return emergencyBellLocation;
	}

	public void setEmergencyBellLocation(String emergencyBellLocation) {
		this.emergencyBellLocation = emergencyBellLocation;
	}

	public char getDiaperChangingStationYn() {
		return diaperChangingStationYn;
	}

	public void setDiaperChangingStationYn(char diaperChangingStationYn) {
		this.diaperChangingStationYn = diaperChangingStationYn;
	}

	public String getDiaperChangingStationLocation() {
		return diaperChangingStationLocation;
	}

	public void setDiaperChangingStationLocation(String diaperChangingStationLocation) {
		this.diaperChangingStationLocation = diaperChangingStationLocation;
	}

	public String getInstallationDate() {
		return installationDate;
	}

	public void setInstallationDate(String installationDate) {
		this.installationDate = installationDate;
	}

	public String getRemodelingDate() {
		return remodelingDate;
	}

	public void setRemodelingDate(String remodelingDate) {
		this.remodelingDate = remodelingDate;
	}

	public String getDataReferenceDate() {
		return dataReferenceDate;
	}

	public void setDataReferenceDate(String dataReferenceDate) {
		this.dataReferenceDate = dataReferenceDate;
	}

}