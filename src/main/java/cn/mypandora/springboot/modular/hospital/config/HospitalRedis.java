package cn.mypandora.springboot.modular.hospital.config;

import cn.mypandora.springboot.modular.hospital.model.Hospital;
import cn.mypandora.springboot.modular.hospital.service.HospitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hankaibo
 * @date 1/12/2021
 */
@Slf4j
@Component
public class HospitalRedis implements ApplicationRunner {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalRedis(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动成功，开始同步医院数据至Redis中……");
        List<Hospital> hospitalList = hospitalService.getAllHospital();
        hospitalService.saveHospital2Redis(hospitalList);
        log.info("同步医院数据完成。");
    }
}
