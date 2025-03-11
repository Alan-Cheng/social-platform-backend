package backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class WebSocketController {

    @GetMapping("/host-ip")
    public String getHostIp() throws UnknownHostException {
        // 獲取主機的 IP 地址
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }
}
