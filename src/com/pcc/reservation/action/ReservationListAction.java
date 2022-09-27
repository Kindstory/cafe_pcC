package com.pcc.reservation.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pcc.member.db.MemberDAO;
import com.pcc.reservation.db.ReservationDAO;
import com.pcc.reservation.db.ReservationDTO;
import com.sun.org.apache.bcel.internal.generic.DASTORE;

import action.Action;
import vo.ActionForward;

public class ReservationListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8"); // request라는 내장객체는 자바에 없잖아 JSP에만
												// 있으니까 ㅇㅇ
		
		MemberDAO daoM = new MemberDAO();

		// ReservationDAO 객체 생성
		ReservationDAO dao = new ReservationDAO();
		int cnt = dao.getReservationCount();
		System.out.println("예약 목록 총 개수 : "+cnt);
		// 페이징
		// 처리---------------------------------------------------------------------------

		// 2가지 형태
		// DB에서 통쨰로 들고와서 10개씩 보여주는거, DB에서 아애 10개씩 들고오는거.
		// 따로 들고오는게 효율적임.
		// 데이터가 많아 질수록 나눠서 들고오는게 좋음. 지금이야 몇개 안되서 차이가 얼마 안남.

		// 한 페이지에 보여줄 글의 개수 설정

		// ./BoardList.com?pageNum=2&pageSize=3
		
		String urlPageSize = "10";

		urlPageSize = request.getParameter("pageSize");
		if (urlPageSize == null) {
			urlPageSize = "10";
		}

		int pageSize = Integer.parseInt(urlPageSize);
//		System.out.println("페이지 사이즈 : "+pageSize);

		String pageNum = request.getParameter("pageNum");
//		System.out.println("페이지 번호 : "+pageNum);
		if (pageNum == null) {
			pageNum = "1";
		}
//		System.out.println("페이지 번호 : "+pageNum);

		int currentPage = Integer.parseInt(pageNum);
		int startRow = (currentPage - 1) * pageSize + 1;
		int endRow = currentPage * pageSize;

		// 페이징 처리 2 : 목록 하단 숫자 버튼 --------------------------
		
		HttpSession session = request.getSession();
		List<ReservationDTO> reservationList = null;
		if(session != null) {
			String mem_num = (String)session.getAttribute("mem_num");
			String mgr_num = (String)session.getAttribute("mgr_num");
					
			if(mem_num != null) {
				reservationList = 
						dao.reservationList(Integer.parseInt(mem_num), startRow, pageSize);
				request.setAttribute("reservationList", reservationList);
			} else if(mgr_num != null ){
				reservationList = dao.reservationList(startRow, pageSize);
				request.setAttribute("reservationList", reservationList);
			} else {
				daoM.alert(response, "로그인 후 이용 가능합니다.", "location.href='./MainPage.pcc';");
				return null;
			}
		}	
		
		int pageCount = (cnt/pageSize)+(cnt%pageSize== 0 ? 0:1);
		int pageBlock = 5;
		int startPage = ((currentPage-1)/pageBlock)*pageBlock+1;
		int endPage = startPage+pageBlock-1;

		if(endPage > pageCount){
			endPage = pageCount;
		}
		System.out.println("M :  페이징 처리정보 완료");

		// 페이징 처리 2(하단 페이지
		// 링크)-----------------------------------------------------------------------------

		request.setAttribute("pageNum", pageNum);
		request.setAttribute("cnt", cnt);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("pageBlock", pageBlock);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		
		System.out.println("reservationList 정보 전달 완료");
		ActionForward forward = new ActionForward();
		forward.setPath("./reservation/reservationList.jsp");
		forward.setRedirect(false);

		return forward;
	}

}
