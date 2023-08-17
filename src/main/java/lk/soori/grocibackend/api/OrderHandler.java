package lk.ijse.gdse.webposbackend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gdse.webposbackend.api.util.Convertor;
import lk.ijse.gdse.webposbackend.dao.DaoFactory;
import lk.ijse.gdse.webposbackend.dao.DaoType;
import lk.ijse.gdse.webposbackend.dao.custom.OrderDAO;
import lk.ijse.gdse.webposbackend.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse.webposbackend.dto.ItemDTO;
import lk.ijse.gdse.webposbackend.dto.OrderDTO;
import lk.ijse.gdse.webposbackend.dto.RespondsDTO;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//@WebServlet(urlPatterns = "/order")
public class OrderHandler extends HttpServlet {

    private Connection connection;
    private OrderDAO orderDAO;
    private OrderDetailsDAO orderDetailsDAO;
    private Convertor convertor;

    @Override
    public void init() throws ServletException {

        try {

            InitialContext initialContext = new InitialContext();
            DataSource pool = (DataSource) initialContext.lookup("java:comp/env/jdbc/Web_Pos");
            this.connection = pool.getConnection();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.orderDAO = DaoFactory.getInstance().getDao(DaoType.ORDER, connection);
        this.orderDetailsDAO = DaoFactory.getInstance().getDao(DaoType.ORDERDETAILS, connection);
        this.convertor = new Convertor();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        resp.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();

        try {

            List<OrderDTO> list = orderDAO.getAll().stream().map(order -> convertor.fromOrder(order)).
                    collect(Collectors.toList());

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            jsonb.toJson(new RespondsDTO(200, "Done", list), resp.getWriter());

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        Jsonb jsonb = JsonbBuilder.create();
        OrderDTO orderDTO = jsonb.fromJson(req.getReader(), OrderDTO.class);

        System.out.println(orderDTO);
    }
}
