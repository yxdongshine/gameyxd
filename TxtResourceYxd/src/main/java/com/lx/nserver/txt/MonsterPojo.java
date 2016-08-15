package com.lx.nserver.txt;

import com.lib.utils.IConst;
import com.lib.utils.ToolUtils;
import com.read.tool.txt.TxtFloat;
import com.read.tool.txt.TxtInt;
import com.read.tool.txt.TxtKey;
import com.read.tool.txt.TxtString;

/**
 * 怪物
 **/

public class MonsterPojo {
	
	public MonsterPojo() {
	}
	
	/** 怪物Id **/
	@TxtKey(name = "")
	@TxtInt(name = "")
	private int id;
	
	/** 怪物名 **/
	@TxtString(name = "")
	private String name;
	
	/**怪物模型**/
	@TxtString(name = "")
	private String modelName;
	/** 怪物等级 **/
	@TxtInt(name = "")
	private int level;
	
	/** 怪物类型  1=普通怪物
2=精英怪物
3=首领怪物
4=BOSS**/
	@TxtInt(name = "")
	private int type;
	
	/** 怪物的血 **/
	@TxtInt(name = "")
	private int hp;
	/** 怪物的技能id组(怪物技能列表，列表内是技能id，由AI调用技能) **/
	@TxtString(name = "")
	private String skillList;
	private int skills[] = new int[0];
	
	/** 怪物朝向 **/
	@TxtFloat(name = "")
	private float dir;
	
	/** 待机机率 **/
	@TxtInt(name = "")
	private int idleRate;
	
	/** 待机持续时间 **/
	@TxtInt(name = "")
	private int idleLastTime;
	
	/** 巡逻机率 **/
	@TxtInt(name = "")
	private int patrolRate;
	
	/** 巡逻半径 **/
	@TxtInt(name = "")
	private int patrolRange;
	
	/** 每次巡逻时间 **/
	@TxtInt(name = "")
	private int patrolInterval;
	
	/** 警戒范围(半径) **/
	@TxtInt(name = "")
	private int searchRange;
	
	/** 追击范围(半径) **/
	@TxtInt(name = "")
	private int chaseRange;
	
	/** 攻击类型  1=主动攻击
	2=被动攻击**/
	@TxtInt(name = "")
	private int atkType;
	
	/** 重生时间(毫秒) **/
	@TxtInt
	private int rebornTime;
	
	/** 模型 **/
	@TxtString(name = "")
	private String monsterModel;
	
	/** 头像图片 **/
	@TxtString(name = "")
	private String face;
	
	/** 贴图 **/
	@TxtString(name = "")
	private String chartlet;
	
	/** 特效 **/
	@TxtString(name = "")
	private String effect;
	
	/** 特效绑定位置（骨骼等） **/
	@TxtString(name = "")
	private String effcetPosition;
	
	/** 特效绑定位置偏移（x,y,z） **/
	@TxtString(name = "")
	private String effectOffset;
	
	/** 移动动画播放速度 **/
	@TxtInt(name = "")
	private int moveAniSpeed;
	
	/** 旋转角速度（度/秒）转向用 **/
	@TxtInt(name = "")
	private int rotateSpeed;
	
	/** 尸体存在的时间（秒） **/
	@TxtInt(name = "")
	private int corpseTime;
	
	/** 移动声 **/
	@TxtString(name = "")
	private String moveSound;
	
	/** 普通攻击声 **/
	@TxtString(name = "")
	private String attackSound;
	
	/** 受击声or受击叫声 **/
	@TxtString(name = "")
	private String onhitSound;
	
	/** 死亡叫声 **/
	@TxtString(name = "")
	private String dieSound;
	
	/** AI的id **/
	@TxtInt(name = "")
	private int aiId;
	
	/** 杀怪奖励货币 **/
	@TxtInt(name = "")
	private int rewardGold;
	
	/** 杀怪奖励经验 **/
	@TxtInt(name = "")
	private int rewardExp;
	
	/** 奖励单个道具（必掉） **/
	@TxtInt(name = "")
	private int rewardItem;
	
	/** 列表，按几率奖励多个道具之一 **/
	@TxtString(name = "")
	private String rewardItemList;
	
	/** 第一列道具id,第二列数量,第三列机率 **/
	private int rewardItems[][] = new int[0][];
	private int totalRate = 0;//总的奖励道具机率
	
	/** 气力 **/
	@TxtInt(name = "")
	private int air;
	
