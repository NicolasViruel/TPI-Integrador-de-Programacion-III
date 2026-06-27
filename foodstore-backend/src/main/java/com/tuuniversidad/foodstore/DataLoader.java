package com.tuuniversidad.foodstore;

import com.tuuniversidad.foodstore.dto.categoria.CategoriaCreate;
import com.tuuniversidad.foodstore.dto.detallePedido.DetallePedidoCreate;
import com.tuuniversidad.foodstore.dto.pedido.PedidoCreateRequest;
import com.tuuniversidad.foodstore.dto.producto.ProductoCreate;
import com.tuuniversidad.foodstore.dto.producto.ProductoDto;
import com.tuuniversidad.foodstore.dto.usuario.UsuarioCreate;
import com.tuuniversidad.foodstore.model.enums.Estado;
import com.tuuniversidad.foodstore.model.enums.FormaPago;
import com.tuuniversidad.foodstore.model.enums.Rol;
import com.tuuniversidad.foodstore.service.impl.CategoriaService;
import com.tuuniversidad.foodstore.service.impl.PedidoService;
import com.tuuniversidad.foodstore.service.impl.ProductoService;
import com.tuuniversidad.foodstore.service.impl.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final UsuarioService usuarioService;
    private final CategoriaService categoriaService;
    private final ProductoService productoService;
    private final PedidoService pedidoService;

    public DataLoader(UsuarioService usuarioService,
                      CategoriaService categoriaService,
                      ProductoService productoService,
                      PedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    @Override
    public void run(String... args) {
        if (!usuarioService.listarActivos().isEmpty()) {
            log.info("Datos ya cargados. Se omite la inicialización.");
            return;
        }

        log.info("Cargando datos iniciales desde DTOs...");

        var admin = usuarioService.crear(UsuarioCreate.builder()
                .nombre("Admin")
                .apellido("FoodStore")
                .mail("admin@admin.com")
                .celular("3815551001")
                .contrasena("123456")
                .rol(Rol.ADMIN)
                .build());

        var cliente = usuarioService.crear(UsuarioCreate.builder()
                .nombre("Nicolas")
                .apellido("Viruel")
                .mail("cliente@foodstore.com")
                .celular("3815551002")
                .contrasena("usuario123")
                .rol(Rol.USUARIO)
                .build());

        var hamburguesas = categoriaService.crear(CategoriaCreate.builder()
                .nombre("Hamburguesas")
                .descripcion("Hamburguesas artesanales y combos")
                .imagen("/assets/hamburguesa.avif")
                .build());

        var pizzas = categoriaService.crear(CategoriaCreate.builder()
                .nombre("Pizzas")
                .descripcion("Pizzas al horno de barro")
                .imagen("/assets/pizza.jpg")
                .build());

        var bebidas = categoriaService.crear(CategoriaCreate.builder()
                .nombre("Bebidas")
                .descripcion("Gaseosas, aguas y jugos")
                .imagen("/assets/bebida.jpg")
                .build());

        var productos = List.of(
                crearProducto("Hamburguesa Clasica", new BigDecimal("3500"),
                        "Pan suave, carne vacuna, queso cheddar, lechuga y tomate fresco.",
                        30, "/assets/hamburguesa.avif", true, hamburguesas.getId()),
                crearProducto("Hamburguesa Doble", new BigDecimal("4800"),
                        "Doble carne, doble queso, bacon y salsa especial.",
                        20, "/assets/hamburguesa.avif", true, hamburguesas.getId()),
                crearProducto("Pizza Muzzarella", new BigDecimal("5200"),
                        "Masa casera, salsa de tomate, abundante mozzarella y aceitunas.",
                        15, "/assets/pizza.jpg", true, pizzas.getId()),
                crearProducto("Pizza Napolitana", new BigDecimal("5800"),
                        "Muzzarella, tomate en rodajas, ajo y albahaca fresca.",
                        12, "/assets/pizza.jpg", true, pizzas.getId()),
                crearProducto("Gaseosa Cola", new BigDecimal("1800"),
                        "Bebida refrescante de 1,5 litros.",
                        40, "/assets/bebida.jpg", true, bebidas.getId()),
                crearProducto("Agua Mineral", new BigDecimal("1200"),
                        "Botella de agua sin gas 1,5 litros.",
                        35, "/assets/bebida.jpg", true, bebidas.getId()),
                crearProducto("Papas Fritas", new BigDecimal("2200"),
                        "Porcion grande de papas crujientes con aderezo opcional.",
                        25, "/assets/hamburguesa.avif", true, hamburguesas.getId()),
                crearProducto("Papas con Cheddar", new BigDecimal("2800"),
                        "Papas fritas con queso cheddar fundido y verdeo.",
                        18, "/assets/hamburguesa.avif", true, hamburguesas.getId()),
                crearProducto("Pizza Fugazzeta", new BigDecimal("5500"),
                        "Masa rellena, cebolla caramelizada y queso.",
                        10, "/assets/pizza.jpg", true, pizzas.getId()),
                crearProducto("Limonada", new BigDecimal("1500"),
                        "Limonada casera con menta y hielo.",
                        22, "/assets/bebida.jpg", true, bebidas.getId())
        );

        pedidoService.crear(PedidoCreateRequest.builder()
                .estado(Estado.CONFIRMADO)
                .formaPago(FormaPago.TARJETA)
                .idUsuario(cliente.getId())
                .telefono("26547366")
                .direccion("Calle unica 254")
                .costoEnvio(500d)
                .detallePedido(List.of(
                        DetallePedidoCreate.builder().cantidad(1).idProducto(productos.get(0).getId()).build(),
                        DetallePedidoCreate.builder().cantidad(1).idProducto(productos.get(4).getId()).build(),
                        DetallePedidoCreate.builder().cantidad(1).idProducto(productos.get(6).getId()).build()
                ))
                .build());

        pedidoService.crear(PedidoCreateRequest.builder()
                .estado(Estado.PENDIENTE)
                .formaPago(FormaPago.TRANSFERENCIA)
                .idUsuario(cliente.getId())
                .detallePedido(List.of(
                        DetallePedidoCreate.builder().cantidad(1).idProducto(productos.get(2).getId()).build(),
                        DetallePedidoCreate.builder().cantidad(2).idProducto(productos.get(5).getId()).build()
                ))
                .build());

        pedidoService.crear(PedidoCreateRequest.builder()
                .estado(Estado.TERMINADO)
                .formaPago(FormaPago.EFECTIVO)
                .idUsuario(admin.getId())
                .detallePedido(List.of(
                        DetallePedidoCreate.builder().cantidad(2).idProducto(productos.get(1).getId()).build(),
                        DetallePedidoCreate.builder().cantidad(1).idProducto(productos.get(3).getId()).build(),
                        DetallePedidoCreate.builder().cantidad(1).idProducto(productos.get(9).getId()).build()
                ))
                .build());

        log.info("Datos cargados: 2 usuarios, 3 categorías, 10 productos, 3 pedidos.");
    }

    private ProductoDto crearProducto(String nombre, BigDecimal precio, String descripcion, int stock,
                                      String imagen, boolean disponible, Long categoriaId) {
        return productoService.crear(ProductoCreate.builder()
                .nombre(nombre)
                .precio(precio)
                .descripcion(descripcion)
                .stock(stock)
                .imagen(imagen)
                .disponible(disponible)
                .categoriaId(categoriaId)
                .build());
    }
}
