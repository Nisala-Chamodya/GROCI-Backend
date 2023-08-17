package lk.ijse.gdse.webposbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class OrderDTO {
    private String orderID;
    private String customerID;
    private Date orderDate;
    private List<OrderDetailsDTO> orderDetailsList;
}