	/** 灵敏 **/
	@TxtInt(name = "")
	private int agility;
	
	/** 内力 **/
	@TxtInt(name = "")
	private int innerForce;
	
	/** 掌控 **/
	@TxtInt(name = "")
	private int control;
	
	/** 坚韧 **/
	@TxtInt(name = "")
	private int tenacity;
	
	/** 外功攻击 **/
	@TxtInt(name = "")
	private int externalForceAttack;
	
	/** 内功攻击 **/
	@TxtInt(name = "")
	private int innerForceAttack;
	
	/** 烈阳攻击 **/
	@TxtInt(name = "")
	private int scorchingSunAttack;
	
	/** 天罡攻击 **/
	@TxtInt(name = "")
	private int ploughAttack;
	
	/** 幽冥攻击 **/
	@TxtInt(name = "")
	private int netherAttack;
	
	/** 太阴攻击 **/
	@TxtInt(name = "")
	private int lunarAttack;
	
	/** 近战命中 **/
	@TxtInt(name = "")
	private int meleeHitRate;
	
	/** 远程命中 **/
	@TxtInt(name = "")
	private int remoteHitRate;
	
	/** 近战闪避 **/
	@TxtInt(name = "")
	private int meleeDodgeRate;
	
	/** 远程闪避 **/
	@TxtInt(name = "")
	private int remoteDodgeRate;
	
	/** 最大生命 **/
	@TxtInt(name = "")
	private int maxHp;
	
	/** 当前生命 **/
	@TxtInt(name = "")
	private int curHp;
	
	/** 最大真气 **/
	@TxtInt(name = "")
	private int maxMp;
	
	/** 当前真气 **/
	@TxtInt(name = "")
	private int curMp;
	
	/** 防御 **/
	@TxtInt(name = "")
	private int defence;
	
	/** 外功伤害减免 **/
	@TxtInt(name = "")
	private int externalForceDamageReduce;
	
	/** 内功伤害减免 **/
	@TxtInt(name = "")
	private int innerForceDamageReduce;
	
	/** 烈阳伤害减免 **/
	@TxtInt(name = "")
	private int scorechingSunDamageReduce;
	
	/** 天罡伤害减免 **/
	@TxtInt(name = "")
	private int ploughDamageReduce;
	
	/** 幽冥伤害减免 **/
	@TxtInt(name = "")
	private int netherDamageReduce;
	
	/** 太阴伤害减免 **/
	@TxtInt(name = "")
	private int lunarDamageReduce;
	
	/** 会心率 **/
	@TxtInt(name = "")
	private int knowingRate;
	
	/** 抵挡率 **/
	@TxtInt(name = "")
	private int parryRate;
	
	/** 攻击频率 **/
	@TxtInt(name = "")
	private int attackSpeed;
	
	/** 攻击范围 **/
	@TxtInt(name = "")
	private int attackScope;
	
	/** 移动速度 **/
	@TxtInt(name = "")
	private int speed;
	
	/** 所有伤害降低 **/
	@TxtInt(name = "")
	private int allHurtLower;
	
	/** 经验获得基数 **/
	@TxtInt
	private int expBase;
	
	/** 对人伤害系数 **/
	@TxtInt
	private int targetRoleCoefficient;
	
	/** 对怪伤害系数 **/
	@TxtInt
	private int targetMonsterCoefficient;
	
	/** 对宠伤害系数 **/
	@TxtInt
	private int targetPetCoefficient;
	
	/** 对召唤物伤害系数 **/
	@TxtInt
	private int targetCallMonsterCoefficient;
	
	/** 被人伤害系数 **/
	@TxtInt
	private int coefficientFromRole;
	
	/** 被怪伤害系数 **/
	@TxtInt
	private int coefficientFromMonster;
	
	/** 被宠伤害系数 **/
	@TxtInt
	private int coefficientFromPet;
	
	/** 被召唤物伤害系数 **/
	@TxtInt
	private int cofficientFromCallMonster;
	
	/** 生命回复速度 **/
	@TxtInt
	private int hpRecoverSpeed;
	
	/** 真气回复速度 **/
	@TxtInt
	private int mpRecoverSpeed;
	
	/** 投射物移动速度 **/
	@TxtInt
	private int spritSpeed;
	
	/** 投射穿透率 **/
	@TxtInt
	private int spritPierceRate;
	
	/** 眩晕概率 **/
	@TxtInt
	private int dizzinessRate;
	
