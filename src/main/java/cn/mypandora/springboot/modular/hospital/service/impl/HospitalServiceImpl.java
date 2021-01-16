package cn.mypandora.springboot.modular.hospital.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.mypandora.springboot.config.exception.EntityNotFoundException;
import cn.mypandora.springboot.core.base.PageInfo;
import cn.mypandora.springboot.modular.hospital.mapper.HospitalMapper;
import cn.mypandora.springboot.modular.hospital.model.Hospital;
import cn.mypandora.springboot.modular.hospital.service.HospitalService;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * @author hankaibo
 * @date 1/10/2021
 */
@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    private final String GEO_KEY = "beijing-hospital";

    private final HospitalMapper hospitalMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public HospitalServiceImpl(HospitalMapper hospitalMapper, RedisTemplate<String, Object> redisTemplate) {
        this.hospitalMapper = hospitalMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Hospital> getAllHospital() {
        return hospitalMapper.selectAll();
    }

    @Override
    public PageInfo<Hospital> pageHospital(int pageNum, int pageSize, Hospital hospital) {
        PageHelper.startPage(pageNum, pageSize);
        // 使用通用 Mapper Example 用法，亦可用传统的 xml 配置文件。
        Example example = new Example(Hospital.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(hospital.getName())) {
            criteria.andEqualTo("name", hospital.getName());
        }
        List<Hospital> hospitalList = hospitalMapper.selectByExample(example);
        return new PageInfo<>(hospitalList);
    }

    @Override
    public void addHospital(Hospital hospital) {
        LocalDateTime now = LocalDateTime.now();
        hospital.setCreateTime(now);
        hospitalMapper.insert(hospital);
    }

    @Override
    public Hospital getHospitalById(Long id) {
        Hospital info = hospitalMapper.selectByPrimaryKey(id);
        if (info == null) {
            throw new EntityNotFoundException(Hospital.class, "医院不存在。");
        }
        return info;
    }

    @Override
    public void updateHospital(Hospital hospital) {
        LocalDateTime now = LocalDateTime.now();
        hospital.setUpdateTime(now);
        hospitalMapper.updateByPrimaryKeySelective(hospital);
    }

    @Override
    public void deleteHospital(Long id) {
        hospitalMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteBatchHospital(Long[] ids) {
        hospitalMapper.deleteByIds(StringUtils.join(ids, ','));
    }

    @Override
    public Long saveHospital2Redis(Collection<Hospital> hospitalCollection) {
        if (hospitalCollection.size() == 0) {
            return 0L;
        }
        GeoOperations<String, Object> ops = redisTemplate.opsForGeo();

        Set<RedisGeoCommands.GeoLocation<Object>> locationSet = new HashSet<>();
        hospitalCollection.stream().filter(hospital -> hospital.getLng() != null && hospital.getLat() != null)
            .forEach(hospital -> locationSet.add(
                new RedisGeoCommands.GeoLocation<>(hospital.getId(), new Point(hospital.getLng(), hospital.getLat()))));

        return ops.add(GEO_KEY, locationSet);
    }

    @Override
    public List<Point> getHospitalPos(String[] hospitals) {
        GeoOperations<String, Object> ops = redisTemplate.opsForGeo();

        return ops.position(GEO_KEY, hospitals);
    }

    @Override
    public Distance getTwoHospitalDistance(String hospital1, String hospital2, Metric metric) {
        GeoOperations<String, Object> ops = redisTemplate.opsForGeo();
        return metric == null ? ops.distance(GEO_KEY, hospital1, hospital2)
            : ops.distance(GEO_KEY, hospital1, hospital2, metric);
    }

    @Override
    public List<Hospital> getPointRadius(Circle circle, RedisGeoCommands.GeoRadiusCommandArgs args) {
        List<Hospital> hospitalList = new ArrayList<>();

        GeoOperations<String, Object> ops = redisTemplate.opsForGeo();
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults =
            args == null ? ops.radius(GEO_KEY, circle) : ops.radius(GEO_KEY, circle, args);

        assert geoResults != null;
        List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> content = geoResults.getContent();

        content.forEach(item -> hospitalList.add(Hospital.builder().distance(item.getDistance().getValue())
            .lat(item.getContent().getPoint().getX()).lng(item.getContent().getPoint().getY())
            .id(Long.valueOf((Integer)item.getContent().getName())).build()));

        return getHospital(hospitalList);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> getMemberRadius(String member, Distance distance,
        RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoOperations<String, Object> ops = redisTemplate.opsForGeo();
        return args == null ? ops.radius(GEO_KEY, member, distance) : ops.radius(GEO_KEY, member, distance, args);
    }

    @Override
    public List<String> getCityGeoHash(String[] hospitals) {
        GeoOperations<String, Object> ops = redisTemplate.opsForGeo();
        return ops.hash(GEO_KEY, hospitals);
    }

    /**
     * 根据redis返回的数据补全
     * 
     * @param hospitalList
     *            不完整的医院信息
     * @return 完整的医院信息
     */
    private List<Hospital> getHospital(List<Hospital> hospitalList) {
        if (hospitalList.size() == 0) {
            return hospitalList;
        }
        Example example = new Example(Hospital.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", hospitalList.stream().mapToLong(Hospital::getId).boxed().collect(Collectors.toList()));
        return hospitalMapper.selectByExample(example);
    }
}
