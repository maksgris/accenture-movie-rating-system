package com.avas.usertype.microservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeDTO {

    private Long id;
    private String type;
    private List<Long> userIds;

}