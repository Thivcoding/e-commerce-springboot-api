package org.hokvanthiv.ecommerce_springboot_api.service;

import java.util.Map;

public interface BakongClientService {

    Map<String, Object> generateQR(Long orderId, Double amount);

    Map<String, Object> checkPayment(String md5);
}
