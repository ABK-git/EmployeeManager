package dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ConnectionDatabase.DatabaseConnect;
import beans.Picture;

public class PictureDAO {
	//データベースパスのインスタンス
	DatabaseConnect connect=new DatabaseConnect();

	//写真を新規登録、編集するメソッド
	public boolean addEdit(Picture picture,Connection conn,boolean addEdit) {
		//返すboolean型
		boolean trueFalse=false;
		//SQL文
		String sql=null;
		//新規登録の場合
		if(addEdit==true) {
			sql="INSERT INTO PICTURE (FILENAME,CONTTYPE,PHOTO,ENTDATE,ID) "
					+ "VALUES (?,?,?,?,?)";
		}else {
			sql="UPDATE PICTURE SET FILENAME=?,CONTTYPE=?,PHOTO=?,ENTDATE=?"
					+ " WHERE ID=?";
		}
		//データベース指定
		try {
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//コネクションを取得
		Connection con=conn;
		try {
			//PrepareStatementにSQL文をセット
			PreparedStatement ps=con.prepareStatement(sql);
			//?に値を代入する
			ps.setString(1,picture.getName());
			ps.setString(2, picture.getContextType());
			ps.setBlob(3, new ByteArrayInputStream(picture.getPhoto()));
			ps.setDate(4, picture.getDate());
			ps.setInt(5, picture.getId());
			//更新
			int update=ps.executeUpdate();
			if(update!=0) {
				trueFalse=true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return trueFalse;
	}

	//写真情報選定メソッド
	private List<Picture>selectPicture(int id,int startIndex,int length){
		//返すListの型
		List<Picture>list=new ArrayList<>();
		//コネクション宣言
		Connection con=null;
		try {
			Class.forName("org.h2.Driver");
			con=DriverManager.getConnection(connect.getJDBC_URL(),
					connect.getJDBC_USER(), connect.getJDBC_PASS());
			//SQL文の設定
			String sql="SELECT * FROM PICTURE";
			//編集の場合(idがある場合)
			if(id!=0) {
				sql+=" WHERE ID='"+id+"'";
			}
			//リストをすべて取得する場合
			else {
				//IDで降順に並び替える
				sql+=" ORDER BY ID ASC";
			}
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
			while(rs.next()) {
				//結果を読み込む
				Picture picture=new Picture();
				picture.setId(rs.getInt("ID"));
				picture.setName(rs.getString("FILENAME"));
				picture.setContextType(rs.getString("CONTTYPE"));
				picture.setBlob(rs.getBlob("PHOTO").getBinaryStream());
				picture.setDate(rs.getDate("ENTDATE"));
				list.add(picture);
				//読み込み件数の確認
				if(length>0) {
					if(--length==0) {
						//読み込み終了
						break;
					}
				}
			}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	//写真データをすべて取得する
	public List<Picture>findAll(){
		//返すリストの型
		return this.selectPicture(0, 0, -1);
	}

	//編集する写真データを取得する
	public Picture editPicture(int id) {
		List<Picture>list=this.selectPicture(id, 0, -1);
		if(list.size()==0 | list==null) {
			return null;
		}
		return list.get(0);
	}

	//写真情報を削除する
	public boolean delete(int id,Connection con) {
		//返すboolean型
		boolean trueFalse=false;
		//SQL文の準備
		String sql="DELETE FROM PICTURE WHERE ID=?";
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
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
