package com.vhung.studentmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //setter, getter, toString, equals
@NoArgsConstructor
@AllArgsConstructor
@Builder //đánhdaausus dùng loomhok builder
public class UserRequestDTO {
    private String userName;
    private String passWord;
    private String role; //dùng String nhận dữ liệu Json rồi convert  dễ xử lí hơn dùng Enum


}
