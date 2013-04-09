package org.madbit.sharecontact.addressbook.vcard;

import java.io.BufferedReader;
import java.io.StringReader;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Intents.Insert;

//this class is used as a factory

public class VCardFactory {

	//method making a string representation of a vcard
	//from the name and the id of a contact
	public static String makeVCardString(Context c, String name, String id){
		String vcard = "";
		vcard += "BEGIN:VCARD\n";
		vcard += "VERSION:2.1\n";
		vcard += "FN:"+name+"\n";

		ContentResolver cr = c.getContentResolver();
		
		//Let's start with getting the phones
		Cursor pCur = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				null, 
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
				new String[]{id}, null);
		
		while (pCur.moveToNext()) {
			String phone = pCur.getString(
                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
			int pType = Integer.parseInt(pCur.getString(
                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
			String pLabel = pCur.getString(
                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
			
			switch(pType){
			case 0:
				vcard += "TEL;X-"+pLabel+":"+phone+"\n";
				break;
			case 1:
				vcard += "TEL;HOME;VOICE:"+phone+"\n";
				break;
			case 2:
				vcard += "TEL;CELL:"+phone+"\n";
				break;
			case 3:
				vcard += "TEL;WORK;VOICE:"+phone+"\n";
				break;
			case 4:
				vcard += "TEL;WORK;FAX:"+phone+"\n";
				break;
			case 5:
				vcard += "TEL;HOME;FAX:"+phone+"\n";
				break;
			case 6:
				vcard += "TEL;PAGER:"+phone+"\n";
				break;
			case 9:
				vcard += "TEL;CAR:"+phone+"\n";
				break;
			case 11:
				vcard += "TEL;ISDN:"+phone+"\n";
				break;
			default:
				vcard += "TEL;VOICE:"+phone+"\n";
				break;
			}
		} 
		pCur.close();
		
		//the emails
		Cursor eCur = cr.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
				null, 
				ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = ?", 
				new String[]{id}, null);
		
		while (eCur.moveToNext()) {
			String email = eCur.getString(
                    eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			int eType = Integer.parseInt(eCur.getString(
                    eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
			String eLabel = eCur.getString(
                    eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL));
						
			switch(eType){
			case 0:
				vcard += "EMAIL;X-"+eLabel+":"+email+"\n";
				break;
			case 1:
				vcard += "EMAIL;HOME:"+email+"\n";
				break;
			case 2:
				vcard += "EMAIL;WORK:"+email+"\n";
				break;
			case 4:
				vcard += "EMAIL;CELL:"+email+"\n";
				break;
			default:
				vcard += "EMAIL;INTERNET:"+email+"\n";
				break;
			}
		} 
		eCur.close();
		
		//the adresses
		Cursor aCur = cr.query(
				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, 
				null, 
				ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID +" = ?", 
				new String[]{id}, null);

		while (aCur.moveToNext()) {
			String str = aCur.getString(
                    aCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
			String cit = aCur.getString(
                    aCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
			String sta = aCur.getString(
                    aCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
			String cou = aCur.getString(
                    aCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
			String pos = aCur.getString(
                    aCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
			String aLabel = aCur.getString(
                    aCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL));
			
			if(str==null) str="";
			if(cit==null) cit="";
			if(sta==null) sta="";
			if(cou==null) cou="";
			if(pos==null) pos="";
			
			int aType = Integer.parseInt(aCur.getString(
                    aCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)));
			
			switch(aType){
			case 0:
				vcard += "ADR;X-"+aLabel+":;;"+str+";"+cit+";"+sta+";"+pos+";"+cou+"\n";
				break;
			case 1:
				vcard += "ADR;HOME:;;"+str+";"+cit+";"+sta+";"+pos+";"+cou+"\n";
				break;
			case 2:
				vcard += "ADR;WORK:;;"+str+";"+cit+";"+sta+";"+pos+";"+cou+"\n";
				break;
			default:
				vcard += "ADR;POSTAL:;;"+str+";"+cit+";"+sta+";"+pos+";"+cou+"\n";
				break;
			}
		} 
		aCur.close();
		vcard += "END:VCARD";
		return vcard;
	}

	//method making the intent for the insertion of o contact
	//from the string representation of a vcard
	public static Intent makeVCardIntent(String vcard){
		Intent i=new Intent(Intent.ACTION_INSERT);
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		i.setData(uri);
		BufferedReader br = new BufferedReader(new StringReader(vcard));
		String line;
		int nbTel = 0;
		int nbEmail= 0;
		boolean hasAdr = false;
		/*boolean hasNote = false;
		boolean hasOrg = false;
		boolean hasTitle = false;*/
		try{
			while ((line=br.readLine())!=null){
				if(line.startsWith("FN")) i.putExtra(Insert.NAME, line.substring(3));
				else if(line.startsWith("TEL") && nbTel<3){
					if(line.regionMatches(4, "HOME", 0, 4)){
						if(line.regionMatches(9, "VOICE", 0, 5)){
							if(nbTel == 0){
								i.putExtra(Insert.PHONE, line.substring(15));
								i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_HOME);
								nbTel++;
							}else if(nbTel == 1){
								i.putExtra(Insert.SECONDARY_PHONE, line.substring(15));
								i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_HOME);
								nbTel++;
							}else if(nbTel == 2){
								i.putExtra(Insert.TERTIARY_PHONE, line.substring(15));
								i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_HOME);
								nbTel++;
							}
						}else if(line.regionMatches(9, "FAX", 0, 3)){
							if(nbTel == 0){
								i.putExtra(Insert.PHONE, line.substring(13));
								i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_FAX_HOME);
								nbTel++;
							}else if(nbTel == 1){
								i.putExtra(Insert.SECONDARY_PHONE, line.substring(13));
								i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_FAX_HOME);
								nbTel++;
							}else if(nbTel == 2){
								i.putExtra(Insert.TERTIARY_PHONE, line.substring(13));
								i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_FAX_HOME);
								nbTel++;
							}
						}
					}else if(line.regionMatches(4, "WORK", 0, 4)){
						if(line.regionMatches(9, "VOICE", 0, 5)){
							if(nbTel == 0){
								i.putExtra(Insert.PHONE, line.substring(15));
								i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_WORK);
								nbTel++;
							}else if(nbTel == 1){
								i.putExtra(Insert.SECONDARY_PHONE, line.substring(15));
								i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_WORK);
								nbTel++;
							}else if(nbTel == 2){
								i.putExtra(Insert.TERTIARY_PHONE, line.substring(15));
								i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_WORK);
								nbTel++;
							}
						}else if(line.regionMatches(9, "FAX", 0, 3)){
							if(nbTel == 0){
								i.putExtra(Insert.PHONE, line.substring(13));
								i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_FAX_WORK);
								nbTel++;
							}else if(nbTel == 1){
								i.putExtra(Insert.SECONDARY_PHONE, line.substring(13));
								i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_FAX_WORK);
								nbTel++;
							}else if(nbTel == 2){
								i.putExtra(Insert.TERTIARY_PHONE, line.substring(13));
								i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_FAX_WORK);
								nbTel++;
							}
						}
					}else if(line.regionMatches(4, "CELL", 0, 4)){
						if(nbTel == 0){
							i.putExtra(Insert.PHONE, line.substring(9));
							i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_MOBILE);
							nbTel++;
						}else if(nbTel == 1){
							i.putExtra(Insert.SECONDARY_PHONE, line.substring(9));
							i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_MOBILE);
							nbTel++;
						}else if(nbTel == 2){
							i.putExtra(Insert.TERTIARY_PHONE, line.substring(9));
							i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_MOBILE);
							nbTel++;
						}
					}else if(line.regionMatches(4, "PAGER", 0, 5)){
						if(nbTel == 0){
							i.putExtra(Insert.PHONE, line.substring(10));
							i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_PAGER);
							nbTel++;
						}else if(nbTel == 1){
							i.putExtra(Insert.SECONDARY_PHONE, line.substring(10));
							i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_PAGER);
							nbTel++;
						}else if(nbTel == 2){
							i.putExtra(Insert.TERTIARY_PHONE, line.substring(10));
							i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_PAGER);
							nbTel++;
						}
					}else if(line.regionMatches(4, "VOICE", 0, 5)){
						if(nbTel == 0){
							i.putExtra(Insert.PHONE, line.substring(10));
							i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_OTHER);
							nbTel++;
						}else if(nbTel == 1){
							i.putExtra(Insert.SECONDARY_PHONE, line.substring(10));
							i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_OTHER);
							nbTel++;
						}else if(nbTel == 2){
							i.putExtra(Insert.TERTIARY_PHONE, line.substring(10));
							i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_OTHER);
							nbTel++;
						}
					}
					else if(line.regionMatches(4, "CAR", 0, 3)){
						if(nbTel == 0){
							i.putExtra(Insert.PHONE, line.substring(8));
							i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_CAR);
							nbTel++;
						}else if(nbTel == 1){
							i.putExtra(Insert.SECONDARY_PHONE, line.substring(8));
							i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_CAR);
							nbTel++;
						}else if(nbTel == 2){
							i.putExtra(Insert.TERTIARY_PHONE, line.substring(8));
							i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_CAR);
							nbTel++;
						}
					}else if(line.regionMatches(4, "ISDN", 0, 4)){
						if(nbTel == 0){
							i.putExtra(Insert.PHONE, line.substring(9));
							i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_ISDN);
							nbTel++;
						}else if(nbTel == 1){
							i.putExtra(Insert.SECONDARY_PHONE, line.substring(9));
							i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_ISDN);
							nbTel++;
						}else if(nbTel == 2){
							i.putExtra(Insert.TERTIARY_PHONE, line.substring(9));
							i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_ISDN);
							nbTel++;
						}
					}else if(line.regionMatches(4, "X-", 0, 2)){
						if(nbTel == 0){
							int finLabel = line.indexOf(':');
							String label = line.substring(6, finLabel);
							i.putExtra(Insert.PHONE, line.substring(finLabel+1));
							i.putExtra(Insert.PHONE_TYPE, label);
							nbTel++;
						}else if(nbTel == 1){
							int finLabel = line.indexOf(':');
							String label = line.substring(6, finLabel);
							i.putExtra(Insert.SECONDARY_PHONE, line.substring(finLabel+1));
							i.putExtra(Insert.SECONDARY_PHONE_TYPE, label);
							nbTel++;
						}else if(nbTel == 2){
							int finLabel = line.indexOf(':');
							String label = line.substring(6, finLabel);
							i.putExtra(Insert.TERTIARY_PHONE, line.substring(finLabel+1));
							i.putExtra(Insert.TERTIARY_PHONE_TYPE, label);
							nbTel++;
						}
					}else{
						int ind = line.indexOf(':');
						if(nbTel == 0){
							i.putExtra(Insert.PHONE, line.substring(ind+1));
							i.putExtra(Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_OTHER);
							nbTel++;
						}else if(nbTel == 1){
							i.putExtra(Insert.SECONDARY_PHONE, line.substring(ind+1));
							i.putExtra(Insert.SECONDARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_OTHER);
							nbTel++;
						}else if(nbTel == 2){
							i.putExtra(Insert.TERTIARY_PHONE, line.substring(ind+1));
							i.putExtra(Insert.TERTIARY_PHONE_TYPE, CommonDataKinds.Phone.TYPE_OTHER);
							nbTel++;
						}
					}
				}
				else if(line.startsWith("EMAIL") && nbEmail<3){
					if(line.regionMatches(6, "HOME", 0, 4)){
						if(nbEmail == 0){
							i.putExtra(Insert.EMAIL, line.substring(11));
							i.putExtra(Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_HOME);
							nbEmail++;
						}else if(nbEmail == 1){
							i.putExtra(Insert.SECONDARY_EMAIL, line.substring(11));
							i.putExtra(Insert.SECONDARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_HOME);
							nbEmail++;
						}else if(nbEmail == 2){
							i.putExtra(Insert.TERTIARY_EMAIL, line.substring(11));
							i.putExtra(Insert.TERTIARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_HOME);
							nbEmail++;
						}
					}else if(line.regionMatches(6, "WORK", 0, 4)){
						if(nbEmail == 0){
							i.putExtra(Insert.EMAIL, line.substring(11));
							i.putExtra(Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_WORK);
							nbEmail++;
						}else if(nbEmail == 1){
							i.putExtra(Insert.SECONDARY_EMAIL, line.substring(11));
							i.putExtra(Insert.SECONDARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_WORK);
							nbEmail++;
						}else if(nbEmail == 2){
							i.putExtra(Insert.TERTIARY_EMAIL, line.substring(11));
							i.putExtra(Insert.TERTIARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_WORK);
							nbEmail++;
						}
					}else if(line.regionMatches(6, "CELL", 0, 4)){
						if(nbEmail == 0){
							i.putExtra(Insert.EMAIL, line.substring(11));
							i.putExtra(Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_MOBILE);
							nbEmail++;
						}else if(nbEmail == 1){
							i.putExtra(Insert.SECONDARY_EMAIL, line.substring(11));
							i.putExtra(Insert.SECONDARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_MOBILE);
							nbEmail++;
						}else if(nbEmail == 2){
							i.putExtra(Insert.TERTIARY_EMAIL, line.substring(11));
							i.putExtra(Insert.TERTIARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_MOBILE);
							nbEmail++;
						}
					}else if(line.regionMatches(6, "INTERNET", 0, 8)){
						if(nbEmail == 0){
							i.putExtra(Insert.EMAIL, line.substring(15));
							i.putExtra(Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_OTHER);
							nbEmail++;
						}else if(nbEmail == 1){
							i.putExtra(Insert.SECONDARY_EMAIL, line.substring(15));
							i.putExtra(Insert.SECONDARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_OTHER);
							nbEmail++;
						}else if(nbEmail == 2){
							i.putExtra(Insert.TERTIARY_EMAIL, line.substring(15));
							i.putExtra(Insert.TERTIARY_EMAIL_TYPE, CommonDataKinds.Email.TYPE_OTHER);
							nbEmail++;
						}
					}else if(line.regionMatches(6, "X-", 0, 2)){
						if(nbEmail == 0){
							int finLabel = line.indexOf(':');
							String label = line.substring(8, finLabel);
							i.putExtra(Insert.EMAIL, line.substring(finLabel+1));
							i.putExtra(Insert.EMAIL_TYPE, label);
							nbEmail++;
						}else if(nbEmail == 1){
							int finLabel = line.indexOf(':');
							String label = line.substring(8, finLabel);
							i.putExtra(Insert.SECONDARY_EMAIL, line.substring(finLabel+1));
							i.putExtra(Insert.SECONDARY_EMAIL_TYPE, label);
							nbEmail++;
						}else if(nbEmail == 2){
							int finLabel = line.indexOf(':');
							String label = line.substring(8, finLabel);
							i.putExtra(Insert.TERTIARY_EMAIL, line.substring(finLabel+1));
							i.putExtra(Insert.TERTIARY_EMAIL_TYPE, label);
							nbEmail++;
						}
					}
				}
				else if(line.startsWith("ADR") && !hasAdr){
					if(line.regionMatches(4, "HOME", 0, 4)){
						int ind1pv = line.indexOf(';', 9);
						int ind2pv = line.indexOf(';', ind1pv+1);
						int indst = line.indexOf(';', ind2pv+1);
						String st = line.substring(ind2pv+1, indst);
						int indci = line.indexOf(';', indst+1);
						String ci = line.substring(indst+1, indci);
						int indre = line.indexOf(';', indci+1);
						String re = line.substring(indci+1, indre);
						int intpc = line.indexOf(';', indre+1);
						String pc = line.substring(indre+1, intpc);
						String ad = st+" "+ci+" "+re+" "+pc;
						ad = ad.trim();
						i.putExtra(Insert.POSTAL, ad);
						i.putExtra(Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.TYPE_HOME);
					}else if(line.regionMatches(4, "WORK", 0, 4)){
						int ind1pv = line.indexOf(';', 9);
						int ind2pv = line.indexOf(';', ind1pv+1);
						int indst = line.indexOf(';', ind2pv+1);
						String st = line.substring(ind2pv+1, indst);
						int indci = line.indexOf(';', indst+1);
						String ci = line.substring(indst+1, indci);
						int indre = line.indexOf(';', indci+1);
						String re = line.substring(indci+1, indre);
						int intpc = line.indexOf(';', indre+1);
						String pc = line.substring(indre+1, intpc);
						String ad = st+" "+ci+" "+re+" "+pc;
						ad = ad.trim();
						i.putExtra(Insert.POSTAL, ad);
						i.putExtra(Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.TYPE_WORK);
					}else if(line.regionMatches(4, "X-", 0, 2)){
						int ind2p = line.indexOf(':');
						String label = line.substring(6, ind2p);
						int ind1pv = line.indexOf(';', ind2p+1);
						int ind2pv = line.indexOf(';', ind1pv+1);
						int indst = line.indexOf(';', ind2pv+1);
						String st = line.substring(ind2pv+1, indst);
						int indci = line.indexOf(';', indst+1);
						String ci = line.substring(indst+1, indci);
						int indre = line.indexOf(';', indci+1);
						String re = line.substring(indci+1, indre);
						int intpc = line.indexOf(';', indre+1);
						String pc = line.substring(indre+1, intpc);
						String ad = st+" "+ci+" "+re+" "+pc;
						ad = ad.trim();
						i.putExtra(Insert.POSTAL, ad);
						i.putExtra(Insert.POSTAL_TYPE, label);
					}else{
						int ind2p = line.indexOf(':');
						int ind1pv = line.indexOf(';', ind2p+1);
						int ind2pv = line.indexOf(';', ind1pv+1);
						int indst = line.indexOf(';', ind2pv+1);
						String st = line.substring(ind2pv+1, indst);
						int indci = line.indexOf(';', indst+1);
						String ci = line.substring(indst+1, indci);
						int indre = line.indexOf(';', indci+1);
						String re = line.substring(indci+1, indre);
						int intpc = line.indexOf(';', indre+1);
						String pc = line.substring(indre+1, intpc);
						String ad = st+" "+ci+" "+re+" "+pc;
						ad = ad.trim();
						i.putExtra(Insert.POSTAL, ad);
						i.putExtra(Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.TYPE_OTHER);
					}
					hasAdr = true;
				}
				/*else if(line.startsWith("NOTE") && !hasNote){
					i.putExtra(Insert.NOTES, line.substring(5));
					hasNote = true;
				}
				else if(line.startsWith("ORG") && !hasOrg){
					i.putExtra(Insert.COMPANY, line.substring(4));
					hasOrg = true;
				}
				else if(line.startsWith("TITLE") && !hasTitle){
					i.putExtra(Insert.JOB_TITLE, line.substring(6));
					hasTitle = true;
				}*/
			}

		}catch(Exception e){	}

		return i;
	}

}
