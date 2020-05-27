package beans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;

public class Picture implements Serializable{
	//写真ID
	private int id=0;
	//名前
	private String name=null;
	//コンテキストタイプ
	private String contextType=null;
	//画像データのバイト配列
	byte[]photo=null;
	//入力日
	private Date date=null;

	//引数なしのコンストラクタ
	public Picture() {}

	//引数ありのコンストラクタ
	public Picture(int id, String name, String contextType, byte[] photo, Date date) {
		super();
		this.id = id;
		this.name = name;
		this.contextType = contextType;
		this.photo = photo;
		this.date = date;
	}

	//getter,setterメソッド
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContextType() {
		return contextType;
	}

	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public void setBlob(InputStream is) {
		// 画像データの読み込み
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		try {
			while((len=is.read(buf))>0) {
				baos.write(buf,0,len);
			}
			//バッファを取得
			this.photo=baos.toByteArray();
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
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
