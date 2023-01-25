package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
public class Item {

    private Long id;
    @NotBlank(groups = UpdateCheck.class) // 업데이트 시에만 적용
    private String itemName;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class}) // 업데이트, 세이브 둘 다 적용
    @Range(min = 1000, max = 1000000)
    private Integer price;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
