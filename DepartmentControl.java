package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Department;
import dao.DepartmentDAO;

/**
 * Servlet implementation class DepartmentControl
 */
@WebServlet("/DepartmentControl")
public class DepartmentControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
    //daoインスタンスの取得
	DepartmentDAO dd=new DepartmentDAO();

	//フォワード
	RequestDispatcher rd=null;
	//登録成否表示ページに戻る
	private final String TRUE_FALSE="/WEB-INF/jsp/TrueOrFalse.jsp";
	//新規、編集登録画面へ
	private final String ADD_EDIT="/WEB-INF/jsp/AddEditDepartment.jsp";
	//部署リスト一覧画面へ
	private final String DEPART_LIST="/WEB-INF/jsp/DepartmentList.jsp";

	//登録成功メッセージ
	private final String TRUE_REGISTER="登録に成功しました。";
	//登録失敗メッセージ
	private final String FALSE_REGISTER="登録に失敗しました。";
	//削除成功メッセージ
	private final String DELETE_TRUE="削除に成功しました。";
	//削除失敗メッセージ
	private final String DELETE_FALSE="削除に失敗しました。";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DepartmentControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//文字コードを指定
		request.setCharacterEncoding("UTF-8");
		//編集idの取得
		int id=this.toNumber(request.getParameter("id"));

		//編集の場合(idが渡されていた場合)
		if(id!=0) {
			Department depart=dd.getDepartment(id);
			//部署情報をセットする
			request.setAttribute("Depart", depart);
		}else {
			//idを生成する
			List<Department>list=dd.allDepartment();
			//部署IDを生成する
			id=1;
			for(Department depart:list) {
				if(depart.getId()==id) {
					id++;
				}
			}
			//部署IDをセットする
			request.setAttribute("id", id);
		}
		this.toForward(request, response, ADD_EDIT);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//文字コードを設定
		request.setCharacterEncoding("UTF-8");
		//新規追加、編集の判定
		boolean addOrEdit=false;
		String action=request.getParameter("action");
		//idを取得
	    int id=this.toNumber(request.getParameter("id"));
	    //名前を取得
	    String name=request.getParameter("name");
	    //名前があった場合、新規登録か編集になる
	    if(name!=null && name.length()>0) {
	    	//部署インスタンスを生成する
			Department department=new Department(id,name);
			if(action.equals("add")) {
				addOrEdit=true;
			}
			//部署情報をデータベースに登録する
			boolean trueFalse=dd.addEdit(department, addOrEdit);
			//登録に成功した場合
			if(trueFalse==true) {
				//登録成功メッセージを送る
				request.setAttribute("Message", TRUE_REGISTER);
			}//登録に失敗した場合
			else {
				//登録失敗メッセージを送る
				request.setAttribute("Message",FALSE_REGISTER);
			}
			//登録成否画面へ
			this.toForward(request, response, TRUE_FALSE);
	    }else {
	    	//削除の場合
	    	boolean delete=dd.delete(id);
	    	if(delete==true) {
	    		//削除成功メッセージを送信
	    		request.setAttribute("Message",DELETE_TRUE);
	    	}else {
	    		//削除失敗メッセージを送信
	    		request.setAttribute("Message",DELETE_FALSE);
	    	}
	    	//部署リストをセットする
	    	request.setAttribute("DepartmentList", dd.allDepartment());
	    	//部署リスト一覧画面へ
	    	this.toForward(request, response, DEPART_LIST);
	    }
	}
	//String文を数値に変換して返すメソッド
	public int toNumber(String str) {
		int num=0;
		try {
			num=Integer.parseInt(str);
		}catch(NumberFormatException e) {
			num=0;
		}
		return num;
	}
	//フォワードするメソッド
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
