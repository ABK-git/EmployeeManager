package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Department;
import beans.Employee;
import dao.DepartmentDAO;
import dao.EmployeeDAO;

/**
 * Servlet implementation class FirstAccess
 */
@WebServlet("/FirstAccess")
public class FirstAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FirstAccess() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//文字コードをセットする
		request.setCharacterEncoding("UTF-8");
		EmployeeDAO ed=new EmployeeDAO();
		//セッションスコープの生成
		HttpSession session=request.getSession();
		//検索条件を取得する
		Employee employee=(Employee)session.getAttribute("Employee");
		String departName="";
		//リスト宣言
		List<Employee>empList=new ArrayList<>();
		//検索条件があった場合
		if(employee!=null){
			//リストを取得
			empList=ed.findAll(employee);
			//部署IDがあった場合
			if(employee.getPostID()!=0){
				DepartmentDAO dd=new DepartmentDAO();
				//部署名取得
				Department dep=dd.getDepartment(employee.getPostID());
				departName=dep.getName();
				//部署名を送る
				request.setAttribute("departName", departName);
			}
		}else{
			//検索条件がなかった場合
			empList=ed.findAll(null);
		}
		//リストを送る
		request.setAttribute("empList",empList);

		//フォワードする
		RequestDispatcher rd=request.getRequestDispatcher("/TopFinalEmployee.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