	/** 眩晕抵抗 **/
	@TxtInt
	private int dizzinessResistance;
	
	/** 生命偷取比例 **/
	@TxtInt
	private int hpSteal;
	
	/** 真气偷取比例 **/
	@TxtInt
	private int mpSteal;
	
	/** 冰冻概率 **/
	@TxtInt
	private int frozenRate;
	
	/** 冰冻抗性 **/
	@TxtInt
	private int frozenResistance;
	
	/** 生命偷取抗性 **/
	@TxtInt
	private int hpStealResistance;
	
	/** 真气偷取抗性 **/
	@TxtInt
	private int mpStealResistance;
	
	/** 生命流失抗性 **/
	@TxtInt
	private int hpBurnResistance;
	
	/** 真气流失抗性 **/
	@TxtInt
	private int mpBurnResistance;
	
	/** 外功伤害吸收 **/
	@TxtInt
	private int externalForceDamageObsorb;
	
	/** 内功伤害吸收 **/
	@TxtInt
	private int innerForceDamageObsorb;
	
	/** 烈阳伤害吸收 **/
	@TxtInt
	private int scorchingSunDamageObsorb;
	
	/** 天罡伤害吸收 **/
	@TxtInt
	private int ploughDamageObsorb;
	
	/** 幽冥伤害吸收 **/
	@TxtInt
	private int netherDamageObsorb;
	
	/** 太阴伤害吸收 **/
	@TxtInt
	private int lunarDamageObsorb;
	
	/** 外功反弹 **/
	@TxtInt
	private int externalForceRebind;
	
	/** 内功反弹 **/
	@TxtInt
	private int innerForceRebind;
	
	/** 烈阳反弹 **/
	@TxtInt
	private int scorchingSunRebind;
	
	/** 天罡反弹 **/
	@TxtInt
	private int ploughRebind;
	
	/** 幽冥反弹 **/
	@TxtInt
	private int netherRebind;
	
	/** 太阴反弹 **/
	@TxtInt
	private int lunarRebind;
	
	/** 外功伤害系数 **/
	@TxtInt
	private int externalForceDamageCoefficient;
	
	/** 内功伤害系数 **/
	@TxtInt
	private int innerForceDamageCoefficient;
	
	/** 烈阳伤害系数 **/
	@TxtInt
	private int scorchingSunDamageCoefficient;
	
	/** 天罡伤害系数 **/
	@TxtInt
	private int ploughDamageCoefficent;
	
	/** 幽冥伤害系数 **/
	@TxtInt
	private int netherDamageCoefficient;
	
	/** 太阴伤害系数 **/
	@TxtInt
	private int lunarDamageCoefficient;
	
	/** 会心伤害 **/
	@TxtInt
	private int knowingHurtHurtRate;
	
	/** 抵挡能力 **/
	@TxtInt
	private int parryHurtRate;
	
	/** 所有伤害提升 **/
	@TxtInt
	private int allHurtAdd;
	
	/** 重创攻击 **/
	@TxtInt
	private int heavyAttack;
	
	/** 护体防御 **/
	@TxtInt
	private int watchBosDef;
	
	/** 定身概率 **/
	@TxtInt
	private int noMoveRate;
	
	/** 定身抗性 **/
	@TxtInt
	private int noMoveResistance;
	
	/** 韧性 **/
	@TxtInt
	private int toughnessRate;
	
	/** 韧性能力 **/
	@TxtInt
	private int toughnessEffect;
	
	/** 破防 **/
	@TxtInt
	private int disrupting;
	
	/** 破防能力 **/
	@TxtInt
	private int disruptingEffect;
	
	/** 额外技能点 **/
	@TxtInt
	private int extraSkillPoint;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	public String getModelName(){
		return modelName;
	}

