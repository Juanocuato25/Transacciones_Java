package org.jarenas.ca.java.jdbc.repositorio;

import org.jarenas.ca.java.jdbc.model.Categoria;
import org.jarenas.ca.java.jdbc.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioImpl implements Repositorio<Producto> {


    private Connection conn;

    public ProductoRepositorioImpl(Connection conn) {
        this.conn = conn;
    }

    public ProductoRepositorioImpl() {
    }



    public void setConn(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        //Creamos un arraylist para almacenar la lista de productos
        List<Producto> productos = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT p.*, c.nombre as categoria FROM productos " +
                     "as p INNER JOIN categorias as c ON (p.categoria_id = c.id)")) {

            while (rs.next()) {
                Producto p = crearProdcuto(rs);
                productos.add(p);
            }

        }
        return productos;
    }


    @Override
    public Producto porId(Long id) throws SQLException {
        Producto pId = null;

        //Se hace con preparedstamen porque es una sentencia preparada, que requiere de parametros
        try (PreparedStatement stmt = conn.
                     prepareStatement("SELECT p.*, c.nombre as categoria FROM productos as p " +
                             "INNER JOIN categorias as c ON (p.categoria_id = c.id) WHERE p.id = ?")) {
            //Pasamos como parametro el index de la tabla y el id a buscar
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    //Tenemos que llenar el objeto producto con todos los datos que tiene en la bd
                    pId = crearProdcuto(rs);
                }
            }
        }
        return pId;
    }

    @Override
    public Producto guardar(Producto producto) throws SQLException {
        String sql;
        if (producto.getId() != null && producto.getId() > 0) {
            sql = "UPDATE productos SET nombre=?, precio=?, categoria_id=?, sku=? WHERE id=?";
        } else {
            sql = "INSERT INTO productos(nombre, precio, categoria_id, sku, fecha_registro) VALUES (?,?,?,?,?)";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.getNombre());
            stmt.setLong(2, producto.getPrecio());
            stmt.setLong(3, producto.getCategoria().getId());
            stmt.setString(4, producto.getSku());

            if (producto.getId() != null && producto.getId() > 0) {
                stmt.setLong(5, producto.getId());
            } else {
                stmt.setDate(5, new Date(producto.getFechaRegistro().getTime()));
            }
            stmt.executeUpdate();
            if (producto.getId() == null){
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()){
                        producto.setId(rs.getLong(1));
                    }
                }
            }
        }
        return producto;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM productos WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    //Con este metodo, llenamos el objeto de la clase con todos sus datos y lo retornamos
    private static Producto crearProdcuto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getInt("precio"));
        p.setSku(rs.getString("sku"));
        p.setFechaRegistro(rs.getDate("fecha_registro"));
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("categoria_id"));
        categoria.setNombre(rs.getString("categoria"));
        p.setCategoria(categoria);
        return p;
    }
}
