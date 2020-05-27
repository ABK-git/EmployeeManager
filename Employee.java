package beans;

import java.io.Serializable;
import java.sql.Date;

/*
 * * 社員の個人データを管理するDAOです。データベースのテーブルは以下のレコードより成立します。
 *
 *	<項目>	: <フィールド>		: <構成>								: <値>
 *	ID		: ID			: INT IDENTITY(1,1) PRIMARY KEY		: 1 以上の整数
 *	社員番号	: EMPID			: VARCHAR(10) not null				: 「EMP000000」等
 *	名前		: NAME			: VARCHAR(40) not null				: 「山田　太郎」等
 *  年齢		: AGE			: TINYINT							: 「20」等
 *  性別		: GENDER		: TINYINT							: 0=男性, 1=女性
 *  写真		: PHOTOID		: INT								: PhotoDAO の ID
 *  郵便番号	: ZIP			: VARCHAR(10)						: 「111-2222」等
 *  都道府県	: PREF			: VARCHAR(10)						: 「東京都」等
 *  住所		: ADDRESS		: VARCHAR(100)						: 「千代田区1-1」等
 *  所属部署	: POSTID		: INT								: PostDAO の ID
 *  入社日付	: ENTDATE		: DATE								: 2016-01-01 等
 *  退社日付	: RETDATE		: DATE
 */

public class Employee implements Serializable{
	//隠しID
	private int id=0;
	//社員番号
	private String  employeeID;
	//名前
	private String name;
	//年齢
	private int age=0;
	//性別(1が男で,2が女,0は性別未記入)
	private int gender;
	//写真ID
	private int pictureID;
	//郵便番号
	private String zip;
	//都道府県
	private String pref;
	//住所
	private String address;
	//所属部署
	private int postID;
	//入社日
	private Date joinDate=null;
	//退社日
	private Date outDate=null;

	//エラーメッセージ
	private String errorMessage=null;

	//引数なしのコンストラクタ
	public Employee() {}

	//検索用のコンストラクタ(社員ID,名前の一部,部署ID)
	public Employee(String employeeID,String name,int postID) {
		this.employeeID=employeeID;
		this.name=name;
		this.postID=postID;
	}

	//引数ありのコンストラクタ
	public Employee(int id, String employeeID, String name, int age, int gender, int pictureID, String zip, String pref,
			String address, int postID, Date joinDate, Date outDate) {
		super();
		this.id = id;
		this.employeeID = employeeID;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.pictureID = pictureID;
		this.zip = zip;
		this.pref = pref;
		this.address = address;
		this.postID = postID;
		this.joinDate = joinDate;
		this.outDate = outDate;
	}

	//データベースに登録できるかのチェック
	public boolean isValiable() {
		//返すbooleanの型
		boolean trueFalse=false;
		//渡された引数に登録するエラーメッセージ
		String error="";
		//社員番号がnullか10文字を超える場合
		if(this.employeeID==null || this.employeeID.length()>10) {
			error="社員番号を10文字以内で入力してください!<br>";
		}
		//名前がnullか40文字を超える場合
		if(this.name==null || this.name.length()>40) {
			error+=System.lineSeparator()+"名前を40文字以内で入力してください!<br>";
		}else if(this.name.length()==0) {
			error+="名前を入力してください!<br>";
		}
		//年齢がoかどうか
		if(this.age==0) {
			error+="半角数字で年齢を入力してください!<br>";
		}
		//郵便番号が10文字を超える場合
		if(this.zip.length()>10 || this.zip==null) {
			error+="正しい郵便番号を入力してください!<br>";
		}
		//住所が100文字を超える場合
		if(this.address.length()>100) {
			error+="住所は100文字以内で入力してください!<br>";
		}
		//エラーメッセージがなければ登録可能
		if(error.length()==0) {
			trueFalse=true;
		}else {
			//エラーメッセージをセットする
			this.errorMessage=error;
		}
		return trueFalse;
	}

	//getterメソッド,setterメソッド
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getPictureID() {
		return pictureID;
	}

	public void setPictureID(int pictureID) {
		this.pictureID = pictureID;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPref() {
		return pref;
	}

	public void setPref(String pref) {
		this.pref = pref;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) {
		this.postID = postID;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
