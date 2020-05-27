package servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import ConnectionDatabase.DatabaseConnect;
import beans.Employee;
import beans.Picture;
import dao.DepartmentDAO;
import dao.EmployeeDAO;
import dao.PictureDAO;


/**
 * Servlet implementation class AddEditControl
 */
@MultipartConfig
@WebServlet("/AddEditControl")
public class AddEditControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher=null;
	//DAOインスタンス
	EmployeeDAO ed=new EmployeeDAO();
	DepartmentDAO dd=new DepartmentDAO();
	PictureDAO pd=new PictureDAO();
	//都道府県の配列
	String[] prefList={"未選択","北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県",
			"茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県",
			"新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県",
			"静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県",
			"奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県",
			"徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県",
			"熊本県","大分県","宮崎県","鹿児島県","沖縄県"
	};

	//登録成否の画面のパス
	private final String TRUE_FALSE="/WEB-INF/jsp/TrueOrFalse.jsp";
	//登録編集画面のパス
	private final String ADD_EDIT="/WEB-INF/jsp/AddEditEmployee.jsp";
	//デフォルト画像のパス
	private final String DEFAULT_PHOTO="/WEB-INF/picture/humanPicture.jpg";
	//登録成功時のメッセージ
	private final String TRUE="登録に成功しました。";
	//登録失敗時のメッセージ
	private final String FALSE="登録に失敗しました。<br>";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddEditControl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		//写真の名前
		String pictureName=null;

		//社員の隠しIDを取得(自動的に割り振られる連番)
		String hiddenID=request.getParameter("hiddenID");
		int id=0;
		//隠しIDがnullではなかった場合(編集の場合)
		if(hiddenID!=null) {
			try {
				id=Integer.parseInt(hiddenID);
			}catch(NumberFormatException e) {
				//登録失敗画面へ
				request.setAttribute("Message", "画面遷移に失敗しました。");
				toForward(request,response,this.TRUE_FALSE);
				return;
			}
			//社員インスタンスを渡す
			Employee emp=ed.editEmployee(id);
			request.setAttribute("emp", emp);
			//写真IDを渡す
			request.setAttribute("pictureID",emp.getPictureID());
			//写真の名前を取得する
			Picture picture=pd.editPicture(emp.getPictureID());
			pictureName=picture.getName();
		}
		//新規追加の場合
		else {
			//社員番号の設定
			int employeeID=1;
			//新規社員番号を生成
			String empNumber="EMP"+String.format("%04d", employeeID);
			//写真IDの設定
			int pictureID=1;
			//リストを取得
			List<Employee>listEmp=ed.findAll(null);
			//リストの中身があった場合
			if(listEmp.size()>0 && listEmp!=null) {
				for(Employee emp:listEmp) {
					//社員番号が等しい場合
					if(empNumber.equals(emp.getEmployeeID())) {
						//社員IDを1ずらす
						employeeID++;
						//新しい社員番号を設定
						empNumber="EMP"+String.format("%04d", employeeID);
					}
					if(pictureID==emp.getPictureID()) {
						//写真IDを1ずらす
						pictureID++;
					}
				}
			}
			//社員番号を渡す
			request.setAttribute("employeeID", empNumber);

			//写真IDを送る
			request.setAttribute("pictureID", pictureID);
			//新規写真の名前を生成
			pictureName="P"+String.format("%05d", pictureID);
		}

		//写真の名前を渡す
		request.setAttribute("pictureName", pictureName);

		//都道府県リストを渡す
		request.setAttribute("prefList", prefList);
		//フォワード
		toForward(request,response,this.ADD_EDIT);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//文字コート設定
		request.setCharacterEncoding("UTF-8");
		//新規登録、編集の判定(新規がtrue、編集がfalse)
		boolean addOrEdit=false;
		//隠しIDを取得(新規登録時は0になる)
		int hiddenID=changeNumber(request.getParameter("hiddenID"));
		if(hiddenID==0) {
			addOrEdit=true;
		}

		//社員番号を取得
		String employeeID=this.toNull(request.getParameter("employeeID"));
		//名前を取得
		String name=this.toNull(request.getParameter("name"));
		//年齢を取得
		int age=changeNumber(request.getParameter("age"));
		//性別を取得
		int gender=changeNumber(request.getParameter("gender"));
		//写真番号を取得
		int pictureID=changeNumber(request.getParameter("pictureID"));
		//郵便番号を取得
		String zip=this.toNull(request.getParameter("zip"));
		//都道府県を取得
		String pref=this.toNull(request.getParameter("prefCity"));
		//住所を取得
		String address=this.toNull(request.getParameter("address"));
		//所属部署のIDを取得
		int postID=changeNumber(request.getParameter("departmentID"));

		//入社日を取得
		Date joinDate=null;
		//入社日の文字列を取得
		String join=request.getParameter("joinDate");
		//文字列を日付に変換
		if(join!=null) {
			try {
				//入社日のインスタンス
				joinDate=Date.valueOf(join);
			}catch(IllegalArgumentException e) {
				joinDate=null;
			}
		}

		//退社日を取得
		Date outDate=null;
		//退社日の文字列を取得
		String out=request.getParameter("outDate");
		//文字列を日付に変換
		if(out!=null) {
			try {
				//退社日のインスタンス
				outDate=Date.valueOf(out);
			}catch(IllegalArgumentException e) {
				outDate=null;
			}
		}
		//社員インスタンスを生成する
		Employee employee=new Employee(hiddenID,employeeID,name,age,gender,
				pictureID,zip,pref,address,postID,joinDate,outDate);

		//写真ファイルを取得する
		Part part=request.getPart("pictureFile");
		//写真インスタンスを宣言
		Picture picture=null;
		//写真の名前を取得
		String pictureName=request.getParameter("pictureName");
		//写真ファイルがあった場合
		if(part!=null && part.getSize()>0) {
			//バッファを取得する
			byte[]image=this.getImage(part.getInputStream());
			//コンテキストタイプを取得
			String contextType=part.getContentType();
			//日付を取得
			Date date=new Date(System.currentTimeMillis());
			picture=new Picture(pictureID,pictureName,contextType,image,date);
		}
		//新規登録かつ写真ファイルがなかった場合
		else if(part.getSize()==0 && addOrEdit==true) {
			//デフォルト画像インスタンスを取得する
			picture=this.defaultPicture(request,response,pictureID,pictureName);
		}
		//編集で,すでに画像が登録済みかつ、ファイルなし
		else {
			picture=null;
		}

		//社員インスタンスをデータベースに登録ができるかの確認
		boolean isValiable=employee.isValiable();

		//入力内容に不具合があった場合//
		if(isValiable==false) {
			//エラーメッセージを渡す
			request.setAttribute("Message", this.FALSE+employee.getErrorMessage());
			//登録成否の画面へ
			toForward(request,response,this.TRUE_FALSE);
		}
		//正しく入力できている場合
		else {
			//Connectionを生成する
			DatabaseConnect pass=new DatabaseConnect();
			Connection  conn = null;
			try {
				//コネクションを作成
				conn=DriverManager.getConnection(pass.getJDBC_URL(),
						pass.getJDBC_USER(),pass.getJDBC_PASS());
				//トランスザクション宣言
				conn.setAutoCommit(false);

				//写真情報の登録の成否(親テーブル)
				boolean photoRegister=false;
				//画像登録が不要の場合
				if(picture==null) {
					photoRegister=true;
				}
				//画像登録が必要な場合
				else {
					photoRegister=pd.addEdit(picture, conn,addOrEdit);
				}

				//画像登録に失敗した場合
				if(photoRegister==false) {
					//登録取り消し
					conn.rollback();
					//メッセージセット
					request.setAttribute("Message", FALSE);
					//登録成否の画面へ
					this.toForward(request, response, this.TRUE_FALSE);
				}
				//登録に成功した場合
				else {
					//社員情報の登録の成否(子テーブル)
					boolean employeeRegister=ed.addEdit(employee, conn);
					if(employeeRegister==false) {
						conn.rollback();
						//メッセージのセット
						request.setAttribute("Message", FALSE);
						//登録の成否画面へ
						this.toForward(request, response, this.TRUE_FALSE);
					}else {
						conn.commit();
						request.setAttribute("Message", TRUE);
					}
				}

			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				if(conn!=null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
			}
			this.toForward(request, response, TRUE_FALSE);
		}
	}
	//文字列を数字に変換するメソッド
	public int changeNumber(String number) {
		int num=0;
		if(number!=null)
			try{
				num=Integer.parseInt(number);
			}catch(NumberFormatException e) {
				num=0;
			}
		return num;
	}
	//nullを変換するメソッド
	public String toNull(String str) {
		if(str==null) {
			str="";
		}
		return str;
	}
	//画面遷移メソッド
	public void toForward(HttpServletRequest request,HttpServletResponse response,String forward) {
		this.dispatcher=request.getRequestDispatcher(forward);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	//画像データを取得するメソッド
	public byte[]getImage(InputStream is){
		//返すbyte配列の型
		byte[]imageDate=null;
		// 画像データの読み込み
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		try {
			while((len=is.read(buf))>0) {
				baos.write(buf,0,len);
			}
			//バッファを取得
			imageDate=baos.toByteArray();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(is!=null) {
				try {
					is.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			if(baos!=null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
		return imageDate;
	}
	//デフォルト画像データ取得メソッド
	public Picture defaultPicture(HttpServletRequest request,HttpServletResponse response,int id,String name) {
		//返す写真インスタンスの型
		Picture picture=null;
		//日付を取得
		Date date=new Date(System.currentTimeMillis());
		//画像パスを取得
		ServletContext context=request.getServletContext();
		String pass=context.getRealPath(DEFAULT_PHOTO);
		//画像ファイルを指定
		File file=new File(pass);
		//画像のバッファを取得
		byte[]photo=null;

		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		//ファイルの読み込み4
		try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));){
			byte[]data=new byte[1024];

			int len;
			//データを読み込む
			while((len=bis.read(data))>0) {
				//ByteArrayOutputStreamに書き込み
				baos.write(data,0,len);
			}
			//バッファを取得
			photo=baos.toByteArray();
			//画像のタイプを取得
			String type="image/jpeg";
			//画像インスタンスを生成
			picture=new Picture(id,name,type,photo,date);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(baos!=null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
		return picture;
	}
}
