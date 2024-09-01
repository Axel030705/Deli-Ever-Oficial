package Cliente.Pedidos;

import java.io.Serializable;

public class PedidoClase implements Serializable {
    private String idPedido;
    private String Fecha_Hora;
    private String Nombre_Cliente;
    private String Direccion;
    private String Producto;
    private String MontoSinDescuento;
    private String Estado;
    private String Descuento;
    private String idTienda;
    private String ImgProducto;
    private String Cantidad;
    private String idCliente;
    private String idProducto;
    private String Calificado;
    private String Ubi_cliente;
    private String Ubi_vendedor;
    private String propina;
    private String referencia;
    private String MontoConDescuento;

    public PedidoClase() {

    }

    public PedidoClase(String idPedido, String fecha_Hora, String nombre_Cliente, String direccion, String producto, String montoSinDescuento, String estado, String descuento, String idTienda, String imgProducto, String cantidad, String idCliente, String idProducto, String calificado, String ubi_cliente, String ubi_vendedor, String propina, String referencia, String montoConDescuento) {
        this.idPedido = idPedido;
        Fecha_Hora = fecha_Hora;
        Nombre_Cliente = nombre_Cliente;
        Direccion = direccion;
        Producto = producto;
        MontoSinDescuento = montoSinDescuento;
        Estado = estado;
        Descuento = descuento;
        this.idTienda = idTienda;
        ImgProducto = imgProducto;
        Cantidad = cantidad;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        Calificado = calificado;
        Ubi_cliente = ubi_cliente;
        Ubi_vendedor = ubi_vendedor;
        this.propina = propina;
        this.referencia = referencia;
        MontoConDescuento = montoConDescuento;
    }
    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getFecha_Hora() {
        return Fecha_Hora;
    }

    public void setFecha_Hora(String fecha_Hora) {
        Fecha_Hora = fecha_Hora;
    }

    public String getNombre_Cliente() {
        return Nombre_Cliente;
    }

    public void setNombre_Cliente(String nombre_Cliente) {
        Nombre_Cliente = nombre_Cliente;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String promocion) {
        Descuento = promocion;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getImgProducto() {
        return ImgProducto;
    }

    public void setImgProducto(String imgProducto) {
        ImgProducto = imgProducto;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getCalificado() {
        return Calificado;
    }

    public void setCalificado(String calificado) {
        Calificado = calificado;
    }

    public String getUbi_cliente() {
        return Ubi_cliente;
    }

    public void setUbi_cliente(String ubi_cliente) {
        Ubi_cliente = ubi_cliente;
    }

    public String getUbi_vendedor() {
        return Ubi_vendedor;
    }

    public void setUbi_vendedor(String ubi_vendedor) {
        Ubi_vendedor = ubi_vendedor;
    }

    public String getPropina() {
        return propina;
    }

    public void setPropina(String propina) {
        this.propina = propina;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getMontoSinDescuento() {
        return MontoSinDescuento;
    }

    public void setMontoSinDescuento(String montoSinDescuento) {
        MontoSinDescuento = montoSinDescuento;
    }

    public String getMontoConDescuento() {
        return MontoConDescuento;
    }

    public void setMontoConDescuento(String montoConDescuento) {
        MontoConDescuento = montoConDescuento;
    }
}


