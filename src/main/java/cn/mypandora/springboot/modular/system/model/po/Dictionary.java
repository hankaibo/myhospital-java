package cn.mypandora.springboot.modular.system.model.po;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.ibatis.type.JdbcType;

import cn.mypandora.springboot.core.enums.StatusEnum;
import cn.mypandora.springboot.core.validate.AddGroup;
import cn.mypandora.springboot.core.validate.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

/**
 * Dictionary
 *
 * @author hankaibo
 * @date 2019/6/14
 */
@ApiModel("字典对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_dictionary")
@NameStyle(Style.camelhumpAndLowercase)
public class Dictionary extends BaseEntity {

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    @NotBlank
    private String name;

    /**
     * 字典编码
     */
    @ApiModelProperty(value = "字典编码")
    @NotBlank
    private String code;

    /**
     * 状态
     */
    @ApiModelProperty(value = "字典状态")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "{dictionary.status.notNull}")
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private StatusEnum status;

    /**
     * 字典描述
     */
    @ApiModelProperty(value = "字典描述")
    @Size(max = 255, groups = {AddGroup.class, UpdateGroup.class}, message = "{dictionary.description.size}")
    private String description;

}
