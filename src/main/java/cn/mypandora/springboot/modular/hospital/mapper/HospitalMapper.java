package cn.mypandora.springboot.modular.hospital.mapper;

import java.util.List;
import java.util.Map;

import cn.mypandora.springboot.core.base.MyBaseMapper;
import cn.mypandora.springboot.modular.hospital.model.Hospital;

/**
 * @author hankaibo
 * @date 1/10/2021
 */
public interface HospitalMapper extends MyBaseMapper<Hospital> {

    /**
     * 分组统计各医院数量
     * 
     * @return 各类别医院数量
     */
    List<Map<String, Object>> countHospitalByType();

}
