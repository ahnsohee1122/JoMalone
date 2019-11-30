package kh.jomalone.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kh.jomalone.DAO.AskDAO;
import kh.jomalone.DTO.AskCommentsDTO;
import kh.jomalone.DTO.AskDTO;
import kh.jomalone.configuration.Configuration;

@WebServlet("*.ask")
public class AskBoardController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		String cmd = request.getRequestURI().substring(request.getContextPath().length());
		/* ajax 한글깨짐 방지 */
		response.setCharacterEncoding("utf8");
		response.setContentType("text/html; charset=UTF-8");

		// String id = (String) request.getSession().getAttribute("loginId");
		// 밑에 회원id랑 관리자id 임의지정.(테스트 끝나면 지우기!!!!!!!!!!!!!!!!!)
		String id = "TestID";
		request.getSession().setAttribute("loginId", id);
		//String id = "TestID2";
		//String id = "TestID3";
		//request.getSession().setAttribute("adminId", "admin");
		//request.getSession().invalidate();
		AskDAO dao = AskDAO.getInstance();

		if (cmd.contentEquals("/list.ask")) {//나의문의게시판
			request.getSession().invalidate();
			try {
				int currentPage = 1;
				String page = request.getParameter("currentPage");
				if (page != null) {
					currentPage = Integer.parseInt(request.getParameter("currentPage"));
				}
				int start = currentPage * (Configuration.recordCountPerPage) - (Configuration.recordCountPerPage - 1);
				int end = currentPage * (Configuration.recordCountPerPage);
				List<AskDTO> result = dao.selectByPageById(start, end, id);
				String pageNavi = dao.getPageNavi(currentPage,"list.ask");
				request.setAttribute("selectResult", result);
				request.setAttribute("pageNavi", pageNavi);
				request.getRequestDispatcher("askboard/AskList.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
			
		}else if (cmd.contentEquals("/newList.ask")) {//(관리자)신규문의게시판
			request.getSession().setAttribute("adminId", "admin");
			request.getSession().setAttribute("fromAdminAskPage", "yes");
			try {
				int currentPage = 1;
				String page = request.getParameter("currentPage");
				if (page != null) {
					currentPage = Integer.parseInt(request.getParameter("currentPage"));
				}
				int start = currentPage * (Configuration.recordCountPerPage) - (Configuration.recordCountPerPage - 1);
				int end = currentPage * (Configuration.recordCountPerPage);
				List<AskDTO> result = dao.selectByPageNotYetAnswer(start, end);
				String pageNavi = dao.getPageNavi(currentPage,"newList.ask");
				request.setAttribute("selectResult", result);
				request.setAttribute("pageNavi", pageNavi);
				request.getRequestDispatcher("askboard/NewAskList.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		}else if (cmd.contentEquals("/allList.ask")) {//(관리자)전체문의게시판
			request.getSession().setAttribute("adminId", "admin");
			request.getSession().setAttribute("fromAdminAskPage", "yes");
			try {
				int currentPage = 1;
				String page = request.getParameter("currentPage");
				if (page != null) {
					currentPage = Integer.parseInt(request.getParameter("currentPage"));
				}
				int start = currentPage * (Configuration.recordCountPerPage) - (Configuration.recordCountPerPage - 1);
				int end = currentPage * (Configuration.recordCountPerPage);
				List<AskDTO> result = dao.selectByPage(start, end);
				String pageNavi = dao.getPageNavi(currentPage,"allList.ask");
				request.setAttribute("selectResult", result);
				request.setAttribute("pageNavi", pageNavi);
				request.getRequestDispatcher("askboard/AllAskList.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		} else if (cmd.contentEquals("/write.ask")) {// 문의글 등록
			String uploadPath = request.getServletContext().getRealPath("/files");
			File uploadFilePath = new File(uploadPath);
			if (!uploadFilePath.exists()) {
				uploadFilePath.mkdir();
			}
			int maxSize = 1024 * 1024 * 10;
			MultipartRequest multi = new MultipartRequest(request, uploadPath, maxSize, "UTF8",
					new DefaultFileRenamePolicy());

			String askCode = multi.getParameter("askMenu");
			String title = multi.getParameter("title");
			String contents = multi.getParameter("contents");
			String emailCheck = multi.getParameter("emailCheck");
			if (emailCheck == null) {
				emailCheck = "N";
			} else {
				emailCheck = "Y";
			}
			// 전달값 확인
			System.out.println(askCode + " : " + title + " : " + contents + " : " + emailCheck);

			try {
				dao.insertAsk(new AskDTO(0, askCode, title, contents, id, null, null, emailCheck));
				int seq = dao.findLatestAskSeqById(id);
				response.sendRedirect("read.ask?no=" + seq);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}

		} else if (cmd.contentEquals("/read.ask")) {// 문의글 상세페이지 보기
			int seq = Integer.parseInt(request.getParameter("no"));
			System.out.println(seq);
			try {
				// 본문
				AskDTO readDTO = dao.selectAskBySeq(seq);
				request.setAttribute("readDTO", readDTO);
				// 댓글
				List<AskCommentsDTO> coResult = dao.selectCommentsByAskSeq(seq);				
				request.setAttribute("coList", coResult);

				request.getRequestDispatcher("askboard/AskDetailView.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
			
		} else if (cmd.contentEquals("/modify.ask")) {// 문의글 수정
			int seq = Integer.parseInt(request.getParameter("askSeq"));
			try {
				AskDTO result = dao.selectAskBySeq(seq);
				request.setAttribute("readDTO", result);
				request.getRequestDispatcher("askboard/AskModify.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		} else if (cmd.contentEquals("/modifyConfirm.ask")) {// 문의글 수정완료			
			String uploadPath = request.getServletContext().getRealPath("/files");
			File uploadFilePath = new File(uploadPath);
			if (!uploadFilePath.exists()) {
				uploadFilePath.mkdir();
			}
			int maxSize = 1024 * 1024 * 10;
			MultipartRequest multi = new MultipartRequest(request, uploadPath, maxSize, "UTF8",
					new DefaultFileRenamePolicy());

			int seq = Integer.parseInt(multi.getParameter("askSeq"));			
			String askCode = multi.getParameter("askMenu");
			String title = multi.getParameter("title");
			String contents = multi.getParameter("contents");
			String emailCheck = multi.getParameter("emailCheck");
			if (emailCheck == null) {
				emailCheck = "N";
			} else {
				emailCheck = "Y";
			}
			// 전달값 확인
			System.out.println(askCode + " : " + title + " : " + contents + " : " + emailCheck);

			try {
				int result = dao.updateAsk(new AskDTO(seq, askCode, title, contents, id, null, null, emailCheck));
				System.out.println(result);
				response.sendRedirect("read.ask?no=" + seq);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
			
		} else if (cmd.contentEquals("/delete.ask")) {// 문의글 삭제
			int seq = Integer.parseInt(request.getParameter("no"));
			System.out.println(seq);
			try {
				dao.deleteAsk(seq);
				response.sendRedirect("list.ask");
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		} else if (cmd.contentEquals("/summernote.ask")) {
			String uploadPath = request.getServletContext().getRealPath("/files");
			File uploadFilePath = new File(uploadPath);
			if (!uploadFilePath.exists()) {
				uploadFilePath.mkdir();
			}
			int maxSize = 1024 * 1024 * 10;
			MultipartRequest multi = new MultipartRequest(request, uploadPath, maxSize, "UTF8",
					new DefaultFileRenamePolicy());

			List<String> fileNameList = new ArrayList<>();
			Enumeration e = multi.getFileNames();
			while (e.hasMoreElements()) {
				String file = (String) e.nextElement();
				String fileName = multi.getFilesystemName(file);
				fileNameList.add(fileName);
			}

			JsonArray list = new JsonArray();
			for (String fileName : fileNameList) {
				JsonObject obj = new JsonObject();
				obj.addProperty("url", request.getContextPath() + "/files/" + fileName);
				list.add(obj);
			}

			System.out.println(list);
			response.getWriter().append(list.toString());
		} else if (cmd.contentEquals("/writeComment.ask")) {// 댓글작성 ajax
			int originSeq = Integer.parseInt(request.getParameter("writingSeq"));
			String contents = request.getParameter("contents");
			PrintWriter pWriter = response.getWriter();
			try {
				dao.insertAskComment(new AskCommentsDTO(0,originSeq,contents,null));
				dao.AnswerAskCondition("Y", originSeq);
				List<AskCommentsDTO> result = dao.selectCommentsByAskSeq(originSeq);				
				Gson g = new Gson();
				String dtoList = g.toJson(result);
				pWriter.append(dtoList);

			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}

		} else if (cmd.contentEquals("/deleteComment.ask")) {
			int originSeq = Integer.parseInt(request.getParameter("no"));
			int seq = Integer.parseInt(request.getParameter("coNo"));
			System.out.println(originSeq);
			System.out.println(seq);
			try {
				dao.deleteAskComment(seq);
				dao.AnswerAskCondition("N", originSeq);
				response.sendRedirect("read.ask?no="+originSeq);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}

		} else if (cmd.contentEquals("/modifyComment.ask")) {
			int seq = Integer.parseInt(request.getParameter("coNo"));
			System.out.println(seq);
			PrintWriter pWriter = response.getWriter();
			try {
				AskCommentsDTO dto = dao.selectCommentByCoSeq(seq);
				Gson g = new Gson();
				String contents = g.toJson(dto);
				pWriter.append(contents);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		} else if (cmd.contentEquals("/modifyCommentConfirm.ask")) {
			int originSeq = Integer.parseInt(request.getParameter("modSeq"));
			int seq = Integer.parseInt(request.getParameter("modCoNo"));
			String contents = (String) request.getParameter("modComment");
			System.out.println(originSeq + " : " + seq + " : " + contents);
			try {
				dao.updateAskComment(contents, seq);
				response.sendRedirect("read.ask?no=" + originSeq);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
