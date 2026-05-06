package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CloudinaryResponse {
    private String url;
    private String publicId;
}
