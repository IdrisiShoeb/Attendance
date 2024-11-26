package com.shoeb.full.helpers;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Message {
    private String content;
    @Builder.Default
    private String type= "green";

}