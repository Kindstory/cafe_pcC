package com.pcc.reservation.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pcc.member.db.MemberDAO;
import com.pcc.reservation.db.ReservationDAO;
import com.pcc.reservation.db.ReservationDTO;

import action.Action;
import vo.ActionForward;

public class ReservationDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8"); 
		MemberDAO daoM = new MemberDAO();
		
		System.out.println("DTO객체 생성");
		
		HttpSession session = request.getSession();
		int res_num = Integer.parseInt(request.getParameter("res_num"));
	
		ReservationDTO dto = new ReservationDTO();
		dto.setRes_num(res_num);
		
		ReservationDAO dao = new ReservationDAO();
		
		dao.deleteReservation(dto);
		
		daoM.alert(response, "예약이 삭제되었습니다.", "location.href = 'ReservationList.re?pageNum=1';");
		
		
		  
		
		return null;
	}

}
