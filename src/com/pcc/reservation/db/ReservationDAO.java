package com.pcc.reservation.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.pcc.reservation.db.ReservationDTO;

// 예약 관련 모든 메서드를 생성하는 클래스

public class ReservationDAO {

	private Connection con = null;
	private String sql = "";
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public ReservationDAO() {}

	// 1. CP를 이용한 DB 연결 -----------------------------------------
	private Connection getConnect() {
		try {
			// 1-1. 프로젝트 정보 초기화
			Context initCTX = new InitialContext();
			// 1-2. 초기화된 프로젝트 중 데이터 관련 정보 불러오기
			DataSource ds = (DataSource) initCTX.lookup("java:comp/env/jdbc/cafe_pcc");
			con = ds.getConnection();

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return con;
	}

	// 2. getConnect() 메서드의 역순으로 DB 연결 해제 ------------------

	private void closeDB() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 3. -----------------------------------------

	public int reservation(ReservationDTO dto) throws Exception {
		
		int result = -1;  // 1: 예약 가능, 0: 오전 예약 가득 참, -1: 오후 예약 가득 참 
		int res_num = 0;
		int res_table = 0;
		
		try {
			con = getConnect();
			
			// 예약 시간이 오전(10시~13시)일 때
			if(dto.getRes_time() < 13) {
				// 특정 날의 오전 예약 개수 구하기
				sql = "select sum(res_table) from reservations "
						+ "where res_date = ? and res_time <= 13";			
				pstmt = con.prepareStatement(sql);
				pstmt.setDate(1, dto.getRes_date());
				rs = pstmt.executeQuery();
				
				if(rs.next()){					
					System.out.println(dto.getRes_date()+" 오전 예약되어있는 테이블 수  : "
							+ rs.getInt(1));
					
					// 예약 가능한 테이블이 남아있을 때 
					if(rs.getInt(1)  <= 13 ||  // 예약 가능한 테이블이 2개 이상일 때
							rs.getInt(1) == 14 && dto.getRes_persons() <5 ) { // 예약 가능한 테이블이 한 개이고 예약인원이 4명 이하일 때
						sql = "select max(res_num) from reservations";
						pstmt = con.prepareStatement(sql);
						rs = pstmt.executeQuery();
						
						// 예약번호 부여
						if(rs.next()) {
							res_num = rs.getInt(1) + 1;
						
							System.out.println("예약번호 : "+res_num);
							
							sql = "insert into reservations (res_num, mem_num, name, "
									+ "res_date, res_time, res_persons, res_table) "
									+"values (?,?,?,?,?,?,?)";
							pstmt = con.prepareStatement(sql);
							
							pstmt.setInt(1, res_num);
							pstmt.setInt(2,  dto.getMem_num());
							pstmt.setString(3, dto.getName());
							pstmt.setDate(4, dto.getRes_date());
							pstmt.setInt(5,  dto.getRes_time());
							pstmt.setInt(6, dto.getRes_persons());
								res_table = (dto.getRes_persons()/4) + (dto.getRes_persons()%4==0?0:1);
							pstmt.setInt(7, res_table);
							
							pstmt.executeUpdate();
							System.out.println(dto.getRes_date() +" "+dto.getRes_time()+"시" 
								+ dto.getRes_persons()+"명 예약 완료");
							
							result = 1;
						}	
					// 예약 가능한 테이블이 없을 때
					} else {
						result = 0;
					}
				} 
			}

			// 예약 시간이 오후(13시~17시)일 때
			else if(dto.getRes_time() > 13) {
				// 특정 날의 오전 예약 개수 구하기
				sql = "select sum(res_table) from reservations "
						+ "where res_date = ? and res_time > 13";			
				pstmt = con.prepareStatement(sql);
				pstmt.setDate(1, dto.getRes_date());
				rs = pstmt.executeQuery();
				
				if(rs.next()){					
					System.out.println(dto.getRes_date()+" 오전 예약되어있는 테이블 수  : "
							+ rs.getInt(1));
					
					// 예약 가능한 테이블이 남아있을 때 
					if(rs.getInt(1) <= 13 ||  // 예약 가능한 테이블이 2개 이상일 때
							rs.getInt(1) == 14 && dto.getRes_persons() <5 ) { // 예약 가능한 테이블이 한 개이고 예약인원이 4명 이하일 때
						sql = "select max(res_num) from reservations";
						pstmt = con.prepareStatement(sql);
						rs = pstmt.executeQuery();
						
						// 예약번호 부여
						if(rs.next()) {
							res_num = rs.getInt(1) + 1;
						
							System.out.println("예약번호 : "+res_num);
							
							sql = "insert into reservations (res_num, mem_num, name, "
									+ "res_date, res_time, res_persons, res_table) "
									+"values (?,?,?,?,?,?,?)";
							pstmt = con.prepareStatement(sql);
							
							pstmt.setInt(1, res_num);
							pstmt.setInt(2,  dto.getMem_num());
							pstmt.setString(3, dto.getName());
							pstmt.setDate(4, dto.getRes_date());
							pstmt.setInt(5,  dto.getRes_time());
							pstmt.setInt(6, dto.getRes_persons());
								res_table = (dto.getRes_persons()/4) + (dto.getRes_persons()%4==0?0:1);
							pstmt.setInt(7, res_table);
							
							pstmt.executeUpdate();
							System.out.println(dto.getRes_date() +" "+dto.getRes_time()+"시 " 
								+ dto.getRes_persons()+"명 예약 완료");
							
							result = 1;
						}	
					// 예약 가능한 테이블이 없을 때
					} else {
						result = 0;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return result;
	}

	public List<ReservationDTO> reservationList() {
		List<ReservationDTO> reservationList = new ArrayList<ReservationDTO>();
		
		try{
			con = getConnect();
			
			sql = "select* from reservations";
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				ReservationDTO dto = new ReservationDTO();
				
				
				dto.setRes_num(rs.getInt(1));
				dto.setMem_num(rs.getInt(2));
				dto.setName(rs.getString(3));
				dto.setRes_date(rs.getDate(4));
				dto.setRes_time(rs.getInt(5));
				dto.setRes_persons(rs.getInt(6));

				reservationList.add(dto);
				
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			closeDB();
		}
		 
		
		
		return reservationList;
		
	}
	
	public List<ReservationDTO> reservationList(int startRow, int pageSize) {
		List<ReservationDTO> reservationList = new ArrayList<ReservationDTO>();
		
		try{
			con = getConnect();
			
			sql = "select * from reservations order by res_num desc limit ?, ?";   
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, startRow - 1);
			pstmt.setInt(2, pageSize);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				ReservationDTO dto = new ReservationDTO();
				
				
				dto.setRes_num(rs.getInt("res_num"));
				dto.setMem_num(rs.getInt("mem_num"));
				dto.setName(rs.getString("name"));
				dto.setRes_date(rs.getDate("res_date"));
				dto.setRes_time(rs.getInt("res_time"));
				dto.setRes_persons(rs.getInt("res_persons"));
				
				reservationList.add(dto);
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			closeDB();
		}
		return reservationList;
	}
		
	public List<ReservationDTO> reservationList(int mem_num, int startRow, int pageSize) {
		List<ReservationDTO> reservationList = new ArrayList<>();
		
		try{
				con = getConnect();
				
				sql = "select * from reservations where mem_num = ? "
						+ "order by res_num desc limit ?, ?";   
				pstmt = con.prepareStatement(sql);
				
				pstmt.setInt(1,  mem_num);
				pstmt.setInt(2, startRow - 1);
				pstmt.setInt(3, pageSize);
				
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					ReservationDTO dto = new ReservationDTO();
					
					dto.setRes_num(rs.getInt("res_num"));
					dto.setMem_num(rs.getInt("mem_num"));
					dto.setName(rs.getString("name"));
					dto.setRes_date(rs.getDate("res_date"));
					dto.setRes_time(rs.getInt("res_time"));
					dto.setRes_persons(rs.getInt("res_persons"));
						
					reservationList.add(dto);
				}
		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			closeDB();
		}
		return reservationList;
	}

	
	// 매니저용 예약 내역 조회
	public ReservationDTO getReservationInfo(int res_num){
		ReservationDTO dto = null;
		
		try{
			con = getConnect();
			sql = "select * from reservations where res_num = ? order by res_num desc";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, res_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ReservationDTO();
				dto.setRes_num(res_num);
				dto.setMem_num(rs.getInt("mem_num"));
				dto.setName(rs.getString("name"));
				dto.setRes_date(rs.getDate("res_date"));
				dto.setRes_time(rs.getInt("res_time"));
				dto.setRes_persons(rs.getInt("res_persons"));
				dto.setRes_table(rs.getInt("res_table"));
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			closeDB();
		}
		return dto;
	}
	// 회원용 예약 내역 조회
	public ReservationDTO getReservationInfo(int res_num, int mem_num){
		ReservationDTO dto = null;
		
		try{
			con = getConnect();
			sql = "select count(*) from reservations where res_num = ? and mem_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, mem_num);
			pstmt.setInt(2,  res_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ReservationDTO();
				dto.setRes_num(res_num);
				dto.setMem_num(mem_num);
				dto.setName(rs.getString("name"));
				dto.setRes_date(rs.getDate("res_date"));
				dto.setRes_time(rs.getInt("res_time"));
				dto.setRes_persons(rs.getInt("res_persons"));
				dto.setRes_table(rs.getInt("res_table"));
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			closeDB();
		}

		return dto;
	}

	public int getReservationCount() {
		int cnt = 0;
		try {
			con = getConnect();
			sql = "select count(*) from reservations";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return cnt = rs.getInt(1)+1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return cnt;
	}

	public int updateReservation(int res_num, ReservationDTO dto) {
		int result = -1;  // 1: 예약 가능, 0: 오전 예약 가득 참, -1: 오후 예약 가득 참 
		
		try {
			con = getConnect();
			
			// 예약 시간이 오전(10시~13시)일 때
			if(dto.getRes_time() < 13) {
				// 특정 날의 오전 예약 개수 구하기
				sql = "select sum(res_table) from reservations "
						+ "where res_date = ? and res_time <= 13";			
				pstmt = con.prepareStatement(sql);
				pstmt.setDate(1, dto.getRes_date());
				rs = pstmt.executeQuery();
				
				if(rs.next()){					
					System.out.println(dto.getRes_date()+" 오전 예약되어있는 테이블 수  : "
							+ rs.getInt(1));
					
					// 예약 가능한 테이블이 남아있을 때 
					if(rs.getInt(1)  <= 13 ||  // 예약 가능한 테이블이 2개 이상일 때
							rs.getInt(1) == 14 && dto.getRes_persons() <5 ) { // 예약 가능한 테이블이 한 개이고 예약인원이 4명 이하일 때
												
					sql = "update reservations set res_date=?, res_time=?, res_persons=?, res_table=? "
								+"where res_num = ?";
					
					pstmt = con.prepareStatement(sql);
					
					pstmt.setDate(1, dto.getRes_date());
					pstmt.setInt(2,  dto.getRes_time());
					pstmt.setInt(3, dto.getRes_persons());
						int res_table = (dto.getRes_persons()/4) + (dto.getRes_persons()%4==0?0:1);
					pstmt.setInt(4, res_table);
					pstmt.setInt(5, res_num);
					
					pstmt.executeUpdate();
					
					System.out.println(dto.getRes_date() +" "+dto.getRes_time()+"시 " 
						+ dto.getRes_persons()+"명 예약 수정 완료");
					
					result = 1;
					}	
					// 예약 가능한 테이블이 없을 때
				} else {
					result = 0;
				}
			} 
			
		// 예약 시간이 오후(13시~17시)일 때
			else if(dto.getRes_time() > 13) {
			// 특정 날의 오전 예약 개수 구하기
			sql = "select sum(res_table) from reservations "
					+ "where res_date = ? and res_time > 13";			
			pstmt = con.prepareStatement(sql);
			pstmt.setDate(1, dto.getRes_date());
			rs = pstmt.executeQuery();
			
			if(rs.next()){					
				System.out.println(dto.getRes_date()+" 오전 예약되어있는 테이블 수  : "
						+ rs.getInt(1));
				
				// 예약 가능한 테이블이 남아있을 때 
				if(rs.getInt(1) <= 13 ||  // 예약 가능한 테이블이 2개 이상일 때
						rs.getInt(1) == 14 && dto.getRes_persons() <5 ) { // 예약 가능한 테이블이 한 개이고 예약인원이 4명 이하일 때
					sql = "update reservations "
							+ "set res_date=?, res_time=?, res_persons=?, res_table=? "
							+"where res_num = ?";
				
				pstmt = con.prepareStatement(sql);
				
				pstmt.setDate(1, dto.getRes_date());
				pstmt.setInt(2,  dto.getRes_time());
				pstmt.setInt(3, dto.getRes_persons());
					int res_table = (dto.getRes_persons()/4) + (dto.getRes_persons()%4==0?0:1);
				pstmt.setInt(4, res_table);
				pstmt.setInt(5, res_num);
				
				pstmt.executeUpdate();
				
				System.out.println(dto.getRes_date() +" "+dto.getRes_time()+"시 " 
					+ dto.getRes_persons()+"명 예약 수정 완료");
						
						result = 1;
				}	
				// 예약 가능한 테이블이 없을 때
				} else {
					result = 0;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return result;
	}
	
	public void deleteReservation (ReservationDTO dto) {
        
 
        try {
        	con = getConnect();
            String sql = "delete from reservations where res_num = ?";
            pstmt = con.prepareStatement(sql);
            // ?에 값을 넣기
 
            pstmt.setInt(1, dto.getRes_num());
           
            pstmt.executeUpdate();
 
            con.close();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }


} // DAO 끝
