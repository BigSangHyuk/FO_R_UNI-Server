package bigsanghyuk.four_uni.post.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PostControllerTest {

    static String encodedWithBracket = encode("{1,2,3,4}", StandardCharsets.UTF_8);
    static String encodedWithoutBracket = encode("1,2,3,4", StandardCharsets.UTF_8);
    static String queryParameter = "1-2-3-4";

    @Test
    @DisplayName("괄호 있는 인코딩")
    void urlEncodingWithBracket() {
        long startTime = System.nanoTime();

        String decoded = decode(encodedWithBracket, StandardCharsets.UTF_8);
        String substring = decoded.substring(1, decoded.length() - 1);
        List<Long> result = parseLongList(substring);

        long endTime = System.nanoTime();
        System.out.printf("괄호 있는 인코딩: %dns%n", endTime - startTime);
    }

    @Test
    @DisplayName("괄호 없는 인코딩")
    void test() {
        long startTime = System.nanoTime();

        String decoded = decode(encodedWithoutBracket, StandardCharsets.UTF_8);
        List<Long> result = parseLongList(decoded);

        long endTime = System.nanoTime();
        System.out.printf("괄호 없는 인코딩: %dns%n", endTime - startTime);
    }

    @Test
    @DisplayName("하이픈 문자열")
    void queryParam() {
        long startTime = System.nanoTime();

        List<Long> result = parseLongList(queryParameter, "-");

        long endTime = System.nanoTime();
        System.out.printf("하이픈 문자열: %dns%n", endTime - startTime);
    }

    private static List<Long> parseLongList(String input) {
        List<Long> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(Long.parseLong(matcher.group()));
        }
        return result;
    }

    private static List<Long> parseLongList(String input, String delimiter) {
        List<Long> result = new ArrayList<>();
        String[] tokens = input.split(delimiter);
        for (String token : tokens) {
            result.add(Long.parseLong(token));
        }
        return result;
    }
}