package bigsanghyuk.four_uni.category.entity;

import lombok.Getter;

@Getter
public enum CategoryName {
    ACADEMY("학사"),
    CREDIT_EXCHANGE("학점 교류"),
    GENERAL("일반/행사/모집"),
    SCHOLARSHIP("장학금"),
    TUITION("등록금 납부"),
    EDU_EXAM("교육시험"),
    VOLUNTEER("봉사"),
    RECRUITMENT("채용정보"),
    DEPT("학과");

    private String categoryName;

    CategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
