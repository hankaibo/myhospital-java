package cn.mypandora.springboot.modular.hospital.service;

import cn.mypandora.springboot.core.base.PageInfo;
import cn.mypandora.springboot.modular.hospital.model.Hospital;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.Collection;
import java.util.List;

/**
 * @author hankaibo
 * @date 1/10/2021
 */
public interface HospitalService {

    /**
     * 查询所有医院
     *
     * @return 所有医院
     */
    List<Hospital> getAllHospital();

    /**
     * 根据分页参数查询医院。
     *
     * @param pageNum  当前页码
     * @param pageSize 当前页数
     * @param hospital 医院信息
     * @return 医院列表
     */
    PageInfo<Hospital> pageHospital(int pageNum, int pageSize, Hospital hospital);

    /**
     * 添加医院。
     *
     * @param hospital 医院
     */
    void addHospital(Hospital hospital);

    /**
     * 根据医院Id查询医院。
     *
     * @param id 医院Id
     * @return 医院信息
     */
    Hospital getHospitalById(Long id);

    /**
     * 更新医院。
     *
     * @param hospital 医院
     */
    void updateHospital(Hospital hospital);

    /**
     * 删除医院。
     *
     * @param id 医院id
     */
    void deleteHospital(Long id);

    /**
     * 批量删除医院。
     *
     * @param ids [1,2,3,4]
     */
    void deleteBatchHospital(Long[] ids);

    /**
     * 保存医院信息到 redis 中
     *
     * @param hospitalCollection 医院信息
     * @return 数量
     */
    Long saveHospital2Redis(Collection<Hospital> hospitalCollection);

    /**
     * 获取给定城市的坐标
     *
     * @param hospitals 城市key
     * @return 坐标列表
     */
    List<Point> getHospitalPos(String[] hospitals);

    /**
     * 获取两个医院之间的距离
     *
     * @param hospital1 医院1
     * @param hospital2 医院2
     * @param metric    单位信息
     * @return 距离
     */
    Distance getTwoHospitalDistance(String hospital1, String hospital2, Metric metric);

    /**
     * 根据给定地理位置坐标获取指定范围内的地理位置集合
     *
     * @param circle 中心点和距离
     * @param args   限制返回的个数和排序方式
     * @return 结果集合
     */
    List<Hospital> getPointRadius(Circle circle, RedisGeoCommands.GeoRadiusCommandArgs args);

    /**
     * 根据给定地理位置获取指定范围内的地理位置集合
     *
     * @param member   数量
     * @param distance 距离
     * @param args     参数
     * @return 位置集合
     */
    GeoResults<RedisGeoCommands.GeoLocation<Object>> getMemberRadius(String member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args);

    /**
     * 获取某个地理位置的 geohash 值
     *
     * @param hospitals 医院key
     * @return geohash 列表
     */
    List<String> getCityGeoHash(String[] hospitals);
}


