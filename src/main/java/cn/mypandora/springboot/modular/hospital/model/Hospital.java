package cn.mypandora.springboot.modular.hospital.model;

import cn.mypandora.springboot.core.util.CustomLocalDateTimeDeserializer;
import cn.mypandora.springboot.core.util.CustomLocalDateTimeSerializer;
import cn.mypandora.springboot.core.validate.AddGroup;
import cn.mypandora.springboot.core.validate.UpdateGroup;
import cn.mypandora.springboot.modular.system.model.po.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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
    public Hospital() {
    }

    @ApiModelProperty(value = "主键id")
    @Positive(groups = {AddGroup.class, UpdateGroup.class}, message = "主键必须为正整数")
    @NotNull(groups = {UpdateGroup.class}, message = "主键不能为空")
    @KeySql(dialect = IdentityDialect.MYSQL)
    @Id
    private Long id;

    /**
     * 医院名称
     */
    @ApiModelProperty(value = "医院名称")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "医院名称长度最大至255字符")
    private String name;

    /**
     * 医院编码
     */
    @ApiModelProperty(value = "医院编码")
    @Size(max = 10, groups = {AddGroup.class, UpdateGroup.class}, message = "医院编码长度最大至10字符")
    private String code;

    /**
     * 医院所属区县
     */
    @ApiModelProperty(value = "医院所属区县")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "所属区县长度最大至255字符")
    private String district;

    /**
     * 医院类别
     */
    @ApiModelProperty(value = "类别")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "医院类别长度最大至255字符")
    private String type;

    /**
     * 医院等级
     */
    @ApiModelProperty(value = "医院等级")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "医院等级长度最大至255字符")
    private String lvl;

    /**
     * 医院地址
     */
    @ApiModelProperty(value = "医院地址")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "医院地址长度最大至255字符")
    private String address;

    /**
     * 医院编码
     */
    @ApiModelProperty(value = "医院编码")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "医院编码长度最大至255字符")
    private String zipCode;

    /**
     * 医院简介
     */
    @ApiModelProperty(value = "医院简介")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "医院简介长度最大至255字符")
    private String introduction;

    /**
     * 医院经度
     */
    @ApiModelProperty(value = "医院经度")
    @Positive(groups = {AddGroup.class, UpdateGroup.class}, message = "")
    private Double lng;

    /**
     * 医院纬度
     */
    @ApiModelProperty(value = "医院纬度")
    @Positive(groups = {AddGroup.class, UpdateGroup.class}, message = "")
    private Double lat;

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
