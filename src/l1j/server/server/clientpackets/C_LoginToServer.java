/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_0_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_6_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_2_6_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_0_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_6_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_3_6_S;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_BLOODSTAIN_OF_ANTHARAS;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_BLOODSTAIN_OF_FAFURION;
import static l1j.server.server.model.skill.L1SkillId.EFFECT_END;
import static l1j.server.server.model.skill.L1SkillId.STATUS_THIRD_SPEED;
import static l1j.server.server.model.skill.L1SkillId.COOKING_WONDER_DRUG;
import static l1j.server.server.model.skill.L1SkillId.MIRROR_IMAGE;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_RIBRAVE;
import static l1j.server.server.model.skill.L1SkillId.UNCANNY_DODGE;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.ClientThread;
import l1j.server.server.WarTimeController;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanRecommendTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ActiveSpells;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_Bookmarks;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharacterConfig;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_InitialAbilityGrowth;
import l1j.server.server.serverpackets.S_InvList;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_LoginGame;
import l1j.server.server.serverpackets.S_Mail;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_OwnCharPack;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RuneSlot;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconThirdSpeed;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.S_War;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_bonusstats;
import l1j.server.server.templates.L1GetBackRestart;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來登入到伺服器的封包
 */
public class C_LoginToServer extends ClientBasePacket {

	private static final String C_LOGIN_TO_SERVER = "[C] C_LoginToServer";

	private static Logger _log = Logger.getLogger(C_LoginToServer.class.getName());

