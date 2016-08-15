package com.lx.game.res.item;

/**
 * ClassName:Property <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-3 上午11:25:22 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public abstract class Property {
	
	private int id;
	
	private int folderableNum;
	
	private byte cantSold;
	
	private int bagClass;
	
	public byte getCantSold() {
		return cantSold;
	}
	
	public void setCantSold(byte cantSold) {
		this.cantSold = cantSold;
	}
	
	public int getBagClass() {
		return bagClass;
	}
	
	public void setBagClass(int bagClass) {
		this.bagClass = bagClass;
	}
	
	/**
	 * Creates a new instance of Property.
	 * 
	 */
	public Property() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * create:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param uuid
	 * @return
	 */
	public Item create(long uuid) {
		Item item = subCreate(uuid);
		return item;
	}
	
	abstract Item subCreate(long uuid);
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFolderableNum() {
		return folderableNum;
	}
	
	public void setFolderableNum(int folderableNum) {
		this.folderableNum = folderableNum;
	}
	
}
