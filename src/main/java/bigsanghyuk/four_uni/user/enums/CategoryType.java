package bigsanghyuk.four_uni.user.enums;

import lombok.Getter;

public enum CategoryType {

    ACADEMY("학사", 246),
    CREDIT_EXCHANGE("학점교류", 247),
    GENERAL("일반/행사/모집", 2611),
    SCHOLARSHIP("장학금", 249),
    TUITION("등록금 납부", 250),
    EDU_EXAM("교육시험", 252),
    VOLUNTEER("봉사", 253),
    ME("기계공학과", 814),
    ELEC("전기공학과", 364),
    ELECTRON("전자공학과", 367),
    IME("산업경영공학과", 826),
    MSE("신소재공학과", 355),
    SAFETY("안전공학과", 358),
    ENERGY("에너지화학공학과", 868),
    MECA("바이오-로봇시스템 공학과", 349),
    CONTRACT("계약학과 - 도시건설공학과, 테크노경영학과, 글로벌무역물류학과", 2790),
    URBAN("도시행정학과", 1213),
    CIVIL("건설환경공학", 1237),
    ET("환경공학", 1267),
    UCV("도시공학과", 1252),
    ARCHI("건축공학, 도시건축학", 1205),
    KOREAN("국어국문학과", 286),
    UI("영어영문학과", 642),
    GERNAM("독어독문학과", 289),
    INUFRANCE("불어불문학과", 292),
    UNJAPAN("일본지역문화학과", 298),
    INUCHINA("중어중국학과", 301),
    IT("정보기술대학", 2608),
    ISIS("컴퓨터공학부", 376),
    ITE("정보통신공학과", 373),
    ESE("임베디드시스템공학과", 370),
    LIFE("생명과학부(생명과학전공)", 1290),
    MOLBIO("생명과학부(분자의생명전공)", 1883),
    ENGINEERINGLIFE("생명공학부(생명공학전공)", 1285),
    NANOBIO("생명공학부(나노바이오공학전공)", 1279),
    ISU("수학과", 307),
    PHYSICS("물리학과", 304),
    CHEM("화학과", 316),
    UIFASHION("패션산업학과", 1735),
    MARINE("해양학과", 730),
    BIZ("경영학부", 379),
    DATASCIENCE("데이터과학과", 1825),
    TAX("세무회계학과", 384),
    NAS("동북아국제통상전공", 1830),
    SLOG("스마트물류공학전공", 1832),
    IBE("IBE전공", 1840),
    DSW("사회복지학과", 322),
    SHINBANG("미디어커뮤니케이션학과", 757),
    CLS("문헌정보학과", 319),
    HRD("창의인재개발학과", 328),
    FINEARTS("한국화전공(조형예술학부), 서양화전공(조형예술학부)", 409),
    DESIGN("디자인학부", 1842),
    UIPA10("공연예술학과", 400),
    INUPE("스포츠과학부(체육학부)", 412),
    UIEX("운동건강학부", 406),
    LAW("법학부", 1299),
    UIPA("행정학과", 1707),
    POLITICS("정치외교학과", 337),
    ECON("경제학과", 331),
    TRADE("무역학부", 334),
    CCS("소비자학과", 340),
    EDUKOREAN("국어교육과", 1074),
    EDUENGLISH("영어교육과", 1123),
    EDUJAPANESE("일어교육과", 1176),
    EDUMATH("수학교육과", 1088),
    EDUPHYSICAL("체육교육과", 1861),
    ECE("유아교육과", 1143),
    EDUHISTORY("역사교육과", 1104),
    EDUETHICS("윤리교육과", 1161);

    CategoryType (String value, Integer id) {
        this.value = value;
        this.id = id;
    }

    private String key;
    @Getter
    private String value;
    @Getter
    private Integer id;

    public String getKey() {
        return name();
    }

}
