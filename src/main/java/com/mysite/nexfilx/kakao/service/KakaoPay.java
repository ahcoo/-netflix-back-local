package com.mysite.nexfilx.kakao.service;

import com.mysite.nexfilx.User.domain.User;
import com.mysite.nexfilx.User.dto.UserDto;
import com.mysite.nexfilx.kakao.ReadyResponse;
import com.mysite.nexfilx.kakao.domain.ReadyResponseVO;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

@Service
//@Log
@Slf4j
public class KakaoPay {

    private static final String HOST = "https://kapi.kakao.com";
        private ReadyResponseVO readyResponseVO;


    public String payReady(UserDto userDto) {
        LocalDate currentDate = LocalDate.now();


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "91ec9a03250976dfee1bae77778003fa");
        headers.add("Accept",   MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        params.add("cid", "TC0ONETIME"); // 가맹정 코드 (test는 카카오에서 제공하는 샘플 코드 사용)
        params.add("partner_order_id", userDto.getNowdate()+"test"); // 주문 번호
        params.add("partner_user_id", userDto.getUseremail()); // 주문자 아이디
        params.add("item_name", "넷플릭스 스탠다드"+userDto.getUseremail()); // 상품 이름
        params.add("quantity", "1"); // 상품 수량
        params.add("total_amount", "13500"); // 결제 금액
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:3000/choiceprofile"); //결제 완료 시 이동 페이지
        params.add("cancel_url", "http://localhost:3000/payinfo"); // 결제 취소 시 이동 페이지
        params.add("fail_url", "http://localhost:3000/payinfo");

        HttpEntity<MultiValueMap<String,String>> body = new HttpEntity<MultiValueMap<String , String>>(params, headers);

        try {
            readyResponseVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, ReadyResponseVO.class);
            log.info("" + readyResponseVO);

            return readyResponseVO.getNext_redirect_pc_url();
        } catch (RestClientException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return "kakao";

    }
}
