package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ConnectionDatabase.DatabaseConnect;
import beans.Department;
import dao.DepartmentDAO;
import dao.EmployeeDAO;
import dao.PictureDAO;

/**
 * Servlet implementation class TopListControl
 */
@WebServlet("/TopListControl")
public class TopListControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//フォワード用
	RequestDispatcher rd=null;
	//トップ画面へのパス
	private final String EMPLOYEE_TOP="/FirstAccess";
	//部署リスト画面へのパス
	private final String DEPART_TOP="/WEB-INF/jsp/DepartmentList.jsp";
	//データベース接続パス
	DatabaseConnect dc=new DatabaseConnect();
	//daoインスタンス
	EmployeeDAO ed=new EmployeeDAO();
	PictureDAO pd=new PictureDAO();
	//削除成功メッセージ
    private final String DELETE_TRUE="データの削除に成功しました。";
    //削除失敗メッセージ
    private final String DELETE_FALSE="データの削除に失敗しました。";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopListControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		//操作メニューを取得する
		String action=request.getParameter("action");
		switch(action) {
		//検索条件削除の場合
		case "removeSession":
			//sessionインスタンスを取得
			HttpSession session=request.getSession();
			//検索条件を削除
			session.removeAttribute("Employee");
			this.toForward(request, response,EMPLOYEE_TOP);
			break;

		//部署リストページに移行する場合
		case "DepartList":
			//部署リストを取得する
			DepartmentDAO dd=new DepartmentDAO();
			List<Department>list=dd.allDepartment();
			//部署リストをセットする
			request.setAttribute("DepartmentList", list);
			this.toForward(request, response, DEPART_TOP);
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		//社員番号取得
		int employeeID=this.toNumber(request.getParameter("employeeID"));
		//写真番号取得
		int pictureID=this.toNumber(request.getParameter("pictureID"));
		//社員情報と写真番号が正しく受け取れてた場合
		if(employeeID!=0 && pictureID!=0) {
			//社員情報と写真情報を削除する
			boolean trueFalse=this.removeData(employeeID,pictureID);
			//削除成功の場合
			if(trueFalse==true) {
				//削除成功メッセージを送る
				request.setAttribute("Message", this.DELETE_TRUE);
			}else {
				//削除失敗メッセージを送る
				request.setAttribute("Message", this.DELETE_FALSE);
			}
		}
		//フォワード
		this.toForward(request, response, EMPLOYEE_TOP);
	}

	//フォワードメソッド
	public void toForward(HttpServletRequest request,HttpServletResponse response,String pass) {
		this.rd=request.getRequestDispatcher(pass);
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
	//nullを数値に変換するメソッド
	public int toNumber(String str) {
		int number=0;
		if(str!=null) {
			try {
				number=Integer.parseInt(str);
			}catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return number;
	}

	//社員情報と写真情報を削除するメソッド
	public boolean removeData(int employee,int picture) {
		//返すboolean型
		boolean trueFalse=false;
		//Connectionを生成する
		Connection con=null;
		try {
			con=DriverManager.getConnection
					(dc.getJDBC_URL(),dc.getJDBC_USER(), dc.getJDBC_PASS());
			//トランスザクション宣言
			con.setAutoCommit(false);
			//社員情報を削除する
			boolean deleteEmployee=ed.delete(employee, con);
			//削除が成功した場合
			if(deleteEmployee==true) {
				//写真情報を削除
				boolean deletePicture=pd.delete(picture, con);
				//削除に成功したら
				if(deletePicture==true) {
					//trueを返す
					trueFalse=true;
					//処理を確定する
					con.commit();
				}else {
					//処理を取り消す
					con.rollback();
				}
			}else {
				//処理を取り消す
				con.rollback();
			}

		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
		return trueFalse;
	}

}
