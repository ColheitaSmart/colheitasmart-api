
package br.com.colheitasmart.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("oracle")
public class MigracaoSenhaConfig {

    @Bean
    public ApplicationRunner migrarSenhaAgricultor(
            DataSource dataSource) {

        return args -> {
            try (Connection conexao = dataSource.getConnection()) {

                String consulta = """
                    SELECT COUNT(*)
                    FROM USER_TAB_COLUMNS
                    WHERE TABLE_NAME = 'TB_AGRICULTOR'
                      AND COLUMN_NAME = 'DS_SENHA_HASH'
                    """;

                boolean colunaExiste;

                try (PreparedStatement ps =
                         conexao.prepareStatement(consulta);
                     ResultSet rs = ps.executeQuery()) {

                    rs.next();
                    colunaExiste = rs.getInt(1) > 0;
                }

                if (!colunaExiste) {
                    try (Statement stmt = conexao.createStatement()) {
                        stmt.executeUpdate("""
                            ALTER TABLE TB_AGRICULTOR
                            ADD DS_SENHA_HASH VARCHAR2(100 CHAR)
                            """);
                    }

                    System.out.println(
                        "ColheitaSmart: coluna de senha criada."
                    );
                } else {
                    System.out.println(
                        "ColheitaSmart: coluna de senha ja existe."
                    );
                }

            } catch (SQLException ex) {
                throw new IllegalStateException(
                    "Falha ao preparar a coluna de senha no Oracle.",
                    ex
                );
            }
        };
    }
}
