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

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.utils.PrintUtil;

public abstract class ClientBasePacket {
	private static Logger _log = Logger.getLogger(ClientBasePacket.class.getName());

	private static final String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;

	private byte _decrypt[];

	private int _off;

	public ClientBasePacket(byte abyte0[]) {
		PrintUtil.println(getType() + "  " + Arrays.toString(abyte0));
		_log.finest("type=" + getType() + ", len=" + abyte0.length);
		_decrypt = abyte0;
		_off = 1;
	}

	public ClientBasePacket(ByteBuffer bytebuffer, ClientThread clientthread) {
	}

	/**
	 * 		   小端 byte 轉 int
	 * 		   此段可改寫成以下形式
	 *         int i = _decrypt[1] & 0xff;
	 *         i += (_decrypt[2] & 0xff) << 8 ;
	 *         i += (_decrypt[3] & 0xff) << 16;
	 *         i += (_decrypt[4] & 0xff) << 24;
	 *
	 * 		   byte & 0xff 使 (-128 ~ 127) 的有符號二進位數轉為無符號二進位數 (0 ~ 255)
	 * 		   00000000 ~ 01111111 (0 ~ 127)
	 * 		   10000000 ~ 11111111 (-128 ~ -1) 二的補數 127 ~ 255
	 * 		   10000000 & 0xff = 01111111 = 127
	 * 		   11111111 & 0xff = 11111111 = 256
	 * */
	public int readD() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		i |= _decrypt[_off++] << 16 & 0xff0000;
		i |= _decrypt[_off++] << 24 & 0xff000000;
		return i;
	}

	public int readC() {
		int i = _decrypt[_off++] & 0xff;
		return i;
	}

	public int readH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		return i;
	}

	public int readCH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		i |= _decrypt[_off++] << 16 & 0xff0000;
		return i;
	}

	public double readF() {
		long l = _decrypt[_off++] & 0xff;
		l |= _decrypt[_off++] << 8 & 0xff00;
		l |= _decrypt[_off++] << 16 & 0xff0000;
		l |= _decrypt[_off++] << 24 & 0xff000000;
		l |= (long) _decrypt[_off++] << 32 & 0xff00000000L;
		l |= (long) _decrypt[_off++] << 40 & 0xff0000000000L;
		l |= (long) _decrypt[_off++] << 48 & 0xff000000000000L;
		l |= (long) _decrypt[_off++] << 56 & 0xff00000000000000L;
		return Double.longBitsToDouble(l);
	}

	public String readS() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, CLIENT_LANGUAGE_CODE);
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes(CLIENT_LANGUAGE_CODE).length + 1;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}

	public byte[] readByte() {
		byte[] result = new byte[_decrypt.length - _off];
		try {
			System.arraycopy(_decrypt, _off, result, 0, _decrypt.length - _off);
			_off = _decrypt.length;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return result;
	}

	/**
	 * 返回客戶端的封包類型。("[C] C_DropItem" 等)
	 */
	public String getType() {
		return "[C] " + this.getClass().getSimpleName();
	}
}
