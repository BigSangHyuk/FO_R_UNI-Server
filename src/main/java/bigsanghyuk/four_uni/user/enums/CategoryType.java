package bigsanghyuk.four_uni.user.enums;

import lombok.Getter;

public enum CategoryType {

    NOT_SELECTED("선택 안됨", "NOT_SELECTED", -1),  // getId() null 방지, 사실상 더미값
    ACADEMY("학사", "ACADEMY", 246),
    CREDIT_EXCHANGE("학점교류", "CREDIT_EXCHANGE", 247),
    GENERAL("일반/행사/모집", "GENERAL", 2611),
    SCHOLARSHIP("장학금", "SCHOLARSHIP", 249),
    TUITION("등록금 납부", "TUITION", 250),
    EDU_EXAM("교육시험", "EDU_EXAM", 252),
    VOLUNTEER("봉사", "VOLUNTEER", 253),
    ME("기계공학과", "ME", 814),
    ELEC("전기공학과", "ELEC", 364),
    ELECTRON("전자공학과", "ELECTRON", 367),
    IME("산업경영공학과", "IME", 826),
    MSE("신소재공학과", "MSE", 355),
    SAFETY("안전공학과", "SAFETY", 358),
    ENERGY("에너지화학공학과", "ENERGY", 868),
    MECA("바이오-로봇시스템 공학과", "MECA", 349),
    CONTRACT("계약학과", "CONTRACT", 2790),
    URBAN("도시행정학과", "URBAN", 1213),
    CIVIL("건설환경공학", "CIVIL", 1237),
    ET("환경공학", "ET", 1267),
    UCV("도시공학과", "UCV", 1252),
    ARCHI("건축공학, 도시건축학", "ARCHI", 1205),
    KOREAN("국어국문학과", "KOREAN", 286),
    UI("영어영문학과", "UI", 642),
    GERMAN("독어독문학과", "GERMAN", 289),
    INUFRANCE("불어불문학과", "INUFRANCE", 292),
    UNJAPAN("일본지역문화학과", "UNJAPAN", 298),
    INUCHINA("중어중국학과", "INUCHINA", 301),
    IT("정보기술대학", "IT", 2608),
    ISIS("컴퓨터공학부", "ISIS", 376),
    ITE("정보통신공학과", "ITE", 373),
    ESE("임베디드시스템공학과", "ESE", 370),
    LIFE("생명과학부(생명과학전공)", "LIFE", 1290),
    MOLBIO("생명과학부(분자의생명전공)", "MOLBIO", 1883),
    ENGINEERINGLIFE("생명공학부(생명공학전공)", "ENGINEERINGLIFE", 1285),
    NANOBIO("생명공학부(나노바이오공학전공)", "NANOBIO", 1279),
    ISU("수학과", "ISU", 307),
    PHYSICS("물리학과", "PHYSICS", 304),
    CHEM("화학과", "CHEM", 316),
    UIFASHION("패션산업학과", "UIFASHION", 1735),
    MARINE("해양학과", "MARINE", 730),
    BIZ("경영학부", "BIZ", 379),
    DATASCIENCE("데이터과학과", "DATASCIENCE", 1825),
    TAX("세무회계학과", "TAX", 384),
    NAS("동북아국제통상전공", "NAS", 1830),
    SLOG("스마트물류공학전공", "SLOG", 1832),
    IBE("IBE전공", "IBE", 1840),
    DSW("사회복지학과", "DSW", 322),
    SHINBANG("미디어커뮤니케이션학과", "SHINBANG", 757),
    CLS("문헌정보학과", "CLS", 319),
    HRD("창의인재개발학과", "HRD", 328),
    FINEARTS("한국화전공, 서양화전공(조형예술학부)", "FINEARTS", 409),
    DESIGN("디자인학부", "DESIGN", 1842),
    UIPA10("공연예술학과", "UIPA10", 400),
    INUPE("스포츠과학부(체육학부)", "INUPE", 412),
    UIEX("운동건강학부", "UIEX", 406),
    LAW("법학부", "LAW", 1299),
    UIPA("행정학과", "UIPA", 1707),
    POLITICS("정치외교학과", "POLITICS", 337),
    ECON("경제학과", "ECON", 331),
    TRADE("무역학부", "TRADE", 334),
    CCS("소비자학과", "CCS", 340),
    EDUKOREAN("국어교육과", "EDUKOREAN", 1074),
    EDUENGLISH("영어교육과", "EDUENGLISH", 1123),
    EDUJAPANESE("일어교육과", "EDUJAPANESE", 1176),
    EDUMATH("수학교육과", "EDUMATH", 1088),
    EDUPHYSICAL("체육교육과", "EDUPHYSICAL", 1861),
    ECE("유아교육과", "ECE", 1143),
    EDUHISTORY("역사교육과", "EDUHISTORY", 1104),
    EDUETHICS("윤리교육과", "EDUETHICS", 1161);

    CategoryType (String value, String code, Integer id) {
        this.value = value;
        this.code = code;
        this.id = id;
    }

    private String key;
    @Getter
    private String value;
    @Getter
    private String code;
    @Getter
    private Integer id;

    public String getKey() {
        return name();
    }

}
