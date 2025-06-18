package de.hskl.swtp.carpooling;

import de.hskl.swtp.carpooling.dto.UserRegisterDTOIn;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.service.UserDBAccess;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@Rollback(value = true)

class DemoApplicationTests {
	@Autowired
	UserDBAccess userDBAccess;
//	@BeforeAll
//	public static void initDB()
//	{
//		try( Connection con = JDBCConnectionHelper.getInstance().getConnection() ) {
//			Statement stmt = con.createStatement();
//			stmt.executeUpdate("DELETE FROM user");
//			con.close();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//	@AfterAll
//	public static void clearDB()
//	{
//		try( Connection con = JDBCConnectionHelper.getInstance().getConnection() ) {
//			Statement stmt = con.createStatement();
//			stmt.executeUpdate("DELETE FROM user");
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
	@Test
	void contextLoads() {
	}
	@Test
	@Order(1)
	public void createUser(){
		UserRegisterDTOIn dto=new UserRegisterDTOIn("Bond"
				,"james007","james007"
				,"James","Bond"
				,new Position(0.00,0.00)
				,"16","Virginiastr."
				,"66482","Zweibrücken","bond@mi5.uk");
		User user = userDBAccess
				.createUser(dto);
		assertEquals("Bond", user.getUsername() );
		assertEquals("bond@mi5.uk", user.getEmail() );
		int userId = user.getUserId();
		assertNotNull( userDBAccess.findUserById(userId) );
	}
	@Test
	@Order(2)
	public void findUser(){
		User user =userDBAccess.findUserByUsername("bond");
		assertNotNull(user);
	}
	@Test
	@Order(3)
	public void getAllUsers(){

	}
	@Test
	@Order(4)
	public void findUserWithWrongUsername(){

	}
	@Test
	@Order(5)
	public void findUserWithWrongPassword(){

	}
	@Test
	@Order(5)
	public void findUserAndDelete(){

	}
}
