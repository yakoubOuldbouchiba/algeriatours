package com.yakoub.ea.execptions;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestError {
    String type ;
    String message;
    String originMessage;
    String reason;
    String advice;
}
