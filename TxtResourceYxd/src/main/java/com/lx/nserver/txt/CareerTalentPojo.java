package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*职业资质
**/
public class CareerTalentPojo{

	public CareerTalentPojo(){
	}

	/**资质编号**/
	@TxtKey
	@TxtInt
	private int id;

	/**职业类型**/
	@TxtInt
	private int careerId;

	/**资质名称**/
	@TxtString
	private String talentName;

	/**资质出现机率**/
	@TxtInt
	private int talentRate;

	/**增加的气力**/
	@TxtInt
	private int addAir;

	/**增加的内力**/
	@TxtInt
	private int addInnerForce;

	/**增加的敏捷**/
	@TxtInt
	private int addAgility;

	/**增加的掌控**/
	@TxtInt
	private int addControl;

	/**增加的坚韧**/
	@TxtInt
	private int addTenacity;

	/**减少的气力**/
	@TxtInt
	private int reduceAir;

	/**减少内力**/
	@TxtInt
	private int reduceInnerForce;

	/**减少敏捷**/
	@TxtInt
	private int reduceAgility;

	/**减少掌控**/
	@TxtInt
	private int reduceControl;

	/**减少坚韧**/
	@TxtInt
	private int reduceTenacity;



	public int getId(){
		return id;
	}

	public void setId(int _id){
		id= _id;
	}

	public int getCareerId(){
		return careerId;
	}

	public void setCareerId(int _careerId){
		careerId= _careerId;
	}

	public String getTalentName(){
		return talentName;
	}

	public void setTalentName(String _talentName){
		talentName= _talentName;
	}

	public int getTalentRate(){
		return talentRate;
	}

	public void setTalentRate(int _talentRate){
		talentRate= _talentRate;
	}

	public int getAddAir(){
		return addAir;
	}

	public void setAddAir(int _addAir){
		addAir= _addAir;
	}

	public int getAddInnerForce(){
		return addInnerForce;
	}

	public void setAddInnerForce(int _addInnerForce){
		addInnerForce= _addInnerForce;
	}

	public int getAddAgility(){
		return addAgility;
	}

	public void setAddAgility(int _addAgility){
		addAgility= _addAgility;
	}

	public int getAddControl(){
		return addControl;
	}

	public void setAddControl(int _addControl){
		addControl= _addControl;
	}

	public int getAddTenacity(){
		return addTenacity;
	}

	public void setAddTenacity(int _addTenacity){
		addTenacity= _addTenacity;
	}

	public int getReduceAir(){
		return reduceAir;
	}

	public void setReduceAir(int _reduceAir){
		reduceAir= _reduceAir;
	}

	public int getReduceInnerForce(){
		return reduceInnerForce;
	}

	public void setReduceInnerForce(int _reduceInnerForce){
		reduceInnerForce= _reduceInnerForce;
	}

	public int getReduceAgility(){
		return reduceAgility;
	}

	public void setReduceAgility(int _reduceAgility){
		reduceAgility= _reduceAgility;
	}

	public int getReduceControl(){
		return reduceControl;
	}

	public void setReduceControl(int _reduceControl){
		reduceControl= _reduceControl;
	}

	public int getReduceTenacity(){
		return reduceTenacity;
	}

	public void setReduceTenacity(int _reduceTenacity){
		reduceTenacity= _reduceTenacity;
	}



}