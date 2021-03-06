package jm.stockx.dto.shoeSize;

import jm.stockx.entity.ItemSize;
import jm.stockx.enums.ItemSizeTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShoeSizeDto {

    private Long id;

    @Min(3)
    @NotNull
    private String size;

    @NotNull
    private ItemSizeTypes sizeTypes;

    public ShoeSizeDto(ItemSize shoesize) {
        this.id = shoesize.getId();
        this.size = shoesize.getSize();
        this.sizeTypes = shoesize.getSizeTypes();
    }
}
