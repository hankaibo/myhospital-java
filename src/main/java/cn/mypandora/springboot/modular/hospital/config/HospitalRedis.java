package cn.mypandora.springboot.modular.hospital.config;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.mypandora.springboot.modular.hospital.model.Hospital;
import cn.mypandora.springboot.modular.hospital.service.HospitalService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hankaibo
 * @date 1/12/2021
 */
@Slf4j
@Component
public class HospitalRedis implements ApplicationRunner {

    private final HospitalService hospitalService;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public HospitalRedis(HospitalService hospitalService, StringRedisTemplate stringRedisTemplate) {
        this.hospitalService = hospitalService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("清空redis...");
        Set<String> keys = stringRedisTemplate.keys("*");
        stringRedisTemplate.delete(keys);

        log.info("启动成功，开始同步医院数据至Redis中……");
        List<Hospital> hospitalList = hospitalService.getAllHospital();
        hospitalService.saveHospital2Redis(hospitalList);
        log.info("同步医院数据完成。");
    }
}
