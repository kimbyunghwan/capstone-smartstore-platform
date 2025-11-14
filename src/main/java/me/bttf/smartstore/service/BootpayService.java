package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootpayService {

    @Value("${bootpay.rest-key}")
    private String restKey;

    @Value("${bootpay.private-key}")
    private String privateKey;

    private final WebClient web = WebClient.builder()
            .baseUrl("https://api.bootpay.co.kr/v2")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();

    /** 결제 영수증 검증 */
    public boolean verifyPayment(String receiptId, int expectedAmount) {
        String token = requestToken();

        Map<String, Object> res = web.get()
                .uri(uriBuilder -> uriBuilder.path("/receipt/{rid}").build(receiptId))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        log.info("[Bootpay] receipt verify raw. receiptId={}, expected={}, response={}",
                receiptId, expectedAmount, res);

        if (res == null) return false;

        Map<String, Object> data; // 실제 영수증 데이터가 들어갈 맵
        Integer code = toIntOrNull(res.get("code"));

        if (code != null) {
            // 형태 (A): {"code":0,"data":{...}}
            if (code != 0) {
                log.warn("[Bootpay] verify failed: code={}", code);
                return false;
            }
            //noinspection unchecked
            data = (Map<String, Object>) res.get("data");
        } else {
            // 형태 (B): 영수증 필드가 평평하게 옴
            data = res;
        }

        if (data == null) {
            log.warn("[Bootpay] verify failed: data is null");
            return false;
        }

        // ----- 상태 체크 -----
        // 정수 status(1=결제완료) 우선, 그 외 텍스트도 보조로 허용
        Integer statusNum = toIntOrNull(data.get("status"));
        String statusText = firstNonBlank(
                asString(data.get("status_en")),
                asString(data.get("status_locale")),
                asString(data.get("status"))
        );
        boolean statusOk = (statusNum != null && statusNum == 1) || isPaidStatus(statusText);

        // ----- 금액 체크 -----
        BigDecimal paid = toBigDecimal(data.get("price"));
        boolean amountOk = paid != null &&
                paid.compareTo(BigDecimal.valueOf(expectedAmount)) == 0;

        log.info("[Bootpay] parsed. statusNum={}, statusText={}, statusOk={}, paid={}, amountOk={}",
                statusNum, statusText, statusOk, paid, amountOk);

        return statusOk && amountOk;
    }

    private static Integer toIntOrNull(Object o) {
        try {
            if (o == null) return null;
            if (o instanceof Number n) return n.intValue();
            return Integer.valueOf(o.toString().replaceAll("\\D+", ""));
        } catch (Exception ignored) { return null; }
    }

    private static String asString(Object o) { return o == null ? null : String.valueOf(o); }

    private static String firstNonBlank(String... vals) {
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return "";
    }

    private static boolean isPaidStatus(String s) {
        if (s == null) return false;
        String v = s.trim().toLowerCase().replace('_', ' ');
        return Set.of("paid","pay done","completed","complete","done","success","결제완료","승인완료")
                .contains(v);
    }

    private static BigDecimal toBigDecimal(Object o) {
        try {
            if (o == null) return null;
            if (o instanceof BigDecimal b) return b;
            if (o instanceof Number n) return new BigDecimal(n.toString());
            return new BigDecimal(o.toString());
        } catch (Exception e) { return null; }
    }
    /** REST 토큰 요청 (REST Key + Private Key) — v2 응답 형태 모두 지원 */
    private String requestToken() {
        Map<String, String> body = Map.of(
                "application_id", restKey,   // REST API Key
                "private_key",    privateKey // Private Key
        );

        Map<String, Object> tokenRes = web.post()
                .uri("/request/token")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResp ->
                        clientResp.bodyToMono(String.class)
                                .map(b -> new IllegalStateException("Bootpay token error "
                                        + clientResp.statusCode() + " body=" + b)))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (tokenRes == null) {
            throw new IllegalStateException("Bootpay token response null");
        }

        // v2 성공 케이스 1) {"access_token":"...", "expires_in":1800}
        Object at = tokenRes.get("access_token");
        if (at != null) return String.valueOf(at);

        // v2 성공 케이스 2) {code:0, data:{token:"..."}}
        Object codeObj = tokenRes.get("code");
        if (codeObj != null) {
            int code = (codeObj instanceof Number) ? ((Number) codeObj).intValue()
                    : parseIntSafe(String.valueOf(codeObj), -1);
            if (code == 0) {
                Object data = tokenRes.get("data");
                if (data instanceof Map<?,?> d && d.get("token") != null) {
                    return String.valueOf(d.get("token"));
                }
            }
        }

        // 여기까지 오면 성공 구조가 아님 → 전체 바디를 예외로 노출
        throw new IllegalStateException("Bootpay token failed: " + tokenRes);
    }

    private static int parseIntSafe(String s, int def) {
        try { return Integer.parseInt(s.replaceAll("\\D+", "")); } catch (Exception e) { return def; }
    }

    private static record BootpayVerifyResult(
            boolean success,
            String reason,
            int paidAmount,
            String statusText
    ) {}
}
