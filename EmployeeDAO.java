package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ConnectionDatabase.DatabaseConnect;
import beans.Employee;

public class EmployeeDAO {
	//データベース接続
	DatabaseConnect connect=new DatabaseConnect();
	/*
	 * idは隠しID
	 * startIndexは何番目からリストを取得するか
	 * lengthはいくつデータを取得するか
	 *
	 */
	//社員情報選定メソッド
	private List<Employee>selectEmployee(int id,int startIndex,int length,Employee conditions){
		//返すリストの箱
		List<Employee>employeeList=new ArrayList<>();
		//コネクション宣言
		Connection con=null;
		try {
			Class.forName("org.h2.Driver");
			con=DriverManager.getConnection(connect.getJDBC_URL(), connect.getJDBC_USER(), connect.getJDBC_PASS());
			//sql文の設定
			String sql="SELECT * FROM EMP";
			//検索条件があった場合
			if(conditions!=null) {
				//条件SQL文
				String where="";
				//社員番号があった場合
				if(conditions.getEmployeeID()!=null && conditions.getEmployeeID().length()>0) {
					where=" WHERE EMPID='"+conditions.getEmployeeID()+"'";
				}
				//部署IDがあった場合
				if(conditions.getPostID()!=0) {
					where+=(where.length()>0)?" AND":" WHERE";
				    where+=" POSTID='"+conditions.getPostID()+"'";
				}
				//名前の一部があった場合
				if(conditions.getName().length()>0) {
					where+=(where!=null)?" AND":" WHERE";
					where+=" NAME LIKE '%"+conditions.getName()+"%'";
				}
				//SQL文を作成
				sql+=where;
			}//end 検索条件があった場合

			else {
				//編集の場合
				if(id!=0) {
					sql+=" WHERE ID='"+id+"'";
				}
			}
			//社員番号で降順に並び替える
			sql+=" ORDER BY EMPID ASC";
			//SQL文をセット
			PreparedStatement ps=con.prepareStatement(sql);
			//リストの取得
			ResultSet rs=ps.executeQuery();

			//読み込み開始位置が0だった場合
			if(startIndex==0) {
				//読み込み位置を0からにする
				rs.beforeFirst();
			}else {
				//読み込み位置を設定する
				rs.absolute(startIndex);
			}
			while (rs.next()) {
				// 結果を読み込みます
				Employee employee = new Employee();
				employee.setId(rs.getInt("ID"));
				employee.setEmployeeID(rs.getString("EMPID"));
				employee.setName(rs.getString("NAME"));
				employee.setAge(rs.getByte("AGE"));
				employee.setGender(rs.getByte("GENDER"));
				employee.setPictureID(rs.getInt("PHOTOID"));
				employee.setZip(rs.getString("ZIP"));
				employee.setPref(rs.getString("PREF"));
				employee.setAddress(rs.getString("ADDRESS"));
				employee.setPostID(rs.getInt("POSTID"));
				employee.setJoinDate(rs.getDate("ENTDATE"));
				employee.setOutDate(rs.getDate("RETDATE"));
				//リストに格納する
				employeeList.add(employee);

				// 読み込み件数を確認します(lengthが最初から-1だったらすべて読み込み)
				if (length > 0) {
					if (--length == 0) {
						// 読み込みを終了します
						break;
					}
				}
			}

		}catch(ClassNotFoundException | SQLException e) {
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
		return employeeList;
	}

	//社員情報を新規追加または編集するメソッド
	public boolean addEdit(Employee emp,Connection conn) {
		//返すboolean型
		boolean trueOrFalse=false;
		//データベース指定
		try {
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//Connectionの取得
		Connection con=conn;
		//sql文の用意
		String sql=null;
		//編集の場合
		if (emp.getId()!=0) {
			sql = "UPDATE EMP SET EMPID=?,NAME=?,AGE=?,GENDER=?,PHOTOID=?,ZIP=?,PREF=?,"
					+ "ADDRESS=?,POSTID=?,ENTDATE=?,RETDATE=? WHERE ID="+emp.getId();
		} else {
			//新規登録の場合
			sql = "INSERT INTO EMP (EMPID,NAME,AGE,GENDER,PHOTOID,ZIP,PREF,ADDRESS,"
					+ "POSTID,ENTDATE,RETDATE) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		}
		try {
			//sql文をprepareStatementにセット
			PreparedStatement pstm=con.prepareStatement(sql);
			//?に数値を代入する
			pstm.setString(1,emp.getEmployeeID());
			pstm.setString(2, emp.getName());
			pstm.setInt(3, emp.getAge());
			pstm.setInt(4, emp.getGender());
			pstm.setInt(5, emp.getPictureID());
			pstm.setString(6, emp.getZip());
			pstm.setString(7, emp.getPref());
			pstm.setString(8, emp.getAddress());
			pstm.setInt(9,emp.getPostID());
			pstm.setDate(10,emp.getJoinDate());
			pstm.setDate(11, emp.getOutDate());
			//sql文を送信する
			int update=pstm.executeUpdate();
			//更新が成功した場合
			if(update!=0) {
				trueOrFalse=true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return trueOrFalse;
	}

	//リストをすべて出力する
	public List<Employee>findAll(Employee emp){
		//idの指定なし、リストは最初から全部取得する
		return selectEmployee(0,0,-1,emp);
	}

	//編集する社員情報を取得する
	public Employee editEmployee(int id) {
		List<Employee>list=this.selectEmployee(id, 0, -1, null);
		if(list.size()==0 | list==null) {
			return null;
		}
		return list.get(0);
	}

	//社員情報を削除する
	public boolean delete(int id,Connection con) {
		//返すboolean型
		boolean trueFalse=false;
		try {
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//sql文の用意
		String sql="DELETE FROM EMP WHERE ID=?";
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, id);
			int trueOrFalse=ps.executeUpdate();
			if(trueOrFalse!=0) {
				trueFalse=true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return trueFalse;
	}

}
