package servlet;

import java.io.IOException;
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

/**
 * Servlet implementation class SerchControl
 */
@WebServlet(name = "SearchControl", urlPatterns = { "/SearchControl" })
public class SerchControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//検索用の画面遷移パス
	private final String SEARCH="/WEB-INF/jsp/SearchEmployee.jsp";
	//トップ画面への遷移パス
	private final String TOP="/FirstAccess";
	//フォワード
	RequestDispatcher rd=null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SerchControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//部署リストを生成
		DepartmentDAO dd=new DepartmentDAO();
		List<Department>list=dd.allDepartment();
		//リストの最初に未配属インスタンスを入れる
		Department depart=new Department(0,"未配属");
		list.add(0,depart);
		//部署リストを渡す
		request.setAttribute("list",list);
		//フォワード
		this.toForward(request, response,this.SEARCH);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//文字コードセット
		request.setCharacterEncoding("UTF-8");
		//社員番号を取得
		String employeeID=request.getParameter("employeeID");
		//名前の一部を取得
		String name=request.getParameter("name");
		//部署IDを取得
		String departmentID=request.getParameter("Department");
		int postID=0;
		try {
			postID=Integer.parseInt(departmentID);
		}catch(NumberFormatException e){
			postID=0;
		}
		if(employeeID.length()>0 || name.length()>0 || postID>0) {
			//社員情報インスタンスを生成
			Employee employee=new Employee(employeeID,name,postID);
			//セッションスコープに社員情報を格納する
			HttpSession session=request.getSession();
			session.setAttribute("Employee",employee);
		}
		//フォワード
		this.toForward(request, response,this.TOP );
	}

	//フォワードメソッド
	public void toForward(HttpServletRequest request,HttpServletResponse response,String forward) {
		this.rd=request.getRequestDispatcher(forward);
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
