package org.jarenas.ca.java.jdbc.repositorio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RepositorioCategoria <T>{
    List<T> listarCategoria() throws SQLException;

    T porId(Long id) throws SQLException;

    T guardar(T t) throws SQLException;

    void eliminar(Long id) throws SQLException;
    void setConn(Connection conn) throws SQLException;


}
