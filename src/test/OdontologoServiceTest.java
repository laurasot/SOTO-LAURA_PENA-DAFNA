package test;

import dao.impl.DaoEnMemoria;
import dao.impl.DaoH2Odontologo;
import model.Odontologo;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.OdontologoService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoServiceTest {
    static final Logger logger = Logger.getLogger(OdontologoServiceTest.class);

    OdontologoService odontologoService =new OdontologoService(new DaoH2Odontologo());
    OdontologoService odontologoServiceEnMemoria =new OdontologoService(new DaoEnMemoria());

    @BeforeAll
    static void crearTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./cuentas;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }

    }


    @Test
    @DisplayName("Testear que un odontologo fue cargado correctamente en la bbdd")
    void caso1(){
        //Dado
        Odontologo odontologo = new Odontologo( "asdas", "Juan", "SANCHEZ");

        //cuando
        Odontologo odontologo1 = odontologoService.guardarOdontologo(odontologo);
        // entonces
        assertNotNull(odontologo1.getId());
    }


    @Test
    @DisplayName("Testear que se traen todos los odontologos de la bbdd")
    void caso2(){

        List<Odontologo> todosLosOdontologos = new ArrayList<>();

        //Dado
        Odontologo odontologo = new Odontologo( "asdas", "Juan", "SANCHEZ");
        Odontologo odontologo1 = new Odontologo( "SKJAKS", "Juana", "PEREZ");
        Odontologo odontologo3 = new Odontologo( "ASJKAJSK", "Juanita", "FERNANDEZ");

        odontologoService.guardarOdontologo(odontologo);
        odontologoService.guardarOdontologo(odontologo1);
        odontologoService.guardarOdontologo(odontologo3);

        todosLosOdontologos = odontologoService.buscarTodos();
        for ( Odontologo odontologuito:todosLosOdontologos ) {
            logger.info("El odontologo se llama "+ odontologuito.getNombre());
        }

        // entonces
        assertFalse(todosLosOdontologos.isEmpty());
    }

    @Test
    @DisplayName("Testear la base de datos en memoria")
    void caso3(){
        //Dado
        Odontologo odontologo = new Odontologo( "asdas", "Juan", "SANCHEZ");

        //cuando
        Odontologo odontologo1 = odontologoServiceEnMemoria.guardarOdontologo(odontologo);
        // entonces
        assertNotNull(odontologo1.getId());
    }

    @Test
    @DisplayName("Testear la base de datos en memoria, listar todos")
    void caso4(){
        List<Odontologo> todosLosOdontologos = new ArrayList<>();

        //Dado
        Odontologo odontologo = new Odontologo( "asdas", "Juan", "SANCHEZ");
        Odontologo odontologo1 = new Odontologo( "SKJAKS", "Juana", "PEREZ");
        Odontologo odontologo3 = new Odontologo( "ASJKAJSK", "Juanita", "FERNANDEZ");

        odontologoServiceEnMemoria.guardarOdontologo(odontologo);
        odontologoServiceEnMemoria.guardarOdontologo(odontologo1);
        odontologoServiceEnMemoria.guardarOdontologo(odontologo3);

        todosLosOdontologos = odontologoServiceEnMemoria.buscarTodos();
        for ( Odontologo odontologuito:todosLosOdontologos ) {
            logger.info("El odontologo se llama "+ odontologuito.getNombre());
        }

        // entonces
        assertFalse(todosLosOdontologos.isEmpty());
    }

}
