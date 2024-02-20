package jpabook.jpashop.shinhanDs;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    private int id;
    private String username;
    private String password;
    private String hash;
    private String session;
    private String message;
}
