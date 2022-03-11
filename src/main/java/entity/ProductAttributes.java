package entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class ProductAttributes implements Serializable {
    private String name;
    private String value;
}
