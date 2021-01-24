package cn.mypandora.springboot.modular.hospital.model;

import java.time.LocalDateTime;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mypandora.springboot.core.util.CustomLocalDateTimeDeserializer;
import cn.mypandora.springboot.core.util.CustomLocalDateTimeSerializer;
import cn.mypandora.springboot.core.validate.AddGroup;
import cn.mypandora.springboot.core.validate.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.Style;

/**
 * @author hankaibo
 * @date 1/10/2021
 */
@ApiModel("医院实体")
@Builder
@Data
@Table(name = "tbl_hospital")
@NameStyle(Style.camelhumpAndLowercase)
public class Hospital {

    @Tolerate
    public Hospital() {}

    @ApiModelProperty(value = "主键id")
    @Positive(groups = {AddGroup.class, UpdateGroup.class}, message = "主键必须为正整数")
    @NotNull(groups = {UpdateGroup.class}, message = "主键不能为空")
    @KeySql(dialect = IdentityDialect.MYSQL)
    @Id
    private Long id;

    /**
     * 医院名称
     */
    @ApiModelProperty(value = "名称")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.name.notNull}")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.name.size}")
    private String name;

    /**
     * 医院编码
     */
    @ApiModelProperty(value = "编码")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.code.notNull}")
    @Size(max = 8, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.code.size}")
    private String code;

    /**
     * 医院所属区县
     */
    @ApiModelProperty(value = "所属区县")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.district.notNull}")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.district.size}")
    private String district;

    /**
     * 医院类别
     */
    @ApiModelProperty(value = "类别")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.type.notNull}")
    @Size(max = 5, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.type.size}")
    private String type;

    /**
     * 医院等级
     */
    @ApiModelProperty(value = "等级")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.level.notNull}")
    @Size(max = 3, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.level.size}")
    private String level;

    /**
     * 医院地址
     */
    @ApiModelProperty(value = "地址")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.address.size}")
    private String address;

    /**
     * 医院编码
     */
    @ApiModelProperty(value = "编码")
    @Size(max = 6, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.postalCode.size}")
    private String postalCode;

    /**
     * 医院简介
     */
    @ApiModelProperty(value = "简介")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.introduction.size}")
    private String introduction;

    /**
     * 医院经度
     */
    @ApiModelProperty(value = "医院经度")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.lng.notNull}")
    private Double lng;

    /**
     * 医院纬度
     */
    @ApiModelProperty(value = "医院纬度")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.lat.notNull}")
    private Double lat;

    /**
     * 医院标示，A，表示是A类定点医院
     */
    @ApiModelProperty(value = "医院标示，A，表示是A类定点医院")
    @Size(max = 1, groups = {AddGroup.class, UpdateGroup.class}, message = "{hospital.flag.size}")
    private String flag;

    /**
     * 距离
     */
    @Transient
    private Double distance;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

}
