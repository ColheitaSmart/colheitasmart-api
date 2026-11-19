package br.com.colheitasmart.exception;
import java.sql.SQLException;
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(SQLException causa) { super("Falha na persistencia.", causa); }
    public boolean conflito() {
        SQLException sql = (SQLException) getCause();
        for (SQLException atual = sql; atual != null; atual = atual.getNextException()) {
            int codigo = atual.getErrorCode();
            String estado = atual.getSQLState();
            if (codigo == 1 || codigo == 2291 || codigo == 2292
                || (estado != null && estado.startsWith("23"))) return true;
        }
        return false;
    }
}
