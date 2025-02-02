package com.pcc.product.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

// 주문 관련 모든 메서드를 생성하는 클래스

public class OrderDAO {

	private Connection con = null;
	private String sql ="";
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public OrderDAO () {
		//System.out.println("DAO : DB 연결을 위한 모든 정보 준비 완료");
	}
	
	// 1. CP를 이용한 DB 연결 -----------------------------------------
	private Connection getConnect() {
		try {
			// 1-1. 프로젝트 정보 초기화
			Context initCTX = new InitialContext();
			// 1-2. 초기화된 프로젝트 중 데이터 관련 정보 불러오기
			DataSource ds = (DataSource) initCTX.lookup("java:comp/env/jdbc/cafe_pcc");
			con = ds.getConnection();
			
			//System.out.println("DAO : DB 연결 완료");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
	}
	
	// 2. getConnect() 메서드의 역순으로 DB 연결 해제 ------------------
	
	private void closeDB() {
		try {
			if(rs != null)	rs.close();
			if(pstmt != null)  pstmt.close();
			if(con != null)  con.close();
			//System.out.println("DAO : DB 자원(rs, pstmt, con) 해제 완료");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("DAO : DB 연결 해제");
	}

	
	
	
	// 3. orderWrite() 메서드  -----------------------------------------
	public void orderWrite(OrderDTO order_dto) {
		System.out.println("4. orderWrite DAO");
		
		int order_num = 0;
		
		try{
			//1. 드라이버 로드
			//2. 디비 연결
			con = getConnect();
			//3. sql 작성 & pstmt 객체
			//   카트 번호(cart_num) 계산 (생성된 마지막 번호 + 1)
			sql = "select max(order_num) from orders";
			pstmt = con.prepareStatement(sql);
			//4. sql 실행
			rs = pstmt.executeQuery();//DB 값을 다 가져오겠다
			
			//5. 데이터 처리(글번호 계산 : 마지막 글번호 + 1)
			if(rs.next()){
				order_num = rs.getInt("max(order_num)")+1;
			}
			else{
				order_num = 1;
			}
			
//			System.out.println(" DAO : 카트번호  cart_num : " + cart_num);
			
			//카트 데이터 입력
			//3. sql 작성 & pstmt 객체
			sql = "insert into orders(order_num, order_price, pickup_time)"
					+ " values(?,?,?);";
			
			pstmt = con.prepareStatement(sql);
			
			//???
			pstmt.setInt(1, order_num);
			pstmt.setInt(2,  order_dto.getOrder_price());
			pstmt.setInt(3,  order_dto.getPickup_time());
			
			//4. sql 실행
			pstmt.executeUpdate();//insert 구문은 Update 사용
			
//			System.out.println(" DAO : 카트 담기 완료");
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
		
		
		
	}

	
	
	
	
	// 4.   -----------------------------------------
	
	
	
	
	// 5.   -----------------------------------------
	
	
	
	
	// 6.   -----------------------------------------
	
	
	
	
	// 7.   -----------------------------------------
	
	
	
	
	// 8.   -----------------------------------------
	
	
	
	
	// 9.   -----------------------------------------
	
	
	
	
	// 10.   -----------------------------------------
	
	
	
	
	
	
	
}

