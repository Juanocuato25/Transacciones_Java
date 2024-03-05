package org.jarenas.ca.java.jdbc;import org.jarenas.ca.java.jdbc.model.Categoria;import org.jarenas.ca.java.jdbc.model.Producto;import org.jarenas.ca.java.jdbc.repositorio.CategoriaRepositorioImpl;import org.jarenas.ca.java.jdbc.repositorio.ProductoRepositorioImpl;import org.jarenas.ca.java.jdbc.repositorio.Repositorio;import org.jarenas.ca.java.jdbc.repositorio.RepositorioCategoria;import org.jarenas.ca.java.jdbc.servicio.CatalogoServicio;import org.jarenas.ca.java.jdbc.servicio.Servicio;import org.jarenas.ca.java.jdbc.util.ConexionBaseDatos;import java.sql.Connection;import java.sql.SQLException;import java.util.Date;public class EjemploJdbc3 {    public static void main(String[] args) throws SQLException {        Servicio servicio = new CatalogoServicio();        System.out.println(" === LISTADO DE PRODUCTOS ===");        servicio.listar().forEach(System.out::println);        System.out.println(" =============================================== ");        System.out.println(" === Insertar nueva categoria === ");        Categoria cat = new Categoria();        cat.setNombre("Jardineria");        System.out.println(" =============================================== ");        System.out.println(" === Guardar producto === ");        Producto prod = new Producto();        prod.setNombre("Abono 10kg");        prod.setPrecio(35);        prod.setFechaRegistro(new Date());        prod.setSku("garden335");        servicio.guardarProductoConCategoria(prod, cat);        System.out.println("Producto guardado: " + prod.getId());        servicio.listar().forEach(System.out::println);    }}