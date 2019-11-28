package kh.jomalone.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import kh.jomalone.DTO.CartDTO;

public class CartDAO {
	private static CartDAO instance;
	private static BasicDataSource bds = new BasicDataSource();
	private CartDAO() {
		bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		bds.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
		bds.setUsername("jomalone");
		bds.setPassword("jomalone");
		bds.setInitialSize(30);
	}
	public synchronized static CartDAO getInstance() {
		if(instance == null) {
			instance = new CartDAO();
		}
		return instance;
	}
	private Connection getConnection() throws Exception{
		return bds.getConnection();
	}
	
	public int insertCart(CartDTO dto) throws Exception{
		String sql = "insert into cart values(cart_seq.nextval,?,?,?,?,?)";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, dto.getMem_id());
			pstat.setString(2, dto.getMem_name());
			pstat.setString(3, dto.getProd_name());
			pstat.setInt(4, dto.getProd_quantity());
			pstat.setInt(5, dto.getPrice());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public boolean checkProdExist(String mem_id,String prod_name)throws Exception{
		String sql = "select * from cart where mem_id=? and prod_name=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, mem_id);
			pstat.setString(2, prod_name);
			try(ResultSet rs = pstat.executeQuery();){
				boolean result = rs.next();
				return result;
			}
		}
	}
	
	public int deleteCart(int seq) throws Exception{
		String sql = "delete from cart where seq=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public int updateProdQuantity(int prod_quantity,int seq) throws Exception{ // 수량변경
		String sql = "update cart set prod_quantity=? where seq=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setInt(1,prod_quantity);
			pstat.setInt(2, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public List<CartDTO> selectCart(String mem_id) throws Exception{
		String sql = "select * from cart where mem_id=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, mem_id);
			try(ResultSet rs = pstat.executeQuery();){
				List<CartDTO> list = new ArrayList<>();
				while(rs.next()) {
					CartDTO dto = new CartDTO();
					dto.setSeq(rs.getInt(1));
					dto.setMem_id(rs.getString(2));
					dto.setMem_name(rs.getString(3));
					dto.setProd_name(rs.getString(4));
					dto.setProd_quantity(rs.getInt(5));
					dto.setPrice(rs.getInt(6));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	public int sumProdQuantity(int prod_quantity,String mem_id,String prod_name) throws Exception{ // 상품명 겹쳤을때 수량 합치기
		String sql = "update cart set prod_quantity=prod_quantity+? where mem_id=? and prod_name=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setInt(1, prod_quantity);
			pstat.setString(2, mem_id);
			pstat.setString(3, prod_name);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
}