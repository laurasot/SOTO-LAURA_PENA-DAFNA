package dao.impl;

import dao.IDao;
import db.H2Connection;
import model.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoH2Odontologo  implements IDao<Odontologo> {
    public static final Logger logger = Logger.getLogger(DaoH2Odontologo.class);

    public static final String INSERT = "INSERT INTO ODONTOLOGOS VALUES(DEFAULT,?,?,?)";

    public static final String SELECT_ALL = "SELECT * FROM ODONTOLOGOS";
    @Override
    public Odontologo guardar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologoARetornar = null;

        try{
            connection = H2Connection.connectionDb();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,odontologo.getMatricula());
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3,odontologo.getApellido());
            preparedStatement.executeUpdate();
            connection.commit();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();//devuelve un objeto ResultSet que contiene las claves generadas.
            while (resultSet.next()){
                Integer idDB = resultSet.getInt(1);
                odontologoARetornar = new Odontologo(idDB, odontologo.getMatricula(), odontologo.getNombre(), odontologo.getApellido());
            }
            logger.info( "Odontologo persistido "+ odontologoARetornar);

        }catch (Exception e){
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error(e.getMessage());
                } finally {
                    try {
                        connection.setAutoCommit(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return odontologoARetornar;

    }

    @Override
    public List<Odontologo> buscarTodos() {
        Connection connection = null;
        Odontologo odontologoARetornar = null;
        List<Odontologo> odontologos = new ArrayList<>();

        try{
            connection = H2Connection.connectionDb();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while (resultSet.next()){ //por cada registro encontrado
                Integer id = resultSet.getInt(1);
                String matricula = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String apellido = resultSet.getString(4);
                odontologoARetornar = new Odontologo(id,matricula, nombre, apellido);
                odontologos.add(odontologoARetornar);
                logger.info("odontologo"+ odontologoARetornar);
            }
            connection.setAutoCommit(false);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    return  odontologos;
    }
}
