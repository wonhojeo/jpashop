package jpabook.jpashop.shinhanDs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.catalina.User;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class ApiController {
    //1번 문제. 서버 헬스 체크
    @GetMapping("/api/server-health-check")
    public CreateServerHealthCheckResponse serverHealthCheck(){
        return new CreateServerHealthCheckResponse();
    }

    //2번 문제. 주가 정보 조회
    @GetMapping("/api/price/display")
    public CreateStockInfoResponse stockInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message;
        int display = Integer.parseInt(request.getParameter("display"));

        return new CreateStockInfoResponse(display);
    }
    //3번 문제. 종목 상세 정보 조회
    @GetMapping("/api/detail/{symbol}")
    public CreateStockDetailResponse stockDetailInfo(@RequestBody @Valid String symbol) throws IOException {
        return new CreateStockDetailResponse(symbol);
    }

    //4번 문제. 로그인
    @PostMapping("/api/login")
    public CreateLoginResponse login(@RequestBody @Valid UserDto loginInfo) throws IOException, NoSuchAlgorithmException {

        return new CreateLoginResponse(loginInfo);
    }


    /**=====================**/
    //1번 문제. 서버 헬스 체크
    @Data
    static class CreateServerHealthCheckResponse {
        private String message;
        public CreateServerHealthCheckResponse(){this.message="server is running";}

    }

    static class CreateStockInfoResponse {

        private double adj_close; //29.393011,
        private double close;   // 29.393011,
        private float high;    // 29.91,
        private int id;  // 486,
        private double low; // 29.280001,
        private double open;    // 29.43,
        private String symbol;  // "WY",
        private float volume;  // 4336400.0

        public CreateStockInfoResponse(int display) throws IOException {
            String message;
            if(Objects.isNull(display)){
                message ="display parameter is required";
            }else if(display >=1 && display <=501 ){
                message ="display parameter must be an integer";
            } else {
                message ="display parameter is out of range";
            }

            List<StockDto> stockList = readPrice();
            List<StockDto> responseList = new ArrayList<>();
            for (StockDto stockDto : stockList) {
                if (stockDto.getId() == display) {
                    responseList.add(stockDto);
                }
            }

        }
    }

    private static List<StockDto> readPrice() throws IOException {
        ClassPathResource resource = new ClassPathResource("price.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<StockDto> stock = new ArrayList<>();
        String str = "";
        while((str = br.readLine()) != null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                // 스트링에서 DTO LIST로 매핑하기
                stock = Arrays.asList(mapper.readValue(br, StockDto[].class));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return stock;
    }

    //3번 문제. 종목 상세 정보 조회
    static class CreateStockDetailResponse{

        public CreateStockDetailResponse(String symbol) throws IOException {
            List<DetailDto> detailList = readDetail();
            List<DetailDto> responseList = new ArrayList<>();

            for (DetailDto detailDto : detailList) {
                if (detailDto.getSymbol().equals(symbol)) {
                    responseList.add(detailDto);
                }
            }

            if(responseList.isEmpty()){
                String message = "request symbol does not exist";
            }

        }

    }

    private static List<DetailDto> readDetail() throws IOException {
        ClassPathResource resource = new ClassPathResource("detail.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<DetailDto> detail = new ArrayList<>();
        String str = "";
        while((str = br.readLine()) != null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                // 스트링에서 DTO LIST로 매핑하기
                detail = Arrays.asList(mapper.readValue(br, DetailDto[].class));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return detail;
    }
    //4번 문제. 로그인
    static class CreateLoginResponse{

        public CreateLoginResponse(UserDto user) throws IOException, NoSuchAlgorithmException {
            String message = null;
            UserDto userResponse = new UserDto();
            List<UserDto> userList = readUser();

            //1단계
            for (UserDto userDto : userList) {
                if(userDto.getUsername().equals(user.getUsername())){
                    String sha1Password;
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                    byte[] result = messageDigest.digest(user.getPassword().getBytes());
                    sha1Password = Arrays.toString(result);
                    if(userDto.getHash().equals(sha1Password)&&userDto.getPassword().equals(user.getPassword())) {
                        message = "login success";
                        userResponse.setSession(userDto.getSession());
                        userResponse.setMessage(message);
                    }

                }

            }

        }

    }

    private static List<UserDto> readUser() throws IOException {
        ClassPathResource resource = new ClassPathResource("user.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<UserDto> user = new ArrayList<>();
        String str = "";
        while((str = br.readLine()) != null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                // 스트링에서 DTO LIST로 매핑하기
                user = Arrays.asList(mapper.readValue(br, UserDto[].class));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return user;
    }
}
