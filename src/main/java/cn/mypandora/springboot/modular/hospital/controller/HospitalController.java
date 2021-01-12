package cn.mypandora.springboot.modular.hospital.controller;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.mypandora.springboot.core.base.PageInfo;
import cn.mypandora.springboot.core.validate.AddGroup;
import cn.mypandora.springboot.core.validate.BatchGroup;
import cn.mypandora.springboot.core.validate.UpdateGroup;
import cn.mypandora.springboot.modular.hospital.model.Hospital;
import cn.mypandora.springboot.modular.hospital.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author hankaibo
 * @date 1/10/2021
 */
@Validated
@Api(tags = "社保定点医院管理")
@RestController
@RequestMapping("/api/v1/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    /**
     * 分页查询医院数据。
     *
     * @param pageNum
     *            页码
     * @param pageSize
     *            每页条数
     * @param name
     *            医院名称
     * @return 分页数据
     */
    @ApiOperation(value = "医院列表")
    @GetMapping
    public PageInfo<Hospital> pageHospital(
        @Positive @RequestParam(value = "current", defaultValue = "1") @ApiParam(value = "页码",
            required = true) int pageNum,
        @Positive @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam(value = "每页条数",
            required = true) int pageSize,
        @RequestParam(value = "name", required = false) @ApiParam(value = "医院名称") String name) {
        Hospital hospital = new Hospital();
        hospital.setName(name);
        return hospitalService.pageHospital(pageNum, pageSize, hospital);
    }

    /**
     * 新建医院。
     *
     * @param hospital
     *            医院数据
     */
    @ApiOperation(value = "医院新建")
    @PostMapping
    public void addHospital(
        @Validated({AddGroup.class}) @RequestBody @ApiParam(value = "医院数据", required = true) Hospital hospital) {
        hospitalService.addHospital(hospital);
    }

    /**
     * 查询医院详细数据。
     *
     * @param id
     *            医院主键id
     * @return 医院信息
     */
    @ApiOperation(value = "医院详情")
    @GetMapping("/{id}")
    public Hospital
        getHospitalById(@Positive @PathVariable("id") @ApiParam(value = "医院主键id", required = true) Long id) {
        return hospitalService.getHospitalById(id);
    }

    /**
     * 更新医院。
     *
     * @param hospital
     *            医院数据
     */
    @ApiOperation(value = "医院更新")
    @PutMapping("/{id}")
    public void updateHospital(
        @Validated({UpdateGroup.class}) @RequestBody @ApiParam(value = "医院数据", required = true) Hospital hospital) {
        hospitalService.updateHospital(hospital);
    }

    /**
     * 删除医院。
     *
     * @param id
     *            医院主键id
     */
    @ApiOperation(value = "医院删除")
    @DeleteMapping("/{id}")
    public void deleteHospital(@Positive @PathVariable("id") @ApiParam(value = "医院主键id", required = true) Long id) {
        hospitalService.deleteHospital(id);
    }

    /**
     * 批量删除医院。
     *
     * @param idList
     *            医院主键id数组
     */
    @ApiOperation(value = "医院删除(批量)")
    @DeleteMapping
    public void deleteBatchHospital(
        @Validated({BatchGroup.class}) @RequestBody @ApiParam(value = "医院主键id数组", required = true) Long[] idList) {
        hospitalService.deleteBatchHospital(idList);
    }

    /**
     * 获取指定圆内的医院
     *
     * @param lng
     *            圆心经度
     * @param lat
     *            圆心纬度
     * @param r
     *            圆心半径
     * @return 医院列表
     */
    @ApiOperation(value = "获取指定圆内的医院")
    @GetMapping("/circle")
    public List<Hospital> hospitalWithinCircle(@Positive Double lng, @Positive Double lat, @Positive Double r) {
        // 圆
        Point center = new Point(lng, lat);
        Distance radius = new Distance(r, RedisGeoCommands.DistanceUnit.MILES);
        Circle circle = new Circle(center, radius);

        // 参数
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
            .includeDistance().includeCoordinates().sortAscending().limit(20);

        return hospitalService.getPointRadius(circle, args);
    }

}