	public C_LoginToServer(byte abyte0[], ClientThread client) throws FileNotFoundException, Exception {
		super(abyte0);

		String login = client.getAccountName();

		String charName = readS();

		if (client.getActiveChar() != null) {
			_log.info("同一個角色重複登入，強制切斷 " + client.getHostname() + ") 的連結");
			client.close();
			return;
		}

		L1PcInstance pc = L1PcInstance.load(charName);
		Account account = Account.load(pc.getAccountName());
		if (account.isOnlineStatus()) {
			_log.info("同一個帳號雙重角色登入，強制切斷 " + client.getHostname() + ") 的連結");
			client.close();
			return;
		}
		if ((pc == null) || !login.equals(pc.getAccountName())) {
			_log.info("無效的角色名稱: char=" + charName + " account=" + login + " host=" + client.getHostname());
			client.close();
			return;
		}

		if (Config.LEVEL_DOWN_RANGE != 0) {
			if (pc.getHighLevel() - pc.getLevel() >= Config.LEVEL_DOWN_RANGE) {
				_log.info("登錄請求超出了容忍的等級下降的角色: char=" + charName + " account=" + login + " host=" + client.getHostname());
				client.kick();
				return;
			}
		}

		_log.info("角色登入到伺服器中: char=" + charName + " account=" + login+ " host=" + client.getHostname());
		
		int currentHpAtLoad = pc.getCurrentHp();
		int currentMpAtLoad = pc.getCurrentMp();
		pc.clearSkillMastery();
		pc.setOnlineStatus(1);
		CharacterTable.updateOnlineStatus(pc);
		L1World.getInstance().storeObject(pc);

		pc.setNetConnection(client);
		pc.setPacketOutput(client);
		client.setActiveChar(pc);
		
		pc.sendPackets(new S_LoginGame(pc));
		
		Account.OnlineStatus(account, true); // OnlineStatus = 1

		// 如果設定檔中設定自動回村的話
		GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
		L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
		for (L1GetBackRestart gbr : gbrList) {
			if (pc.getMapId() == gbr.getArea()) {
				pc.setX(gbr.getLocX());
				pc.setY(gbr.getLocY());
				pc.setMap(gbr.getMapId());
				break;
			}
		}

		// altsettings.properties 中 GetBack 設定為 true 就自動回村
		if (Config.GET_BACK) {
			int[] loc = Getback.GetBack_Location(pc, true);
			pc.setX(loc[0]);
			pc.setY(loc[1]);
			pc.setMap((short) loc[2]);
		}

		// 如果標記是在戰爭期間，如果不是血盟成員回到城堡。
		int castle_id = L1CastleLocation.getCastleIdByArea(pc);
		if (0 < castle_id) {
			if (WarTimeController.getInstance().isNowWar(castle_id)) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					if (clan.getCastleId() != castle_id) {
						// 沒有城堡
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
					}
				} else {
					// 有城堡就回到城堡
					int[] loc = new int[3];
					loc = L1CastleLocation.getGetBackLoc(castle_id);
					pc.setX(loc[0]);
					pc.setY(loc[1]);
					pc.setMap((short) loc[2]);
				}
			}
		}
		L1World.getInstance().addVisibleObject(pc);
		
		if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
			pc.sendPackets(new S_CharacterConfig(pc.getId()));
		}
	
		items(pc);
		
		pc.sendPackets(new S_Mail(pc , 0));
		pc.sendPackets(new S_Mail(pc , 1));
		pc.sendPackets(new S_Mail(pc , 2));
		
		pc.beginGameTimeCarrier();
		
		pc.sendPackets(new S_RuneSlot(S_RuneSlot.RUNE_CLOSE_SLOT, 3)); // 符文關閉欄位數
		pc.sendPackets(new S_RuneSlot(S_RuneSlot.RUNE_OPEN_SLOT, 1)); // 符文開放欄位數
		
		pc.setEquipments();
		
		pc.sendPackets(new S_ActiveSpells(pc));
		
		pc.sendPackets(new S_Bookmarks(pc));
		
		pc.sendPackets(new S_OwnCharStatus(pc));
		
		pc.sendPackets(new S_MapID(pc.getMapId(), pc.getMap().isUnderwater()));
		
		pc.sendPackets(new S_OwnCharPack(pc));

		pc.sendPackets(new S_SPMR(pc));

		S_CharTitle s_charTitle = new S_CharTitle(pc.getId(), pc.getTitle());
		pc.sendPackets(s_charTitle);
		pc.broadcastPacket(s_charTitle);

		pc.sendVisualEffectAtLogin(); // 皇冠，毒，水和其他視覺效果顯示
		
		pc.sendPackets(new S_OwnCharStatus2(pc, 1)); // 角色初始素質 
		
		pc.sendPackets(new S_InitialAbilityGrowth(pc)); // 角色狀態獎勵 

		pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()));

		skills(pc);
		
		buff(client, pc);
		
		pc.turnOnOffLight();

		pc.sendPackets(new S_Karma(pc)); // 友好度
		
		pc.sendPackets(new S_PacketBox(S_PacketBox.DODGE_RATE_PLUS, pc.getDodge())); // 閃避率 正
		pc.sendPackets(new S_PacketBox(S_PacketBox.DODGE_RATE_MINUS, pc.getNdodge())); // 閃避率 負
		
		checkPledgeRecommendation(pc);

		if (pc.getCurrentHp() > 0) {
			pc.setDead(false);
			pc.setStatus(0);
		} else {
			pc.setDead(true);
			pc.setStatus(ActionCodes.ACTION_Die);
		}

		if ((pc.getLevel() >= 51) && (pc.getLevel() - 50 > pc.getBonusStats())) {
			if ((pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon()+ pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha()) < 210) {
				pc.sendPackets(new S_bonusstats(pc.getId(), 1));
			}
		}

		serchSummon(pc);

		WarTimeController.getInstance().checkCastleWar(pc);

		if (pc.getClanid() != 0) { // 有血盟
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				if ((pc.getClanid() == clan.getClanId()) && // 血盟解散、又重新用同樣名字創立時的對策
						pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
					L1PcInstance[] clanMembers = clan.getOnlineClanMember();
					for (L1PcInstance clanMember : clanMembers) {
						if (clanMember.getId() != pc.getId()) {
							clanMember.sendPackets(new S_ServerMessage(843, pc.getName())); // 只今、血盟員の%0%sがゲームに接続しました。
						}
					}

					// 取得所有的盟戰
					for (L1War war : L1World.getInstance().getWarList()) {
						boolean ret = war.CheckClanInWar(pc.getClanname());
						if (ret) { // 盟戰中
							String enemy_clan_name = war.GetEnemyClanName(pc.getClanname());
							if (enemy_clan_name != null) {
								// あなたの血盟が現在_血盟と交戦中です。
								pc.sendPackets(new S_War(8, pc.getClanname(),enemy_clan_name));
							}
							break;
						}
					}
				} else {
					pc.setClanid(0);
					pc.setClanname("");
					pc.setClanRank(0);
					pc.save(); // 儲存玩家的資料到資料庫中
				}
			}
		}

		if (pc.getPartnerId() != 0) { // 結婚中
			L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
			if ((partner != null) && (partner.getPartnerId() != 0)) {
				if ((pc.getPartnerId() == partner.getId())&& (partner.getPartnerId() == pc.getId())) {
					pc.sendPackets(new S_ServerMessage(548)); // あなたのパートナーは今ゲーム中です。
					partner.sendPackets(new S_ServerMessage(549)); // あなたのパートナーはたった今ログインしました。
				}
			}
		}

		if (currentHpAtLoad > pc.getCurrentHp()) {
			pc.setCurrentHp(currentHpAtLoad);
		}
		if (currentMpAtLoad > pc.getCurrentMp()) {
			pc.setCurrentMp(currentMpAtLoad);
		}
		pc.startHpRegeneration();
		pc.startMpRegeneration();
		pc.startObjectAutoUpdate();
		pc.setCryOfSurvivalTime();
		client.CharReStart(false);
		pc.beginExpMonitor();
		pc.save(); // 儲存玩家的資料到資料庫中

		if (pc.getHellTime() > 0) {
			pc.beginHell(false);
		}

		// 處理新手保護系統(遭遇的守護)狀態資料的變動
		pc.checkNoviceType();
	}

	private void items(L1PcInstance pc) {
		// 從資料庫中讀取角色的道具
		CharacterTable.getInstance().restoreInventory(pc);

		pc.sendPackets(new S_InvList(pc.getInventory().getItems()));
	}

	private void skills(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			int i = 0;
			int lv1 = 0;
			int lv2 = 0;
			int lv3 = 0;
			int lv4 = 0;
			int lv5 = 0;
			int lv6 = 0;
			int lv7 = 0;
			int lv8 = 0;
			int lv9 = 0;
			int lv10 = 0;
			int lv11 = 0;
			int lv12 = 0;
			int lv13 = 0;
			int lv14 = 0;
			int lv15 = 0;
			int lv16 = 0;
			int lv17 = 0;
			int lv18 = 0;
			int lv19 = 0;
			int lv20 = 0;
			int lv21 = 0;
			int lv22 = 0;
			int lv23 = 0;
			int lv24 = 0;
			int lv25 = 0;
			int lv26 = 0;
			int lv27 = 0;
			int lv28 = 0;
			while (rs.next()) {
				int skillId = rs.getInt("skill_id");
				L1Skills l1skills = SkillsTable.getInstance().getTemplate(
						skillId);
				if (l1skills.getSkillLevel() == 1) {
					lv1 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 2) {
					lv2 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 3) {
					lv3 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 4) {
					lv4 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 5) {
					lv5 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 6) {
					lv6 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 7) {
					lv7 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 8) {
					lv8 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 9) {
					lv9 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 10) {
					lv10 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 11) {
					lv11 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 12) {
					lv12 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 13) {
					lv13 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 14) {
					lv14 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 15) {
					lv15 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 16) {
					lv16 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 17) {
					lv17 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 18) {
					lv18 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 19) {
					lv19 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 20) {
					lv20 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 21) {
					lv21 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 22) {
					lv22 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 23) {
					lv23 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 24) {
					lv24 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 25) {
					lv25 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 26) {
					lv26 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 27) {
					lv27 |= l1skills.getId();
				}
				if (l1skills.getSkillLevel() == 28) {
					lv28 |= l1skills.getId();
				}
				i = lv1 + lv2 + lv3 + lv4 + lv5 + lv6 + lv7 + lv8 + lv9 + lv10
						+ lv11 + lv12 + lv13 + lv14 + lv15 + lv16 + lv17 + lv18
						+ lv19 + lv20 + lv21 + lv22 + lv23 + lv24 + lv25 + lv26
						+ lv27 + lv28;
				pc.setSkillMastery(skillId);
			}
			if (i > 0) {
				pc.sendPackets(new S_AddSkill(lv1, lv2, lv3, lv4, lv5, lv6,
						lv7, lv8, lv9, lv10, lv11, lv12, lv13, lv14, lv15,
						lv16, lv17, lv18, lv19, lv20, lv21, lv22, lv23, lv24,
						lv25, lv26, lv27, lv28));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void serchSummon(L1PcInstance pc) {
		for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
			if (summon.getMaster().getId() == pc.getId()) {
				summon.setMaster(pc);
				pc.addPet(summon);
				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc));
				}
			}
		}
	}

	private void buff(ClientThread clientthread, L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			while (rs.next()) {
				int skillid = rs.getInt("skill_id");
				int remaining_time = rs.getInt("remaining_time");
				int time = 0;
				switch (skillid) {
				case SHAPE_CHANGE: // 變身
					int poly_id = rs.getInt("poly_id");
					L1PolyMorph.doPoly(pc, poly_id, remaining_time,L1PolyMorph.MORPH_BY_LOGIN);
					break;
				case STATUS_BRAVE: // 勇敢藥水
					pc.sendPackets(new S_SkillBrave(pc.getId(), 1,remaining_time));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
					pc.setBraveSpeed(1);
					pc.setSkillEffect(skillid, remaining_time * 1000);
					break;
				case STATUS_ELFBRAVE: // 精靈餅乾
					pc.sendPackets(new S_SkillBrave(pc.getId(), 3,remaining_time));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
					pc.setBraveSpeed(3);
					pc.setSkillEffect(skillid, remaining_time * 1000);
					break;
				case STATUS_BRAVE2: // 超級加速
					pc.sendPackets(new S_SkillBrave(pc.getId(), 5,remaining_time));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 5, 0));
					pc.setBraveSpeed(5);
					pc.setSkillEffect(skillid, remaining_time * 1000);
					break;
				case STATUS_HASTE: // 加速
					pc.sendPackets(new S_SkillHaste(pc.getId(), 1,remaining_time));
					pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					pc.setMoveSpeed(1);
					pc.setSkillEffect(skillid, remaining_time * 1000);
					break;
				case STATUS_BLUE_POTION: // 藍色藥水
					pc.sendPackets(new S_SkillIconGFX(34, remaining_time));
					pc.setSkillEffect(skillid, remaining_time * 1000);
					break;
				case STATUS_CHAT_PROHIBITED: // 禁言
					pc.sendPackets(new S_SkillIconGFX(36, remaining_time));
					pc.setSkillEffect(skillid, remaining_time * 1000);
					break;
				case STATUS_THIRD_SPEED: // 三段加速
					time = remaining_time / 4;
					pc.sendPackets(new S_Liquor(pc.getId(), 8)); // 人物 *
																	// 1.15
					pc.broadcastPacket(new S_Liquor(pc.getId(), 8)); // 人物 *
																		// 1.15
					pc.sendPackets(new S_SkillIconThirdSpeed(time));
					pc.setSkillEffect(skillid, time * 4 * 1000);
					break;
				case MIRROR_IMAGE: // 鏡像
				case UNCANNY_DODGE: // 暗影閃避
					time = remaining_time / 16;
					pc.addDodge((byte) 5); // 閃避率 + 50%
					// 更新閃避率顯示
					pc.sendPackets(new S_PacketBox(88, pc.getDodge()));
					pc.sendPackets(new S_PacketBox(21, time));
					pc.setSkillEffect(skillid, time * 16 * 1000);
					break;
				case EFFECT_BLOODSTAIN_OF_ANTHARAS: // 安塔瑞斯的血痕
					remaining_time = remaining_time / 60;
					if (remaining_time != 0) {
						L1BuffUtil.bloodstain(pc, (byte) 0, remaining_time,
								false);
					}
					break;
				case EFFECT_BLOODSTAIN_OF_FAFURION: // 法利昂的血痕
					remaining_time = remaining_time / 60;
					if (remaining_time != 0) {
						L1BuffUtil.bloodstain(pc, (byte) 1, remaining_time,
								false);
					}
					break;
				default:
					// 魔法料理
					if (((skillid >= COOKING_1_0_N) && (skillid <= COOKING_1_6_N))
							|| ((skillid >= COOKING_1_0_S) && (skillid <= COOKING_1_6_S))
							|| ((skillid >= COOKING_2_0_N) && (skillid <= COOKING_2_6_N))
							|| ((skillid >= COOKING_2_0_S) && (skillid <= COOKING_2_6_S))
							|| ((skillid >= COOKING_3_0_N) && (skillid <= COOKING_3_6_N))
							|| ((skillid >= COOKING_3_0_S) && (skillid <= COOKING_3_6_S))) {
						L1Cooking.eatCooking(pc, skillid, remaining_time);
					}
					// 生命之樹果實、商城道具
					else if (skillid == STATUS_RIBRAVE
							|| (skillid >= EFFECT_BEGIN && skillid <= EFFECT_END)
							|| skillid == COOKING_WONDER_DRUG) {
						;
					} else {
						L1SkillUse l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(clientthread.getActiveChar(),
								skillid, pc.getId(), pc.getX(), pc.getY(),
								null, remaining_time, L1SkillUse.TYPE_LOGIN);
					}
					break;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void checkPledgeRecommendation(L1PcInstance pc){
		if(pc.getClanid() > 0){ 
			pc.sendPackets(new S_ClanAttention());
			pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));
			if(pc.getClanRank() == L1Clan.CLAN_RANK_PRINCE || pc.getClanRank() == L1Clan.CLAN_RANK_GUARDIAN
			 ||pc.getClanRank() == L1Clan.CLAN_RANK_LEAGUE_GUARDIAN || pc.getClanRank() == L1Clan.CLAN_RANK_LEAGUE_VICEPRINCE
		     ||pc.getClanRank() == L1Clan.CLAN_RANK_LEAGUE_PRINCE){
				// 有登錄，結束
				if(ClanRecommendTable.getInstance().isRecorded(pc.getClanid())){
					// 是否有人申請
					if(ClanRecommendTable.getInstance().isClanApplyByPlayer(pc.getClanid())){
						pc.sendPackets(new S_ServerMessage(3248));
					}
				} else {
					// 吳登錄
					pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));
					pc.sendPackets(new S_ClanAttention());
					pc.sendPackets(new S_ServerMessage(3246));
				}
			}
		} else {
			if(pc.isCrown()){
				pc.sendPackets(new S_ServerMessage(3247));
			} else {
				// 如果有登記 不發送
				if(ClanRecommendTable.getInstance().isApplied(pc.getName())){
					
				} else {
					// 沒有登記
					pc.sendPackets(new S_ServerMessage(3245));
				}
			}
		}
		
	}
	

	@Override
	public String getType() {
		return C_LOGIN_TO_SERVER;
	}
}