	public void setModelName(String _modelName){
		modelName= _modelName;
	}
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int _level) {
		level = _level;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int _type) {
		type = _type;
	}
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int _hp) {
		hp = _hp;
	}
	
	public String getSkillList() {
		return skillList;
	}
	
	public void setSkillList(String _skillList) {
		skillList = _skillList;
		if (!ToolUtils.isStringNull(skillList)) {
			skills = ToolUtils.splitStringToInt(skillList, IConst.WELL);
		}
	}
	
	public float getDir() {
		return dir;
	}
	
	public void setDir(float _dir) {
		dir = _dir;
	}
	
	public int getIdleRate() {
		return idleRate;
	}
	
	public void setIdleRate(int _idleRate) {
		idleRate = _idleRate;
	}
	
	public int getIdleLastTime() {
		return idleLastTime;
	}
	
	public void setIdleLastTime(int _idleLastTime) {
		idleLastTime = _idleLastTime;
	}
	
	public int getPatrolRate() {
		return patrolRate;
	}
	
	public void setPatrolRate(int _patrolRate) {
		patrolRate = _patrolRate;
	}
	
	public int getPatrolRange() {
		return patrolRange;
	}
	
	public void setPatrolRange(int _patrolRange) {
		patrolRange = _patrolRange;
	}
	
	public int getPatrolInterval() {
		return patrolInterval;
	}
	
	public void setPatrolInterval(int _patrolInterval) {
		patrolInterval = _patrolInterval;
	}
	
	public int getSearchRange() {
		return searchRange;
	}
	
	public void setSearchRange(int _searchRange) {
		searchRange = _searchRange;
	}
	
	public int getChaseRange() {
		return chaseRange;
	}
	
	public void setChaseRange(int _chaseRange) {
		chaseRange = _chaseRange;
	}
	
	public int getAtkType() {
		return atkType;
	}
	
	public void setAtkType(int _atkType) {
		atkType = _atkType;
	}
	
	public int getRebornTime() {
		return rebornTime;
	}
	
	public void setRebornTime(int _rebornTime) {
		rebornTime = _rebornTime;
	}
	
	public String getMonsterModel() {
		return monsterModel;
	}
	
	public void setMonsterModel(String _monsterModel) {
		monsterModel = _monsterModel;
	}
	
	public String getFace() {
		return face;
	}
	
	public void setFace(String _face) {
		face = _face;
	}
	
	public String getChartlet() {
		return chartlet;
	}
	
	public void setChartlet(String _chartlet) {
		chartlet = _chartlet;
	}
	
	public String getEffect() {
		return effect;
	}
	
	public void setEffect(String _effect) {
		effect = _effect;
	}
	
	public String getEffcetPosition() {
		return effcetPosition;
	}
	
	public void setEffcetPosition(String _effcetPosition) {
		effcetPosition = _effcetPosition;
	}
	
	public String getEffectOffset() {
		return effectOffset;
	}
	
	public void setEffectOffset(String _effectOffset) {
		effectOffset = _effectOffset;
	}
	
	public int getMoveAniSpeed() {
		return moveAniSpeed;
	}
	
	public void setMoveAniSpeed(int _moveAniSpeed) {
		moveAniSpeed = _moveAniSpeed;
	}
	
	public int getRotateSpeed() {
		return rotateSpeed;
	}
	
	public void setRotateSpeed(int _rotateSpeed) {
		rotateSpeed = _rotateSpeed;
	}
	
	public int getCorpseTime() {
		return corpseTime;
	}
	
	public void setCorpseTime(int _corpseTime) {
		corpseTime = _corpseTime;
	}
	
	public String getMoveSound() {
		return moveSound;
	}
	
	public void setMoveSound(String _moveSound) {
		moveSound = _moveSound;
	}
	
	public String getAttackSound() {
		return attackSound;
	}
	
	public void setAttackSound(String _attackSound) {
		attackSound = _attackSound;
	}
	
	public String getOnhitSound() {
		return onhitSound;
	}
	
	public void setOnhitSound(String _onhitSound) {
		onhitSound = _onhitSound;
	}
	
	public String getDieSound() {
		return dieSound;
	}
	
	public void setDieSound(String _dieSound) {
		dieSound = _dieSound;
	}
	
	public int getAiId() {
		return aiId;
	}
	
	public void setAiId(int _aiId) {
		aiId = _aiId;
	}
	
	public int getRewardGold() {
		return rewardGold;
	}
	
	public void setRewardGold(int _rewardGold) {
		rewardGold = _rewardGold;
	}
	
	public int getRewardExp() {
		return rewardExp;
	}
	
	public void setRewardExp(int _rewardExp) {
		rewardExp = _rewardExp;
	}
	
	public int getRewardItem() {
		return rewardItem;
	}
	
	public void setRewardItem(int _rewardItem) {
		rewardItem = _rewardItem;
	}
	
	public String getRewardItemList() {
		return rewardItemList;
	}
	
	public void setRewardItemList(String _rewardItemList) {
		rewardItemList = _rewardItemList;
		if (!ToolUtils.isStringNull(_rewardItemList)) {
			rewardItems = ToolUtils.splitStr(rewardItemList, IConst.WELL, IConst.STAR);
			for (int i = 0; i < rewardItems.length; i++){
				if (rewardItems[i].length >= 3){
					totalRate+= rewardItems[i][2];
				}
			}
		}
	}
	
	public int getAir() {
		return air;
	}
	
	public void setAir(int _air) {
		air = _air;
	}
	
	public int getAgility() {
		return agility;
	}
	
	public void setAgility(int _agility) {
		agility = _agility;
	}
	
	public int getInnerForce() {
		return innerForce;
	}
	
	public void setInnerForce(int _innerForce) {
		innerForce = _innerForce;
	}
	
	public int getControl() {
		return control;
	}
	
	public void setControl(int _control) {
		control = _control;
	}
	
	public int getTenacity() {
		return tenacity;
	}
	
	public void setTenacity(int _tenacity) {
		tenacity = _tenacity;
	}
	
	public int getExternalForceAttack() {
		return externalForceAttack;
	}
	
	public void setExternalForceAttack(int _externalForceAttack) {
		externalForceAttack = _externalForceAttack;
	}
	
	public int getInnerForceAttack() {
		return innerForceAttack;
	}
	
	public void setInnerForceAttack(int _innerForceAttack) {
		innerForceAttack = _innerForceAttack;
	}
	
	public int getScorchingSunAttack() {
		return scorchingSunAttack;
	}
	
	public void setScorchingSunAttack(int _scorchingSunAttack) {
		scorchingSunAttack = _scorchingSunAttack;
	}
	
	public int getPloughAttack() {
		return ploughAttack;
	}
	
	public void setPloughAttack(int _ploughAttack) {
		ploughAttack = _ploughAttack;
	}
	
	public int getNetherAttack() {
		return netherAttack;
	}
	
	public void setNetherAttack(int _netherAttack) {
		netherAttack = _netherAttack;
	}
	
	public int getLunarAttack() {
		return lunarAttack;
	}
	
	public void setLunarAttack(int _lunarAttack) {
		lunarAttack = _lunarAttack;
	}
	
	public int getMeleeHitRate() {
		return meleeHitRate;
	}
	
	public void setMeleeHitRate(int _meleeHitRate) {
		meleeHitRate = _meleeHitRate;
	}
	
	public int getRemoteHitRate() {
		return remoteHitRate;
	}
	
	public void setRemoteHitRate(int _remoteHitRate) {
		remoteHitRate = _remoteHitRate;
	}
	
	public int getMeleeDodgeRate() {
		return meleeDodgeRate;
	}
	
	public void setMeleeDodgeRate(int _meleeDodgeRate) {
		meleeDodgeRate = _meleeDodgeRate;
	}
	
	public int getRemoteDodgeRate() {
		return remoteDodgeRate;
	}
	
	public void setRemoteDodgeRate(int _remoteDodgeRate) {
		remoteDodgeRate = _remoteDodgeRate;
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	public void setMaxHp(int _maxHp) {
		maxHp = _maxHp;
	}
	
	public int getCurHp() {
		return curHp;
	}
	
	public void setCurHp(int _curHp) {
		curHp = _curHp;
	}
	
	public int getMaxMp() {
		return maxMp;
	}
	
	public void setMaxMp(int _maxMp) {
		maxMp = _maxMp;
	}
	
	public int getCurMp() {
		return curMp;
	}
	
	public void setCurMp(int _curMp) {
		curMp = _curMp;
	}
	
	public int getDefence() {
		return defence;
	}
	
	public void setDefence(int _defence) {
		defence = _defence;
	}
	
	public int getExternalForceDamageReduce() {
		return externalForceDamageReduce;
	}
	
	public void setExternalForceDamageReduce(int _externalForceDamageReduce) {
		externalForceDamageReduce = _externalForceDamageReduce;
	}
	
	public int getInnerForceDamageReduce() {
		return innerForceDamageReduce;
	}
	
	public void setInnerForceDamageReduce(int _innerForceDamageReduce) {
		innerForceDamageReduce = _innerForceDamageReduce;
	}
	
	public int getScorechingSunDamageReduce() {
		return scorechingSunDamageReduce;
	}
	
	public void setScorechingSunDamageReduce(int _scorechingSunDamageReduce) {
		scorechingSunDamageReduce = _scorechingSunDamageReduce;
	}
	
	public int getPloughDamageReduce() {
		return ploughDamageReduce;
	}
	
	public void setPloughDamageReduce(int _ploughDamageReduce) {
		ploughDamageReduce = _ploughDamageReduce;
	}
	
	public int getNetherDamageReduce() {
		return netherDamageReduce;
	}
	
	public void setNetherDamageReduce(int _netherDamageReduce) {
		netherDamageReduce = _netherDamageReduce;
	}
	
	public int getLunarDamageReduce() {
		return lunarDamageReduce;
	}
	
	public void setLunarDamageReduce(int _lunarDamageReduce) {
		lunarDamageReduce = _lunarDamageReduce;
	}
	
	public int getKnowingRate() {
		return knowingRate;
	}
	
	public void setKnowingRate(int _knowingRate) {
		knowingRate = _knowingRate;
	}
	
	public int getParryRate() {
		return parryRate;
	}
	
	public void setParryRate(int _parryRate) {
		parryRate = _parryRate;
	}
	
	public int getAttackSpeed() {
		return attackSpeed;
	}
	
	public void setAttackSpeed(int _attackSpeed) {
		attackSpeed = _attackSpeed;
	}
	
	public int getAttackScope() {
		return attackScope;
	}
	
	public void setAttackScope(int _attackScope) {
		attackScope = _attackScope;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int _speed) {
		speed = _speed;
	}
	
	public int getAllHurtLower() {
		return allHurtLower;
	}
	
	public void setAllHurtLower(int _allHurtLower) {
		allHurtLower = _allHurtLower;
	}
	
	public int getExpBase() {
		return expBase;
	}
	
	public void setExpBase(int _expBase) {
		expBase = _expBase;
	}
	
	public int getTargetRoleCoefficient() {
		return targetRoleCoefficient;
	}
	
	public void setTargetRoleCoefficient(int _targetRoleCoefficient) {
		targetRoleCoefficient = _targetRoleCoefficient;
	}
	
	public int getTargetMonsterCoefficient() {
		return targetMonsterCoefficient;
	}
	
	public void setTargetMonsterCoefficient(int _targetMonsterCoefficient) {
		targetMonsterCoefficient = _targetMonsterCoefficient;
	}
	
	public int getTargetPetCoefficient() {
		return targetPetCoefficient;
	}
	
	public void setTargetPetCoefficient(int _targetPetCoefficient) {
		targetPetCoefficient = _targetPetCoefficient;
	}
	
	public int getTargetCallMonsterCoefficient() {
		return targetCallMonsterCoefficient;
	}
	
	public void setTargetCallMonsterCoefficient(int _targetCallMonsterCoefficient) {
		targetCallMonsterCoefficient = _targetCallMonsterCoefficient;
	}
	
	public int getCoefficientFromRole() {
		return coefficientFromRole;
	}
	
	public void setCoefficientFromRole(int _coefficientFromRole) {
		coefficientFromRole = _coefficientFromRole;
	}
	
	public int getCoefficientFromMonster() {
		return coefficientFromMonster;
	}
	
	public void setCoefficientFromMonster(int _coefficientFromMonster) {
		coefficientFromMonster = _coefficientFromMonster;
	}
	
	public int getCoefficientFromPet() {
		return coefficientFromPet;
	}
	
	public void setCoefficientFromPet(int _coefficientFromPet) {
		coefficientFromPet = _coefficientFromPet;
	}
	
	public int getCofficientFromCallMonster() {
		return cofficientFromCallMonster;
	}
	
	public void setCofficientFromCallMonster(int _cofficientFromCallMonster) {
		cofficientFromCallMonster = _cofficientFromCallMonster;
	}
	
	public int getHpRecoverSpeed() {
		return hpRecoverSpeed;
	}
	
	public void setHpRecoverSpeed(int _hpRecoverSpeed) {
		hpRecoverSpeed = _hpRecoverSpeed;
	}
	
	public int getMpRecoverSpeed() {
		return mpRecoverSpeed;
	}
	
	public void setMpRecoverSpeed(int _mpRecoverSpeed) {
		mpRecoverSpeed = _mpRecoverSpeed;
	}
	
	public int getSpritSpeed() {
		return spritSpeed;
	}
	
	public void setSpritSpeed(int _spritSpeed) {
		spritSpeed = _spritSpeed;
	}
	
	public int getSpritPierceRate() {
		return spritPierceRate;
	}
	
	public void setSpritPierceRate(int _spritPierceRate) {
		spritPierceRate = _spritPierceRate;
	}
	
	public int getDizzinessRate() {
		return dizzinessRate;
	}
	
	public void setDizzinessRate(int _dizzinessRate) {
		dizzinessRate = _dizzinessRate;
	}
	
	public int getDizzinessResistance() {
		return dizzinessResistance;
	}
	
	public void setDizzinessResistance(int _dizzinessResistance) {
		dizzinessResistance = _dizzinessResistance;
	}
	
	public int getHpSteal() {
		return hpSteal;
	}
	
	public void setHpSteal(int _hpSteal) {
		hpSteal = _hpSteal;
	}
	
	public int getMpSteal() {
		return mpSteal;
	}
	
	public void setMpSteal(int _mpSteal) {
		mpSteal = _mpSteal;
	}
	
	public int getFrozenRate() {
		return frozenRate;
	}
	
	public void setFrozenRate(int _frozenRate) {
		frozenRate = _frozenRate;
	}
	
	public int getFrozenResistance() {
		return frozenResistance;
	}
	
	public void setFrozenResistance(int _frozenResistance) {
		frozenResistance = _frozenResistance;
	}
	
	public int getHpStealResistance() {
		return hpStealResistance;
	}
	
	public void setHpStealResistance(int _hpStealResistance) {
		hpStealResistance = _hpStealResistance;
	}
	
	public int getMpStealResistance() {
		return mpStealResistance;
	}
	
	public void setMpStealResistance(int _mpStealResistance) {
		mpStealResistance = _mpStealResistance;
	}
	
	public int getHpBurnResistance() {
		return hpBurnResistance;
	}
	
	public void setHpBurnResistance(int _hpBurnResistance) {
		hpBurnResistance = _hpBurnResistance;
	}
	
	public int getMpBurnResistance() {
		return mpBurnResistance;
	}
	
	public void setMpBurnResistance(int _mpBurnResistance) {
		mpBurnResistance = _mpBurnResistance;
	}
	
	public int getExternalForceDamageObsorb() {
		return externalForceDamageObsorb;
	}
	
	public void setExternalForceDamageObsorb(int _externalForceDamageObsorb) {
		externalForceDamageObsorb = _externalForceDamageObsorb;
	}
	
	public int getInnerForceDamageObsorb() {
		return innerForceDamageObsorb;
	}
	
	public void setInnerForceDamageObsorb(int _innerForceDamageObsorb) {
		innerForceDamageObsorb = _innerForceDamageObsorb;
	}
	
	public int getScorchingSunDamageObsorb() {
		return scorchingSunDamageObsorb;
	}
	
	public void setScorchingSunDamageObsorb(int _scorchingSunDamageObsorb) {
		scorchingSunDamageObsorb = _scorchingSunDamageObsorb;
	}
	
	public int getPloughDamageObsorb() {
		return ploughDamageObsorb;
	}
	
	public void setPloughDamageObsorb(int _ploughDamageObsorb) {
		ploughDamageObsorb = _ploughDamageObsorb;
	}
	
	public int getNetherDamageObsorb() {
		return netherDamageObsorb;
	}
	
	public void setNetherDamageObsorb(int _netherDamageObsorb) {
		netherDamageObsorb = _netherDamageObsorb;
	}
	
	public int getLunarDamageObsorb() {
		return lunarDamageObsorb;
	}
	
	public void setLunarDamageObsorb(int _lunarDamageObsorb) {
		lunarDamageObsorb = _lunarDamageObsorb;
	}
	
	public int getExternalForceRebind() {
		return externalForceRebind;
	}
	
	public void setExternalForceRebind(int _externalForceRebind) {
		externalForceRebind = _externalForceRebind;
	}
	
	public int getInnerForceRebind() {
		return innerForceRebind;
	}
	
	public void setInnerForceRebind(int _innerForceRebind) {
		innerForceRebind = _innerForceRebind;
	}
	
	public int getScorchingSunRebind() {
		return scorchingSunRebind;
	}
	
	public void setScorchingSunRebind(int _scorchingSunRebind) {
		scorchingSunRebind = _scorchingSunRebind;
	}
	
	public int getPloughRebind() {
		return ploughRebind;
	}
	
	public void setPloughRebind(int _ploughRebind) {
		ploughRebind = _ploughRebind;
	}
	
	public int getNetherRebind() {
		return netherRebind;
	}
	
	public void setNetherRebind(int _netherRebind) {
		netherRebind = _netherRebind;
	}
	
	public int getLunarRebind() {
		return lunarRebind;
	}
	
	public void setLunarRebind(int _lunarRebind) {
		lunarRebind = _lunarRebind;
	}
	
	public int getExternalForceDamageCoefficient() {
		return externalForceDamageCoefficient;
	}
	
	public void setExternalForceDamageCoefficient(int _externalForceDamageCoefficient) {
		externalForceDamageCoefficient = _externalForceDamageCoefficient;
	}
	
	public int getInnerForceDamageCoefficient() {
		return innerForceDamageCoefficient;
	}
	
	public void setInnerForceDamageCoefficient(int _innerForceDamageCoefficient) {
		innerForceDamageCoefficient = _innerForceDamageCoefficient;
	}
	
	public int getScorchingSunDamageCoefficient() {
		return scorchingSunDamageCoefficient;
	}
	
	public void setScorchingSunDamageCoefficient(int _scorchingSunDamageCoefficient) {
		scorchingSunDamageCoefficient = _scorchingSunDamageCoefficient;
	}
	
	public int getPloughDamageCoefficent() {
		return ploughDamageCoefficent;
	}
	
	public void setPloughDamageCoefficent(int _ploughDamageCoefficent) {
		ploughDamageCoefficent = _ploughDamageCoefficent;
	}
	
	public int getNetherDamageCoefficient() {
		return netherDamageCoefficient;
	}
	
	public void setNetherDamageCoefficient(int _netherDamageCoefficient) {
		netherDamageCoefficient = _netherDamageCoefficient;
	}
	
	public int getLunarDamageCoefficient() {
		return lunarDamageCoefficient;
	}
	
	public void setLunarDamageCoefficient(int _lunarDamageCoefficient) {
		lunarDamageCoefficient = _lunarDamageCoefficient;
	}
	
	public int getKnowingHurtHurtRate() {
		return knowingHurtHurtRate;
	}
	
	public void setKnowingHurtHurtRate(int _knowingHurtHurtRate) {
		knowingHurtHurtRate = _knowingHurtHurtRate;
	}
	
	public int getParryHurtRate() {
		return parryHurtRate;
	}
	
	public void setParryHurtRate(int _parryHurtRate) {
		parryHurtRate = _parryHurtRate;
	}
	
	public int getAllHurtAdd() {
		return allHurtAdd;
	}
	
	public void setAllHurtAdd(int _allHurtAdd) {
		allHurtAdd = _allHurtAdd;
	}
	
	public int getHeavyAttack() {
		return heavyAttack;
	}
	
	public void setHeavyAttack(int _heavyAttack) {
		heavyAttack = _heavyAttack;
	}
	
	public int getWatchBosDef() {
		return watchBosDef;
	}
	
	public void setWatchBosDef(int _watchBosDef) {
		watchBosDef = _watchBosDef;
	}
	
	public int getNoMoveRate() {
		return noMoveRate;
	}
	
	public void setNoMoveRate(int _noMoveRate) {
		noMoveRate = _noMoveRate;
	}
	
	public int getNoMoveResistance() {
		return noMoveResistance;
	}
	
	public void setNoMoveResistance(int _noMoveResistance) {
		noMoveResistance = _noMoveResistance;
	}
	
	public int getToughnessRate() {
		return toughnessRate;
	}
	
	public void setToughnessRate(int _toughnessRate) {
		toughnessRate = _toughnessRate;
	}
	
	public int getToughnessEffect() {
		return toughnessEffect;
	}
	
	public void setToughnessEffect(int _toughnessEffect) {
		toughnessEffect = _toughnessEffect;
	}
	
	public int getDisrupting() {
		return disrupting;
	}
	
	public void setDisrupting(int _disrupting) {
		disrupting = _disrupting;
	}
	
	public int getDisruptingEffect() {
		return disruptingEffect;
	}
	
	public void setDisruptingEffect(int _disruptingEffect) {
		disruptingEffect = _disruptingEffect;
	}
	
	public int getExtraSkillPoint() {
		return extraSkillPoint;
	}
	
	public void setExtraSkillPoint(int _extraSkillPoint) {
		extraSkillPoint = _extraSkillPoint;
	}
	
	public int[] getSkills() {
		return skills;
	}
	
	public void setSkills(int[] skills) {
		this.skills = skills;
	}

	public int[][] getRewardItems() {
    	return rewardItems;
    }

	public void setRewardItems(int[][] rewardItems) {
    	this.rewardItems = rewardItems;
    }
	
}